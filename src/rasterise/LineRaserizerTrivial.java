package rasterise;

public class LineRaserizerTrivial extends LineRasterizer{
    public LineRaserizerTrivial(RasterBufferImage raster) {
        super(raster);
    }
    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        float k = (float)(y2 - y1) / (float)(x2 - x1);
        float b = y1 - k * x1;

        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }

        for (int x = x1; x <= x2; x++) {
            int y = (int)(k * x + b);
            raster.setPixel(x, y, 0xff0000);
        }
    }
}
