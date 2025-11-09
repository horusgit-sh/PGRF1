package fill;

import rasterize.Raster;
import model.Point;

import java.util.Stack;

public class SeedFiller {

    public void fill(int startX, int startY, Raster raster, int fillColor) {

        int targetColor = raster.getPixel(startX, startY);
        if(targetColor == fillColor) return; // ничего делать не надо

        Stack<Point> stack = new Stack<>();
        stack.push(new Point(startX, startY));

        while(!stack.isEmpty()) {
            Point p = stack.pop();
            int x = p.getX();
            int y = p.getY();

            // проверка выхода за границы
            if(x < 0 || y < 0 || x >= raster.getSirska() || y >= raster.getVyska()) continue;

            // проверка цвета
            if(raster.getPixel(x,y) != targetColor) continue;

            // красим пиксель
            raster.setPixel(x,y,fillColor);

            // добавляем соседей (4-связность)
            stack.push(new Point(x+1, y));
            stack.push(new Point(x-1, y));
            stack.push(new Point(x, y+1));
            stack.push(new Point(x, y-1));
        }
    }
    public void fillBoundary(int startX, int startY, Raster raster, int fillColor, int boundaryColor) {
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(startX, startY));
        while(!stack.isEmpty()){
            Point p = stack.pop();
            int x=p.getX(); int y=p.getY();
            if(x<0||y<0||x>=raster.getSirska()||y>=raster.getVyska()) continue;
            int c=raster.getPixel(x,y);
            if(c==fillColor || c==boundaryColor) continue;
            raster.setPixel(x,y,fillColor);
            stack.push(new Point(x+1,y));
            stack.push(new Point(x-1,y));
            stack.push(new Point(x,y+1));
            stack.push(new Point(x,y-1));
        }
    }
}