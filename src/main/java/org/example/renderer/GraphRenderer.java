package org.example.renderer;

import org.example.model.Edge;
import org.example.model.GraphModel;
import org.example.model.Node;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GraphRenderer {
    private NodeRenderer nodeRenderer;
    private EdgeRenderer edgeRenderer;

    public GraphRenderer() {
        nodeRenderer = new NodeRenderer();
        edgeRenderer = new EdgeRenderer();
    }

    public void render(Graphics2D g2d, GraphModel graph, Node selectedNode) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Edge edge : graph.getEdges()) {
            edgeRenderer.draw(g2d, edge, graph.isDirected());
        }

        for (Node node : graph.getNodes()) {
            nodeRenderer.draw(g2d, node, node == selectedNode);
        }
    }
}