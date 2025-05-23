import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96; // critical value of 1.96
    private int trials; // instance variable for # of trials
    private double[] tresults; // instance variable to store results in array


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        // throw exception if either or both n and t are <= 0
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("N and T must be greater than zero");
        tresults = new double[trials];
        this.trials = trials;
        // loops through trial amounts randomly choosing a site
        for (int i = 0; i < trials; i++) {
            Percolation trial = new Percolation(n);
            int tcount = 0;

            while (!trial.percolates()) { // repeats trials until system percolates
                int row = StdRandom.uniformInt(n);
                int col = StdRandom.uniformInt(n);
                if (!trial.isOpen(row, col)) {
                    trial.open(row, col);
                    tcount++;
                }
            }
            tresults[i] = (double) tcount / (n * n); // stores results of each trial

        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(tresults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(tresults);

    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(trials));

    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(trials));

    }

    // test client (see below)
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trial = Integer.parseInt(args[1]);

        Stopwatch stopwatch = new Stopwatch();
        PercolationStats stats = new PercolationStats(n, trial);
        double time = stopwatch.elapsedTime();

        System.out.printf("mean()            = %.6f\n", stats.mean());
        System.out.printf("stddev()          = %.6f\n", stats.stddev());
        System.out.printf("confidenceLow()   = %.6f\n", stats.confidenceLow());
        System.out.printf("confidenceHigh()  = %.6f\n", stats.confidenceHigh());
        System.out.printf("elapsed time()    = %.3f\n", time);


    }

}
