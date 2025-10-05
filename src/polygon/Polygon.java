package polygon;

import java.util.*;

public class Polygon {
    private final List<Point> vertices = new ArrayList<>();

    public void addVertex(Point p) {
        vertices.add(p);
    }

    public List<Point> getVertices() {
        return vertices;
    }

    public void clear() {
        vertices.clear();
    }

    public int size() {
        return vertices.size();
    }

    public Point get(int index) {
        return vertices.get(index);
    }

    public Point getFirst() {
        return vertices.get(0);
    }

    public Point getLast() {
        return vertices.get(vertices.size() - 1);
    }
}
