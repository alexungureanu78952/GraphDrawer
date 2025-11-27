package org.example.algorithm;

import org.example.model.GraphModel;
import org.example.model.Node;
import org.example.model.Edge;
import java.util.*;

public class ConnectedComponents {
    private final GraphModel graph;
    private final Map<Node, Integer> componentMap;
    private int componentCount;
    private List<List<Node>> components;

    public ConnectedComponents(GraphModel graph) {
        this.graph = graph;
        this.componentMap = new HashMap<>();
        this.componentCount = 0;
    }

    public int compute() {
        componentMap.clear();
        componentCount = 0;

        if (graph.isDirected()) {
            return computeStronglyConnected();
        } else {
            return computeWeaklyConnected();
        }
    }

    private int computeWeaklyConnected() {
        if (graph.getNodes().isEmpty()) {
            components = new ArrayList<>();
            return 0;
        }

        Set<Node> unvisited = new HashSet<>(graph.getNodes());
        Deque<Node> processing = new LinkedList<>();
        Set<Node> processed = new HashSet<>();
        components = new ArrayList<>();

        Map<Node, Integer> nextIndex = new HashMap<>();

        Node startNode = unvisited.iterator().next();
        unvisited.remove(startNode);
        processing.addLast(startNode);
        componentCount = 1;

        List<Node> currentComponent = new ArrayList<>();
        currentComponent.add(startNode);

        while (processed.size() < graph.getNodes().size()) {
            while (!processing.isEmpty()) {
                Node currentNode = processing.peekLast();

                Node neighbor = findNeighborInSet(currentNode, unvisited, nextIndex);
                if (neighbor != null) {
                    unvisited.remove(neighbor);
                    processing.addLast(neighbor);
                    currentComponent.add(neighbor);
                } else {
                    processing.removeLast();
                    processed.add(currentNode);
                }
            }

            for (Node node : currentComponent) {
                componentMap.put(node, componentCount - 1);
            }
            currentComponent.sort(Comparator.comparingInt(Node::getId));
            components.add(new ArrayList<>(currentComponent));

            if (!unvisited.isEmpty()) {
                startNode = unvisited.iterator().next();
                unvisited.remove(startNode);
                processing.addLast(startNode);
                componentCount++;
                currentComponent.clear();
                currentComponent.add(startNode);
            }
        }

        return componentCount;
    }

    private Node findNeighborInSet(Node node, Set<Node> targetSet, Map<Node, Integer> nextIndex) {
        List<Node> successors = graph.getSuccessors(node);
        int idx = nextIndex.getOrDefault(node, 0);
        while (idx < successors.size()) {
            Node neighbor = successors.get(idx);
            idx++;
            if (targetSet.contains(neighbor)) {
                nextIndex.put(node, idx);
                return neighbor;
            }
        }
        nextIndex.put(node, idx);
        return null;
    }

    private static class InversionResult {
        final GraphModel inversedGraph;
        final Map<Node, Node> inversedToOriginal;

        InversionResult(GraphModel inversedGraph, Map<Node, Node> inversedToOriginal) {
            this.inversedGraph = inversedGraph;
            this.inversedToOriginal = inversedToOriginal;
        }
    }

    private int computeStronglyConnected() {
        Map<Node, Integer> finishTime = new HashMap<>();

        dfsPhase1(finishTime);

        InversionResult inversionResult = inverseGraphWithMapping();

        dfsPhase2(inversionResult, finishTime);

        return componentCount;
    }

    private void dfsPhase1(Map<Node, Integer> finishTime) {
        Set<Node> visited = new HashSet<>();
        int[] time = {0};

        for (Node node : graph.getNodes()) {
            if (!visited.contains(node)) {
                dfsVisit(node, visited, finishTime, time);
            }
        }
    }

    private void dfsVisit(Node node, Set<Node> visited, Map<Node, Integer> finishTime, int[] time) {
        Stack<Node> stack = new Stack<>();
        Stack<Integer> phase = new Stack<>();
        Set<Node> onStack = new HashSet<>();

        stack.push(node);
        phase.push(0);
        onStack.add(node);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            int p = phase.pop();

            if (p == 0) {
                if (visited.contains(current)) {
                    onStack.remove(current);
                    continue;
                }

                visited.add(current);

                stack.push(current);
                phase.push(1);

                List<Node> successors = graph.getSuccessors(current);
                for (int i = successors.size() - 1; i >= 0; i--) {
                    Node successor = successors.get(i);
                    if (!visited.contains(successor) && !onStack.contains(successor)) {
                        stack.push(successor);
                        phase.push(0);
                        onStack.add(successor);
                    }
                }
            } else {
                time[0]++;
                finishTime.put(current, time[0]);
                onStack.remove(current);
            }
        }
    }

    private InversionResult inverseGraphWithMapping() {
        GraphModel inversed = new GraphModel(true);
        Map<Integer, Node> nodeMap = new HashMap<>();
        Map<Node, Node> inversedToOriginal = new HashMap<>();

        for (Node node : graph.getNodes()) {
            Node newNode = inversed.addNode(node.getX(), node.getY());
            if (newNode != null) {
                nodeMap.put(node.getId(), newNode);
                inversedToOriginal.put(newNode, node);
            }
        }

        for (Edge edge : graph.getEdges()) {
            Node from = nodeMap.get(edge.getFrom().getId());
            Node to = nodeMap.get(edge.getTo().getId());
            if (from != null && to != null) {
                inversed.addEdge(to, from, edge.getCost());
            }
        }

        return new InversionResult(inversed, inversedToOriginal);
    }

    private void dfsPhase2(InversionResult inversionResult, Map<Node, Integer> finishTime) {
        GraphModel inversed = inversionResult.inversedGraph;
        Map<Node, Node> inversedToOriginal = inversionResult.inversedToOriginal;

        List<Node> sortedNodes = new ArrayList<>(inversed.getNodes());
        sortedNodes.sort((a, b) -> {
            Node origA = inversedToOriginal.get(a);
            Node origB = inversedToOriginal.get(b);
            Integer timeA = finishTime.getOrDefault(origA, 0);
            Integer timeB = finishTime.getOrDefault(origB, 0);
            return timeB.compareTo(timeA);
        });

        Set<Node> visited = new HashSet<>();
        components = new ArrayList<>();

        for (Node node : sortedNodes) {
            if (!visited.contains(node)) {
                List<Node> component = new ArrayList<>();
                dfsCollect(inversed, node, visited, component);

                List<Node> originalComponent = new ArrayList<>();
                for (Node n : component) {
                    Node original = inversedToOriginal.get(n);
                    if (original != null) {
                        originalComponent.add(original);
                    }
                }
                originalComponent.sort(Comparator.comparingInt(Node::getId));
                components.add(originalComponent);

                for (Node original : originalComponent) {
                    componentMap.put(original, componentCount);
                }
                componentCount++;
            }
        }
    }

    private void dfsCollect(GraphModel g, Node start, Set<Node> visited, List<Node> component) {
        Stack<Node> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            if (visited.contains(current)) continue;

            visited.add(current);
            component.add(current);

            for (Node successor : g.getSuccessors(current)) {
                if (!visited.contains(successor)) {
                    stack.push(successor);
                }
            }
        }
    }

    public GraphModel buildCondensationGraph() {
        if (!graph.isDirected() || componentCount == 0) {
            return null;
        }

        GraphModel condensation = new GraphModel(true);
        Map<Integer, Node> componentNodes = new HashMap<>();

        int spacing = 150;
        int startX = 100;
        int startY = 100;
        int nodesPerRow = (int) Math.ceil(Math.sqrt(componentCount));

        for (int i = 0; i < componentCount; i++) {
            int row = i / nodesPerRow;
            int col = i % nodesPerRow;
            int x = startX + col * spacing;
            int y = startY + row * spacing;

            Node newNode = condensation.addNode(x, y);
            if (newNode != null) {
                StringBuilder label = new StringBuilder();
                List<Node> compNodes = components.get(i);
                for (int j = 0; j < compNodes.size(); j++) {
                    label.append(compNodes.get(j).getId());
                    if (j < compNodes.size() - 1) {
                        label.append(",");
                    }
                }
                newNode.setCustomLabel(label.toString());
                componentNodes.put(i, newNode);
            }
        }

        Set<String> addedEdges = new HashSet<>();

        for (Edge edge : graph.getEdges()) {
            Node from = edge.getFrom();
            Node to = edge.getTo();

            int compFrom = componentMap.get(from);
            int compTo = componentMap.get(to);

            if (compFrom != compTo) {
                String edgeKey = compFrom + "->" + compTo;
                if (!addedEdges.contains(edgeKey)) {
                    Node condensedFrom = componentNodes.get(compFrom);
                    Node condensedTo = componentNodes.get(compTo);

                    if (condensedFrom != null && condensedTo != null) {
                        condensation.addEdge(condensedFrom, condensedTo, 1);
                        addedEdges.add(edgeKey);
                    }
                }
            }
        }

        return condensation;
    }

    public int getComponentCount() {
        return componentCount;
    }

    public Map<Node, Integer> getComponentMap() {
        return new HashMap<>(componentMap);
    }

    public int getComponent(Node node) {
        return componentMap.getOrDefault(node, -1);
    }

    public List<List<Node>> getComponents() {
        return components;
    }
}
