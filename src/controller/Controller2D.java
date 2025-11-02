// Controller2D - ovlada interakci uzivatele s platnem (mys, klavesnice)
package controller;

import model.Polygon;
import rasterize.PolygonRasterize;
import rasterize.FilledLineRasterizer;
import rasterize.RasterBufferedImage;
import view.Panel;

import model.Point;
import fill.SeedFiller;
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
    private boolean gradientMode = false;
    private boolean rectMode=false;
    private int startX, startY, endX, endY;

    private final Clipper clipper = new Clipper();
    private final java.util.List<Point> currentPoly = new java.util.ArrayList<>();
    private final java.util.List<Polygon> finishedPolys = new java.util.ArrayList<>();
    private boolean seedBG=false, seedBND=false;
    private final SeedFiller seedFiller = new SeedFiller();

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
                // simple fill modes
                if(seedBG || seedBND){
                    int x=e.getX(), y=e.getY();
                    if(seedBG) seedFiller.fill(x,y,raster,0x00FF00);
                    if(seedBND) seedFiller.fill(x,y,raster,0xFF0000);
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
                    drawing=false;
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

}