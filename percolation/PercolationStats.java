/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {


    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException("Arguments must "
                                                                 + "be greater than 0;");
        }


        double mean = 0;
        double stddev = 0;
        double confidenceLo = 0;
        double confidenceHi = 0;
    }

    public double mean() {

        return this.mean;
    }

    public double stddev() {

        return this.stddev;
    }

    public double confidenceLo() {

        return this.confidenceLo;
    }

    public double confidenceHi() {

        return this.confidenceHi;
    }

    public static void main(String[] args) {

        int size = Integer.parseInt(args[0]);
        int numTrials = Integer.parseInt(args[1]);
        PercolationStats pstat = new PercolationStats(size, numTrials);

        double[] data = new double[numTrials];

        for (int i = 0; i < numTrials; i++) {
            Percolation board = new Percolation(size);
            while (!board.percolates()) {
                board.open(StdRandom.uniform(1, size), StdRandom.uniform(1, size));
            }
            data[i] = board.numberOfOpenSites();
        }

        pstat.mean = StdStats.mean(data);
        pstat.stddev = StdStats.stddev(data);
        pstat.confidenceLo = pstat.mean - (1.96 * pstat.stddev / java.lang.Math.sqrt(numTrials));
        pstat.confidenceHi = pstat.mean + (1.96 * pstat.stddev / java.lang.Math.sqrt(numTrials));
    }
}
