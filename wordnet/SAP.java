import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph digraph;
    private int totalDistance;
    private int closestCommonAncestor;
    private final int verts;

    public SAP(Digraph G) {
        digraph = new Digraph(G);
        totalDistance = Integer.MAX_VALUE;
        closestCommonAncestor = -1;
        verts = digraph.V();
    }

    private void search(int v, int w) {
        totalDistance = Integer.MAX_VALUE;
        closestCommonAncestor = -1;
        if (v != w) {
            BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(digraph, v);
            BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(digraph, w);
            for (int i = 0; i < verts; i++) {
                if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                    int dist = bfdpV.distTo(i) + bfdpW.distTo(i);
                    if (dist < totalDistance) {
                        totalDistance = dist;
                        closestCommonAncestor = i;
                    }
                }
            }
        }
        else {
            totalDistance = 0;
            closestCommonAncestor = v;
        }


    }

    private void search(Iterable<Integer> v, Iterable<Integer> w) {
        totalDistance = Integer.MAX_VALUE;
        closestCommonAncestor = -1;
        if (v.iterator().hasNext() && w.iterator().hasNext()) {
            BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(digraph, v);
            BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(digraph, w);
            for (int i = 0; i < verts; i++) {
                if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                    int dist = bfdpV.distTo(i) + bfdpW.distTo(i);
                    if (dist < totalDistance) {
                        totalDistance = dist;
                        closestCommonAncestor = i;
                    }
                }
            }
        }

    }

    public int length(int v, int w) {
        if (v >= verts || v < 0) throw new IllegalArgumentException();
        if (w >= verts || w < 0) throw new IllegalArgumentException();
        search(v, w);
        if (totalDistance == Integer.MAX_VALUE) return -1;
        return totalDistance;
    }

    public int ancestor(int v, int w) {
        if (v >= verts || v < 0) throw new IllegalArgumentException();
        if (w >= verts || w < 0) throw new IllegalArgumentException();
        search(v, w);
        if (closestCommonAncestor == -1) return -1;
        return closestCommonAncestor;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        /* int shortestDistance = Integer.MAX_VALUE;
        for (int i : v) {
            for (int j : w) {
                if (length(i, j) < shortestDistance) {
                    shortestDistance = totalDistance;
                }
            }
        }

        if (shortestDistance == Integer.MAX_VALUE) {
            return -1;
        }
        return shortestDistance; */
        search(v, w);
        if (totalDistance == Integer.MAX_VALUE) return -1;
        return totalDistance;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        /* int shortestDistance = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i : v) {
            for (int j : w) {
                if (length(i, j) < shortestDistance) {
                    shortestDistance = totalDistance;
                    ancestor = closestCommonAncestor;
                }
            }
        }

        if (ancestor == -1) return -1;
        return ancestor; */
        search(v, w);
        if (closestCommonAncestor == -1) return -1;
        return closestCommonAncestor;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

    }
}
