package org.example.model;

public class Node {
    private final int id;
    private int x;
    private int y;
    private static final int RADIUS = 25;
    private String customLabel;

    public Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.customLabel = null;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static int getRadius() {
        return RADIUS;
    }

    public boolean contains(int px, int py) {
        int dx = px - x;
        int dy = py - y;
        return dx * dx + dy * dy <= RADIUS * RADIUS;
    }

    public boolean overlaps(Node other) {
        int dx = x - other.x;
        int dy = y - other.y;
        int minDist = 2 * RADIUS + 10;
        return dx * dx + dy * dy < minDist * minDist;
    }

    public String getCustomLabel() {
        return customLabel;
    }

    public void setCustomLabel(String customLabel) {
        this.customLabel = customLabel;
    }

    public String getDisplayLabel() {
        return customLabel != null ? customLabel : String.valueOf(id);
    }
}