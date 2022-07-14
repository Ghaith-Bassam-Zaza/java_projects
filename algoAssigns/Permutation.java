

import edu.princeton.cs.algs4.StdIn;

/**
 *
 * @author ghaith
 */
public class Permutation {

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> que = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            que.enqueue(StdIn.readString());
        }
        for (int i = 0; i < k; i++) {
            System.out.println(que.dequeue());
        }
    }

}

