

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 *
 * @author ghaith
 */
public final class PercolationStats {

    private static final double C = 1.96;
    private final double[] p;
    private final int t;
    private final double x, s, ch, cl;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n > 0 && trials > 0) {
            t = trials;
            p = new double[trials];
            for (int i = 0; i < trials; i++) {
                Percolation perc = new Percolation(n);
                while (true) {
                    perc.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
                    if (perc.percolates()) {
                        break;
                    }
                }
                p[i] = (double) perc.numberOfOpenSites() / (double) (n * n);
            }
            x = StdStats.mean(p);
            s = StdStats.stddev(p);
            ch = confidenceHi();
            cl = confidenceLo();

        } else {
            throw (new IllegalArgumentException());
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        return x;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return s;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return x - (C * s) / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return x + (C * s) / Math.sqrt(t);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats PS = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = " + PS.x);
        System.out.println("stddev                  = " + PS.s);
        System.out.println("95% confidence interval = [" + PS.cl + ", " + PS.ch + "]");

    }

}

