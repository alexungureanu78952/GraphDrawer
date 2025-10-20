package org.example;

import org.example.ui.GraphDrawerFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphDrawerFrame frame = new GraphDrawerFrame();
            frame.setVisible(true);
        });
    }
}