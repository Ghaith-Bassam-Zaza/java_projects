import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 *
 * @author Ghaith
 */
public class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;

    }
    // given an array of WordNet nouns, return an outcast

    public String outcast(String[] nouns) {

        String outcast = null;
        int dTemp;
        int dMax = 0;
        for (String s0 : nouns) {
            dTemp = 0;
            for (String s1 : nouns) {
                dTemp += wordnet.distance(s0, s1);

            }
            if (dTemp > dMax) {
                dMax = dTemp;
                outcast = s0;
            }
        }
        return outcast;

    }
    // see test client below

    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}
