package student;


import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A class for computing and interacting with the shortest paths of a graph.
 */
public class ShortestPaths {

    private Graph graph;
    private Vertex[][] path_matrix; // internally used to reconstruct the shortest paths.
    private double[][] weight_matrix; // contains the weights of all the shortest paths.

    /**
     * Create a new ShortestPaths object.
     *
     * Initial construction has time complexity O(V^3), where V is the number of vertices in the given graph.
     *
     * @param graph: The graph to compute the shortest paths for.
     */
    public ShortestPaths(Graph graph) {
        this.graph = graph;
        initializeShortestPathMatrices();
        computeShortestPaths();
    }

    /**
     * Get the weight of the shortest path between two vertices.
     *
     * The time complexity of this is O(1). All weights are saved on object creation.
     *
     * @param v1: The start vertex of the path.
     * @param v2: The end vertex of the path.
     * @return The weight of the shortest path between the two vertices.
     */
    public double getPathWeight(Vertex v1, Vertex v2) {
        return this.weight_matrix[v1.getId()][v2.getId()];
    }

    /**
     * Get the vertices which make up the shortest path between two vertices.
     *
     * The time complexity of this is O(V), where V is the number of vertices which make up the path connecting v1 and v2.
     *
     * @param v1: The start vertex of the path.
     * @param v2: The end vertex of the path.
     * @return The vertices which make up the shortest path between the two vertices.
     * If there is no path between the two vertices then an empty list is returned.
     */
    public ArrayList<Vertex> getPath(Vertex v1, Vertex v2) {

        // If there is no path between the vertices then return an empty list of vertices.
        if (this.path_matrix[v1.getId()][v2.getId()] == null) {
            return new ArrayList<>();
        }
        else {
            ArrayList<Vertex> path = new ArrayList<>();
            path.add(v1);

            //continually move forward to the next vertex in the path, each time adding to the path.
            while (v1 != v2) {
                v1 = this.path_matrix[v1.getId()][v2.getId()];
                path.add(v1);
            }
            return path;
        }
    }

    /**
     * Compute all pairs shortest paths on this objects Graph.
     *
     * The time complexity of this is O(V^3), where V is the number of vertices in this objects Graph.
     *
     * Uses the Floyd–Warshall algorithm with path reconstruction.
     * https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm#Path_reconstruction
     */
    private void computeShortestPaths() {
        for (int k = 0; k < graph.numVertices(); k++) {
            for (int i = 0; i < graph.numVertices(); i++) {
                for (int j = 0; j < graph.numVertices(); j++) {

                    // If using an intermediary vertex is a better path
                    if ((weight_matrix[i][k] + weight_matrix[k][j]) < weight_matrix[i][j]) {

                        // Use the intermediary vertex
                        weight_matrix[i][j] = weight_matrix[i][k] + weight_matrix[k][j];

                        // Save this vertex to the path_matrix used for reconstructing paths.
                        path_matrix[i][j] = path_matrix[i][k];
                    }
                }
            }
        }
    }

    /**
     * Initialize the matrices used for computing and reconstructing the shortest paths.
     *
     * The path matrix is initialized to the paths of directly connected vertices.
     * Paths to vertices which are not directly connected are initialized to null.
     *
     * The weight matrix is initialized to the weights of the edges of directly connected vertices.
     * Weights of the paths of vertices which are not directly connected are initialized to infinity.
     *
     * The average time complexity of this is O(D*V^2),
     * where D is the average degree of the vertices of this graph, and V is the number of vertices in this graph.
     *
     * If the graph is fully connected, i.e every vertex connects to every other vertex, the complexity is O(V^3).
     */
    private void initializeShortestPathMatrices() {
        this.path_matrix = new Vertex[graph.numVertices()][graph.numVertices()];
        this.weight_matrix = new double[graph.numVertices()][graph.numVertices()];

        Iterator<Vertex> vertex_iterator_one = graph.vertexIterator();

        while(vertex_iterator_one.hasNext()) {
            Vertex v1 = vertex_iterator_one.next();
            Iterator<Vertex> vertex_iterator_two = graph.vertexIterator();
            while (vertex_iterator_two.hasNext()) {
                Vertex v2 = vertex_iterator_two.next();
                if (v1 == v2) { // The weight from v1 to v1 is 0, path from v1 to v1 is just v1.
                    weight_matrix[v1.getId()][v2.getId()] = 0;
                    path_matrix[v1.getId()][v2.getId()] = v2;
                }
                else {
                    Edge edge = graph.findEdge(v1, v2); // O(D) computation, D = degree of v1.

                    if (edge != null) { // If there is an edge directly connecting v1 and v2.
                        weight_matrix[v1.getId()][v2.getId()] = edge.getWeight(); // The weight is just that edge weight.
                        path_matrix[v1.getId()][v2.getId()] = v2; // The path from v1 to v2 is just v2.
                    }
                    else {
                        // They don't connect initially so the weight is infinite and there is no path between them.
                        weight_matrix[v1.getId()][v2.getId()] = Double.POSITIVE_INFINITY;
                        path_matrix[v1.getId()][v2.getId()] = null;
                    }
                }
            }
        }
    }
}
