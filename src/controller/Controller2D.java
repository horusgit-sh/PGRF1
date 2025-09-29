package controller;


import rasterise.LineRaserizerTrivial;
import rasterise.LineRasterizer;
import rasterise.LineRasterizerGraphics;
import view.Panel;

import java.awt.event.*;

public class Controller2D {
    private final Panel panel;
    private int color = 0xff0000;

    private LineRasterizer lineRasterizer;

    public Controller2D(Panel panel) {
        this.panel = panel;

        lineRasterizer = new LineRaserizerTrivial(panel.getRaster());

        initListeners();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int cntrY = panel.getRaster().getVyska() / 2;
                int cntrX = panel.getRaster().getSirska() / 2;
                for (int x = cntrX; x < panel.getRaster().getSirska(); x++) {
                    panel.getRaster().setPixel(x, cntrY, 0xff0000);
                }
                panel.repaint();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int centerY = panel.getRaster().getVyska() / 2;
                int centerX = panel.getRaster().getSirska() / 2;

                panel.getRaster().clear();
                lineRasterizer.rasterize(centerX, centerY, e.getX(), e.getY());
                panel.repaint();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_0) {
                    color = 0xff0000;
                }
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    color = 0x00ff00;
                }
            }
        });

        panel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                panel.getRaster().setPixel(e.getX(), e.getY(), 0xff0000);
            }
        });
    }
}
