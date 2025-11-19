package org.example.test;

import org.example.model.GraphModel;
import org.example.model.Node;
import org.example.algorithm.TopologicalSort;
import org.example.algorithm.ShortestPath;
import java.util.List;

public class TestTopo {
    public static void main(String[] args) {
        System.out.println("=== TestTopo start ===");
        testAcyclic();
        testCyclic();
    }

    private static void testAcyclic() {
        GraphModel g = new GraphModel(true);
        Node n0 = g.addNode(0,0);
        Node n1 = g.addNode(200,0);
        Node n2 = g.addNode(400,0);
        Node n3 = g.addNode(600,0);
        g.addEdge(n0, n1, 1);
        g.addEdge(n0, n2, 1);
        g.addEdge(n1, n3, 1);
        g.addEdge(n2, n3, 1);

        TopologicalSort ts = new TopologicalSort(g);
        List<Node> order = ts.sort();
        System.out.println("Acyclic test - hasCycle? " + (order == null));
        if (order != null) {
            System.out.print("Order: ");
            for (Node n : order) System.out.print(n.getId() + " ");
            System.out.println();
        }
    }

    private static void testCyclic() {
        GraphModel g = new GraphModel(true);
        Node n0 = g.addNode(0,0);
        Node n1 = g.addNode(200,0);
        Node n2 = g.addNode(400,0);
        g.addEdge(n0, n1, 1);
        g.addEdge(n1, n2, 1);
        g.addEdge(n2, n0, 1);

        TopologicalSort ts = new TopologicalSort(g);
        List<Node> order = ts.sort();
        System.out.println("Cyclic test - hasCycle? " + (order == null));
        if (order != null) {
            System.out.print("Order: ");
            for (Node n : order) System.out.print(n.getId() + " ");
            System.out.println();
        }
    }
}
