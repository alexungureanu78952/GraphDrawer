package org.example.model;

import java.util.ArrayList;
import java.util.Iterator;

public class GraphModel {
    private final ArrayList<Node> nodes;
    private final ArrayList<Edge> edges;
    private int nextId;
    private boolean isDirected;

    public GraphModel(boolean isDirected) {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        nextId = 0;
        this.isDirected = isDirected;
    }

    public void setDirected(boolean directed) {
        this.isDirected = directed;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public Node addNode(int x, int y) {
        Node newNode = new Node(nextId++, x, y);
        for (Node n : nodes) {
            if (n.overlaps(newNode)) {
                return null;
            }
        }
        nodes.add(newNode);
        return newNode;
    }

    public boolean addEdge(Node from, Node to) {
        if (from == to) {
            return false;
        }

        for (Edge e : edges) {
            if (isDirected) {
                if (e.connectsDirected(from, to)) {
                    return false;
                }
            } else {
                if (e.connects(from, to)) {
                    return false;
                }
            }
        }

        edges.add(new Edge(from, to));
        return true;
    }

    public Node getNodeAt(int x, int y) {
        for (Node n : nodes) {
            if (n.contains(x, y)) {
                return n;
            }
        }
        return null;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public int[][] getAdjacencyMatrix() {
        int n = nodes.size();
        int[][] matrix = new int[n][n];

        for (Edge e : edges) {
            int fromId = e.getFrom().getId();
            int toId = e.getTo().getId();
            matrix[fromId][toId]++;
            if (!isDirected) {
                matrix[toId][fromId]++;
            }
        }

        return matrix;
    }
    public boolean removeAllEdgesFromNode(Node node) {
        boolean removed = false;

        Iterator<Edge> iterator = edges.iterator();
        while (iterator.hasNext()) {
            Edge e = iterator.next();
            if (isDirected) {
                if (e.getFrom().equals(node)) {
                    iterator.remove();
                    removed = true;
                }
            } else {
                if (e.getFrom().equals(node) || e.getTo().equals(node)) {
                    iterator.remove();
                    removed = true;
                }
            }
        }

        return removed;
    }
}