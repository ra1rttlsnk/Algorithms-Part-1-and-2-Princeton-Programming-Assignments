/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.LSD;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;


public class BurrowsWheeler {

    public static void transform() {
        if (!BinaryStdIn.isEmpty()) {
            // read in the original message
            String inputString = BinaryStdIn.readString();
            CircularSuffixArray csa = new CircularSuffixArray(inputString);
            char[] c = new char[inputString.length()];
            for (int i = 0; i < inputString.length(); i++) {
                if (csa.index(i) == 0) {
                    BinaryStdOut.write(i);
                    c[i] = inputString.charAt(inputString.length() - 1);
                }
                else c[i] = inputString.charAt(csa.index(i) - 1);
            }
            for (int i = 0; i < inputString.length(); i++) {
                BinaryStdOut.write(c[i]);
            }
        }
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        String burrowsWheelerTransformString;
        int first;
        // final int R = 256;
        if (!BinaryStdIn.isEmpty()) {
            first = BinaryStdIn.readInt();
            burrowsWheelerTransformString = BinaryStdIn.readString();

            // getting the first column
            char[] fc = burrowsWheelerTransformString.toCharArray();
            int[] fcint = new int[fc.length];
            for (int i = 0; i < fc.length; i++) {
                fcint[i] = fc[i];
            }
            LSD.sort(fcint);
            for (int i = 0; i < fc.length; i++) {
                fc[i] = (char) fcint[i];
            }

            String firstColumn = new String(fc);

            // now to construct the next array
            // i'll keep a marked array to keep track of the ones found

            // need to change it as it takes quadratic time
            int n = burrowsWheelerTransformString.length();
            int[] next = new int[n];
            HashMap<Integer, Queue<Integer>> indexQueue = new HashMap<>();
            for (int i = 0; i < n; i++) {
                char c = burrowsWheelerTransformString.charAt(i);
                if (indexQueue.containsKey((int) c)) {
                    indexQueue.get((int) c).enqueue(i);
                }
                else {
                    indexQueue.put((int) c, new Queue<Integer>());
                    indexQueue.get((int) c).enqueue(i);
                }
            }

            for (int i = 0; i < n; i++) {
                char c = firstColumn.charAt(i);
                next[i] = indexQueue.get((int) c).dequeue();
            }

            int c = 0;
            for (int i = first; c < n; i = next[i]) {
                BinaryStdOut.write(burrowsWheelerTransformString.charAt(next[i]));
                c++;
            }

        }
        BinaryStdOut.close();

    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
