import view.Window;
import controller.Controller3D;

public class Main {
    public static void main(String[] args) {
        Window window = new Window(1300, 800);
        new Controller3D(window.getPanel());
    }
}