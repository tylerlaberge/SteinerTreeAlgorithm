package student;

import java.awt.*;
import java.util.*;

import graph.*;
import steinerTree.SteinerTreeTester;

/*
 * This Student class is meant to contain your algorithm.
 * You should implement the static method:
 *
 *   steinerTree - which finds a good Steiner Tree on the graph
 *
 *   You do not need to find the optimal solution, but shorter is better!
 *   It should set the mark field on edges that are part of your tree.
 *   It should return the sum of the edge weights of the selected edges.
 *
 * The inputs are:
 *   1. Graph object, which has:
 *      an ArrayList of all vertices - use graph.vertexIterator()
 *      an ArrayList of all edges - use graph.edgeIterator()
 *      each vertex has an ArrayList of its edges - use vertex.edgeIterator()
 *      see the documentation for: Graph, Vertex, and Edge for more details
 *   2. An ArrayList of vertices that are the targeted vertices for inclusion
 *      in your Steiner tree. The mark fields are also already set in the graph
 *      for these vertices.
 */
public class SteinerTree
{
    // Simple example routine that just does a depth first search until it reaches
    // all of the target vertices.
    public static int steinerTree(Graph g, ArrayList<Vertex> targets)
    {
        Vertex[][] all_paths = new Vertex[g.numVertices()][g.numVertices()];
        double[][] shortest_paths = shortestPaths(g, all_paths);
        PathBuilder path_builder = new PathBuilder(all_paths);

        Set<Vertex> selected_vertices = new HashSet<>();
        selected_vertices.add(targets.get(0));

        int total_weight = 0;
        for (int i = 1; i < targets.size(); i++) {
            Vertex target = targets.get(i);

            int min_weight = Integer.MAX_VALUE;
            ArrayList<Vertex> min_path = new ArrayList<>();
            for (Vertex vertex : selected_vertices) {
                int weight = (int) shortest_paths[vertex.getId()][target.getId()];
                if (weight < min_weight) {
                    min_weight = weight;
                    min_path = path_builder.getPath(vertex, target);
                }
            }
            markPath(g, min_path);
            selected_vertices.addAll(min_path);
            total_weight += min_weight;
        }
        return total_weight;
    }

    private static void markPath(Graph g, ArrayList<Vertex> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex v1 = path.get(i);
            Vertex v2 = path.get(i + 1);

            g.findEdge(v1, v2).setMark(1);
        }
    }
    private static int sum(ArrayList<Integer> values) {
        int sum = 0;
        for (int value : values) {
            sum += value;
        }
        return sum;
    }
    private static double[][] shortestPaths(Graph graph, Vertex[][] all_paths) {
        double[][] shortest_paths = new double[graph.numVertices()][graph.numVertices()];
        buildInitialPathMatrices(graph, shortest_paths, all_paths);

        for (int k = 0; k < graph.numVertices(); k++) {
            for (int i = 0; i < graph.numVertices(); i++) {
                for (int j = 0; j < graph.numVertices(); j++) {
                    if ((shortest_paths[i][k] + shortest_paths[k][j]) < shortest_paths[i][j]) {
                        shortest_paths[i][j] = shortest_paths[i][k] + shortest_paths[k][j];
                        all_paths[i][j] = all_paths[i][k];
                    }
                }
            }
        }

        return shortest_paths;
    }

    private static class PathBuilder {

        private Vertex[][] all_paths;

        private PathBuilder(Vertex[][] all_paths) {
            this.all_paths = all_paths;
        }

        private ArrayList<Vertex> getPath(Vertex v1, Vertex v2) {
            if (this.all_paths[v1.getId()][v2.getId()] == null) {
                return new ArrayList<>();
            }
            else {
                ArrayList<Vertex> path = new ArrayList<>();
                path.add(v1);
                while (v1 != v2) {
                    v1 = this.all_paths[v1.getId()][v2.getId()];
                    path.add(v1);
                }
                return path;
            }
        }
    }

    private static double[][] buildInitialPathMatrices(Graph graph, double[][] adjacency_matrix, Vertex[][] path_matrix) {
        Iterator<Vertex> vertex_iterator_one = graph.vertexIterator();

        while(vertex_iterator_one.hasNext()) {
            Vertex v1 = vertex_iterator_one.next();
            Iterator<Vertex> vertex_iterator_two = graph.vertexIterator();
            while (vertex_iterator_two.hasNext()) {
                Vertex v2 = vertex_iterator_two.next();
                if (v1 == v2) {
                    adjacency_matrix[v1.getId()][v2.getId()] = 0;
                    path_matrix[v1.getId()][v2.getId()] = v2;
                }
                else {
                    Edge edge = graph.findEdge(v1, v2);
                    if (edge != null) {
                        adjacency_matrix[v1.getId()][v2.getId()] = edge.getWeight();
                        path_matrix[v1.getId()][v2.getId()] = v2;
                    }
                    else {
                        adjacency_matrix[v1.getId()][v2.getId()] = Double.POSITIVE_INFINITY;
                        path_matrix[v1.getId()][v2.getId()] = null;
                    }
                }
            }
        }
        return adjacency_matrix;
    }
}

