package org.example.algorithm;

import org.example.model.Edge;
import org.example.model.GraphModel;
import org.example.model.Node;
import java.util.*;

public class TopologicalSort {
    private final GraphModel graph;
    private List<Node> sortedNodes;

    public TopologicalSort(GraphModel graph) {
        this.graph = graph;
    }

    public List<Node> sort() {
        if (!graph.isDirected()) {
            return null;
        }

        Map<Node, Integer> state = new HashMap<>();
        sortedNodes = new ArrayList<>();

        for (Node node : graph.getNodes()) {
            state.put(node, 0);
        }

        for (Node node : graph.getNodes()) {
            if (state.get(node) == 0) {
                if (!dfsIterative(node, state)) {
                    return null;
                }
            }
        }

        Collections.reverse(sortedNodes);
        return sortedNodes;
    }

    private boolean dfsIterative(Node start, Map<Node, Integer> state) {
        Stack<Node> processingStack = new Stack<>();
        Stack<Integer> phaseStack = new Stack<>();

        processingStack.push(start);
        phaseStack.push(0);

        while (!processingStack.isEmpty()) {
            Node current = processingStack.pop();
            int phase = phaseStack.pop();

            if (phase == 0) {
                if (state.get(current) == 2) {
                    continue;
                }

                state.put(current, 1);

                processingStack.push(current);
                phaseStack.push(1);

                List<Node> successors = getSuccessors(current);
                for (int i = successors.size() - 1; i >= 0; i--) {
                    Node successor = successors.get(i);
                    int successorState = state.get(successor);
                    if (successorState == 1) {
                        return false;
                    }
                    if (successorState == 0) {
                        state.put(successor, 1);
                        processingStack.push(successor);
                        phaseStack.push(0);
                    }
                }
            } else {
                state.put(current, 2);
                sortedNodes.add(current);
            }
        }

        return true;
    }

    private List<Node> getSuccessors(Node node) {
        List<Node> successors = new ArrayList<>();
        for (Edge edge : graph.getEdges()) {
            if (edge.getFrom().equals(node)) {
                successors.add(edge.getTo());
            }
        }
        return successors;
    }

    public boolean hasCycle() {
        return sort() == null;
    }
}
