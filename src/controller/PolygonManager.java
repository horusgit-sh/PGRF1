package controller;

import rasterize.RasterBufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PolygonManager {
    private final RasterBufferedImage raster;
    private final LineDrawer lineDrawer;
    private final List<Point> vertices = new ArrayList<>();

    private static class Point {
        int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
    }

    public PolygonManager(RasterBufferedImage raster, LineDrawer lineDrawer) {
        this.raster = raster;
        this.lineDrawer = lineDrawer;
    }

    public void addVertex(int x, int y) {
        vertices.add(new Point(x, y));
    }

    public void drawPolygon() {
        if (vertices.size() < 2)  return;
        for (int i = 0; i < vertices.size() - 1; i++) {
            Point p1 = vertices.get(i);
            Point p2 = vertices.get(i + 1);
            lineDrawer.drawLine(p1.x, p1.y, p2.x, p2.y, false);
        }
    }

    public void closePolygon() {
        if (vertices.size() > 2) {
            Point first = vertices.get(0);
            Point last = vertices.get(vertices.size() - 1);
            lineDrawer.drawLine(last.x, last.y, first.x, first.y, false);
            vertices.clear();
        }
    }

    public void clearCanvas() {
        raster.clear();
        vertices.clear();
    }
}