package fill;

import rasterize.Raster;
import model.Point;

import java.util.Stack;

public class SeedFiller {

    public void fill(int startX, int startY, Raster raster, int fillColor) {

        int targetColor = raster.getPixel(startX, startY);
        if (targetColor == fillColor) return;

        // zasobnik pro flood-fill (pozadi)
        Stack<Point> stack = new Stack<>();
        // vlozime pocatecni pixel vyplnovani
        stack.push(new Point(startX, startY));
        // zpracovavame dokud jsou pixely na zasobniku
        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int x = p.getX();
            int y = p.getY();

            // test mimo hranice rasteru
            if (x < 0 || y < 0 || x >= raster.getSirska() || y >= raster.getVyska()) continue;

            // pokud barva neodpovida cilove -> nepokracujeme
            if (raster.getPixel(x, y) != targetColor) continue;

            // obarvime aktualni pixel
            raster.setPixel(x, y, fillColor);

            // pridame sousedni pixely (4-smerna sousednost)
            stack.push(new Point(x + 1, y));
            stack.push(new Point(x - 1, y));
            stack.push(new Point(x, y + 1));
            stack.push(new Point(x, y - 1));
        }
    }

    public void fillBoundary(int startX, int startY, Raster raster, int fillColor, int boundaryColor) {
        // zasobnik pro flood-fill v rezimu ohraniceni
        Stack<Point> stack = new Stack<>();
        // vlozime pocatecni pixel vyplnovani
        stack.push(new Point(startX, startY));
        // iterujeme dokud mame pixely k provereni
        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int x = p.getX();
            int y = p.getY();
            // testujeme zda pixel nelezi mimo raster
            if (x < 0 || y < 0 || x >= raster.getSirska() || y >= raster.getVyska()) continue;
            int c = raster.getPixel(x, y);
            // pokud jsme dosahli barvy vyplne nebo barvy hranice -> zastavime v tomto smeru
            if (c == fillColor || c == boundaryColor) continue;
            // obarvime aktualni pixel barvou vyplne
            raster.setPixel(x, y, fillColor);
            // pridame sousedy (4-smerne) pro dalsi zpracovani
            stack.push(new Point(x + 1, y));
            stack.push(new Point(x - 1, y));
            stack.push(new Point(x, y + 1));
            stack.push(new Point(x, y - 1));
        }
    }
}