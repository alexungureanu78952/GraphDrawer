package org.example.algorithm;

import org.example.model.GraphModel;
import org.example.model.Node;
import java.util.*;

public class TopologicalSort {
    private final GraphModel graph;

    public TopologicalSort(GraphModel graph) {
        this.graph = graph;
    }

    public List<Node> sort() {
        if (!graph.isDirected()) return null;

        Map<Node, Integer> state = new HashMap<>();
        List<Node> sorted = new ArrayList<>();
        for (Node n : graph.getNodes()) state.put(n, 0);

        for (Node n : graph.getNodes()) {
            if (state.get(n) == 0) {
                if (!dfsIterative(n, state, sorted)) return null;
            }
        }

        Collections.reverse(sorted);
        return sorted;
    }

    private boolean dfsIterative(Node start, Map<Node, Integer> state, List<Node> sorted) {
        Stack<Node> processingStack = new Stack<>();
        Stack<Integer> phaseStack = new Stack<>();
        Set<Node> pushed = new HashSet<>();

        processingStack.push(start);
        phaseStack.push(0);
        pushed.add(start);

        while (!processingStack.isEmpty()) {
            Node current = processingStack.pop();
            int phase = phaseStack.pop();

            if (phase == 0) {
                if (state.getOrDefault(current, 0) == 2) continue;
                if (state.getOrDefault(current, 0) == 1) return false;

                state.put(current, 1);

                processingStack.push(current);
                phaseStack.push(1);

                List<Node> successors = graph.getSuccessors(current);
                for (int i = successors.size() - 1; i >= 0; i--) {
                    Node s = successors.get(i);
                    int sstate = state.getOrDefault(s, 0);
                    if (sstate == 1) return false;
                    if (sstate == 0 && !pushed.contains(s)) {
                        processingStack.push(s);
                        phaseStack.push(0);
                        pushed.add(s);
                    }
                }
            } else {
                state.put(current, 2);
                sorted.add(current);
            }
        }

        return true;
    }
}