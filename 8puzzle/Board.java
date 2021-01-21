/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;

public class Board {
    private final int dim;
    private final int[][] gameBoard;

    public Board(int[][] tiles) {
        dim = tiles.length;
        gameBoard = new int[dim][dim];

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                gameBoard[i][j] = tiles[i][j];
            }
        }

    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s.append(String.format("%2d ", gameBoard[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public int dimension() {
        return dim;
    }

    public int hamming() {
        int ham = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i * dim + j + 1 == dim * dim) break;
                if (i * dim + j + 1 != gameBoard[i][j]) {
                    ham += 1;
                }
            }
        }
        return ham;
    }

    public int manhattan() {
        int man = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (gameBoard[i][j] != 0)
                    if (gameBoard[i][j] != i * dim + j + 1)
                        man += Math.abs((i - (gameBoard[i][j] - 1) / dim)) +
                                Math.abs(j - (gameBoard[i][j] - 1) % dim);
            }
        }
        return man;
    }

    public boolean isGoal() {
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++) {
                if (i * dim + j + 1 == dim * dim) {
                    return gameBoard[i][j] == 0;
                }
                if (gameBoard[i][j] != i * dim + j + 1)
                    return false;

            }
        return true;
    }

    public boolean equals(Object y) {
        if (this == y)
            return true;
        if (y == null)
            return false;
        if (this.getClass() != y.getClass())
            return false;
        Board a = (Board) y;
        if (dim != a.dimension())
            return false;
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++) {
                if (gameBoard[i][j] != a.gameBoard[i][j])
                    return false;
            }
        return true;

    }

    private Queue<Board> getNeighbours() {
        Queue<Board> n = new Queue<>();
        int a = -1;
        int b = -1;
        int swap;
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++) {
                if (gameBoard[i][j] == 0) {
                    a = i;
                    b = j;
                    break;
                }
            }

        int[][] proxy = new int[dim][dim];

        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++) {
                proxy[i][j] = gameBoard[i][j];
            }

        if (a > 0) {
            swap = proxy[a - 1][b];
            proxy[a - 1][b] = proxy[a][b];
            proxy[a][b] = swap;
            n.enqueue(new Board(proxy));
            // proxy[a][b] = proxy[a - 1][b];
            // proxy[a - 1][b] = swap;
            proxy = new int[dim][dim];
            for (int i = 0; i < dim; i++)
                for (int j = 0; j < dim; j++) {
                    proxy[i][j] = gameBoard[i][j];
                }
        }

        if (a < dim - 1) {
            swap = proxy[a + 1][b];
            proxy[a + 1][b] = proxy[a][b];
            proxy[a][b] = swap;
            n.enqueue(new Board(proxy));
            // proxy[a][b] = proxy[a + 1][b];
            // proxy[a + 1][b] = swap;
            proxy = new int[dim][dim];
            for (int i = 0; i < dim; i++)
                for (int j = 0; j < dim; j++) {
                    proxy[i][j] = gameBoard[i][j];
                }
        }
        if (b > 0) {
            swap = proxy[a][b - 1];
            proxy[a][b - 1] = proxy[a][b];
            proxy[a][b] = swap;
            n.enqueue(new Board(proxy));
            // proxy[a][b] = proxy[a][b - 1];
            // proxy[a][b - 1] = swap;
            proxy = new int[dim][dim];
            for (int i = 0; i < dim; i++)
                for (int j = 0; j < dim; j++) {
                    proxy[i][j] = gameBoard[i][j];
                }
        }
        if (b < dim - 1) {
            swap = proxy[a][b + 1];
            proxy[a][b + 1] = proxy[a][b];
            proxy[a][b] = swap;
            n.enqueue(new Board(proxy));
            // proxy[a][b] = proxy[a][b + 1];
            // proxy[a][b + 1] = swap;

        }
        return n;
    }


    public Iterable<Board> neighbors() {
        return getNeighbours();
    }

    public Board twin() {
        int[][] twinBoard = new int[dim][dim];
        int swap = -1;
        boolean switch1 = true;
        boolean switch2 = false;
        int a = 0;
        int b = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                twinBoard[i][j] = gameBoard[i][j];
                if (twinBoard[i][j] != 0 && switch1) {
                    swap = twinBoard[i][j];
                    switch1 = false;
                    switch2 = true;
                    a = i;
                    b = j;
                }

                if (twinBoard[i][j] != 0 && twinBoard[i][j] != swap && switch2) {
                    twinBoard[a][b] = twinBoard[i][j];
                    twinBoard[i][j] = swap;
                    switch2 = false;
                }
            }
        }

        return new Board(twinBoard);
    }

    public static void main(String[] args) {

    }

}
