package controller;

import rasterize.FilledLineRasterizer;
import rasterize.LineRasterizer;

public class LineDrawer {

    private final LineRasterizer lineRasterizer;
    private final int color1 = 0xFF0000;
    private final int color2 = 0x0000FF;

    public LineDrawer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void drawLine(int x1, int y1, int x2, int y2, boolean gradientMode) {
        if (lineRasterizer instanceof FilledLineRasterizer raster) {
            if (gradientMode) {
                raster.rasterizeWithGradient(x1, y1, color1, x2, y2, color2);
            } else {
                raster.rasterize(x1, y1, x2, y2);
            }
        } else {
            lineRasterizer.rasterize(x1, y1, x2, y2);
        }
    }


    public int[] getSnappedPoint(int startX, int startY, int endX, int endY) {
        int dx = endX - startX;
        int dy = endY - startY;
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        angle = (angle + 360) % 360;

        int dist = Math.min(Math.abs(dx), Math.abs(dy));
        int newX = endX;
        int newY = endY;

        if (angle >= 337.5 || angle < 22.5) newY = startY;          // horizontal right
        else if (angle < 67.5) {
            newX = startX + dist; newY = startY + dist;
        }
        else if (angle < 112.5) newX = startX;                      // vertical up
        else if (angle < 157.5) {
            newX = startX - dist; newY = startY + dist;
        }
        else if (angle < 202.5) newY = startY;                      // horizontal left
        else if (angle < 247.5) {
            newX = startX - dist; newY = startY - dist;
        }
        else if (angle < 292.5) newX = startX;                      // vertical down
        else {
            newX = startX + dist; newY = startY - dist;
        }        // diagonal

        return new int[]{newX, newY};
    }
}