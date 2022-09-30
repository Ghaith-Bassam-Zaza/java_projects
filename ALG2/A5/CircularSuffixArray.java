
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

/**
 *
 * @author Ghaith
 */
public class CircularSuffixArray {

    private final int index[];

    private class element implements Comparable<Object> {

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

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("null string!");
        }
        index = new int[s.length()];
        if (s.length() == 1) {
            index[0] = 0;
            return;
        }

        if (s == null) {
            throw new IllegalArgumentException("string cannot be null");
        }
        element[] elements = new element[s.length()];
        for (int i = 0; i < s.length(); i++) {
            elements[i] = new element();
            elements[i].index = i;
            elements[i].val = s.charAt(i);
        }
        Arrays.sort(elements);
        for (int i = 0; i < s.length();) {
            int countMax = 0;
            int count;
            int[] charShift = new int[s.length()];
            int iShift = 0;
            do {

                count = 0;
                while ((countMax == 0 || iShift + count < countMax)
                        && i + iShift + count < s.length()
                        && (charShift[i + iShift] == charShift[i + iShift + count])
                        && elements[i + iShift + count].compareTo(elements[i + iShift]) == 0) {

                    count++;

                }
                if (countMax == 0) {
                    countMax = count;
                } else if (count > countMax) {
                    count = countMax;
                }
//                for (element e : elements) {
//                    System.out.print(e.val + " ");
//                }
//                System.out.println("");
                if (count > 1) {
                    for (int j = 0; j < count; j++) {
                        charShift[i + iShift + j]++;
                        if (charShift[i + iShift + j] > s.length() - 1) {
                            break;
                        }
                        if (elements[i + iShift + j].index + charShift[i + iShift] > s.length() - 1) {
                            elements[i + iShift + j].val = s.charAt(elements[i + iShift + j].index + charShift[i + iShift] - s.length());
                        } else {
                            elements[i + iShift + j].val = s.charAt(elements[i + iShift + j].index + charShift[i + iShift]);

                        }

                    }

                }
//                for (element e : elements) {
//                    System.out.print(e.index + " ");
//                }
//                System.out.println("");
//                for (element e : elements) {
//                    System.out.print(e.val + " ");
//                }
//                System.out.println("");
//                System.out.println((i + iShift) + ", " + (i + iShift + count));
                if (count > 1) {
                    Arrays.sort(elements, i + iShift, i + iShift + count);
                }
//                for (element e : elements) {
//                    System.out.print(e.index + " ");
//                }
//                System.out.println("");
//                for (element e : elements) {
//                    System.out.print(e.val + " ");
//                }
//                System.out.println("\n.....................\n");
                if (iShift < countMax && count == 1) {
                    iShift++;

                }

            } while (count > 1 || iShift < countMax);
            i += countMax;
        }
        for (int i = 0; i < s.length(); i++) {
            index[i] = elements[i].index;
        }
    }

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= index.length) {
            throw new IllegalArgumentException("index out of bounds");
        }
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {

        CircularSuffixArray csa = new CircularSuffixArray("ABBBAA");//450321
        for (int i = 0; i < csa.length(); i++) {
            StdOut.print(csa.index(i) + " ");

        }

    }

}
//ABBBAA
//BBBAAA
//BBAAAB
//BAAABB
//AAABBB
//AABBBA

