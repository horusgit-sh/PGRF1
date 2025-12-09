package solid;

import transforms.Point3D;
import java.awt.Color;

/**
 * Coonsova kubická křivka.
 * Interpolační kubická křivka definovaná čtyřmi body.
 */
public class CoonsovaCurve extends Solid {

    public CoonsovaCurve(Point3D p0, Point3D p1, Point3D p2, Point3D p3) {

        int detail = 30;

        for (int i = 0; i <= detail; i++) {
            double t = (double) i / detail;
            double t2 = t * t;
            double t3 = t2 * t;

            // Coonsovy váhové funkce (kubická interpolační báze)
            double h1 = -t3 + 3 * t2 - 3 * t + 1;
            double h2 =  3 * t3 - 6 * t2 + 3 * t;
            double h3 = -3 * t3 + 3 * t2;
            double h4 =  t3;

            // Bod křivky P = h1*P0 + h2*P1 + h3*P2 + h4*P3
            double x = h1 * p0.x + h2 * p1.x + h3 * p2.x + h4 * p3.x;
            double y = h1 * p0.y + h2 * p1.y + h3 * p2.y + h4 * p3.y;
            double z = h1 * p0.z + h2 * p1.z + h3 * p2.z + h4 * p3.z;

            vertexBuffer.add(new Point3D(x, y, z));

            if (i > 0) {
                addIndices(i - 1, i);
            }
        }

        this.color = Color.CYAN;
    }
}
