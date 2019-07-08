/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int numOpenSites;
    private WeightedQuickUnionUF quf;
    private int[] openSqs;
    private int n;


    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("Grid must be at least 1 square");
        }

        this.n = n;
        // all initialized to 0 -> closed; index 0 will always be 0
        openSqs = new int[n * n + 1];

        quf = new WeightedQuickUnionUF(n * n + 2);

        for (int i = 1; i <= n; i++) {
            quf.union(0, i);
        }

        for (int i = n * n - n + 1; i < n * n + 1; i++) {
            quf.union(n * n + 1, i);
        }
    }

    public void open(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new java.lang.IllegalArgumentException("Outside of range");
        }

        if (!isOpen(row, col)) {
            int siteToOpen = (row - 1) * this.n + col;
            this.openSqs[siteToOpen] = 1;
            numOpenSites++;

            // merge with surrounding squares, if present and open
            if (row != 1 && isOpen(row - 1, col)) {
                quf.union(siteToOpen, siteToOpen - this.n);
            }
            if (row != this.n && isOpen(row + 1, col)) {
                quf.union(siteToOpen, siteToOpen + this.n);
            }
            if (col != 1 && isOpen(row, col - 1)) {
                quf.union(siteToOpen, siteToOpen - 1);
            }
            if (col != this.n && isOpen(row, col + 1)) {
                quf.union(siteToOpen, siteToOpen + 1);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new java.lang.IllegalArgumentException("Outside of range");
        }

        if (this.openSqs[(row - 1) * this.n + col] == 1) {
            return true;
        }

        return false;
    }

    public boolean isFull(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new java.lang.IllegalArgumentException("Outside of range");
        }

        return (isOpen(row, col) && quf.connected(0, (row - 1) * this.n + col));
    }

    public int numberOfOpenSites() {

        return this.numOpenSites;
    }

    public boolean percolates() {
        if (this.n == 1) {
            if (isOpen(1, 1)) return true;
            return false;
        }
        return quf.connected(0, this.n * this.n + 1);
    }

    public static void main(String[] args) {

    }
}
