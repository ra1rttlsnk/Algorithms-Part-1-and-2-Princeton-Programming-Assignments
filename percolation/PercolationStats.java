import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int dim;
    private final int t;
    private final double[] fractions;

    public PercolationStats(int n, int trials) {

        if (n < 1 || trials < 1)
            throw new IllegalArgumentException();
        Percolation p;
        dim = n;
        t = trials;
        fractions = new double[trials];

        for (int i = 0; i < trials; i++) {
            p = new Percolation(n);
            openUntilPercolation(p);
            fractions[i] = p.numberOfOpenSites() / (double) (n * n);
        }

    }

    private void openRandomly(Percolation p) {
        p.open(StdRandom.uniform(dim) + 1, StdRandom.uniform(dim) + 1);
    }

    private void openUntilPercolation(Percolation p) {
        while (!p.percolates()) {
            openRandomly(p);
        }
    }

    public double mean() {
        return StdStats.mean(fractions);
    }

    public double stddev() {
        return StdStats.stddev(fractions);
    }

    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(t));
    }

    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(t));
    }

    public static void main(String[] args) {
        PercolationStats pstats = new PercolationStats(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
        );
        StdOut.println("mean = " + pstats.mean());
        StdOut.println("stddev = " + pstats.stddev());
        StdOut.println("95% confidence interval = " + "[" +
                               pstats.confidenceLo() + ", " + pstats.confidenceHi() + "]");

    }
}
