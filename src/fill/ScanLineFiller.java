package fill;

import rasterize.Raster;
import model.Point;
import model.Polygon;

import java.util.Arrays;

public class ScanLineFiller {

    public void fill(Polygon poly, Raster raster, int color) {

        // 1) найдём minY и maxY
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : poly.getVertices()) {
            if (p.getY() < minY) minY = p.getY();
            if (p.getY() > maxY) maxY = p.getY();
        }

        // 2) идём по горизонтальным линиям
        for (int y = minY; y <= maxY; y++) {

            // массив временных пересечений
            // (простой, без отдельных классов Edge)
            int[] xs = new int[poly.getVertices().size()];
            int count = 0;

            // 3) ищем пересечения линии y со всеми рёбрами полигона
            for (int i = 0; i < poly.getVertices().size(); i++) {

                Point p1 = poly.getVertices().get(i);
                Point p2 = poly.getVertices().get((i + 1) % poly.getVertices().size());

                // пропуск горизонтальных рёбер
                if (p1.getY() == p2.getY()) continue;

                // проверяем попадание
                if ((y >= Math.min(p1.getY(), p2.getY())) &&
                        (y < Math.max(p1.getY(), p2.getY()))) {

                    // вычисление X пересечения
                    float t = (float) (y - p1.getY()) / (float) (p2.getY() - p1.getY());
                    int x = (int) (p1.getX() + t * (p2.getX() - p1.getX()));

                    xs[count++] = x;
                }
            }

            // 4) сортируем найденные X
            Arrays.sort(xs, 0, count);

            // 5) красим попарно
            for (int i = 0; i < count; i += 2) {

                int xStart = xs[i];
                int xEnd = xs[i + 1];

                for (int x = xStart; x <= xEnd; x++) {
                    raster.setPixel(x, y, color);
                }
            }
        }
    }


    public void fill(Polygon poly, Raster raster, PatternFill pattern) {

        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : poly.getVertices()) {
            if (p.getY() < minY) minY = p.getY();
            if (p.getY() > maxY) maxY = p.getY();
        }

        for (int y = minY; y <= maxY; y++) {

            int[] xs = new int[poly.size()];
            int count = 0;

            for (int i = 0; i < poly.size(); i++) {
                Point p1 = poly.getVertices().get(i);
                Point p2 = poly.getVertices().get((i + 1) % poly.size());

                if (p1.getY() == p2.getY()) continue;

                if (y >= Math.min(p1.getY(), p2.getY()) &&
                        y < Math.max(p1.getY(), p2.getY())) {

                    float t = (float) (y - p1.getY()) / (float) (p2.getY() - p1.getY());
                    int x = (int) (p1.getX() + t * (p2.getX() - p1.getX()));
                    xs[count++] = x;
                }
            }

            Arrays.sort(xs, 0, count);

            for (int i = 0; i < count; i += 2) {
                int xStart = xs[i];
                int xEnd = xs[i + 1];

                for (int x = xStart; x <= xEnd; x++) {
                    int c = pattern.getPixelColor(x, y);
                    raster.setPixel(x, y, c);
                }
            }
        }
    }
}