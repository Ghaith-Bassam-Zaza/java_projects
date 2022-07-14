import edu.princeton.cs.algs4.StdRandom;

/**
 *
 * @author ghaith
 */
public class Percolation {

    private final boolean[][] open;
    private final boolean[][] full;
    private final boolean[][] r, d;

    private int openSites;
    private final int n;
    private boolean percolated;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 0) {
            throw (new IllegalArgumentException("use suitable numbers"));
        }
        open = new boolean[n][n];
        full = new boolean[n][n];
        r = new boolean[n][n];
        d = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                r[i][j] = false;
                d[i][j] = false;
                open[i][j] = false;
                full[i][j] = false;
            }
        }
        openSites = 0;
        this.n = n;
        percolated = false;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        chkerror(row, col);
        row--;
        col--;
        if (open[row][col]) {
            return;
        }
        openSites++;
        open[row][col] = true;
        if (row != 0 && open[row - 1][col]) {
            d[row - 1][col] = true;
        }
        if (row != n - 1 && open[row + 1][col]) {
            d[row][col] = true;
        }
        if (col != n - 1 && open[row][col + 1]) {
            r[row][col] = true;
        }
        if (col != 0 && open[row][col - 1]) {
            r[row][col - 1] = true;
        }

        if (row == 0
                || (d[row - 1][col] && full[row - 1][col])
                || (d[row][col] && full[row + 1][col])
                || (col != 0 && r[row][col - 1] && full[row][col - 1])
                || (r[row][col] && full[row][col + 1])) {
            fill(row, col);
        }

    }

    private void fill(int row, int col) {
        if (full[row][col]) {
            return;
        }
        full[row][col] = true;
        if (row != 0 && d[row - 1][col]) {
            fill(row - 1, col);
        }
        if (d[row][col]) {
            fill(row + 1, col);
        }
        if (col != 0 && r[row][col - 1]) {
            fill(row, col - 1);
        }
        if (r[row][col]) {
            fill(row, col + 1);
        }
        if (row == n - 1) {
            percolated = true;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        chkerror(row, col);
        return open[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        chkerror(row, col);
        return full[row - 1][col - 1];
    }

    private void chkerror(int row, int col) {
        if (row < 0 || col < 0 || row > n || col > n) {
            throw (new IllegalArgumentException("use suitable numbers"));
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolated;
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 1000;
        Percolation perc = new Percolation(n);
        while (true) {
            perc.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            if (perc.percolates()) {
                break;
            }
        }
        double p = (double) perc.numberOfOpenSites() / (double) (n * n);
        System.out.println(p);
    }

}

