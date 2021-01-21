/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    // private final int[] index;
    private final int n;
    private final String s;
    private final CircularSuffix[] circularSuffixes;

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final int first;

        public CircularSuffix(int first) {
            this.first = first;
        }

        public int compareTo(CircularSuffix that) {
            char thisChar;
            char thatChar;
            for (int i = 0; i < n; i++) {
                thisChar = s.charAt((this.first + i) % n);
                thatChar = s.charAt((that.getFirst() + i) % n);
                if (thisChar < thatChar) return -1;
                else if (thisChar > thatChar) return 1;
            }
            return 0;
        }

        public int getFirst() {
            return first;
        }
    }

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        n = s.length();
        this.s = s;
        circularSuffixes = new CircularSuffix[n];

        // index = new int[n];
        for (int i = 0; i < n; i++) {
            circularSuffixes[i] = new CircularSuffix(i);
        }
        Arrays.sort(circularSuffixes);

       /* for (int i = 0; i < n; i++) {
            index[i] = circularSuffixes[i].getFirst();
        } */


        /* String[] csa = new String[n];
        char[] input = s.toCharArray();
        char[] circularSuffix = new char[n];
        HashMap<String, Integer> sth = new HashMap<>();
        for (int i = 0; i < n; i++) {

            System.arraycopy(input, i, circularSuffix, 0, n - i);
            System.arraycopy(input, 0, circularSuffix, n - i, i);
            csa[i] = new String(circularSuffix);
            sth.put(csa[i], i);
        }

        // Now we have CSA. It's time to sort this matrix and keep track
        // of where each of the strings end up in the sorted CSA.
        // I think a HashMap might do the trick!
        Quick3string.sort(csa);
        for (int i = 0; i < n; i++) {
            index[i] = sth.get(csa[i]);
        } */

    }

    public int length() {
        return n;
    }

    public int index(int i) {
        if (i < 0 || i > n - 1) throw new IllegalArgumentException();
        // return index[i];
        return circularSuffixes[i].getFirst();
    }


    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray c = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            StdOut.print(c.index(i) + "\n");
        }
    }
}
