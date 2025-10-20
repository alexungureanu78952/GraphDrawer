package org.example.model;

public class Edge {
    private final Node from;
    private final Node to;

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public boolean connects(Node n1, Node n2) {
        return (from == n1 && to == n2) || (from == n2 && to == n1);
    }

    public boolean connectsDirected(Node n1, Node n2) {
        return from == n1 && to == n2;
    }
}