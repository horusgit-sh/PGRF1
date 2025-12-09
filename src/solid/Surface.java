package solid;

import transforms.Point3D;
import java.awt.Color;

/**
 * BONUS 1: Parametrická plocha.
 * Třída generuje mřížku (grid) definovanou funkcí z = f(x, y).
 */
public class Surface extends Solid {

    public Surface() {
        int density = 20; // Hustota mřížky (počet čtverečků na stranu)
        double min = -3;  // Rozsah od
        double max = 3;   // Rozsah do

        // 1. Generování vrcholů
        for (int i = 0; i <= density; i++) {
            for (int j = 0; j <= density; j++) {
                // Přepočet indexů na reálné souřadnice X a Z
                double x = min + (max - min) * i / density;
                double z = min + (max - min) * j / density;

                // Sedlová plocha (hyperbolický paraboloid): y = a * (x^2 - z^2)
                double a = 0.1;
                double y = a * (x * x - z * z);

                vertexBuffer.add(new Point3D(x, y, z));
            }
        }

        // 2. Generování indexů (spojení bodů do mřížky)
        for (int i = 0; i < density; i++) {
            for (int j = 0; j < density; j++) {
                int index = i * (density + 1) + j; // Index aktuálního bodu
                int nextRow = index + (density + 1); // Index bodu o řádek dál

                // Vodorovná čára (spojení s pravým sousedem)
                addIndices(index, index + 1);

                // Svislá čára (spojení se spodním sousedem)
                addIndices(index, nextRow);
            }
        }

        this.color = Color.WHITE;
    }
}