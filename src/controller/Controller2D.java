// Controller2D - ovlada interakci uzivatele s platnem (mys, klavesnice)
package controller;

import model.Polygon;
import rasterize.PolygonRasterize;
import rasterize.FilledLineRasterizer;
import rasterize.RasterBufferedImage;
import view.Panel;

import model.Point;
import model.Rectangle;
import fill.SeedFiller;
import fill.ScanLineFiller;
import clipper.Clipper;

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
    private boolean shiftPressed = false;
    private boolean gradientMode = false;
    private int startX, startY, endX, endY;

    private boolean seedBG=false, seedBND=false;
    private boolean rectMode=false, rectAwaitHeight=false;
    private Point rA=null, rB=null;
    private final SeedFiller seedFiller = new SeedFiller();
    private final ScanLineFiller scanFiller = new ScanLineFiller();
    private final Clipper clipper = new Clipper();
    private final java.util.List<Point> currentPoly = new java.util.ArrayList<>();
    private final java.util.List<Polygon> finishedPolys = new java.util.ArrayList<>();
    private java.util.List<Point> clipWindow = java.util.Arrays.asList(
            new Point(200,100),
            new Point(300,150),
            new Point(350,250),
            new Point(250,300),
            new Point(150,200)
    );
    private boolean areaSelectMode=false;

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
                if(areaSelectMode){
                    java.util.List<Polygon> hit=new java.util.ArrayList<>();
                    Point click=new Point(e.getX(),e.getY());
                    for(Polygon p:finishedPolys){ if(isPointInside(click,p)) hit.add(p);}
                    if(hit.size()>0){
                        Polygon inter=hit.get(0);
                        for(int i=1;i<hit.size();i++){
                            inter=clipper.clip(inter,hit.get(i));
                            if(inter==null){
                                // no intersection area
                                areaSelectMode=false;
                                panel.repaint();
                                return;
                            }
                        }
                        finishedPolys.clear(); finishedPolys.add(inter);
                        areaSelectMode=false;
                        raster.clear();
                        for(int i=0;i<inter.getVertices().size();i++){
                            Point a=inter.getVertices().get(i);
                            Point b=inter.getVertices().get((i+1)%inter.getVertices().size());
                            lineDrawer.drawLine(a.x,a.y,b.x,b.y,gradientMode);
                        }
                        panel.repaint();
                        return;
                    }
                }
                if(seedBG||seedBND){
                    if(seedBG) seedFiller.fill(e.getX(),e.getY(),raster,0x00FF00);
                    else seedFiller.fill(e.getX(),e.getY(),raster,0xFF0000);
                    seedBG=seedBND=false;
                    panel.repaint();
                    return;
                }
                if(rectMode && rA==null){
                    rA=new Point(e.getX(),e.getY());
                    drawing=true;
                    startX=e.getX();startY=e.getY();
                    return;
                }
                if(rectMode && rectAwaitHeight && rA!=null && rB!=null){
                    Rectangle rect=new Rectangle();
                    rect.setVertices(rA,rB,new Point(e.getX(),e.getY()));
                    for(int i=0;i<rect.getVertices().size();i++){
                        Point a=rect.getVertices().get(i);
                        Point b=rect.getVertices().get((i+1)%rect.getVertices().size());
                        lineDrawer.drawLine(a.x,a.y,b.x,b.y,gradientMode);
                    }
                    rectMode=false;rectAwaitHeight=false;rA=rB=null;
                    panel.repaint();
                    return;
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
                if(rectMode && drawing && rA!=null && !rectAwaitHeight){
                    rB=new Point(e.getX(),e.getY());
                    drawing=false;
                    rectAwaitHeight=true;
                    return;
                }
                if (e.getButton() != MouseEvent.BUTTON1) return;
                // Dokonceni usecky po uvolneni tlacitka mysi
                if (drawing) {
                    endX = e.getX();
                    endY = e.getY();

                    // Pokud je stisknut SHIFT, zarovnat na vodorovnou/svislou/uhlopricnou linii
                    if (shiftPressed) {
                        int[] snapped = lineDrawer.getSnappedPoint(startX, startY, endX, endY);
                        endX = snapped[0];
                        endY = snapped[1];
                    }

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
                // Pokud je stisknut SHIFT, zarovnat na vodorovnou/svislou/uhlopricnou linii
                if (shiftPressed) {
                    int[] snapped = lineDrawer.getSnappedPoint(startX, startY, x, y);
                    x = snapped[0];
                    y = snapped[1];
                }

                lineDrawer.drawLine(startX, startY, x, y, gradientMode);
                panel.repaint();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftPressed = true;

                }

                if(e.getKeyCode()==KeyEvent.VK_F){
                    seedBG=true;seedBND=false;
                    drawing=false;
                }
                if(e.getKeyCode()==KeyEvent.VK_B){
                    seedBG=false;seedBND=true;
                    drawing=false;
                }
                if(e.getKeyCode()==KeyEvent.VK_R){
                    rectMode=true;rectAwaitHeight=false;
                    rA=rB=null;
                    drawing=false;
                }
                if(e.getKeyCode()==KeyEvent.VK_V){
                    drawing=false;
                    areaSelectMode=true;
                    return;
                }
                if(e.getKeyCode()==KeyEvent.VK_C){
                    currentPoly.clear();
                    finishedPolys.clear();
                    polygonManager.clearCanvas();
                    panel.repaint();
                }


                // Prepina rezim barevneho prechodu (gradient)
                if (e.getKeyCode() == KeyEvent.VK_G) {
                    gradientMode = !gradientMode;
                    lineDrawer.drawLine(startX, startY, endX, endY, gradientMode);
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
            }

        });
    }
    private boolean isPointInside(Point p, Polygon poly){
        if(poly==null) return false;
        boolean res=false;
        for(int i=0,j=poly.getVertices().size()-1;i<poly.getVertices().size();j=i++){
            int xi=poly.getVertices().get(i).x, yi=poly.getVertices().get(i).y;
            int xj=poly.getVertices().get(j).x, yj=poly.getVertices().get(j).y;
            if(((yi>p.y)!=(yj>p.y)) && (p.x<(xj-xi)*(p.y-yi)/(double)(yj-yi+0.00001)+xi))
                res=!res;
        }
        return res;
    }
}