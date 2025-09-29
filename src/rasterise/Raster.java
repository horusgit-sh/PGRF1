package rasterise;

public interface Raster {
    void setPixel(int x, int y, int color);
    int getPixel(int x, int y);
    int getSirska();
    int getVyska();
    void clear();
}