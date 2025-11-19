package org.example.test;

import org.example.model.GraphModel;
import org.example.model.Node;
import org.example.algorithm.TopologicalSort;
import java.util.List;

public class TestSingleGraph {
    public static void main(String[] args) {
        GraphModel g = new GraphModel(true);
        Node n0 = g.addNode(0,0);
        Node n1 = g.addNode(200,0);
        Node n2 = g.addNode(400,0);
        System.out.println("nodes created: " + (n0!=null? n0.getId():"null") + ", " + (n1!=null? n1.getId():"null") + ", " + (n2!=null? n2.getId():"null"));
        boolean e01 = g.addEdge(n0, n1, 1);
        boolean e12 = g.addEdge(n1, n2, 1);
        boolean e02 = g.addEdge(n0, n2, 1);
        System.out.println("added edges: 0->1=" + e01 + " 1->2=" + e12 + " 0->2=" + e02);

        TopologicalSort ts = new TopologicalSort(g);
        List<Node> order = ts.sort();
        System.out.println("hasCycle() = " + (order == null));
        if (order != null) {
            System.out.print("order: ");
            for (Node n : order) System.out.print(n.getId() + " ");
            System.out.println();
        }
    }
}

