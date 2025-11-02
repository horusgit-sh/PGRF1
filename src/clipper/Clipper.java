package clipper;

import model.Point;
import model.Polygon;
import java.util.ArrayList;
import java.util.List;

/**
 * Very simple Sutherland‑Hodgman polygon clipper (convex clip only)
 */
public class Clipper {

    public Polygon clip(Polygon subject, Polygon clipWindow) {
        if (subject == null || clipWindow == null || subject.size() < 3 || clipWindow.size() < 3) {
            return null;
        }

        List<Point> out = new ArrayList<>(subject.getVertices());

        for (int i = 0; i < clipWindow.size(); i++) {
            Point A = clipWindow.get(i);
            Point B = clipWindow.get((i + 1) % clipWindow.size());

            List<Point> newOut = new ArrayList<>();
            Point S = out.get(out.size() - 1);

            for (Point P : out) {
                boolean sIn = inside(S, A, B);
                boolean pIn = inside(P, A, B);

                if (pIn) {
                    if (!sIn) newOut.add(intersect(S, P, A, B));
                    newOut.add(P);
                } else if (sIn) {
                    newOut.add(intersect(S, P, A, B));
                }
                S = P;
            }
            out = newOut;
            if (out.isEmpty()) break;
        }

        if (out.size() < 3) return null;

        Polygon result = new Polygon();
        for (Point p : out) result.addVertex(p);
        return result;
    }

    private boolean inside(Point P, Point A, Point B) {
        return ((B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x)) >= 0;
    }

    private Point intersect(Point S, Point P, Point A, Point B) {
        double dx1 = P.x - S.x, dy1 = P.y - S.y;
        double dx2 = B.x - A.x, dy2 = B.y - A.y;
        double dx3 = S.x - A.x, dy3 = S.y - A.y;
        double denom = dy2 * dx1 - dx2 * dy1;
        if (Math.abs(denom) < 1e-10) return S;
        double t = (dx2 * dy3 - dy2 * dx3) / denom;
        return new Point((int)Math.round(S.x + t * dx1), (int)Math.round(S.y + t * dy1));
    }
}
