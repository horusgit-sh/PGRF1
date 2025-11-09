package model;

/**
 * Speciální třída pro obdélník – dědí z Polygon.
 * Slouží pro úkol 2.
 */
public class Rectangle extends Polygon {

    /**
     * Nastaví vrcholy obdélníku podle základny (p1,p2)
     * a třetího bodu p3, který určuje výšku.
     */
    public void setVertices(Point p1, Point p2, Point p3) {

        clear(); // smaž staré vrcholy

        // vektor základny
        double bx = p2.x - p1.x;
        double by = p2.y - p1.y;

        // kolmá normála
        double nx = -by;
        double ny = bx;

        // normalizace
        double len = Math.sqrt(nx * nx + ny * ny);
        if (len == 0) return;

        nx /= len;
        ny /= len;

        // výška – projekce p3 -> základna
        double h = (p3.x - p1.x) * nx + (p3.y - p1.y) * ny;

        // dva zbývající body
        Point p4 = new Point((int) (p1.x + nx * h), (int) (p1.y + ny * h));
        Point p5 = new Point((int) (p2.x + nx * h), (int) (p2.y + ny * h));

        // Uložit 4 body obdélníku
        addVertex(p1);
        addVertex(p2);
        addVertex(p5);
        addVertex(p4);
    }
}