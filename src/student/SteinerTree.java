package student;

import java.awt.*;
import java.util.*;

import graph.*;
import steinerTree.SteinerTreeTester;
import student.ShortestPaths;

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
    public static int steinerTree(Graph g, ArrayList<Vertex> targets)
    {
        ShortestPaths shortest_paths = new ShortestPaths(g);

        Set<Vertex> selected_vertices = new HashSet<>();
        selected_vertices.add(targets.get(0));

        ArrayList<Vertex> remaining_targets = new ArrayList<>();
        for (int i = 1; i < targets.size(); i ++) {
            remaining_targets.add(targets.get(i));
        }

        int total_weight = 0;
        while(!remaining_targets.isEmpty()) {
            Vertex closest_target = null;
            ArrayList<Vertex> closest_target_path = new ArrayList<>();
            int closest_target_weight = Integer.MAX_VALUE;

            for (Vertex target : remaining_targets) {
                int min_weight = Integer.MAX_VALUE;
                ArrayList<Vertex> min_path = new ArrayList<>();
                for (Vertex vertex : selected_vertices) {
                    int weight = (int) shortest_paths.getPathWeight(vertex, target);
                    if (weight < min_weight) {
                        min_weight = weight;
                        min_path = shortest_paths.getPath(vertex, target);
                    }
                }
                if (min_weight < closest_target_weight) {
                    closest_target_path = min_path;
                    closest_target_weight = min_weight;
                    closest_target = target;
                }
            }
            markPath(g, closest_target_path);
            selected_vertices.addAll(closest_target_path);
            total_weight += closest_target_weight;
            remaining_targets.remove(closest_target);
        }

        return total_weight;
    }

//    private static int closestVertex(Vertex target, ArrayList<Vertex> selected_vertices, ArrayList<Vertex> path) {
//        int min_weight = Integer.MAX_VALUE;
//        for (Vertex vertex : selected_vertices) {
//            int weight = (int) weight_matrix[vertex.getId()][target.getId()];
//            if (weight < min_weight) {
//                min_weight = weight;
//                min_path = path_builder.getPath(vertex, target);
//            }
//        }
//    }

    private static void markPath(Graph g, ArrayList<Vertex> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex v1 = path.get(i);
            Vertex v2 = path.get(i + 1);

            g.findEdge(v1, v2).setMark(1);
        }
    }
}

