package org.example.renderer;

import org.example.model.Edge;
import org.example.model.Node;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;

public class EdgeRenderer {
    private static final Color EDGE_COLOR = Color.GRAY;
    private static final Color HIGHLIGHTED_COLOR = Color.GREEN;
    private static final int EDGE_WIDTH = 2;
    private static final int HIGHLIGHTED_WIDTH = 4;
    private static final int ARROW_SIZE = 12;

    public void draw(Graphics2D g2d, Edge edge, boolean drawArrow) {
        draw(g2d, edge, drawArrow, false);
    }

    public void draw(Graphics2D g2d, Edge edge, boolean drawArrow, boolean highlighted) {
        Node from = edge.getFrom();
        Node to = edge.getTo();

        double angle = Math.atan2(to.getY() - from.getY(), to.getX() - from.getX());
        int x1 = (int)(from.getX() + Node.getRadius() * Math.cos(angle));
        int y1 = (int)(from.getY() + Node.getRadius() * Math.sin(angle));
        int x2 = (int)(to.getX() - Node.getRadius() * Math.cos(angle));
        int y2 = (int)(to.getY() - Node.getRadius() * Math.sin(angle));

        if (highlighted) {
            g2d.setColor(HIGHLIGHTED_COLOR);
            g2d.setStroke(new BasicStroke(HIGHLIGHTED_WIDTH));
        } else {
            g2d.setColor(EDGE_COLOR);
            g2d.setStroke(new BasicStroke(EDGE_WIDTH));
        }

        g2d.drawLine(x1, y1, x2, y2);

        if (drawArrow) {
            drawArrowHead(g2d, x2, y2, angle);
        }

        drawCost(g2d, edge, x1, y1, x2, y2);
    }

    private void drawArrowHead(Graphics2D g2d, int x, int y, double angle) {
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];

        xPoints[0] = x;
        yPoints[0] = y;
        xPoints[1] = (int)(x - ARROW_SIZE * Math.cos(angle - Math.PI / 6));
        yPoints[1] = (int)(y - ARROW_SIZE * Math.sin(angle - Math.PI / 6));
        xPoints[2] = (int)(x - ARROW_SIZE * Math.cos(angle + Math.PI / 6));
        yPoints[2] = (int)(y - ARROW_SIZE * Math.sin(angle + Math.PI / 6));

        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    private void drawCost(Graphics2D g2d, Edge edge, int x1, int y1, int x2, int y2) {
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;

        String costStr = String.valueOf(edge.getCost());
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(costStr);
        int textHeight = fm.getAscent();

        g2d.fillRect(midX - textWidth / 2 - 2, midY - textHeight / 2 - 2, textWidth + 4, textHeight + 4);
        g2d.setColor(Color.BLACK);
        g2d.drawString(costStr, midX - textWidth / 2, midY + textHeight / 2 - 2);
    }
}