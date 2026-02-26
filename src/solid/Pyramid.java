package solid;

import transforms.Point3D;
import transforms.Vec3D;
import java.awt.Color;

public class Pyramid extends Solid {

    public Pyramid() {
        vertexBuffer.add(new Point3D(-0.5, -0.5, -0.5));
        vertexBuffer.add(new Point3D( 0.5, -0.5, -0.5));
        vertexBuffer.add(new Point3D( 0.5, -0.5,  0.5));
        vertexBuffer.add(new Point3D(-0.5, -0.5,  0.5));
        vertexBuffer.add(new Point3D( 0, 0.5, 0));

        uvBuffer.add(new Vec3D(0, 0, 0));
        uvBuffer.add(new Vec3D(1, 0, 0));
        uvBuffer.add(new Vec3D(1, 1, 0));
        uvBuffer.add(new Vec3D(0, 1, 0));
        uvBuffer.add(new Vec3D(0.5, 0.5, 0));

        normalBuffer.add(new Vec3D(-1, -1, -1).normalized());
        normalBuffer.add(new Vec3D( 1, -1, -1).normalized());
        normalBuffer.add(new Vec3D( 1, -1,  1).normalized());
        normalBuffer.add(new Vec3D(-1, -1,  1).normalized());
        normalBuffer.add(new Vec3D( 0,  1,  0).normalized());

        addIndices(0, 1, 1, 2, 2, 3, 3, 0);
        addIndices(0, 4, 1, 4, 2, 4, 3, 4);

        addTriangleIndices(0, 1, 2, 0, 2, 3);
        addTriangleIndices(1, 0, 4);
        addTriangleIndices(2, 1, 4);
        addTriangleIndices(3, 2, 4);
        addTriangleIndices(0, 3, 4);

        this.color = Color.YELLOW;
    }
}