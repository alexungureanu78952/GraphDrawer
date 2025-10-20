package org.example.renderer;

import org.example.model.Edge;
import org.example.model.Node;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class EdgeRenderer {
    private static final Color EDGE_COLOR = Color.GRAY;
    private static final int EDGE_WIDTH = 2;
    private static final int ARROW_SIZE = 12;

    public void draw(Graphics2D g2d, Edge edge, boolean drawArrow) {
        Node from = edge.getFrom();
        Node to = edge.getTo();

        double angle = Math.atan2(to.getY() - from.getY(), to.getX() - from.getX());
        int x1 = (int)(from.getX() + Node.getRadius() * Math.cos(angle));
        int y1 = (int)(from.getY() + Node.getRadius() * Math.sin(angle));
        int x2 = (int)(to.getX() - Node.getRadius() * Math.cos(angle));
        int y2 = (int)(to.getY() - Node.getRadius() * Math.sin(angle));

        g2d.setColor(EDGE_COLOR);
        g2d.setStroke(new BasicStroke(EDGE_WIDTH));
        g2d.drawLine(x1, y1, x2, y2);

        if (drawArrow) {
            drawArrowHead(g2d, x2, y2, angle);
        }
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
}