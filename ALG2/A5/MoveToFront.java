
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 *
 * @author Ghaith
 */
public class MoveToFront {

    private static final int R = 256;
    private static Node frontNode;

    private static class Node {

        char val;
        Node next;

        public Node(char val, Node next) {
            this.val = val;
            this.next = next;
        }

    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        intitialize();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            Node node = frontNode;
            Node prev = null;
            int i = 0;
            while (node.val != c) {
                prev = node;
                node = node.next;
                i++;
            }
            BinaryStdOut.write((char) (i & 0xff));
            if (prev != null) {

                prev.next = node.next;
                node.next = frontNode;
                frontNode = node;
            }
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        intitialize();
        while (!BinaryStdIn.isEmpty()) {
            int in = (int) BinaryStdIn.readChar();
            Node node = frontNode;
            Node prev = null;
            for (int i = 0; i < in; i++) {
                prev = node;
                node = node.next;

            }
            BinaryStdOut.write(node.val);
            if (prev != null) {

                prev.next = node.next;
                node.next = frontNode;
                frontNode = node;
            }

        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        switch (args[0]) {
            case "-":
                encode();
                break;
            case "+":
                decode();
                break;
            default:
                throw new IllegalArgumentException("\"-\" apply move-to-front encoding | \"+\", apply move-to-front decoding");
        }
    }

    private static void intitialize() {
        frontNode = new Node((char) 0, null);
        Node tempNode = frontNode;
        for (int i = 1; i < R; i++) {
            tempNode.next = new Node((char) i, null);
            tempNode = tempNode.next;
        }
    }

}
