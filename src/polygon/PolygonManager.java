package polygon;

import controller.LineDrawer;
import rasterize.RasterBufferedImage;

public class PolygonManager {
    private final RasterBufferedImage raster;
    private final LineDrawer lineDrawer;
    private final Polygon polygon = new Polygon();

    public PolygonManager(RasterBufferedImage raster, LineDrawer lineDrawer) {
        this.raster = raster;
        this.lineDrawer = lineDrawer;
    }

    public void addVertex(int x, int y) {
        polygon.addVertex(new Point(x, y));
    }

    public void drawPolygon(boolean gradientMode) {
        if (polygon.size() < 2) return;
        for (int i = 0; i < polygon.size() - 1; i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get(i + 1);
            lineDrawer.drawLine(p1.x, p1.y, p2.x, p2.y, gradientMode);
        }
    }

    public void closePolygon(boolean gradientMode) {
        if (polygon.size() > 2) {
            lineDrawer.drawLine(polygon.getLast().x, polygon.getLast().y,
                    polygon.getFirst().x, polygon.getFirst().y,
                    gradientMode);
            polygon.clear();
        }
    }

    public void clearCanvas() {
        raster.clear();
        polygon.clear();
    }
}