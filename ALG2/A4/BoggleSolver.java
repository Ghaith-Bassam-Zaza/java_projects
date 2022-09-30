import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;

/**
 *
 * @author Ghaith
 */
public class BoggleSolver {

    private final TrieST tst;
    private final String[] dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new String[dictionary.length];

        System.arraycopy(dictionary, 0, this.dictionary, 0, dictionary.length);
        tst = new TrieST();
        for (String str : this.dictionary) {
            tst.put(str, Boolean.TRUE);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        StringBuilder prifex = new StringBuilder();
        int[] branches = tst.getValueBranches(null);
        ArrayList<String> validWords = new ArrayList<>();
        for (int i = 0; i < board.cols(); i++) {
            for (int j = 0; j < board.rows(); j++) {
                char x = board.getLetter(j, i);

                if (branches[x - 'A'] > -1) {
                    boolean[][] used = new boolean[board.rows()][board.cols()];
                    used[j][i] = true;

                    prifex.append(x);
                    if (x == 'Q') {
                        prifex.append('U');
                    }
                    getExtentions(prifex, used, board, i, j, validWords);
                    prifex.delete(0, 2);
                    
                }

            }
        }
        for (String s : validWords) {
            tst.put(s, Boolean.TRUE);
        }
        return validWords;
    }

    private void getExtentions(StringBuilder prifex, boolean[][] used, BoggleBoard board, int lastX, int lastY, ArrayList<String> validWords) {

        int[] branches = tst.getValueBranches(prifex.toString());
        if (branches == null) {
            return;
        }
        for (int i = lastX - 1; i <= lastX + 1; i++) {
            for (int j = lastY - 1; j <= lastY + 1; j++) {
                if (i == -1 || j == -1 || i == board.cols() || j == board.rows() || i == lastX && j == lastY || used[j][i]) {
                    continue;
                }
                char x = board.getLetter(j, i);

                if (branches[x - 'A'] > -1) {

                    used[j][i] = true;

                    prifex.append(x);
                    if (x == 'Q') {
                        prifex.append('U');
                    }
                    String str = prifex.toString();
                    Boolean b = tst.shut(str);
                    if (branches[x - 'A'] > 0 && prifex.length() > 2 && b != null && b) {
                        validWords.add(str);
                    }

                    getExtentions(prifex, used, board, i, j, validWords);
                    prifex.deleteCharAt(prifex.length() - 1);
                    if (x == 'Q') {
                        prifex.deleteCharAt(prifex.length() - 1);
                    }
                    used[j][i] = false;

                }

            }
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null || word.length() < 3 || tst.get(word) == null) {
            return 0;
        }

        switch (word.length()) {
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    public static void main(String[] args) {
        In in = new In("dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board4x4.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    private class TrieST {

        private static final int R = 26;
        private Node root = new Node();

        private class Node {

            Boolean val;
            Node[] next = new Node[R];
        }

        public void put(String key, Boolean val) {
            root = put(root, key, val, 0);
        }

        public Boolean shut(String key) {
            Node x = get(root, key, 0);
            if (x == null || x.val == null) {
                return null;
            }
            if (x.val == true) {
                x.val = false;
                return true;
            } else {
                return false;
            }
        }

        private Node put(Node x, String key, Boolean val, int d) {
            if (x == null) {
                x = new Node();
            }
            if (d == key.length()) {
                x.val = val;
                return x;
            }
            int c = key.charAt(d) - 'A';
            x.next[c] = put(x.next[c], key, val, d + 1);
            return x;
        }

        public Boolean get(String key) {
            Node x = get(root, key, 0);
            if (x == null) {
                return null;
            }
            return x.val;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) {
                return null;
            }
            if (d == key.length()) {
                return x;
            }
            char c = key.charAt(d);
            return get(x.next[c - 'A'], key, d + 1);
        }

        public int[] getValueBranches(String prefix) {
            Node x;

            if (prefix == null) {
                x = root;
            } else {
                x = get(root, prefix, 0);
            }
            if (x == null) {
                return null;
            }
            int[] branches = new int[R];
            for (int i = 0; i < R; i++) {
                if (x.next[i] == null) {
                    branches[i] = -1;
                } else if (x.next[i].val == null) {
                    branches[i] = 0;
                } else {
                    branches[i] = 1;
                }
            }
            return branches;
        }
    }

}
