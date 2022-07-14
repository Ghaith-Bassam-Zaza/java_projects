

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private int opensites;
    private final boolean[][] open;
    private final WeightedQuickUnionUF uf1;
    private final WeightedQuickUnionUF uf2;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n > 0) {
            this.n = n;
            open = new boolean[n][n];
            opensites = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    open[i][j] = false;
                }
            }

            uf1 = new WeightedQuickUnionUF((n * n) + 2);
            uf2 = new WeightedQuickUnionUF((n * n) + 2);

        } else {
            throw new IllegalArgumentException(":(");
        }
    }

    private int twoDtoD(int row, int col) {
        return (row - 1) * n + col;
    }
    // opens the site (row, col) if it is not open already

    public void open(int row, int col) {
        if (row > 0 && row <= n && col <= n && col > 0) {
            if (open[row - 1][col - 1]) {
                return;
            }
            open[row - 1][col - 1] = true;
            opensites++;
            int x = twoDtoD(row, col);
            if (col > 1) {
                if (isOpen(row, col - 1)) {
                    uf1.union(x, x - 1);
                    uf2.union(x, x - 1);
                }
            }
            if (col < n) {
                if (isOpen(row, col + 1)) {
                    uf1.union(x, x + 1);
                    uf2.union(x, x + 1);
                }
            }
            if (row <= 1) {
                uf1.union(x, 0);
                uf2.union(x, 0);
            } else {
                if (isOpen(row - 1, col)) {
                    uf1.union(x, x - n);
                    uf2.union(x, x - n);
                }
            }
            if (row >= n) {
                uf2.union(x, n * n + 1);
            } else {
                if (isOpen(row + 1, col)) {
                    uf1.union(x, x + n);
                    uf2.union(x, x + n);
                }
            }

        } else {

            throw new IllegalArgumentException(":(");

        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > 0 && row <= n && col <= n && col > 0) {
            try {
                return open[row - 1][col - 1];
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }

        } else {
            throw new IllegalArgumentException(":(");

        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > 0 && row <= n && col <= n && col > 0) {
            int x = twoDtoD(row, col);
            return uf1.find(x) == uf1.find(0);

        } else {
            throw new IllegalArgumentException();
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opensites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf2.find(n * n + 1) == uf2.find(0);
    }
}

