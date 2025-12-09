package solid;

import transforms.Point3D;
import transforms.Mat4;
import java.awt.Color;

/**
 * Třída reprezentující Bézierovu kubiku.
 * Počítá body křivky pomocí maticového násobení (Bernsteinovy polynomy).
 */
public class BezierCurve extends Solid {

    /**
     * Vytvoří Bézierovu křivku ze 4 bodů.
     * P1, P4 = krajní body (křivka jimi prochází).
     * P2, P3 = řídící body (určují tvar).
     */
    public BezierCurve(Point3D p1, Point3D p2, Point3D p3, Point3D p4) {
        // Kubická Bézierova křivka definovaná 4 body
        // Matematicky odpovídá zápisu T · M · G, ale váhy počítáme přímo.

        int detail = 30; // počet úseků křivky

        for (int i = 0; i <= detail; i++) {
            double t = (double) i / detail;
            double t2 = t * t;
            double t3 = t2 * t;

            // Bernsteinovy polynomy
            double h1 = -1 * t3 + 3 * t2 - 3 * t + 1;
            double h2 =  3 * t3 - 6 * t2 + 3 * t;
            double h3 = -3 * t3 + 3 * t2;
            double h4 =  1 * t3;

            // Výpočet bodu křivky P = h1*P1 + h2*P2 + h3*P3 + h4*P4
            double x = h1 * p1.x + h2 * p2.x + h3 * p3.x + h4 * p4.x;
            double y = h1 * p1.y + h2 * p2.y + h3 * p3.y + h4 * p4.y;
            double z = h1 * p1.z + h2 * p2.z + h3 * p3.z + h4 * p4.z;

            vertexBuffer.add(new Point3D(x, y, z));

            if (i > 0) {
                addIndices(i - 1, i);
            }
        }

        this.color = Color.RED;
    }
}