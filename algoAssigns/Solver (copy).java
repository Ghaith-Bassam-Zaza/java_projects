package pkg8puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Comparator;
import edu.princeton.cs.algs4.Stack;
import java.util.Iterator;

/**
 *
 * @author ghaith
 */
public class Solver {

    private Stack<Board> boardStack;
    private Step Goal;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial != null) {
            solvable = true;
            MinPQ<Step> queue = new MinPQ<>(new stepsComparator());
            MinPQ<Step> twinQueue = new MinPQ<>(new stepsComparator());
            queue.insert(new Step(initial, 0, null));
            twinQueue.insert(new Step(initial.twin(), 0, null));
            boolean acheived = false;
            if (initial.isGoal()) {
                Goal = new Step(initial, 0, null);
                acheived = true;
            }
            if (initial.twin().isGoal()) {
                solvable = false;
                acheived = true;
            }
            while (true) {

                if (acheived) {
                    break;
                }

                Step min = queue.delMin();
                for (Board neighboard : min.board.neighbors()) {
                    if (!neighboard.equals(min.board)) {
                        Step in = new Step(neighboard, min.move + 1, min);
                        queue.insert(in);
                        if (neighboard.isGoal()) {
                            Goal = in;
                            acheived = true;
                            break;
                        }
                    }
                }
                    if (acheived) {
                    break;
                }

                
                
                Step twinMin = queue.delMin();
                for (Board neighboard : twinMin.board.neighbors()) {
                    if (!neighboard.equals(twinMin.board)) {
                        Step in = new Step(neighboard, twinMin.move + 1, twinMin);
                        queue.insert(in);
                        if (neighboard.isGoal()) {
                            solvable = false;
                            acheived = true;
                            break;
                        }
                    }
                }
                 
            }
        } else {
            throw new IllegalArgumentException();
        }

    }

    private class stepsComparator implements Comparator<Step> {

        @Override
        public int compare(Step arg0, Step arg1) {
            int s0 = arg0.move + arg0.board.manhattan();
            int s1 = arg1.move + arg1.board.manhattan();
            if (s0 < s1) {
                return -1;
            } else if (s0 > s1) {
                return 1;
            } else {
                return 0;
            }
        }

    }

    private class Step {

        final int move;
        final Board board;
        final Step previous;

        public Step(Board board, int move, Step previous) {
            this.board = board;
            this.move = move;
            this.previous = previous;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solvable) {
            return Goal.move;
        }
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solvable) {
            Step s = Goal;
            boardStack = new Stack<>();
            while (s != null) {
                boardStack.push(s.board);
                s = s.previous;
            }
            return new iterstack();
        }
        return null;
    }

    private class iterstack implements Iterable<Board> {

        @Override
        public Iterator<Board> iterator() {
            return boardStack.iterator();
        }

    }

    // test client (see below) 
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

}

