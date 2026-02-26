package solid;

import transforms.Point3D;
import transforms.Vec3D;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class TextureGenerator {

    public static BufferedImage createCheckerboard(int size, Color c1, Color c2) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        int gridSize = size / 8;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                boolean checker = ((x / gridSize) + (y / gridSize)) % 2 == 0;
                img.setRGB(x, y, checker ? c1.getRGB() : c2.getRGB());
            }
        }
        return img;
    }

    public static BufferedImage createGradient(int size, Color c1, Color c2) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < size; y++) {
            float t = (float) y / size;
            int r = (int) (c1.getRed() * (1 - t) + c2.getRed() * t);
            int g = (int) (c1.getGreen() * (1 - t) + c2.getGreen() * t);
            int b = (int) (c1.getBlue() * (1 - t) + c2.getBlue() * t);
            Color c = new Color(r, g, b);
            for (int x = 0; x < size; x++) {
                img.setRGB(x, y, c.getRGB());
            }
        }
        return img;
    }
}

