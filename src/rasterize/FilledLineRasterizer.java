package rasterize;

import java.awt.*;

/**
 * Implementace rasterizace úsečky pomocí Bresenhamova algoritmu.
 * <p>
 * Algoritmus: Bresenhamův algoritmus pro rasterizaci úseček
 * Princip: Používá pouze celočíselnou aritmetiku pro výpočet pixel
 * Paměťová složitost: O(1)
 * + Výhody: velmi rychlý, používá pouze celočíselné operace, nízká paměťová náročnost
 * - Nevýhody: hůře rozšiřitelný pro křivky, složitější implementace pro antialiasing
 */
public class FilledLineRasterizer extends LineRasterizer {

    public FilledLineRasterizer(RasterBufferedImage raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2, Color color) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;

        int err = dx - dy;

        int x = x1;
        int y = y1;

        while (true) {
            // vykresleni jednoho pixelu podle Bresenhamova kroku
            raster.setPixel(x, y, color.getRGB());

            if (x == x2 && y == y2) {
                break; //
            }

            // Vypocet chyby a posun v ose X a/nebo Y podle Bresenhamova algoritmu
            int e2 = 2 * err;

            if (e2 > -dy) {
                err = err - dy;
                x = x + sx;
            }

            if (e2 < dx) {
                err = err + dx;
                y = y + sy;
            }
        }
    }

}