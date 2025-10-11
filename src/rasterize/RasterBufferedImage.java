// Trida RasterBufferedImage - pracuje s rastrem pomoci objektu BufferedImage
package rasterize;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RasterBufferedImage implements Raster {

    // Uvnitr ulozeny obrazek jako 2D pole pixelu
    private BufferedImage image;

    // Vytvori novy prazdny raster o dane sirce a vysce
    public RasterBufferedImage(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    // Nastavi barvu pixelu na souradnicich (x,y) pokud je v rozsahu
    @Override
    public void setPixel(int x, int y, int color) {
        if (x >= 0 && x < getSirska() && y >= 0 && y < getVyska()) {
            image.setRGB(x, y, color);
        }
    }


    // Vrati sirku rasteru
    @Override
    public int getSirska() {
        return image.getWidth();
    }

    // Vrati vysku rasteru
    @Override
    public int getVyska() {
        return image.getHeight();
    }

    // Smaze obsah rasteru (vycisti platno)
    @Override
    public void clear() {
        Graphics g = image.getGraphics();
        g.clearRect(0, 0, image.getWidth(), image.getHeight());
    }

    public BufferedImage getImage() {
        return image;
    }
}
