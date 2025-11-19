package org.example.ui;

import org.example.model.GraphModel;
import org.example.controller.MouseController;
import org.example.controller.KeyboardController;
import org.example.io.FileManager;
import org.example.renderer.GraphRenderer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import javax.swing.JPanel;

public class GraphPanel extends JPanel {
    private final GraphModel graph;
    private final GraphRenderer renderer;
    private final MouseController mouseController;
    private final KeyboardController keyboardController;

    public GraphPanel(GraphModel graph, FileManager fileManager) {
        this.graph = graph;
        this.renderer = new GraphRenderer();
        this.mouseController = new MouseController(graph, fileManager, this);
        this.keyboardController = new KeyboardController(graph, this, mouseController);

        setBackground(Color.BLACK);
        addMouseListener(mouseController);
        addMouseMotionListener(mouseController);
        addKeyListener(keyboardController);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        renderer.render(g2d, graph, mouseController.getSelectedNode(), keyboardController.getCurrentPath());

        drawInstructions(g2d);
    }

    private void drawInstructions(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));

        int y = 20;
        g2d.drawString("Apasa 'H' pentru ajutor", 10, y);

        if (keyboardController.getSourceNode() != null) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Nod sursa: " + keyboardController.getSourceNode().getId() + " | Apasa SPATIU pentru drumuri", 10, y + 20);
        } else if (mouseController.getSelectedNode() != null) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Nod selectat: " + mouseController.getSelectedNode().getId() + " | Apasa 'S' pentru a seta ca sursa", 10, y + 20);
        }
    }
}
