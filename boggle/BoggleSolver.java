/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {
    private final TST<Integer> dictionaryTST;
    private BoggleBoard boggleBoard;
    private SET<String> words;

    public BoggleSolver(String[] dictionary) {
        dictionaryTST = new TST<>();

        // creation of the TST using the dictionary words
        for (int i = 0; i < dictionary.length; i++) {
            dictionaryTST.put(dictionary[i], i);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        boggleBoard = board;
        words = new SET<>();
        String letter;
        //
        boolean[][] marked = new boolean[board.rows()][board.cols()];
        for (int i = 0; i < boggleBoard.rows(); i++) {
            for (int j = 0; j < boggleBoard.cols(); j++) {
                marked[i][j] = true;

                letter = "";
                letter += boggleBoard.getLetter(i, j);
                if (letter.equals("Q"))
                    letter += "U";

                search(i, j, letter, marked);
                marked[i][j] = false;
            }
        }
        return words;
    }

    // this will be our recursive search method
    private void search(int r, int c, String prefix, boolean[][] marked) {
        // String p = new String(prefix);
        if (r - 1 >= 0 && !marked[r - 1][c]) take(r - 1, c, prefix, marked);
        if (r - 1 >= 0 && c + 1 < boggleBoard.cols() && !marked[r - 1][c + 1])
            take(r - 1, c + 1, prefix, marked);
        if (r - 1 >= 0 && c - 1 >= 0 && !marked[r - 1][c - 1])
            take(r - 1, c - 1, prefix, marked);
        if (c - 1 >= 0 && !marked[r][c - 1]) take(r, c - 1, prefix, marked);
        if (c + 1 < boggleBoard.cols() && !marked[r][c + 1]) take(r, c + 1, prefix, marked);
        if (r + 1 < boggleBoard.rows() && !marked[r + 1][c]) take(r + 1, c, prefix, marked);
        if (r + 1 < boggleBoard.rows() && c - 1 >= 0 && !marked[r + 1][c - 1])
            take(r + 1, c - 1, prefix, marked);
        if (r + 1 < boggleBoard.rows() && c + 1 < boggleBoard.cols() && !marked[r + 1][c + 1])
            take(r + 1, c + 1, prefix, marked);
    }

    private void take(int r, int c, String prefix, boolean[][] marked) {
        String letter = "";
        Queue<String> reachableKeys;
        letter += boggleBoard.getLetter(r, c);

        // Considering the special case of Q --> QU
        if (letter.equals("Q"))
            letter += "U";

        String newPrefix = prefix + letter;
        marked[r][c] = true;
        reachableKeys = (Queue<String>) dictionaryTST
                .keysWithPrefix(newPrefix);
        if (!reachableKeys.isEmpty()) {
            String firstFound = reachableKeys.dequeue();
            if (firstFound.equals(newPrefix) && newPrefix.length() >= 3)
                words.add(firstFound);

            search(r, c, newPrefix, marked);
        }
        marked[r][c] = false;
    }

    /*
        private int binarySearchString(String[] strings, String toBeCompared) {
            int low = 0;
            int high = strings.length - 1;
            int mid = (low + high) / 2;
            int comparison = strings[mid].compareTo(toBeCompared);
            if (comparison != 0 && low == high) return -1;
            if (comparison == 0) return mid;
            else if (comparison > 0) return binarySearchString(strings, toBeCompared, low, mid - 1);
            else return binarySearchString(strings, toBeCompared, mid + 1, high);
        }

        private int binarySearchString(String[] strings, String toBeCompared, int low, int high) {
            int mid = (low + high) >>> 1;
            int comparison = strings[mid].compareTo(toBeCompared);
            if (comparison != 0 && low == high) return -1;
            if (comparison == 0) return mid;
            else if (comparison > 0) return binarySearchString(strings, toBeCompared, low, mid - 1);
            else return binarySearchString(strings, toBeCompared, mid + 1, high);
        }
    */
    public int scoreOf(String word) {
        int length = word.length();
        if (dictionaryTST.contains(word)) {
            if (length == 3 || length == 4) return 1;
            else if (length == 5) return 2;
            else if (length == 6) return 3;
            else if (length == 7) return 5;
            else if (length >= 8) return 11;
        }
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        BoggleSolver bs = new BoggleSolver(in.readAllStrings());
        StdOut.print(bs.scoreOf("AMAZED") + "\n");
        for (String s : bs.getAllValidWords(new BoggleBoard(args[1])))
            StdOut.print(s + "\n");

    }
}
