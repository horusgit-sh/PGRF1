package rasterise;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class RasterBufferImage implements Raster{

    private final BufferedImage image;

    public RasterBufferImage(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        // osetrit zapis mimo rasteru
        image.setRGB(x, y, color);
    }

    @Override
    public int getPixel(int x, int y) {

        return 0;
    }

    @Override
    public int getSirska() {
        return image.getWidth();
    }

    @Override
    public int getVyska() {
        return image.getHeight();
    }

    @Override
    public void clear() {
        Graphics g = image.getGraphics();
        g.fillRect(0, 0, image.getWidth() , image.getHeight());

    }


    public BufferedImage getImage() {
        return image;
    }
}
