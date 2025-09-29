package controller;


import view.Panel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller2D {
    private final Panel panel;

    public Controller2D(Panel panel) {
        this.panel = panel;

        panel.getRaster().setRGB(50, 50, 0xff0000);
        panel.repaint();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                panel.getRaster().setRGB(e.getX(), e.getY(), 0xff0000);
                panel.repaint();

                int cntrY = (int) (panel.getRaster().getHeight() / 2);
                int cntrX = (int) (panel.getRaster().getWidth() / 2);
                for (int i = 0; i < cntrY; i++) {
                    panel.getRaster().setRGB(cntrX + i, cntrY, 0x00ff00);
                    panel.repaint();
                }
            }
        });
    }
}
