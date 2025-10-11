package view;

import rasterize.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;

// Panel - vykreslovaci komponenta aplikace
public class Panel extends JPanel {

    // Raster pro kresleni, uchovava obraz
    private final RasterBufferedImage raster;

    // Nastavi velikost panelu a vytvori novy raster
    public Panel(int width, int height) {
        setPreferredSize(new Dimension(width, height));

        raster = new RasterBufferedImage(width, height);
    }

    // Vykresleni obsahu rasteru do panelu
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(raster.getImage(), 0, 0, null);
    }

    // Vrati raster pro kresleni
    public RasterBufferedImage getRaster() {
        return raster;
    }
}
