// Trida Polygon - slouzi pro ukladani vrcholu n-uhelniku
package model;

import java.util.*;

public class Polygon {
    // Seznam vrcholu polygonu
    private final List<Point> points = new ArrayList<>();



    // Prida novy vrchol do polygonu
    public void addVertex(Point p) {
        points.add(p);
    }

    public  List<Point> getVertices() {
        return points;
    }

    // Vymaze vsechny vrcholy polygonu
    public void clear() {
        points.clear();
    }

    // Vrati pocet vrcholu
    public int size() {
        return points.size();
    }

    // Vrati vrchol podle indexu
    public Point get(int index) {
        return points.get(index);
    }

    // Vrati prvni vrchol (pro uzavreni polygonu)
    public Point getFirst() {
        return points.get(0);
    }

    // Vrati posledni vrchol
    public Point getLast() {
        return points.get(points.size() - 1);
    }
}
