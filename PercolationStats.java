import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by USUARIO on 21/02/2017.
 */
public class PercolationStats {

    private double[] openSitePercent;
    private double z = 1.96;
    private int n, trials, siteNumber;


    /**
     * perform trials independent experiments on an n-by-n grid
     *
     * @param n      to calculate grid size nxn
     * @param trials Number of times to run the experiment
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N and trials must be greater than 0");
        }
        this.n = n;
        this.siteNumber = n * n;
        this.trials = trials;
        openSitePercent = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row, col;
                do {
                    row = StdRandom.uniform(1, n + 1);
                    col = StdRandom.uniform(1, n + 1);
                } while (p.isOpen(row, col));

                p.open(row, col);
            }
            openSitePercent[i] = p.numberOfOpenSites() / ((double) siteNumber);
        }
    }

    /**
     * Test client
     *
     * @param args
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats pStats = new PercolationStats(n, trials);

        System.out.println(String.format("%-23s = %f", "mean", pStats.mean()));
        System.out.println(String.format("%-23s = %f", "stddev", pStats.stddev()));
        System.out.println(String.format("%-23s = [%f, %f]", "95% confidence interval",
                pStats.confidenceLo(), pStats.confidenceHi()));

    }

    /**
     * @return sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(this.openSitePercent);
    }

    /**
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        if (trials == 1) {
            return Double.NaN;
        }
        return StdStats.stddev(this.openSitePercent);
    }

    /**
     * @return low  endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - (z * (stddev() / Math.sqrt(n)));
    }

    /**
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + (z * (stddev() / Math.sqrt(n)));
    }
}
