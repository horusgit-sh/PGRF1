package solid;

import transforms.Point3D;
import java.awt.Color;

public class Axis extends Solid {

    public Axis(double x, double y, double z, Color color) {
        vertexBuffer.add(new Point3D(0, 0, 0));
        vertexBuffer.add(new Point3D(x, y, z));
        addIndices(0, 1);

        double len = Math.sqrt(x * x + y * y + z * z);
        double s = len * 0.15;
        if (len > 0) {
            if (Math.abs(x) > 0) {
                vertexBuffer.add(new Point3D(x - Math.signum(x) * s, y + s, z));
                vertexBuffer.add(new Point3D(x - Math.signum(x) * s, y - s, z));
            } else if (Math.abs(y) > 0) {
                vertexBuffer.add(new Point3D(x + s, y - Math.signum(y) * s, z));
                vertexBuffer.add(new Point3D(x - s, y - Math.signum(y) * s, z));
            } else {
                vertexBuffer.add(new Point3D(x + s, y, z - Math.signum(z) * s));
                vertexBuffer.add(new Point3D(x - s, y, z - Math.signum(z) * s));
            }
            addTriangleIndices(1, 2, 3);
        }

        this.color = color;
    }
}