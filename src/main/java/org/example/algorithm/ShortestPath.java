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
        if (startIndex == -1) {
            return false;
        }

        for (int k = startIndex; k < topologicalOrder.size(); k++) {
            Node i = topologicalOrder.get(k);

            Integer distIobj = distances.get(i);
            if (distIobj == null || distIobj == Integer.MAX_VALUE) {
                continue;
            }
            long distI = distIobj.longValue();

            List<Edge> outs = graph.getOutgoingEdges(i);
            for (Edge edge : outs) {
                Node j = edge.getTo();
                long newDistLong = distI + (long) edge.getCost();
                int currentJ = distances.getOrDefault(j, Integer.MAX_VALUE);
                long currentJLong = (currentJ == Integer.MAX_VALUE) ? Long.MAX_VALUE : (long) currentJ;

                if (newDistLong < currentJLong && newDistLong <= Integer.MAX_VALUE) {
                    distances.put(j, (int) newDistLong);
                    predecessors.put(j, i);
                }
            }
        }

        return true;
    }

    public List<Node> getPath(Node target) {
        if (distances.get(target) == Integer.MAX_VALUE) {
            return null;
        }

        LinkedList<Node> path = new LinkedList<>();
        Node current = target;

        while (current != null) {
            path.addFirst(current);
            current = predecessors.get(current);
        }

        return path;
    }

    public int getDistance(Node node) {
        return distances.getOrDefault(node, Integer.MAX_VALUE);
    }
}
