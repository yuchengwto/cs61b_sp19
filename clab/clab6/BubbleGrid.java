import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Arrays;


public class BubbleGrid {
    private int[][] grid;

    /* Create new BubbleGrid with bubble/space locations specified by grid.
     * Grid is composed of only 1's and 0's, where 1's denote a bubble, and
     * 0's denote a space. */
    public BubbleGrid(int[][] grid) {
        this.grid = grid;
    }

    /* Returns an array whose i-th element is the number of bubbles that
     * fall after the i-th dart is thrown. Assume all elements of darts
     * are unique, valid locations in the grid. Must be non-destructive
     * and have no side-effects to grid. */
    public int[] popBubbles(int[][] darts) {

        int[] ret = new int[darts.length];
        int ptr = 0;
        for (int[] d: darts) {
            ret[ptr++] = grid[d[0]][d[1]];
        }

        int row = grid.length;
        int col = grid[0].length;
        WeightedQuickUnionUF wuf = new WeightedQuickUnionUF(row*col + 1);
        System.out.println(Arrays.deepToString(grid));
        for (int[] d: darts) {
            grid[d[0]][d[1]] = 0;
        }
        System.out.println(Arrays.deepToString(grid));
        System.out.println(wuf.count());
        build(wuf);
        System.out.println(wuf.count());
        int count = wuf.count();
        for (int i=darts.length-1; i>=0; i--) {
            int[] d = darts[i];
            if (ret[i] == 1) {
                System.out.println(Arrays.deepToString(grid));
                grid[d[0]][d[1]] = 1;
                System.out.println(Arrays.deepToString(grid));
                System.out.println(wuf.count());
                build(wuf);
                System.out.println(wuf.count());
                ret[i] = count - wuf.count() - 1;
                count = wuf.count();
            }
        }
        return ret;
    }

    private int transfer(int row, int col) {
        return row*grid[0].length + col;
    }

    private void build(WeightedQuickUnionUF wuf) {
        int row = grid.length;
        int col = grid[0].length;
        int top = row*col;

        for (int i=0; i!=row; i++) {
            for (int j=0; j!=col; j++) {
                if (grid[i][j] == 1) {
                    if (i == 0) {
                        wuf.union(top, transfer(i, j));
                    } else {
                        if (j == 0) {
                            if (wuf.connected(top, transfer(i-1, j))) {
                                wuf.union(top, transfer(i, j));
                            }
                        } else {
                            if (wuf.connected(top, transfer(i-1, j))
                            || wuf.connected(top, transfer(i, j-1))) {
                                wuf.union(top, transfer(i, j));
                            }
                        }
                    }
                }
            }
        }
    }

}
