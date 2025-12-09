package solid;

import transforms.Point3D;
import java.awt.Color;

/**
 * Třída pro reprezentaci jedné osy s vlastní barvou.
 */
public class Axis extends Solid {

    /**
     * Vytvoří osu
     */
    public Axis(double x, double y, double z, Color color) {
        // Začátek
        vertexBuffer.add(new Point3D(0, 0, 0));

        // Konec
        vertexBuffer.add(new Point3D(x, y, z));

        // Spojíme
        addIndices(0, 1);

        this.color = color;
    }
}