
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.Topological;
import java.util.ArrayList;

/**
 *
 * @author Ghaith
 */
public final class WordNet {

    private final ArrayList<String> nounsList;
    private final ArrayList<String> TINounsList;
    private final BST<String, Node> nouns;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        Digraph graph;
        ArrayList<String> defs;

        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("argument is null");
        }

        In synsetsIn = new In(synsets);
        In hypernymsIn = new In(hypernyms);
        nouns = new BST<>();
        defs = new ArrayList<>();
        nounsList = new ArrayList<>();
        TINounsList = new ArrayList<>();

        String[] synset;
        int index;
        while (synsetsIn.hasNextLine()) {
            String[] line = synsetsIn.readLine().split(",");
            index = Integer.parseInt(line[0]);

            TINounsList.add(line[1]);

            defs.add(index, line[2]);

        }
        for (int i = 0; i < TINounsList.size(); i++) {

            synset = TINounsList.get(i).split("\\s+");
            for (String synset1 : synset) {
                if (!isNoun(synset1)) {
                    nounsList.add(synset1);
                    Node n = new Node(i);
                    nouns.put(synset1, new Node(i));
                } else {
                    Node temp = nouns.get(synset1);
                    nouns.put(synset1, new Node(i));
                    nouns.get(synset1).next = temp;

                }

            }
        }
        graph = new Digraph(nouns.size());
        while (hypernymsIn.hasNextLine()) {
            String[] line = hypernymsIn.readLine().split(",");
            int v = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                graph.addEdge(v, Integer.parseInt(line[i]));
            }
        }
        Topological topological = new Topological(graph);
        if (!topological.hasOrder()) {
            throw new IllegalArgumentException("Digraph has cycle");
        }
        int heads = 0;
        for (int i = 0; i < graph.V(); i++) {
            if (graph.outdegree(i) == 0 && graph.indegree(i) > 0) {
                heads++;
                if (heads > 1) {
                    break;
                }
            }
        }
        if (heads > 1) {
            throw new IllegalArgumentException("digraph have more than one root");
        }
        sap = new SAP(graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return () -> nounsList.iterator();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return (nouns.get(word) != null);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounB) || !isNoun(nounA)) {
            throw new IllegalArgumentException("noun argument is not a WordNet noun.");
        }
        if (nounA.equals(nounB)) {
            return 0;
        }
        ArrayList<Integer> A = new ArrayList<>();
        ArrayList<Integer> B = new ArrayList<>();
        Node n = nouns.get(nounA);
        while (n != null) {
            A.add(n.val);
            n = n.next;

        }
        n = nouns.get(nounB);
        while (n != null) {
            B.add(n.val);
            n = n.next;
        }

        return sap.length(() -> A.iterator(), () -> B.iterator());
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounB) || !isNoun(nounA)) {
            throw new IllegalArgumentException("noun argument is not a WordNet noun.");
        }
        ArrayList<Integer> A = new ArrayList<>();
        ArrayList<Integer> B = new ArrayList<>();
        Node n = nouns.get(nounA);
        while (n != null) {
            A.add(n.val);
            n = n.next;

        }
        n = nouns.get(nounB);
        while (n != null) {
            B.add(n.val);
            n = n.next;
        }
     //   System.out.println(sap.ancestor(() -> A.iterator(), () -> B.iterator()));
        return TINounsList.get(sap.ancestor(() -> A.iterator(), () -> B.iterator()));
    }

    public static void main(String args[]) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println("********************************************");
        System.out.println(wn.distance("Abnaki", "Aaron"));
        System.out.println(wn.sap("Abnaki", "Aaron"));
    }

    private class Node {

        int val;
        Node next = null;

        public Node(int val) {
            this.val = val;
        }
    }

}
