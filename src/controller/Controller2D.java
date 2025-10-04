package controller;

import rasterize.FilledLineRasterizer;
import rasterize.RasterBufferedImage;
import view.Panel;

import java.awt.event.*;

public class Controller2D {
    private final Panel panel;
    private final RasterBufferedImage raster;
    private final LineDrawer lineDrawer;
    private final PolygonManager polygonManager;

    private boolean drawing = false;
    private boolean shiftPressed = false;
    private boolean gradientMode = false;

    private int startX, startY;

    public Controller2D(Panel panel) {
        this.panel = panel;
        this.raster = panel.getRaster();
        this.lineDrawer = new LineDrawer(new FilledLineRasterizer(raster));
        this.polygonManager = new PolygonManager(raster, lineDrawer);

        initListeners();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) return;

                if (!drawing) {
                    startX = e.getX();
                    startY = e.getY();
                    drawing = true;
                    polygonManager.addVertex(startX, startY);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) return;

                if (drawing) {
                    int endX = e.getX();
                    int endY = e.getY();

                    if (shiftPressed) {
                        int[] snapped = lineDrawer.getSnappedPoint(startX, startY, endX, endY);
                        endX = snapped[0];
                        endY = snapped[1];
                    }

                    lineDrawer.drawLine(startX, startY, endX, endY, gradientMode);
                    polygonManager.addVertex(endX, endY);

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
                polygonManager.drawPolygon();

                int x = e.getX();
                int y = e.getY();
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

                if (e.getKeyCode() == KeyEvent.VK_C) {
                    // Smazat plátno - Uloha1 požadavek
                    polygonManager.clearCanvas();
                    panel.repaint();
                }

                if (e.getKeyCode() == KeyEvent.VK_G) {
                    // Přepnutí režimu gradientu
                    gradientMode = !gradientMode;
                    panel.repaint();
                }

                // Uzavření polygonu - např. mezerník
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    polygonManager.closePolygon();
                    panel.repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) shiftPressed = false;
            }
        });
    }
}