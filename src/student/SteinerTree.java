package student;

import java.util.*;
import graph.*;

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

        Set<Vertex> remaining_targets = new HashSet<>();
        for (int i = 1; i < targets.size(); i ++) {
            remaining_targets.add(targets.get(i));
        }

        int total_weight = 0;
        while(!remaining_targets.isEmpty()) {
            Pair<Vertex, Vertex> closestVertexPair = closestVertexPair(remaining_targets, selected_vertices, shortest_paths);
            Vertex closest_target = closestVertexPair.getFirst();
            Vertex connected_selected_vertex = closestVertexPair.getSecond();

            ArrayList<Vertex> closest_target_path = shortest_paths.getPath(connected_selected_vertex, closest_target);
            int closest_target_weight = (int) shortest_paths.getPathWeight(connected_selected_vertex, closest_target);

            markPath(g, closest_target_path);
            selected_vertices.addAll(closest_target_path);
            total_weight += closest_target_weight;
            remaining_targets.remove(closest_target);
        }

        return total_weight;
    }

    private static Pair<Vertex, Vertex> closestVertexPair(
            Set<Vertex> vertex_set_one, Set<Vertex> vertex_set_two, ShortestPaths shortest_paths) {

        Vertex closest_set_one_vertex = null;
        Vertex closest_set_two_vertex = null;
        int min_weight = Integer.MAX_VALUE;

        for (Vertex set_one_vertex : vertex_set_one) {
            Vertex set_two_vertex = closestVertex(set_one_vertex, vertex_set_two, shortest_paths);
            int path_weight = (int) shortest_paths.getPathWeight(set_one_vertex, set_two_vertex);
            if (path_weight < min_weight) {
                min_weight = path_weight;
                closest_set_one_vertex = set_one_vertex;
                closest_set_two_vertex = set_two_vertex;
            }
        }

        return new Pair<>(closest_set_one_vertex, closest_set_two_vertex);
    }
    private static Vertex closestVertex(Vertex target, Set<Vertex> vertices, ShortestPaths shortest_paths) {
        int min_weight = Integer.MAX_VALUE;
        Vertex closest_vertex = null;
        for (Vertex vertex : vertices) {
            int weight = (int) shortest_paths.getPathWeight(vertex, target);
            if (weight < min_weight) {
                min_weight = weight;
                closest_vertex = vertex;
            }
        }
        return closest_vertex;
    }

    private static void markPath(Graph g, ArrayList<Vertex> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex v1 = path.get(i);
            Vertex v2 = path.get(i + 1);

            g.findEdge(v1, v2).setMark(1);
        }
    }
}

