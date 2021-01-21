/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Stack<Board> solutionStack;
    private final boolean solvable;
    private final SearchNode currNode;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        boolean s = true;
        Board prev = null;
        Board twinPrev = null;
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twinpq = new MinPQ<>();

        SearchNode currentNode = new SearchNode(initial, null);
        SearchNode twinCurrentNode = new SearchNode(initial.twin(), null);
        solutionStack = new Stack<>();

        while (!currentNode.getBoard().isGoal()) {
            if (twinCurrentNode.getBoard().isGoal()) {
                s = false;
                break;
            }

            for (Board b : currentNode.getBoard().neighbors()) {
                if (!b.equals(prev))
                    pq.insert(new SearchNode(b, currentNode));
            }

            for (Board q : twinCurrentNode.getBoard().neighbors()) {
                if (!q.equals(twinPrev))
                    twinpq.insert(new SearchNode(q, twinCurrentNode));
            }

            currentNode = pq.delMin();
            prev = currentNode.getPreviousNode().getBoard();

            twinCurrentNode = twinpq.delMin();
            twinPrev = twinCurrentNode.getPreviousNode().getBoard();

        }
        solvable = s;
        currNode = currentNode;
    }

    public int moves() {
        if (!isSolvable()) return -1;
        return currNode.getMoves();
    }

    public boolean isSolvable() {
        return solvable;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        solutionStack.push(currNode.getBoard());
        SearchNode p = currNode.getPreviousNode();
        while (p != null) {
            solutionStack.push(p.getBoard());
            p = p.getPreviousNode();
        }

        return solutionStack;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final int priority;
        private final Board board;
        private final int manhattan;
        private int moves;
        private final SearchNode previousNode;
        // private final int hamming;

        public SearchNode(Board b, SearchNode p) {
            previousNode = p;
            moves = 0;
            if (p != null)
                moves = p.getMoves() + 1;

            board = b;
            manhattan = board.manhattan();
            priority = moves + manhattan;
            // hamming = board.hamming();
        }

        public int compareTo(SearchNode s) {
            int a = Integer.compare(priority, s.getPriority());
            if (a == 0) {
                return Integer.compare(manhattan, s.getManhattan());
            }
            else return a;
        }

        public Board getBoard() {
            return board;
        }

        public int getManhattan() {
            return manhattan;
        }


        public int getPriority() {
            return priority;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getPreviousNode() {
            return previousNode;
        }

        /* public int getHamming() {
            return hamming;
        } */
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
