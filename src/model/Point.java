package model;

// Trida Point - reprezentuje bod v rovine (x, y)
public class Point {
    // Souradnice bodu
    public final int x, y;

    // Konstruktor bodu se souradnicemi x a y
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
