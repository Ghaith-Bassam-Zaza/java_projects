import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import java.util.ArrayList;

/**
 *
 * @author Ghaith
 */
public class SAP {

    private final Digraph graph;

    private ArrayList<Ansastor> getAnsastors(Iterable<Integer> w) {
        Queue<Integer> q = new Queue<>();
        Queue<Integer> qd = new Queue<>();
        boolean[] marked = new boolean[graph.V()];
        ArrayList<Ansastor> ansastorsW = new ArrayList<>();

        for (int wo : w) {
            if (wo >= graph.V() || wo < 0) {
                throw new IllegalArgumentException("invalid vertex value");
            }
            marked[wo] = true;
            q.enqueue(wo);
            qd.enqueue(0);
        }
        while (!q.isEmpty()) {
            int i = q.dequeue();
            int deg = qd.dequeue();
            boolean found = false;
            for (Ansastor a : ansastorsW) {
                boolean f = a.self == i;
                if (f && a.deg > deg) {
                    a.deg = deg;
                }
                if (f) {
                    found = true;
                }
            }
            if (!found) {
                ansastorsW.add(new Ansastor(i, deg));
            }
            for (int n : graph.adj(i)) {
                if (!marked[n]) {
                    marked[n] = true;
                    q.enqueue(n);
                    qd.enqueue(deg + 1);
                }
            }
        }

        return ansastorsW;
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException(" Graph cannot be null");
        }
        graph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int length = -1;
        if (graph.V() <= v || graph.V() <= w || v < 0 || w < 0) {
            throw new IllegalArgumentException("invalid vertices");
        }
        if (v == w) {
            return 0;
        }
//        BreadthFirstDirectedPaths BFSV = new BreadthFirstDirectedPaths(graph, v);
        Queue<Integer> q = new Queue<>();
        Queue<Integer> qd = new Queue<>();
        boolean[] marked = new boolean[graph.V()];
        q.enqueue(w);
        qd.enqueue(0);
//        Iterable<Integer> path;
        while (!q.isEmpty()) {
            int i = q.dequeue();
            int deg = qd.dequeue();
//            path = BFSV.pathTo(i);

//            if (path != null) {
            int dist = getDist(v, i);

//                for (int n : path) {
//                    dist++;
//                }
            if (dist > -1 && (dist + deg  < length || length == -1)) {
                length = dist + deg;
            }
//            }

            for (int n : graph.adj(i)) {
                if (!marked[n]) {
                    marked[n] = true;
                    q.enqueue(n);
                    qd.enqueue(deg + 1);
                }
            }
        }

        return length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int length = -1;
        int ans = -1;
        if (graph.V() <= v || graph.V() <= w || v < 0 || w < 0) {
            throw new IllegalArgumentException("invalid vertices");
        }
        if (v == w) {
            return v;
        }
//        BreadthFirstDirectedPaths BFSV = new BreadthFirstDirectedPaths(graph, v);
        Queue<Integer> q = new Queue<>();
        Queue<Integer> qd = new Queue<>();
        boolean[] marked = new boolean[graph.V()];
        q.enqueue(w);
        qd.enqueue(0);
//        Iterable<Integer> path;
        while (!q.isEmpty()) {
            int i = q.dequeue();
            int deg = qd.dequeue();
//            path = BFSV.pathTo(i);

//            if (path != null) {
            int dist = getDist(v, i);

//                for (int n : path) {
//                    dist++;
//                }
            if (dist > -1 && (dist + deg  < length || length == -1)) {
                length = dist + deg ;
                ans = i;
            }
//            }

            for (int n : graph.adj(i)) {
                if (!marked[n]) {
                    marked[n] = true;
                    q.enqueue(n);
                    qd.enqueue(deg + 1);
                }
            }
        }

        return ans;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("iterators cannot have null values");
        }
        int len = -1;

        try {
            Iterable<Integer> path;
            ArrayList<Ansastor> ansastorsW = getAnsastors(w);
            for (int vo : v) {
                if (vo >= graph.V() || vo < 0) {
                    throw new IllegalArgumentException("invalid vertex value");
                }
                BreadthFirstDirectedPaths BFSV = new BreadthFirstDirectedPaths(graph, vo);

                for (Ansastor awo : ansastorsW) {

                    path = BFSV.pathTo(awo.self);

                    if (path != null) {
                        int dist = 0;
                        for (int n : path) {
                            dist++;
                        }

                        if (len == -1 || len > dist + awo.deg - 1) {
                            len = dist + awo.deg - 1;
                        }
                    }

                }
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("iterators cannot have null values");
        }
        return len;


    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("iterators cannot have null values");
        }
        int len = -1;
        int ans = -1;
        try {
            Iterable<Integer> path;
            ArrayList<Ansastor> ansastorsW = getAnsastors(w);
            for (int vo : v) {
                if (vo >= graph.V() || vo < 0) {
                    throw new IllegalArgumentException("invalid vertex value");
                }
                BreadthFirstDirectedPaths BFSV = new BreadthFirstDirectedPaths(graph, vo);

                for (Ansastor awo : ansastorsW) {

                    path = BFSV.pathTo(awo.self);

                    if (path != null) {
                        int dist = 0;
                        for (int n : path) {
                            dist++;
                        }

                        if (len == -1 || len > dist + awo.deg - 1) {
                            len = dist + awo.deg - 1;
                            ans = awo.self;
                        }
                    }

                }
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("iterators cannot have null values");
        }

        return ans;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("hypernyms.txt");
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

    private int getDist(int v, int w) {
//        BreadthFirstDirectedPaths B = new BreadthFirstDirectedPaths(graph, v);
//        int dist = B.distTo(w);
//        if (dist == Integer.MAX_VALUE) {
//            return -1;
//        }
//        return B.distTo(w);

        Queue<Integer> q = new Queue<>();
        Queue<Integer> qd = new Queue<>();
        boolean[] marked = new boolean[graph.V()];
        marked[v] = true;
        q.enqueue(v);
        qd.enqueue(0);
        while (!q.isEmpty()) {
            int i = q.dequeue();
            int deg = qd.dequeue();
            if (i == w) {
                return deg;
            }
            for (int n : graph.adj(i)) {
                if (!marked[n]) {
                    marked[n] = true;
                    q.enqueue(n);
                    qd.enqueue(deg + 1);
                }
            }
        }
        return -1;
    }

    private class Ansastor {

        final int self;

        int deg;

        public Ansastor(int self, int deg) {
            this.self = self;
            this.deg = deg;

        }

    }

}
