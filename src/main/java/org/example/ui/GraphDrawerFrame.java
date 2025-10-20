package org.example.ui;

import org.example.model.GraphModel;
import org.example.io.FileManager;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class GraphDrawerFrame extends JFrame {
    private GraphModel graph;
    private GraphPanel panel;
    private FileManager fileManager;
    private JRadioButton directedButton;
    private JRadioButton undirectedButton;

    public GraphDrawerFrame() {
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        setTitle("Graph Drawing Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        graph = new GraphModel(false);
        fileManager = new FileManager("matrix.txt");
        panel = new GraphPanel(graph, fileManager);

        directedButton = new JRadioButton("Orientat");
        undirectedButton = new JRadioButton("Neorientat", true);
    }

    private void setupLayout() {
        JPanel topPanel = new JPanel();
        ButtonGroup group = new ButtonGroup();
        group.add(directedButton);
        group.add(undirectedButton);

        topPanel.add(new JLabel("Tip graf: "));
        topPanel.add(undirectedButton);
        topPanel.add(directedButton);

        add(topPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        ActionListener graphTypeListener = e -> {
            graph.setDirected(directedButton.isSelected());
            panel.repaint();
            fileManager.saveGraph(graph);
        };

        directedButton.addActionListener(graphTypeListener);
        undirectedButton.addActionListener(graphTypeListener);
    }
}