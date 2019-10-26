package hw2;
import edu.princeton.cs.introcs.StdOut;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
import edu.princeton.cs.introcs.Stopwatch;


public class PercolationStats {
    private double mu;
    private double sigma;
    private double confidenceLow;
    private double confidenceHigh;
    private double[] threshold;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N<=0 || T<=0) {
            throw new IllegalArgumentException();
        }
        threshold = new double[T];
        for (int i=0; i!=T; i++) {
            Percolation ipf = pf.make(N);
            int irandom;
            int row;
            int col;
            while (!ipf.percolates()){
                irandom = StdRandom.uniform(N*N);
                row = irandom / N;
                col = irandom % N;
                ipf.open(row, col);
            }
            threshold[i] = (1.0*ipf.numberOfOpenSites()) / (N*N);
        }
        mu = mean();
        sigma = stddev();
        confidenceLow = confidenceLow();
        confidenceHigh = confidenceHigh();
        System.out.println("mu: "+mu);
        System.out.println("sigma: "+sigma);
        System.out.println("confidenceLow: "+confidenceLow);
        System.out.println("confidenceHigh: "+confidenceHigh);
    }

    public double mean() {
        return StdStats.mean(threshold);
    }

    public double stddev() {
        return StdStats.stddev(threshold);
    }

    public double confidenceLow() {
        return mu - (1.96*sigma)/Math.sqrt(threshold.length);
    }

    public double confidenceHigh() {
        return mu + (1.96*sigma)/Math.sqrt(threshold.length);
    }

    public static void main(String[] args) {
        int N = 100;
        int T = 2000;
        Stopwatch sw = new Stopwatch();
        PercolationFactory pf = new PercolationFactory();
        PercolationStats ps = new PercolationStats(N, T, pf);
        StdOut.printf("N_%d, T_%d, cost time: %.2f", N, T, sw.elapsedTime());
        // (N^2*T)
    }
}
