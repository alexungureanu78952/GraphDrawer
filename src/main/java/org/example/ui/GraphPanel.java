package org.example.ui;

import org.example.model.GraphModel;
import org.example.controller.MouseController;
import org.example.io.FileManager;
import org.example.renderer.GraphRenderer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GraphPanel extends JPanel {
    private GraphModel graph;
    private GraphRenderer renderer;
    private MouseController controller;

    public GraphPanel(GraphModel graph, FileManager fileManager) {
        this.graph = graph;
        this.renderer = new GraphRenderer();
        this.controller = new MouseController(graph, fileManager, this);

        setBackground(Color.BLACK);
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        renderer.render(g2d, graph, controller.getSelectedNode());
    }
}