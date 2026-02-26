package rasterize;

public class ZBuffer {
    private final int width;
    private final int height;
    private final double[] depth;

    public ZBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.depth = new double[width * height];
        clear();
    }

    public void clear() {
        for (int i = 0; i < depth.length; i++) {
            depth[i] = Double.POSITIVE_INFINITY;
        }
    }

    public double getDepth(int x, int y) {
        return depth[y * width + x];
    }

    public void setDepth(int x, int y, double value) {
        depth[y * width + x] = value;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

