package org.example.controller;

import org.example.model.GraphModel;
import org.example.model.Node;
import org.example.io.FileManager;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class MouseController extends MouseAdapter {
    private final GraphModel graph;
    private final FileManager fileManager;
    private Node selectedNode;
    private Node draggedNode;
    private Node lastRightClickNode;
    private long lastRightClickTime;
    private Point dragOffset;
    private final JPanel panel;
    private static final int DOUBLE_CLICK_DELAY = 300;

    public MouseController(GraphModel graph, FileManager fileManager, JPanel panel) {
        this.graph = graph;
        this.fileManager = fileManager;
        this.panel = panel;
        this.selectedNode = null;
        this.draggedNode = null;
        this.lastRightClickNode = null;
        this.lastRightClickTime = 0;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        panel.requestFocusInWindow();

        Node clickedNode = graph.getNodeAt(e.getX(), e.getY());

        if (e.getButton() == MouseEvent.BUTTON1) {
            selectedNode = clickedNode;

            if (clickedNode != null) {
                draggedNode = clickedNode;
                dragOffset = new Point(
                        e.getX() - clickedNode.getX(),
                        e.getY() - clickedNode.getY()
                );
            } else {
                Node newNode = graph.addNode(e.getX(), e.getY());
                if (newNode != null) {
                    fileManager.saveGraph(graph);
                    panel.repaint();
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            long currentTime = System.currentTimeMillis();
            if (clickedNode != null) {
                if (lastRightClickNode == clickedNode &&
                        (currentTime - lastRightClickTime) < DOUBLE_CLICK_DELAY) {

                    if (graph.removeAllEdgesFromNode(clickedNode)) {
                        fileManager.saveGraph(graph);
                        panel.repaint();
                    }

                    lastRightClickNode = null;
                    lastRightClickTime = 0;
                    selectedNode = null;
                } else {
                    lastRightClickNode = clickedNode;
                    lastRightClickTime = currentTime;

                    if (selectedNode == null) {
                        selectedNode = clickedNode;
                        panel.repaint();
                    } else if (selectedNode == clickedNode) {
                        selectedNode = null;
                        panel.repaint();
                    } else {
                        String costStr = JOptionPane.showInputDialog(panel, "Introduceti costul arcului:", "1");
                        if (costStr != null) {
                            try {
                                int cost = Integer.parseInt(costStr);
                                if (graph.addEdge(selectedNode, clickedNode, cost)) {
                                    fileManager.saveGraph(graph);
                                }
                            } catch (NumberFormatException ex) {
                                if (graph.addEdge(selectedNode, clickedNode)) {
                                    fileManager.saveGraph(graph);
                                }
                            }
                        }
                        selectedNode = null;
                        panel.repaint();
                    }
                }
            } else {
                selectedNode = null;
                lastRightClickNode = null;
                panel.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (draggedNode != null) {
            fileManager.saveGraph(graph);
            draggedNode = null;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggedNode != null && dragOffset != null) {
            int newX = e.getX() - dragOffset.x;
            int newY = e.getY() - dragOffset.y;
            draggedNode.setX(newX);
            draggedNode.setY(newY);
            panel.repaint();
        }
    }

    public Node getSelectedNode() {
        return selectedNode;
    }
}