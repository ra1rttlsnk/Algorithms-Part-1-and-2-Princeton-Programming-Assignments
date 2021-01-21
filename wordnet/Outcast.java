/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns) {
        int outcastId = -1;
        int maxDistance = 0;
        for (int i = 0; i < nouns.length; i++) {
            int totalDistance = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (j == i) continue;
                totalDistance += wordNet.distance(nouns[i], nouns[j]);
            }
            if (totalDistance > maxDistance) {
                maxDistance = totalDistance;
                outcastId = i;
            }
        }

        return nouns[outcastId];
    }

    public static void main(String[] args) {
        WordNet w = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(w);
        In in = new In(args[0]);
        String out = outcast.outcast(in.readAllStrings());
        StdOut.print(out);
    }
}
