import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {


    private final WeightedQuickUnionUF wuf;
    private final WeightedQuickUnionUF wufFill;
    private boolean[][] board;
    private final int dim;
    private int nOpen;


    public Percolation(int n) {

        if (n <= 0)
            throw new IllegalArgumentException();
        dim = n;
        wuf = new WeightedQuickUnionUF(n * n + 2);
        wufFill = new WeightedQuickUnionUF(n * n + 1);
        nOpen = 0;

        board = new boolean[n][n];

        // Let's make each element to be blocked
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = false;
            }
        }

    }


    private int serial(int row, int col) {
        return (row - 1) * dim + (col - 1);
    }

    public void open(int row, int col) {

        if ((row < 1 || col < 1) || (row > dim || col > dim))
            throw new IllegalArgumentException();

        if (!isOpen(row, col)) {
            board[row - 1][col - 1] = true;
            nOpen += 1;

            if ((col - 1) >= 1)
                if (isOpen(row, col - 1)) {
                    wuf.union(serial(row, col - 1) + 1, serial(row, col) + 1);
                    wufFill.union(serial(row, col - 1) + 1, serial(row, col) + 1);
                }
            if ((row - 1) >= 1)
                if (isOpen(row - 1, col)) {
                    wuf.union(serial(row - 1, col) + 1, serial(row, col) + 1);
                    wufFill.union(serial(row - 1, col) + 1, serial(row, col) + 1);
                }
            if ((col + 1) <= dim)
                if (isOpen(row, col + 1)) {
                    wuf.union(serial(row, col + 1) + 1, serial(row, col) + 1);
                    wufFill.union(serial(row, col + 1) + 1, serial(row, col) + 1);
                }
            if ((row + 1) <= dim)
                if (isOpen(row + 1, col)) {
                    wuf.union(serial(row + 1, col) + 1, serial(row, col) + 1);
                    wufFill.union(serial(row + 1, col) + 1, serial(row, col) + 1);
                }

            if (row == 1) {
                wuf.union(0, serial(row, col) + 1);
                wufFill.union(0, serial(row, col) + 1);
            }
            if (row == dim) {
                wuf.union(dim * dim + 1, serial(row, col) + 1);
            }
        }
    }

    public boolean isOpen(int row, int col) {

        if ((row < 1 || col < 1) || (row > dim || col > dim))
            throw new IllegalArgumentException();

        if (board[row - 1][col - 1])
            return true;
        return false;

    }

    public boolean isFull(int row, int col) {

        if ((row < 1 || col < 1) || (row > dim || col > dim))
            throw new IllegalArgumentException();

        if (isOpen(row, col) && wufFill.find(serial(row, col) + 1) == wufFill.find(0))
            return true;
        return false;


    }

    public int numberOfOpenSites() {
        return nOpen;
    }

    public boolean percolates() {
        if (wuf.find(0) == wuf.find(dim * dim + 1))
            return true;
        return false;
    }

    public static void main(String[] args) {
       /* int n = 10;
        Percolation p = new Percolation(n);
        StdOut.println(p.chooser.size());
        StdOut.println(p.numberOfOpenSites());
        StdOut.println(p.numberOfOpenSites() / (double) (n * n));
        StdOut.println(p.wuf.count());
        StdOut.println(p.chooser); */
    }

}
