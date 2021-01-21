/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;

    public static void encode() {
        char[] r = new char[R];
        // encoding process
        for (int i = 0; i < R; i++) {
            r[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(8);
            for (int i = 0; i < R; i++) {
                if (c == r[i]) {
                    // write bit
                    BinaryStdOut.write(i, 8);
                    // move to front
                    System.arraycopy(r, 0, r, 1, i);
                    r[0] = c;
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        char[] r = new char[R];
        for (int i = 0; i < R; i++) {
            r[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(8);
            char c = r[index];
            BinaryStdOut.write(c, 8);
            System.arraycopy(r, 0, r, 1, index);
            r[0] = c;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        if (args[0].equals("+")) {
            decode();
        }
    }
}
