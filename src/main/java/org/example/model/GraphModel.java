package org.example.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphModel {
    private final ArrayList<Node> nodes;
    private final ArrayList<Edge> edges;
    private int nextId;
    private boolean isDirected;
    private final Map<Node, List<Edge>> outgoing;
    private final Map<Node, List<Node>> successors;

    public GraphModel(boolean isDirected) {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        nextId = 0;
        this.isDirected = isDirected;
        this.outgoing = new HashMap<>();
        this.successors = new HashMap<>();
    }

    public void setDirected(boolean directed) {
        this.isDirected = directed;
        rebuildOutgoing();
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
        rebuildOutgoing();
        return newNode;
    }

    public boolean addEdge(Node from, Node to) {
        if (from == null || to == null) {
            return false;
        }
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
        rebuildOutgoing();
        return true;
    }

    public boolean addEdge(Node from, Node to, int cost) {
        if (from == null || to == null) {
            return false;
        }
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

        edges.add(new Edge(from, to, cost));
        rebuildOutgoing();
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
        if (node == null) return false;
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

        if (removed) {
            rebuildOutgoing();
        }

        return removed;
    }

    private void rebuildOutgoing() {
        outgoing.clear();
        successors.clear();
        for (Node n : nodes) {
            outgoing.put(n, new ArrayList<>());
            successors.put(n, new ArrayList<>());
        }
        for (Edge e : edges) {
            Node from = e.getFrom();
            Node to = e.getTo();
            if (from == null || to == null) continue;
            outgoing.computeIfAbsent(from, k -> new ArrayList<>()).add(e);
            successors.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            if (!isDirected) {
                Edge reverse = new Edge(to, from, e.getCost());
                outgoing.computeIfAbsent(to, k -> new ArrayList<>()).add(reverse);
                successors.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            }
        }
    }

    public List<Edge> getOutgoingEdges(Node node) {
        return outgoing.getOrDefault(node, new ArrayList<>());
    }

    public List<Node> getSuccessors(Node node) {
        return successors.getOrDefault(node, new ArrayList<>());
    }
}