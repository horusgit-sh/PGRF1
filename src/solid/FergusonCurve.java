package solid;

import transforms.Point3D;
import java.awt.Color;

/**
 * Fergusonova (Hermitova) kubická křivka.
 * Definována počátečním a koncovým bodem a jejich tečnými vektory.
 */
public class FergusonCurve extends Solid {

    public FergusonCurve(Point3D p0, Point3D p1, Point3D t0, Point3D t1) {

        int detail = 30;

        for (int i = 0; i <= detail; i++) {
            double t = (double) i / detail;
            double t2 = t * t;
            double t3 = t2 * t;

            // Hermitovy (Fergusonovy) váhové funkce
            double h1 =  2*t3 - 3*t2 + 1;
            double h2 = -2*t3 + 3*t2;
            double h3 =  t3 - 2*t2 + t;
            double h4 =  t3 - t2;

            // Bod křivky P = h1*P0 + h2*P1 + h3*T0 + h4*T1
            double x = h1 * p0.x + h2 * p1.x + h3 * t0.x + h4 * t1.x;
            double y = h1 * p0.y + h2 * p1.y + h3 * t0.y + h4 * t1.y;
            double z = h1 * p0.z + h2 * p1.z + h3 * t0.z + h4 * t1.z;

            vertexBuffer.add(new Point3D(x, y, z));

            if (i > 0) {
                addIndices(i - 1, i);
            }
        }

        this.color = Color.GREEN;
    }
}
