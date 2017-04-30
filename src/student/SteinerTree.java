package student;

import java.util.*;
import graph.*;

/**
 * A static class for approximating Steiner Trees.
 */
public class SteinerTree
{
    /**
     * Approximate the Steiner Tree for the given Targets over the given Graph.
     *
     * The Edges that make up the Steiner Tree which connects all Target nodes will be marked.
     *
     * @param graph: The graph to find the Steiner Tree on.
     * @param targets: The vertices that the Steiner Tree must contain.
     * @return: The total weight of the edges which make up the Steiner Tree.
     */
    public static int steinerTree(Graph graph, ArrayList<Vertex> targets)
    {
        ShortestPaths shortest_paths = new ShortestPaths(graph);

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

            markPath(graph, closest_target_path);
            selected_vertices.addAll(closest_target_path);
            total_weight += closest_target_weight;
            remaining_targets.remove(closest_target);
        }

        return total_weight;
    }

    /**
     * Find the closest pair of vertices from 2 separate sets of vertices.
     *
     * @param vertex_set_one: The set of vertices which will contain the first vertex of the pair.
     * @param vertex_set_two: The set of vertices which will contain the second vertex of the pair.
     * @param shortest_paths: A ShortestPaths object containing the shortest paths between both sets of vertices.
     * @return: The Pair of vertices that are closest.
     *          The first vertex of the pair will be from the first set of vertices.
     *          The second vertex of the pair will be from the second set of vertices.
     */
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

    /**
     * Find the closest vertex to a given target vertex.
     *
     * @param target: The vertex to find the closest vertex too.
     * @param vertices: The set of vertices to select from and to find the closest one to the target.
     * @param shortest_paths: A ShortestPaths object containing the shortest paths between the target and the set of vertices.
     * @return: The vertex in the given set of vertices which is closest to the target vertex.
     */
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

    /**
     * Mark the edges of a graph that make up the given path.
     * @param graph: The Graph object to mark edges on.
     * @param path: The vertices making up the path to mark.
     */
    private static void markPath(Graph graph, ArrayList<Vertex> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex v1 = path.get(i);
            Vertex v2 = path.get(i + 1);

            graph.findEdge(v1, v2).setMark(1);
        }
    }
}

