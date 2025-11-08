package org.example.algorithm;

import org.example.model.Edge;
import org.example.model.GraphModel;
import org.example.model.Node;
import java.util.*;

public class ShortestPath {
    private final GraphModel graph;
    private final Map<Node, Integer> distances;
    private final Map<Node, Node> predecessors;

    public ShortestPath(GraphModel graph) {
        this.graph = graph;
        this.distances = new HashMap<>();
        this.predecessors = new HashMap<>();
    }

    public boolean compute(Node source) {
        TopologicalSort topoSort = new TopologicalSort(graph);
        List<Node> topologicalOrder = topoSort.sort();

        if (topologicalOrder == null) {
            return false;
        }

        for (Node node : graph.getNodes()) {
            distances.put(node, Integer.MAX_VALUE);
            predecessors.put(node, null);
        }

        distances.put(source, 0);

        int startIndex = topologicalOrder.indexOf(source);

        for (int k = startIndex; k < topologicalOrder.size(); k++) {
            Node i = topologicalOrder.get(k);

            if (distances.get(i) == Integer.MAX_VALUE) {
                continue;
            }

            for (Edge edge : graph.getEdges()) {
                if (edge.getFrom().equals(i)) {
                    Node j = edge.getTo();
                    int newDist = distances.get(i) + edge.getCost();

                    if (newDist < distances.get(j)) {
                        distances.put(j, newDist);
                        predecessors.put(j, i);
                    }
                }
            }
        }

        return true;
    }

    public List<Node> getPath(Node target) {
        if (distances.get(target) == Integer.MAX_VALUE) {
            return null;
        }

        List<Node> path = new ArrayList<>();
        Node current = target;

        while (current != null) {
            path.add(0, current);
            current = predecessors.get(current);
        }

        return path;
    }

    public int getDistance(Node node) {
        return distances.getOrDefault(node, Integer.MAX_VALUE);
    }
}
