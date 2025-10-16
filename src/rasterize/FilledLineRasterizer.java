package rasterize;

import java.awt.*;

/**
 * Implementace rasterizace úsečky pomocí Bresenhamova algoritmu.
 *
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
    public void rasterize(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;

        int err = dx - dy;

        int x = x1;
        int y = y1;

        while (true) {
            // Vykresli pixel
            raster.setPixel(x, y, 0xff0000);

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

    @Override
    public void rasterizeWithGradient(int x1, int y1, int color1, int x2, int y2, int color2) {

        Color cStart = new Color(color1);
        Color cEnd = new Color(color2);

        int r1 = cStart.getRed();
        int g1 = cStart.getGreen();
        int b1 = cStart.getBlue();

        int r2 = cEnd.getRed();
        int g2 = cEnd.getGreen();
        int b2 = cEnd.getBlue();

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;

        int err = dx - dy;
        int e2;

        int x = x1;
        int y = y1;

        // Celková délka úsečky pro interpolaci
        double totalLength = Math.sqrt(dx * dx + dy * dy);

        while (true) {
            if (x >= 0 && x < raster.getSirska() && y >= 0 && y < raster.getVyska()) {
                // Výpočet poměru (0.0 - 1.0) pro interpolaci
                double currentLength = Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
                double ratio = (totalLength > 0) ? currentLength / totalLength : 0;
                ratio = Math.max(0, Math.min(1, ratio)); // Omezení na rozsah 0-1

                // Lineární interpolace barev
                int r = (int) (r1 + (r2 - r1) * ratio);
                int g = (int) (g1 + (g2 - g1) * ratio);
                int b = (int) (b1 + (b2 - b1) * ratio);

                int color = (r << 16) | (g << 8) | b;
                raster.setPixel(x, y, color);
            }

            // Ukončení při dosažení koncového bodu
            if (x == x2 && y == y2) break;

            // Vypocet chyby a posun v ose X a/nebo Y podle Bresenhamova algoritmu
            e2 = 2 * err;

            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }

            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }
    }
}