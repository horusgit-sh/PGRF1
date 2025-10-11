// Trida Polygon - slouzi pro ukladani vrcholu n-uhelniku
package polygon;

import java.util.*;

public class Polygon {
    // Seznam vrcholu polygonu
    private final List<Point> vertices = new ArrayList<>();

    // Prida novy vrchol do polygonu
    public void addVertex(Point p) {
        vertices.add(p);
    }

    // Vymaze vsechny vrcholy polygonu
    public void clear() {
        vertices.clear();
    }

    // Vrati pocet vrcholu
    public int size() {
        return vertices.size();
    }

    // Vrati vrchol podle indexu
    public Point get(int index) {
        return vertices.get(index);
    }

    // Vrati prvni vrchol (pro uzavreni polygonu)
    public Point getFirst() {
        return vertices.get(0);
    }

    // Vrati posledni vrchol
    public Point getLast() {
        return vertices.get(vertices.size() - 1);
    }
}
