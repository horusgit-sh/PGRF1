package rasterise;

public abstract class LineRasterizer {
    protected RasterBufferImage raster;

    public LineRasterizer(RasterBufferImage raster) {
        this.raster = raster;
    }

    public void rasterize(int x1, int y1, int x2, int y2){

    }
}
