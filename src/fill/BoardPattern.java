package fill;

public class BoardPattern implements PatternFill {

    private final int cellSize;
    private final int color1;
    private final int color2;

    public BoardPattern(int cellSize, int color1, int color2) {
        this.cellSize = cellSize;
        this.color1 = color1;
        this.color2 = color2;
    }

    @Override
    public int getPixelColor(int x, int y) {
        int cx = x / cellSize;
        int cy = y / cellSize;
        return ((cx + cy) % 2 == 0) ? color1 : color2;
    }
}