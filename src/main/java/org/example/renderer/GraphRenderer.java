package org.example.renderer;

import org.example.model.Edge;
import org.example.model.GraphModel;
import org.example.model.Node;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import java.util.Map;

public class GraphRenderer {
    private NodeRenderer nodeRenderer;
    private EdgeRenderer edgeRenderer;

    public GraphRenderer() {
        nodeRenderer = new NodeRenderer();
        edgeRenderer = new EdgeRenderer();
    }

    public void render(Graphics2D g2d, GraphModel graph, Node selectedNode) {
        render(g2d, graph, selectedNode, null, null);
    }

    public void render(Graphics2D g2d, GraphModel graph, Node selectedNode, List<Node> highlightedPath) {
        render(g2d, graph, selectedNode, highlightedPath, null);
    }

    public void render(Graphics2D g2d, GraphModel graph, Node selectedNode, List<Node> highlightedPath, Map<Node, Integer> componentColors) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Edge edge : graph.getEdges()) {
            boolean highlight = false;
            if (highlightedPath != null && highlightedPath.size() > 1) {
                for (int i = 0; i < highlightedPath.size() - 1; i++) {
                    if (edge.getFrom().equals(highlightedPath.get(i)) &&
                        edge.getTo().equals(highlightedPath.get(i + 1))) {
                        highlight = true;
                        break;
                    }
                }
            }
            edgeRenderer.draw(g2d, edge, graph.isDirected(), highlight);
        }

        for (Node node : graph.getNodes()) {
            boolean highlight = highlightedPath != null && highlightedPath.contains(node);
            Integer componentId = (componentColors != null) ? componentColors.get(node) : null;
            nodeRenderer.draw(g2d, node, node == selectedNode, highlight, componentId);
        }
    }
}