

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Step step;
    private boolean solvable;
    private int moves = 0;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.IllegalArgumentException();
        }
        //minMoves = initial.manhattan();
        int myMoves = 0;
        int twinMoves = 0;

        Queue<Board> neighbors = new Queue<>();
        Queue<Board> twinNeighbors = new Queue<>();

        MinPQ<Step> steps = new MinPQ<>();
        MinPQ<Step> twinSteps = new MinPQ<>();

        Step searchStep = new Step(initial, myMoves, null);
        Step twinSearchStep = new Step(initial.twin(), twinMoves, null);

        twinSteps.insert(twinSearchStep);
        steps.insert(searchStep);

        boolean solved = false;
        boolean twinSolved = false;
        Step current = null;

        while (!solved && !twinSolved) {
            current = steps.delMin();
            Step predecessor = current.getPredecessor();
            Board temp = current.getBoard();
            solved = temp.isGoal();

            Step twinCurrent = twinSteps.delMin();
            Step twinPredecessor = twinCurrent.getPredecessor();
            Board twinTemp = twinCurrent.getBoard();
            twinSolved = twinTemp.isGoal();

            for (Board b : temp.neighbors()) {
                neighbors.enqueue(b);
            }

            for (Board b : twinTemp.neighbors()) {
                twinNeighbors.enqueue(b);
            }

            while (neighbors.size() > 0) {
                Board board = neighbors.dequeue();
                int move = current.getMoves();
                move++;
                if (predecessor != null && predecessor.getBoard().equals(board)) {
                    continue;
                }

                Step neighborNode = new Step(board, move, current);
                steps.insert(neighborNode);
            }

            while (twinNeighbors.size() > 0) {
                Board board = twinNeighbors.dequeue();
                int twinMove = current.getMoves();
                twinMove++;
                if (twinPredecessor != null && twinPredecessor.getBoard().equals(board)) {
                    continue;
                }

                Step neighborNode = new Step(board, twinMove, twinCurrent);
                twinSteps.insert(neighborNode);
            }

            myMoves = current.getMoves() + 1;
            twinMoves = twinCurrent.getMoves() + 1;
            step = current;
        }

        solvable = !twinSolved;
        this.moves = myMoves - 1;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;

    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return moves;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Stack<Board> boards = new Stack<>();
        Step lastNode = this.step;
        if (this.isSolvable()) {
            while (lastNode.getPredecessor() != null) {
                boards.push(lastNode.getBoard());
                lastNode = lastNode.getPredecessor();
            }
            boards.push(lastNode.getBoard());
            return boards;
        }
        return null;
    }

    private class Step implements Comparable<Step> {

        private Step predecessor = null;
        private Board current = null;
        private int moves = 0;
        private int priority = 0;

        public Step(Board initial, int m, Step pred) {
            predecessor = pred;
            moves = m;
            current = initial;

            priority = m + initial.manhattan();
        }

        public int getPriority() {
            return priority;
        }

        public Board getBoard() {
            Board temp = current;
            return temp;
        }

        public int getMoves() {
            return moves;
        }

        public Step getPredecessor() {
            Step temp = predecessor;
            return temp;
        }

        @Override
        public int compareTo(Step o) {

            return this.priority - o.priority;
        }
    }

    // solve a slider puzzle
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

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

