import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;


public class RandomWord {

    public static void main(String[] args) {
        String champ = null;
        double i = 1;
        while (!StdIn.isEmpty()) {
            String temp = StdIn.readString();
            if (temp.equals(".")) {
                break;
            }
            if (StdRandom.bernoulli(1 / i)) {
                champ = temp;
            }
            i++;
        }
        StdOut.println(champ);
    }

}
