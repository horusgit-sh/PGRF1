package solid;

import transforms.Point3D;
import transforms.Vec3D;
import java.awt.Color;

public class Cube extends Solid {

    public Cube() {
        vertexBuffer.add(new Point3D(-0.5, -0.5, -0.5));
        vertexBuffer.add(new Point3D( 0.5, -0.5, -0.5));
        vertexBuffer.add(new Point3D( 0.5,  0.5, -0.5));
        vertexBuffer.add(new Point3D(-0.5,  0.5, -0.5));
        vertexBuffer.add(new Point3D(-0.5, -0.5,  0.5));
        vertexBuffer.add(new Point3D( 0.5, -0.5,  0.5));
        vertexBuffer.add(new Point3D( 0.5,  0.5,  0.5));
        vertexBuffer.add(new Point3D(-0.5,  0.5,  0.5));

        uvBuffer.add(new Vec3D(0, 0, 0));
        uvBuffer.add(new Vec3D(1, 0, 0));
        uvBuffer.add(new Vec3D(1, 1, 0));
        uvBuffer.add(new Vec3D(0, 1, 0));
        uvBuffer.add(new Vec3D(0, 0, 0));
        uvBuffer.add(new Vec3D(1, 0, 0));
        uvBuffer.add(new Vec3D(1, 1, 0));
        uvBuffer.add(new Vec3D(0, 1, 0));

        normalBuffer.add(new Vec3D(-1, -1, -1).normalized());
        normalBuffer.add(new Vec3D( 1, -1, -1).normalized());
        normalBuffer.add(new Vec3D( 1,  1, -1).normalized());
        normalBuffer.add(new Vec3D(-1,  1, -1).normalized());
        normalBuffer.add(new Vec3D(-1, -1,  1).normalized());
        normalBuffer.add(new Vec3D( 1, -1,  1).normalized());
        normalBuffer.add(new Vec3D( 1,  1,  1).normalized());
        normalBuffer.add(new Vec3D(-1,  1,  1).normalized());

        addIndices(0, 1, 1, 2, 2, 3, 3, 0);
        addIndices(4, 5, 5, 6, 6, 7, 7, 4);
        addIndices(0, 4, 1, 5, 2, 6, 3, 7);

        addTriangleIndices(0, 3, 2, 0, 2, 1);
        addTriangleIndices(4, 5, 6, 4, 6, 7);
        addTriangleIndices(0, 4, 7, 0, 7, 3);
        addTriangleIndices(1, 2, 6, 1, 6, 5);
        addTriangleIndices(0, 1, 5, 0, 5, 4);
        addTriangleIndices(3, 7, 6, 3, 6, 2);

        this.color = Color.MAGENTA;
    }
}