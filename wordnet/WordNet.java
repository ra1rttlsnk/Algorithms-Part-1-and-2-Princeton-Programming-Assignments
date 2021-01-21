/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;

public class WordNet {
    private final RedBlackBST<String, ArrayList<Integer>> nounTree;
    private final ArrayList<String> nouns;
    private final SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        In synsetInput = new In(synsets);
        In hypernymInput = new In(hypernyms);
        nounTree = new RedBlackBST<>();
        nouns = new ArrayList<>();

        int id = -1;
        while (!synsetInput.isEmpty()) {
            String[] str = synsetInput.readLine().split(",");

            id = Integer.parseInt(str[0]);
            nouns.add(str[1]);
            String[] s = str[1].split(" ");

            for (int k = 0; k < s.length; k++) {
                if (nounTree.contains(s[k])) {
                    nounTree.get(s[k]).add(id);
                }
                else {
                    ArrayList<Integer> b = new ArrayList<Integer>();
                    b.add(id);
                    nounTree.put(s[k], b);
                }
            }

        }

        Digraph digraph = new Digraph(id + 1);

        while (!hypernymInput.isEmpty()) {
            String[] strings = hypernymInput.readLine().split(",");

            for (int i = 1; i < strings.length; i++) {
                digraph.addEdge(Integer.parseInt(strings[0]), Integer.parseInt(strings[i]));
            }
        }

        Topological topological = new Topological(digraph);
        if (!topological.hasOrder()) throw new IllegalArgumentException();

        // Checking for multiple roots
        int verts = digraph.V();
        int roots = 0;
        for (int i = 0; i < verts; i++) {
            if (digraph.indegree(i) != 0 && digraph.outdegree(i) == 0) roots++;
            if (roots > 1) throw new IllegalArgumentException();
        }

        sap = new SAP(digraph);
    }

    public Iterable<String> nouns() {
        return nounTree.keys();
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Iterable<Integer> nounIdsA = getId(nounA);
        Iterable<Integer> nounIdsB = getId(nounB);

        return sap.length(nounIdsA, nounIdsB);
    }


    private Iterable<Integer> getId(String noun) {
        return nounTree.get(noun);
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Iterable<Integer> nounIdsA = getId(nounA);
        Iterable<Integer> nounIdsB = getId(nounB);
        int ancestor = sap.ancestor(nounIdsA, nounIdsB);
        if (ancestor == -1) return null;
        return nouns.get(ancestor);
    }

    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        if (nounTree.contains(word))
            return true;
        return false;
    }

    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        String[] st = { "goat", "human" };
        StdOut.print(wn.sap(st[0], st[1]) + "\n");
    }
}
