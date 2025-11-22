package org.example.renderer;

import org.example.model.Node;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class NodeRenderer {
    private static final Color NODE_COLOR = Color.WHITE;
    private static final Color SELECTED_COLOR = Color.YELLOW;
    private static final Color HIGHLIGHTED_COLOR = Color.CYAN;
    private static final Color BORDER_COLOR = Color.GRAY;
    private static final Color[] COMPONENT_COLORS = {
        new Color(255, 100, 100),
        new Color(100, 255, 100),
        new Color(100, 100, 255),
        new Color(255, 255, 100),
        new Color(255, 100, 255),
        new Color(100, 255, 255),
        new Color(255, 150, 100),
        new Color(150, 255, 100),
        new Color(100, 150, 255),
        new Color(255, 200, 150)
    };

    public void draw(Graphics2D g2d, Node node, boolean isSelected) {
        draw(g2d, node, isSelected, false, null);
    }

    public void draw(Graphics2D g2d, Node node, boolean isSelected, boolean isHighlighted) {
        draw(g2d, node, isSelected, isHighlighted, null);
    }

    public void draw(Graphics2D g2d, Node node, boolean isSelected, boolean isHighlighted, Integer componentId) {
        int radius = Node.getRadius();

        if (componentId != null && componentId >= 0) {
            g2d.setColor(COMPONENT_COLORS[componentId % COMPONENT_COLORS.length]);
        } else if (isHighlighted) {
            g2d.setColor(HIGHLIGHTED_COLOR);
        } else if (isSelected) {
            g2d.setColor(SELECTED_COLOR);
        } else {
            g2d.setColor(NODE_COLOR);
        }

        g2d.fillOval(node.getX() - radius, node.getY() - radius, 2 * radius, 2 * radius);
        g2d.setColor(BORDER_COLOR);
        g2d.drawOval(node.getX() - radius, node.getY() - radius, 2 * radius, 2 * radius);

        drawLabel(g2d, node);
    }

    private void drawLabel(Graphics2D g2d, Node node) {
        String label = node.getDisplayLabel();
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        int textHeight = fm.getAscent();
        g2d.drawString(label, node.getX() - textWidth / 2, node.getY() + textHeight / 2 - 2);
    }
}