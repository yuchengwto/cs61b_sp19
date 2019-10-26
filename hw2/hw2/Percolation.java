package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int length;
    private int numberOfOpen;
    private WeightedQuickUnionUF wuf;
    private WeightedQuickUnionUF wuf_top;
    private int virtual_top;
    private int virtual_bottom;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be positive.");
        }
        numberOfOpen = 0;
        length = N;
        grid = new boolean[N][N];
        virtual_top = N*N;
        virtual_bottom = N*N+1;
        wuf = new WeightedQuickUnionUF(virtual_bottom+1);
        wuf_top = new WeightedQuickUnionUF(virtual_top+1);
        for (int i = 0; i!=N; i++) {
            wuf.union(virtual_top, transition(0, i));
            wuf_top.union(virtual_top, transition(0, i));
            wuf.union(virtual_bottom, transition(N-1, i));
        }
    }

    private int transition(int row, int col) {
        return row* this.length + col;
    }

    public void open(int row, int col) {
        if ( row >= length || row < 0 || col >= length || col <0 ) {
            throw new IndexOutOfBoundsException("row or col must be smaller than length in open.");
        }
        if (isOpen(row, col)) {
            return;
        }
        grid[row][col] = true;
        numberOfOpen++;
        if (row+1<length && isOpen(row+1, col)) {
            wuf.union(transition(row, col), transition(row+1, col));
            wuf_top.union(transition(row, col), transition(row+1, col));
        }
        if (row-1>=0 && isOpen(row-1, col)) {
            wuf.union(transition(row, col), transition(row-1, col));
            wuf_top.union(transition(row, col), transition(row-1, col));
        }
        if (col+1<length && isOpen(row, col+1)) {
            wuf.union(transition(row, col), transition(row, col+1));
            wuf_top.union(transition(row, col), transition(row, col+1));
        }
        if (col-1>=0 && isOpen(row, col-1)) {
            wuf.union(transition(row, col), transition(row, col-1));
            wuf_top.union(transition(row, col), transition(row, col-1));
        }
    }

    public boolean isOpen(int row, int col) {
        if (row >= length || row < 0 || col >= length || col <0) {
            throw new IndexOutOfBoundsException("row or col must be smaller than length in isOpen.");
        }
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        if (row >= length || row < 0 || col >= length || col <0) {
            throw new IndexOutOfBoundsException("row or col must be smaller than length in isFull.");
        }
        int site = transition(row, col);
        return wuf_top.connected(virtual_top, site);
    }

    public int numberOfOpenSites() {
        return numberOfOpen;
    }

    public boolean percolates() {
        return wuf.connected(virtual_top, virtual_bottom);
    }

    public static void main(String[] args) {}
}
