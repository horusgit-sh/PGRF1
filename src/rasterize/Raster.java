package rasterize;

public interface Raster {
    void setPixel(int x, int y, int color);

    int getSirska();

    int getVyska();

    void clear();

}