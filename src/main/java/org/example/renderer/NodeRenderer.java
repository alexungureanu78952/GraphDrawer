package org.example.renderer;

import org.example.model.Node;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class NodeRenderer {
    private static final Color NODE_COLOR = Color.WHITE;
    private static final Color SELECTED_COLOR = Color.YELLOW;
    private static final Color BORDER_COLOR = Color.GRAY;

    public void draw(Graphics2D g2d, Node node, boolean isSelected) {
        int radius = Node.getRadius();

        if (isSelected) {
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
        String label = String.valueOf(node.getId());
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        int textHeight = fm.getAscent();
        g2d.drawString(label, node.getX() - textWidth / 2, node.getY() + textHeight / 2 - 2);
    }
}