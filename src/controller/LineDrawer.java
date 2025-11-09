package controller;

import rasterize.FilledLineRasterizer;
import rasterize.LineRasterizer;

import java.awt.*;

// Trida pro kresleni usecek
public class LineDrawer {

    // Konkretni rasterizator usecek
    private final LineRasterizer lineRasterizer;
    // Zacatecni barva pro gradient
    private final int color1 = Color.YELLOW.getRGB();
    // Koncova barva pro gradient
    private final int color2 = Color.BLUE.getRGB();

    private Color color = Color.RED;

    // Konstruktor - prijima rasterizator usecek
    public LineDrawer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    // Vykresli usecku, podle rezimu bud normalne nebo s barevnym prechodem
    public void drawLine(int x1, int y1, int x2, int y2, boolean gradientMode) {
        if (lineRasterizer instanceof FilledLineRasterizer raster) {
            if (gradientMode) {
                raster.rasterizeWithGradient(x1, y1, color1, x2, y2, color2);
            } else {
                raster.rasterize(x1, y1, x2, y2, color);
            }
        } else {
            lineRasterizer.rasterize(x1, y1, x2, y2, color);
        }
    }


    // Vrati koncovy bod zarovnany na horizontalni, svislou nebo uhlopricnou linii (SHIFT rezim)
    public int[] getSnappedPoint(int startX, int startY, int endX, int endY) {
        int dx = endX - startX;
        int dy = endY - startY;
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        angle = (angle + 360) % 360;

        int dist = Math.min(Math.abs(dx), Math.abs(dy));
        int newX = endX;
        int newY = endY;

        if (angle >= 337.5 || angle < 22.5){
            newY = startY; // horizontal
        }
        else if (angle < 67.5) {
            newX = startX + dist; newY = startY + dist;
        }
        else if (angle < 112.5) {
            newX = startX; // vertical up
        }
        else if (angle < 157.5) {
            newX = startX - dist; newY = startY + dist;
        }
        else if (angle < 202.5){
            newY = startY; // horizontal left
        }
        else if (angle < 247.5) {
            newX = startX - dist; newY = startY - dist;
        }
        else if (angle < 292.5){
            newX = startX; // vertical down
        }
        else {
            newX = startX + dist; newY = startY - dist; // diagonal
        }

        return new int[]{newX, newY};
    }



    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}