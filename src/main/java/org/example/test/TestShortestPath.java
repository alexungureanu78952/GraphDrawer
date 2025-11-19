package org.example.test;

import org.example.model.GraphModel;
import org.example.model.Node;
import org.example.algorithm.ShortestPath;
import org.example.model.Edge;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class TestShortestPath {
    public static void main(String[] args) {
        try (PrintWriter out = new PrintWriter(new FileWriter("test_shortest_result.txt"))) {
            GraphModel g = new GraphModel(true);
            Node n0 = g.addNode(0,0);
            Node n1 = g.addNode(100,0);
            Node n2 = g.addNode(200,0);
            g.addEdge(n0, n1, 5);
            g.addEdge(n0, n2, 2);
            g.addEdge(n2, n1, 1);

            ShortestPath sp = new ShortestPath(g);
            boolean ok = sp.compute(n0);
            out.println("compute returned: " + ok);
            if (ok) {
                out.println("dist(0) = " + sp.getDistance(n0));
                out.println("dist(1) = " + sp.getDistance(n1));
                out.println("dist(2) = " + sp.getDistance(n2));

                List<Node> p1 = sp.getPath(n1);
                out.print("path to 1: ");
                if (p1 != null) { for (Node n : p1) out.print(n.getId() + " "); }
                out.println();

                List<Node> p2 = sp.getPath(n2);
                out.print("path to 2: ");
                if (p2 != null) { for (Node n : p2) out.print(n.getId() + " "); }
                out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

