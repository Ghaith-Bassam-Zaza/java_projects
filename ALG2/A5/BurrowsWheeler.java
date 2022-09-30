
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Ghaith
 */
public class BurrowsWheeler {

    private static class element implements Comparable<Object> {

        char val;
        int index;

        @Override
        public int compareTo(Object o) {
            if (val > ((element) o).val) {
                return 1;
            }
            if (val < ((element) o).val) {
                return -1;
            }
            return 0;
        }

    }

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
        while (!BinaryStdIn.isEmpty()) {
            int first = 0;
            String s = BinaryStdIn.readString();
            CircularSuffixArray csa = new CircularSuffixArray(s);
            for (int i = 0; i < csa.length(); i++) {
                if (csa.index(i) == 0) {
                    first = i;
                }
            }
//            BinaryStdOut.write(String.format("%02X", (first >> 24) & 0x000000FF) + " ");
//            BinaryStdOut.write(String.format("%02X", (first >> 16) & 0x000000FF) + " ");
//            BinaryStdOut.write(String.format("%02X", (first >> 8) & 0x000000FF) + " ");
//            BinaryStdOut.write(String.format("%02X", first & 0x000000FF) + " ");
            BinaryStdOut.write(first);
            for (int i = 0; i < csa.length(); i++) {
                if (csa.index(i) == 0) {
                    BinaryStdOut.write(s.charAt(csa.length() - 1));
                } else {
                    BinaryStdOut.write(s.charAt(csa.index(i) - 1));
                }
            }
            // BinaryStdOut.write("\n" + ((csa.length() + 4) * 8) + " bits");
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();

        int il = 1;
        ArrayList<element> elements = new ArrayList<>();
        while (!BinaryStdIn.isEmpty()) {

            elements.add(new element());
            elements.get(il - 1).index = il - 1;
            elements.get(il - 1).val = BinaryStdIn.readChar();
            il++;
        }
        Object[] t = elements.toArray();
        Arrays.sort(t);
        int i = first;
        int j = 0;
        char[] str = new char[t.length];
        do {
            str[j] = ((element) t[i]).val;
            i = ((element) t[i]).index;
            j++;
        } while (i != first);
        BinaryStdOut.write(new String(str));

        BinaryStdOut.flush();
    }
    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform

    public static void main(String[] args) {
        switch (args[0]) {
            case "-":
                transform();
                break;
            case "+":
                inverseTransform();
                break;
            default:
                throw new IllegalArgumentException("\"-\" apply move-to-front encoding | \"+\", apply move-to-front decoding");
        }

    }

}
