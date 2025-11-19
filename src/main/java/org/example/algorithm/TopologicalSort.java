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
        if (!graph.isDirected()) {
            return null;
        }

        List<Node> order = new ArrayList<>();
        Map<Node, Integer> state = new HashMap<>();
        for (Node n : graph.getNodes()) state.put(n, 0);

        Stack<Node> stack = new Stack<>();
        Map<Node, Integer> nextIndex = new HashMap<>();
        Set<Node> pushed = new HashSet<>();

        for (Node start : graph.getNodes()) {
            if (state.getOrDefault(start, 0) != 0) continue;

            stack.clear();
            nextIndex.clear();
            pushed.clear();
            stack.push(start);
            pushed.add(start);

            while (!stack.isEmpty()) {
                Node v = stack.peek();
                int st = state.getOrDefault(v, 0);
                if (st == 0) {
                    state.put(v, 1);
                    nextIndex.put(v, 0);
                }

                List<Node> succ = graph.getSuccessors(v);
                int idx = nextIndex.getOrDefault(v, 0);
                if (idx < succ.size()) {
                    Node s = succ.get(idx);
                    nextIndex.put(v, idx + 1);
                    int sstate = state.getOrDefault(s, 0);
                    if (sstate == 0 && !pushed.contains(s)) {
                        stack.push(s);
                        pushed.add(s);
                    } else if (sstate == 1) {
                        return null;
                    }
                } else {
                    state.put(v, 2);
                    stack.pop();
                    order.add(v);
                }
            }
        }

        Collections.reverse(order);
        return order;
    }

    public boolean hasCycle() {
        return !nodesInCycle().isEmpty();
    }

    public List<Node> nodesInCycle() {
        if (!graph.isDirected()) return Collections.emptyList();

        List<Node> nodes = new ArrayList<>(graph.getNodes());
        Map<Node, Integer> indegree = new HashMap<>();
        for (Node n : nodes) indegree.put(n, 0);
        for (Node u : nodes) {
            for (Node v : graph.getSuccessors(u)) {
                indegree.put(v, indegree.getOrDefault(v, 0) + 1);
            }
        }

        Deque<Node> q = new ArrayDeque<>();
        for (Node n : nodes) if (indegree.getOrDefault(n,0) == 0) q.addLast(n);

        List<Node> order = new ArrayList<>();
        while (!q.isEmpty()) {
            Node u = q.removeFirst();
            order.add(u);
            for (Node v : graph.getSuccessors(u)) {
                int d = indegree.getOrDefault(v,0) - 1;
                indegree.put(v, d);
                if (d == 0) q.addLast(v);
            }
        }

        if (order.size() == nodes.size()) return Collections.emptyList();

        Set<Node> processed = new HashSet<>(order);
        List<Node> inCycle = new ArrayList<>();
        for (Node n : nodes) if (!processed.contains(n)) inCycle.add(n);
        return inCycle;
    }
}
