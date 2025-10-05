package rasterize;


public abstract class LineRasterizer {
    protected RasterBufferedImage raster;

    public LineRasterizer(RasterBufferedImage raster) {
        this.raster = raster;
    }


    public abstract void rasterize(int x1, int y1, int x2, int y2);
    public void rasterizeWithGradient(int x1, int y1, int color1, int x2, int y2, int color2) {}
}
