package org.example.test;

import org.example.model.GraphModel;
import org.example.model.Node;
import org.example.model.Edge;
import java.util.List;

public class TestDebug {
    public static void main(String[] args) {
        GraphModel g = new GraphModel(true);
        Node n0 = g.addNode(0,0);
        System.out.println("n0 = " + (n0==null?"null":n0.getId()));
        Node n1 = g.addNode(200,0);
        System.out.println("n1 = " + (n1==null?"null":n1.getId()));
        Node n2 = g.addNode(400,0);
        System.out.println("n2 = " + (n2==null?"null":n2.getId()));
        Node n3 = g.addNode(600,0);
        System.out.println("n3 = " + (n3==null?"null":n3.getId()));

        boolean e01 = g.addEdge(n0, n1, 1);
        System.out.println("addEdge n0->n1 = " + e01);
        boolean e02 = g.addEdge(n0, n2, 1);
        System.out.println("addEdge n0->n2 = " + e02);
        boolean e13 = g.addEdge(n1, n3, 1);
        System.out.println("addEdge n1->n3 = " + e13);
        boolean e23 = g.addEdge(n2, n3, 1);
        System.out.println("addEdge n2->n3 = " + e23);

        System.out.println("Nodes:");
        for (Node n : g.getNodes()) {
            System.out.println("node object=" + n + " id=" + n.getId());
        }

        System.out.println("Edges:");
        for (Edge e : g.getEdges()) {
            System.out.println("from=" + (e.getFrom()==null?"null":e.getFrom().getId()) + " to=" + (e.getTo()==null?"null":e.getTo().getId()) + " cost=" + e.getCost());
        }

        System.out.println("Outgoing lists:");
        for (Node n : g.getNodes()) {
            List<Edge> outs = g.getOutgoingEdges(n);
            System.out.print("node " + n.getId() + " -> ");
            for (Edge e : outs) {
                System.out.print((e.getTo()==null?"null":e.getTo().getId()) + ",");
            }
            System.out.println();
        }

        System.out.println("Successors via getSuccessors():");
        for (Node n : g.getNodes()) {
            List<Node> succ = g.getSuccessors(n);
            System.out.print("node " + n.getId() + " -> ");
            for (Node s : succ) {
                System.out.print((s==null?"null":s.getId()) + ",");
            }
            System.out.println();
        }
    }
}
