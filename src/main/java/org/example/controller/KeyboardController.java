package org.example.controller;

import org.example.algorithm.ShortestPath;
import org.example.algorithm.TopologicalSort;
import org.example.model.GraphModel;
import org.example.model.Node;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class KeyboardController extends KeyAdapter {
    private final GraphModel graph;
    private final JPanel panel;
    private final MouseController mouseController;
    private ShortestPath shortestPath;
    private Node sourceNode;
    private List<Node> currentPath;
    private int currentPathIndex;

    public KeyboardController(GraphModel graph, JPanel panel, MouseController mouseController) {
        this.graph = graph;
        this.panel = panel;
        this.mouseController = mouseController;
        this.currentPathIndex = -1;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            showNextShortestPath();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            setSourceNode();
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            checkForCycles();
        } else if (e.getKeyCode() == KeyEvent.VK_H) {
            showHelp();
        }
    }

    private void showHelp() {
        String help = "=== INSTRUCTIUNI ===\n\n" +
                "CREARE GRAF:\n" +
                "• Click stanga = Adauga nod\n" +
                "• Click dreapta pe Nod1 apoi Nod2 = Adauga arc\n" +
                "• Drag nod = Muta nodul\n" +
                "• Dublu click dreapta pe nod = Sterge arcele\n\n" +
                "ANALIZA DRUM CEL MAI SCURT:\n" +
                "1. Click stanga pe un nod (devine GALBEN)\n" +
                "2. Apasa 'S' = Stabileste nodul sursa\n" +
                "3. Apasa 'SPATIU' = Vezi drumurile cele mai scurte\n\n" +
                "ALTE COMENZI:\n" +
                "• 'C' = Verifica circuite\n" +
                "• 'H' = Afiseaza acest ajutor\n\n" +
                "CULORI:\n" +
                "• GALBEN = Nod selectat\n" +
                "• CYAN = Noduri in drumul cel mai scurt\n" +
                "• VERDE = Arce in drumul cel mai scurt";

        JOptionPane.showMessageDialog(panel, help, "Ajutor", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setSourceNode() {
        sourceNode = mouseController.getSelectedNode();
        if (sourceNode != null && graph.isDirected()) {
            shortestPath = new ShortestPath(graph);
            if (shortestPath.compute(sourceNode)) {
                currentPathIndex = -1;
                JOptionPane.showMessageDialog(panel,
                    "✓ Nod sursa stabilit: " + sourceNode.getId() +
                    "\n\nApasati SPATIU pentru a vedea drumurile cele mai scurte.\n" +
                    "(Apasati SPATIU repetat pentru fiecare nod)",
                    "Nod Sursa Setat",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panel,
                    "✗ Graful contine circuite!\n\nNu se poate calcula sortarea topologica.\n" +
                    "Apasati 'C' pentru detalii.",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
                shortestPath = null;
                sourceNode = null;
            }
            panel.repaint();
        } else if (sourceNode == null) {
            JOptionPane.showMessageDialog(panel,
                "Selectati un nod mai intai!\n\n" +
                "1. Click STANGA pe un nod (devine galben)\n" +
                "2. Apasati 'S' din nou",
                "Niciun Nod Selectat",
                JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(panel,
                "Graful trebuie sa fie ORIENTAT!\n\n" +
                "Bifati 'Orientat' in partea de sus.",
                "Graf Neorientat",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showNextShortestPath() {
        if (shortestPath == null || sourceNode == null) {
            JOptionPane.showMessageDialog(panel,
                "Trebuie sa stabiliti nodul sursa mai intai!\n\n" +
                "1. Click STANGA pe un nod\n" +
                "2. Apasati 'S'",
                "Nod Sursa Necesar",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Node> sortedNodes = new ArrayList<>(graph.getNodes());
        sortedNodes.sort(Comparator.comparingInt(Node::getId));

        currentPathIndex++;
        if (currentPathIndex >= sortedNodes.size()) {
            currentPathIndex = 0;
        }

        Node targetNode = sortedNodes.get(currentPathIndex);

        if (targetNode.equals(sourceNode)) {
            currentPath = new ArrayList<>();
            currentPath.add(sourceNode);
        } else {
            currentPath = shortestPath.getPath(targetNode);
        }

        if (currentPath != null && currentPath.size() > 0) {
            int distance = shortestPath.getDistance(targetNode);
            String distStr = (distance == Integer.MAX_VALUE) ? "infinit" : String.valueOf(distance);

            StringBuilder pathStr = new StringBuilder();
            for (int i = 0; i < currentPath.size(); i++) {
                pathStr.append(currentPath.get(i).getId());
                if (i < currentPath.size() - 1) {
                    pathStr.append(" -> ");
                }
            }

            JOptionPane.showMessageDialog(panel,
                "De la nodul sursa " + sourceNode.getId() + " la nodul " + targetNode.getId() + ":\n\n" +
                "Distanta: " + distStr + "\n" +
                "Drum: " + pathStr + "\n\n" +
                "(Apasati SPATIU pentru urmatorul nod)",
                "Drum Cel Mai Scurt",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(panel,
                "Nu exista drum de la nodul " + sourceNode.getId() +
                " la nodul " + targetNode.getId(),
                "Fara Drum",
                JOptionPane.WARNING_MESSAGE);
        }

        panel.repaint();
    }

    private void checkForCycles() {
        if (!graph.isDirected()) {
            JOptionPane.showMessageDialog(panel,
                "Verificarea ciclurilor este doar pentru grafuri ORIENTATE!\n\n" +
                "Bifati 'Orientat' in partea de sus.",
                "Graf Neorientat",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        TopologicalSort topoSort = new TopologicalSort(graph);
        if (topoSort.hasCycle()) {
            JOptionPane.showMessageDialog(panel,
                "✗ Graful CONTINE circuite!\n\n" +
                "Sortarea topologica nu este posibila.\n" +
                "Drumurile cele mai scurte nu pot fi calculate.",
                "Circuit Detectat",
                JOptionPane.ERROR_MESSAGE);
        } else {
            List<Node> sorted = topoSort.sort();
            StringBuilder sb = new StringBuilder("✓ Graful NU contine circuite!\n\n");
            sb.append("Sortare topologica: ");
            for (int i = 0; i < sorted.size(); i++) {
                sb.append(sorted.get(i).getId());
                if (i < sorted.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n\nPuteti calcula drumuri cele mai scurte!");
            JOptionPane.showMessageDialog(panel, sb.toString(),
                "Graf Fara Circuite",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public List<Node> getCurrentPath() {
        return currentPath;
    }

    public Node getSourceNode() {
        return sourceNode;
    }
}

