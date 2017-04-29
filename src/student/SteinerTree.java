package student;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

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
        // sort each vertex's edges shortest first, this should help a little
        g.sortVertexEdgeLists(new Graph.CompareEdgesSmallestFirst());

        // start at first target vertex
        Vertex v = targets.get(0);
        dfs(g, v, targets.size());       // search based on the number of remaining targets

        // go add up the weights of all the marked edges
        int length = 0;
        Iterator<Edge> itr = g.edgeIterator();  // iterate over all edges in the graph
        while (itr.hasNext()) {
            Edge e = itr.next();
            if (e.getMark() == 1)
                length += e.getWeight();
        }

        return length;
    }

    // Recursively depth first search the graph until all targets are reached.
    // As it searches, it tags vertices as reached by setting their value field to 1.
    // It sets the mark field for edges that were used to reach new target vertices.
    // We can tell this occurred when we return from a recursive search of an edge and
    // the number of targets remaining has decreased.
    //
    // We exit the dfs early once all targets have been found.
    //
    // returns: the number of targets still remaining
    public static int dfs(Graph g, Vertex v, int targetsRemaining) {
        v.setValue(1);             // set value to indicated this vertex has been reached
        if (v.getMark() == 1)
            targetsRemaining--;    // we found a target vertex
        if (targetsRemaining == 0)
            return 0;				// all targets found, we are done

        // iterate over all edges out of this vertex
        for (Edge e : v) {
            Vertex newv = e.getOppositeVertexOf(v);
            if (newv.getValue() == 0) { // found an unreached vertex
                e.setMark(1);              // we are considering
                e.setColor(Color.GREEN);     // color it green for animation)
                SteinerTreeTester.show(g);
                int newRemaining = dfs(g, newv, targetsRemaining); // recursively search
                if (newRemaining < targetsRemaining) { // did this edge lead to any new targets
                    targetsRemaining = newRemaining;
                    e.setMark(1);                 // mark this edge as part of solution
                    SteinerTreeTester.show(g);    // for animation show the graph at this point
                }
                else {
                    e.setMark(0);                 // unmark, this lead to nothing used
                    e.setColor(Color.RED);        // draw in red
                    SteinerTreeTester.show(g);
                }
                if (targetsRemaining == 0)
                    return 0;				// all targets found, we are done
            }
        }

        return targetsRemaining;
    }
}

