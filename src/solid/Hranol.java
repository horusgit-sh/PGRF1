package solid;

import transforms.Point3D;
import transforms.Vec3D;
import java.awt.*;

public class Hranol extends Solid {
    public Hranol() {
        vertexBuffer.add(new Point3D( 0.5, -0.5,  0.0));
        vertexBuffer.add(new Point3D( 0.35, -0.5,  0.35));
        vertexBuffer.add(new Point3D( 0.0, -0.5,  0.5));
        vertexBuffer.add(new Point3D(-0.35, -0.5,  0.35));
        vertexBuffer.add(new Point3D(-0.5, -0.5,  0.0));
        vertexBuffer.add(new Point3D(-0.35, -0.5, -0.35));
        vertexBuffer.add(new Point3D( 0.0, -0.5, -0.5));
        vertexBuffer.add(new Point3D( 0.35, -0.5, -0.35));

        vertexBuffer.add(new Point3D( 0.5,  0.5,  0.0));
        vertexBuffer.add(new Point3D( 0.35, 0.5,  0.35));
        vertexBuffer.add(new Point3D( 0.0,  0.5,  0.5));
        vertexBuffer.add(new Point3D(-0.35, 0.5,  0.35));
        vertexBuffer.add(new Point3D(-0.5,  0.5,  0.0));
        vertexBuffer.add(new Point3D(-0.35, 0.5, -0.35));
        vertexBuffer.add(new Point3D( 0.0,  0.5, -0.5));
        vertexBuffer.add(new Point3D( 0.35, 0.5, -0.35));

        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4.0;
            uvBuffer.add(new Vec3D(0.5 + 0.5 * Math.cos(angle), 0.5 + 0.5 * Math.sin(angle), 0));
        }
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4.0;
            uvBuffer.add(new Vec3D(0.5 + 0.5 * Math.cos(angle), 0.5 + 0.5 * Math.sin(angle), 0));
        }

        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4.0;
            normalBuffer.add(new Vec3D(Math.cos(angle), -0.3, Math.sin(angle)).normalized());
        }
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4.0;
            normalBuffer.add(new Vec3D(Math.cos(angle), 0.3, Math.sin(angle)).normalized());
        }

        addIndices(0,1, 1,2, 2,3, 3,4, 4,5, 5,6, 6,7, 7,0);
        addIndices(8,9, 9,10, 10,11, 11,12, 12,13, 13,14, 14,15, 15,8);
        addIndices(0,8, 1,9, 2,10, 3,11, 4,12, 5,13, 6,14, 7,15);

        addTriangleIndices(0, 7, 6, 0, 6, 5, 0, 5, 4, 0, 4, 3, 0, 3, 2, 0, 2, 1);
        addTriangleIndices(8, 9, 10, 8, 10, 11, 8, 11, 12, 8, 12, 13, 8, 13, 14, 8, 14, 15);

        for (int i = 0; i < 8; i++) {
            int j = (i + 1) % 8;
            addTriangleIndices(j, i, j + 8, i, i + 8, j + 8);
        }

        this.color = Color.ORANGE;
    }
}
