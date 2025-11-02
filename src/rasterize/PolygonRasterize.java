// Trida pro spravu a vykresleni polygonu
package rasterize;

import controller.LineDrawer;
import model.Point;
import model.Polygon;

public class PolygonRasterize {
    // Raster pro kresleni
    private final RasterBufferedImage raster;
    // Nastroj pro kresleni usecek
    private final LineDrawer lineDrawer;
    // Ulozene vrcholy polygonu
    private final Polygon polygon = new Polygon();

    // Konstruktor - prijima raster a kreslic usecek
    public PolygonRasterize(RasterBufferedImage raster, LineDrawer lineDrawer) {
        this.raster = raster;
        this.lineDrawer = lineDrawer;
    }

    // Prida novy vrchol polygonu
    public void addVertex(int x, int y) {
        polygon.addVertex(new Point(x, y));
    }

    // Vykresli otevreny polygon (spoji vrcholy postupne)
    public void drawPolygon(boolean gradientMode) {
        if (polygon.size() < 2) return;
        for (int i = 0; i < polygon.size() - 1; i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get(i + 1);
            lineDrawer.drawLine(p1.x, p1.y, p2.x, p2.y, gradientMode);
        }
    }

    // Uzavre polygon spojenim posledniho a prvniho vrcholu
    public void closePolygon(boolean gradientMode) {
        if (polygon.size() > 2) {
            lineDrawer.drawLine(polygon.getLast().x, polygon.getLast().y,
                    polygon.getFirst().x, polygon.getFirst().y,
                    gradientMode);
            polygon.clear();
        }
    }

    // Smaze platno i data polygonu
    public void clearCanvas() {
        raster.clear();
        polygon.clear();
    }
}