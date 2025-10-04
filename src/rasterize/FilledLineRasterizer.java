package rasterize;

/**
 * Implementace rasterizace úsečky pomocí Bresenhamova algoritmu.
 *
 * Algoritmus: Bresenhamův algoritmus pro rasterizaci úseček
 * Princip: Používá pouze celočíselnou aritmetiku pro výpočet pixel
 * Paměťová složitost: O(1)
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