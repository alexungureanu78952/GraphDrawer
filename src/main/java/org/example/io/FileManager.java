package org.example.io;

import org.example.model.GraphModel;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileManager {
    private String filename;

    public FileManager(String filename) {
        this.filename = filename;
    }

    public void saveGraph(GraphModel graph) {
        try (PrintWriter writer = new PrintWriter(new FileWriter
                ("D:/cod/AG/desenare_graf/src/main/resources/matrix.txt"))) {
            int n = graph.getNodes().size();
            writer.println(n);

            int[][] matrix = graph.getAdjacencyMatrix();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    writer.print(matrix[i][j]);
                    if (j < n - 1) {
                        writer.print(" ");
                    }
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}