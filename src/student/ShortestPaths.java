package student;


import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.Iterator;

public class ShortestPaths {

    private Graph graph;
    private Vertex[][] path_matrix;
    private double[][] weight_matrix;

    public ShortestPaths(Graph graph) {
        this.graph = graph;
        initializeShortestPathMatrices();
        computeShortestPaths();
    }

    public double getPathWeight(Vertex v1, Vertex v2) {
        return this.weight_matrix[v1.getId()][v2.getId()];
    }

    public ArrayList<Vertex> getPath(Vertex v1, Vertex v2) {
        if (this.path_matrix[v1.getId()][v2.getId()] == null) {
            return new ArrayList<>();
        }
        else {
            ArrayList<Vertex> path = new ArrayList<>();
            path.add(v1);
            while (v1 != v2) {
                v1 = this.path_matrix[v1.getId()][v2.getId()];
                path.add(v1);
            }
            return path;
        }
    }

    private void computeShortestPaths() {
        for (int k = 0; k < graph.numVertices(); k++) {
            for (int i = 0; i < graph.numVertices(); i++) {
                for (int j = 0; j < graph.numVertices(); j++) {
                    if ((weight_matrix[i][k] + weight_matrix[k][j]) < weight_matrix[i][j]) {
                        weight_matrix[i][j] = weight_matrix[i][k] + weight_matrix[k][j];
                        path_matrix[i][j] = path_matrix[i][k];
                    }
                }
            }
        }
    }

    private void initializeShortestPathMatrices() {
        this.path_matrix = new Vertex[graph.numVertices()][graph.numVertices()];
        this.weight_matrix = new double[graph.numVertices()][graph.numVertices()];

        Iterator<Vertex> vertex_iterator_one = graph.vertexIterator();

        while(vertex_iterator_one.hasNext()) {
            Vertex v1 = vertex_iterator_one.next();
            Iterator<Vertex> vertex_iterator_two = graph.vertexIterator();
            while (vertex_iterator_two.hasNext()) {
                Vertex v2 = vertex_iterator_two.next();
                if (v1 == v2) {
                    weight_matrix[v1.getId()][v2.getId()] = 0;
                    path_matrix[v1.getId()][v2.getId()] = v2;
                }
                else {
                    Edge edge = graph.findEdge(v1, v2);
                    if (edge != null) {
                        weight_matrix[v1.getId()][v2.getId()] = edge.getWeight();
                        path_matrix[v1.getId()][v2.getId()] = v2;
                    }
                    else {
                        weight_matrix[v1.getId()][v2.getId()] = Double.POSITIVE_INFINITY;
                        path_matrix[v1.getId()][v2.getId()] = null;
                    }
                }
            }
        }
    }
}
