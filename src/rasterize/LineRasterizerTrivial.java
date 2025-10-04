package rasterize;

public class LineRasterizerTrivial extends LineRasterizer {
    public LineRasterizerTrivial(RasterBufferedImage raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        // normalize order by x
        if (x1 > x2) {
            int tx = x1; x1 = x2; x2 = tx;
            int ty = y1; y1 = y2; y2 = ty;
        }

        int dx = x2 - x1;
        int dy = y2 - y1;

        int color = 0xff0000;
        if (dx == 0) {
            int sy = y1 <= y2 ? 1 : -1;
            for (int y = y1; y != y2 + sy; y += sy) {
                rasterSafeSet(x1, y, color); // TODO: use configurable color
            }
            return;
        }

        float k = dy / (float) dx;
        float q = y1 - k * x1;

        // iterate by x for |k| <= 1, else by y
        if (Math.abs(k) <= 1f) {
            for (int x = x1; x <= x2; x++) {
                int y = Math.round(k * x + q);
                rasterSafeSet(x, y, color);
            }
        } else {
            int sy = y1 <= y2 ? 1 : -1;
            // recompute inverse form: x = (y - q) / k
            for (int y = y1; y != y2 + sy; y += sy) {
                int x = Math.round((y - q) / k);
                rasterSafeSet(x, y, color);
            }
        }
    }

    private void rasterSafeSet(int x, int y, int color) {
        if (x >= 0 && y >= 0 && x < raster.getImage().getWidth() && y < raster.getImage().getHeight()) {
            raster.setPixel(x, y, color);
        }
    }
}