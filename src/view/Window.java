package view;

import javax.swing.*;
import java.awt.*;

// Hlavni okno aplikace
public class Window extends JFrame {

    // Vykreslovaci panel
    private final Panel panel;

    // Vytvori okno s danym rozmerem a vlozi do nej panel
    public Window(int width, int heigth) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("PGRF2 2025/2026");
        setVisible(true);

        panel = new Panel(width, heigth);
        add(panel);
        pack();

        panel.setFocusable(true);
        panel.grabFocus();
    }

    public Panel getPanel() {
        return panel;
    }
}
