package rasterize;


import java.awt.*;

public abstract class LineRasterizer {
    protected RasterBufferedImage raster;

    public LineRasterizer(RasterBufferedImage raster) {
        this.raster = raster;
    }


    public abstract void rasterize(int x1, int y1, int x2, int y2, Color color);

}
