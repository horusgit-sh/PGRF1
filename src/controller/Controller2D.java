package controller;

import model.Polygon;
import rasterize.PolygonRasterize;
import rasterize.FilledLineRasterizer;
import rasterize.RasterBufferedImage;
import view.Panel;

import model.Point;
import fill.SeedFiller;
import clipper.Clipper;

import java.awt.*;
import java.awt.event.*;

public class Controller2D {
    // Odkaz na vykreslovaci panel
    private final Panel panel;
    // Raster pro kresleni pixelu
    private final RasterBufferedImage raster;
    // Objekt pro kresleni usecek
    private final LineDrawer lineDrawer;
    // Spravce polygonu
    private final PolygonRasterize polygonManager;

    private boolean drawing = false;
    private boolean gradientMode = false;
    private boolean rectMode = false;
    private int rectStep = 0;
    private Point rectA, rectB;
    private int startX, startY, endX, endY;

    private final Clipper clipper = new Clipper();
    private final java.util.List<Point> currentPoly = new java.util.ArrayList<>();
    private final java.util.List<Polygon> finishedPolys = new java.util.ArrayList<>();
    private boolean seedBG=false, seedBND=false;
    private final SeedFiller seedFiller = new SeedFiller();
    private boolean clipMode = false;
    private Polygon clipWindow = null;

    public Controller2D(Panel panel) {
        this.panel = panel;
        this.raster = panel.getRaster();
        this.lineDrawer = new LineDrawer(new FilledLineRasterizer(raster));
        this.polygonManager = new PolygonRasterize(raster, lineDrawer);

        initListeners();
    }

    // Inicializace posluchacu udalosti mysi a klavesnice
    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // If we are in clip mode: click triggers clipping of all finished polygons by the blue pentagon
                if(clipMode){
                    int cx = e.getX(), cy = e.getY();
                    model.Point clickP = new model.Point(cx,cy);
                    // proceed only if click lies inside the pentagon
                    if(clipWindow!=null && clipper.isPointInside(clickP, clipWindow)){
                        java.util.List<Polygon> clippedList = new java.util.ArrayList<>();
                        for(Polygon fp : finishedPolys){
                            Polygon cp = clipper.clip(fp, clipWindow);
                            if(cp != null && cp.getVertices()!=null && cp.getVertices().size()>=3){
                                clippedList.add(cp);
                            }
                        }
                        finishedPolys.clear();
                        finishedPolys.addAll(clippedList);
                        // clear, redraw results
                        raster.clear();
                        for(Polygon fp: finishedPolys){
                            java.util.List<model.Point> vs = fp.getVertices();
                            for(int i=0;i<vs.size();i++){
                                model.Point a = vs.get(i);
                                model.Point b = vs.get((i+1)%vs.size());
                                lineDrawer.drawLine(a.x,a.y,b.x,b.y,false);
                            }
                        }
                        panel.repaint();
                        // exit clip mode and hide window
                        clipMode=false;
                        clipWindow=null;
                        return;
                    }
                    // if clicked outside pentagon, ignore for clipping
                }

                if(seedBG || seedBND){
                    int x=e.getX(), y=e.getY();
                    if(seedBG) {
                        seedFiller.fill(x,y,raster,0x00FF00);
                        seedBG=false;
                    }
                   if(seedBND) {
                       final int BOUNDARY_COLOR = 0xFFFFFFFF;

                       Color oldColor = lineDrawer.getColor();
                       lineDrawer.setColor(new Color(0xFFFFFFFF, true));

                       for(Polygon poly : finishedPolys){
                           for(int i=0;i<poly.getVertices().size();i++){
                               Point a=poly.getVertices().get(i);
                               Point b=poly.getVertices().get((i+1)%poly.getVertices().size());
                               lineDrawer.drawLine(a.x,a.y,b.x,b.y,false);
                           }
                       }

                       lineDrawer.setColor(oldColor);

                       seedFiller.fillBoundary(x, y, raster,0xFFFF0000, BOUNDARY_COLOR);
                       seedBND=false;
                       panel.repaint();
                       return;
                   }
                    panel.repaint();
                    return;
                }

                if(rectMode){
                    if(rectStep==0){ rectA=new Point(e.getX(),e.getY()); rectStep=1; return; }
                    if(rectStep==1){ rectB=new Point(e.getX(),e.getY()); rectStep=2; return; }
                    if(rectStep==2){
                        Point p3=new Point(e.getX(),e.getY());
                        double vx = rectB.x-rectA.x;
                        double vy = rectB.y-rectA.y;
                        double nx = -vy;
                        double ny = vx;
                        double len = Math.hypot(nx,ny);
                        nx/=len;
                        ny/=len;
                        double h = ( (p3.x-rectA.x)*nx + (p3.y-rectA.y)*ny );
                        Point p4 = new Point((int)Math.round(rectB.x+nx*h),(int)Math.round(rectB.y+ny*h));
                        Point p3p= new Point((int)Math.round(rectA.x+nx*h),(int)Math.round(rectA.y+ny*h));
                        Polygon r=new Polygon();
                        r.addVertex(rectA); r.addVertex(rectB); r.addVertex(p4); r.addVertex(p3p);
                        finishedPolys.add(r);
                        rectMode=false;
                        rectStep=0;
                        raster.clear();
                        for(Polygon fp:finishedPolys){
                            for(int i=0;i<fp.getVertices().size();i++){
                                Point a=fp.getVertices().get(i);
                                Point b=fp.getVertices().get((i+1)%fp.getVertices().size());
                                lineDrawer.drawLine(a.x,a.y,b.x,b.y,false);
                            }
                        }
                        panel.repaint(); return;
                    }
                }

                if (e.getButton() != MouseEvent.BUTTON1) return;
                // Zacatek kresleni pri stisku leveho tlacitka
                if (!drawing) {
                    startX = e.getX();
                    startY = e.getY();
                    drawing = true;
                    polygonManager.addVertex(startX, startY);
                    currentPoly.add(new Point(startX,startY));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) return;
                // Dokonceni usecky po uvolneni tlacitka mysi
                if (drawing) {
                    endX = e.getX();
                    endY = e.getY();

                    lineDrawer.drawLine(startX, startY, endX, endY, gradientMode);
                    polygonManager.addVertex(endX, endY);
                    currentPoly.add(new Point(endX,endY));

                    startX = endX;
                    startY = endY;

                    panel.repaint();
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!drawing) return;

                raster.clear();
                // Redraw finished polygons
                for(Polygon fp : finishedPolys){
                    for(int i=0;i<fp.getVertices().size();i++){
                        Point a=fp.getVertices().get(i);
                        Point b=fp.getVertices().get((i+1)%fp.getVertices().size());
                        lineDrawer.drawLine(a.x,a.y,b.x,b.y,gradientMode);
                    }
                }
                polygonManager.drawPolygon(gradientMode);

                int x = e.getX();
                int y = e.getY();

                lineDrawer.drawLine(startX, startY, x, y, gradientMode);
                panel.repaint();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_V){
                    drawing=false; seedBG=false; seedBND=false; rectMode=false;
                    clipMode = true;
                    // build centered blue pentagon as clipping window
                    clipWindow = makeDefaultClipWindow();
                    // draw it in blue
                    Color __old = lineDrawer.getColor();
                    lineDrawer.setColor(new Color(0xFF0000FF, true));
                    java.util.List<model.Point> pts = clipWindow.getVertices();
                    for(int i=0;i<pts.size();i++){
                        model.Point a = pts.get(i);
                        model.Point b = pts.get((i+1)%pts.size());
                        lineDrawer.drawLine(a.x,a.y,b.x,b.y,false);
                    }
                    lineDrawer.setColor(__old);
                    panel.repaint();
                    return;
                }
                if(e.getKeyCode()==KeyEvent.VK_C){
                    currentPoly.clear();
                    finishedPolys.clear();
                    polygonManager.clearCanvas();
                    panel.repaint();
                }

                // Uzavre polygon mezernikem
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    polygonManager.closePolygon(gradientMode);
                    Polygon saved = new Polygon();
                    for(Point p : currentPoly) saved.addVertex(p);
                    finishedPolys.add(saved);
                    currentPoly.clear();
                    panel.repaint();
                    drawing = false;
                }

                // enable boundary fill
                if(e.getKeyCode()==KeyEvent.VK_B){ seedBG=false; seedBND=true; drawing=false;}
                // enable background fill
                if(e.getKeyCode()==KeyEvent.VK_F){ seedBG=true; seedBND=false; drawing=false; }
                // enable rectangle mode
                if(e.getKeyCode()==KeyEvent.VK_R){
                    drawing=false;
                    seedBG=false;
                    seedBND=false;
                    rectMode=true;
                }
            }

        });
    }

    private Polygon makeDefaultClipWindow(){
        Polygon w = new Polygon();
        int W = raster.getSirska();
        int H = raster.getVyska();
        int cx = W/2; int cy = H/2;
        int r = Math.min(W,H)/4;
        for(int i=0;i<5;i++){
            double ang = Math.toRadians(72*i - 90); // start up
            int x = cx + (int)Math.round(r*Math.cos(ang));
            int y = cy + (int)Math.round(r*Math.sin(ang));
            w.addVertex(new model.Point(x,y));
        }
        return w;
    }

}