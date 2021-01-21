/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BaseballElimination {
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] gamesAgainst;
    private final ArrayList<String> teams;
    private final int numberOfTeams;
    private FordFulkerson fordFulkerson;
    private int gameVerts;
    private ArrayList<String> cOfe;
    private boolean isCalled;

    public BaseballElimination(String filename) {
        // firstly, ensure proper reading of the table
        In in = new In(filename);

        // construction of the arrays
        numberOfTeams = Integer.parseInt(in.readString());
        wins = new int[numberOfTeams];
        losses = new int[numberOfTeams];
        remaining = new int[numberOfTeams];
        gamesAgainst = new int[numberOfTeams][numberOfTeams];
        teams = new ArrayList<>();
        isCalled = false;
        // as the file is of repetitive nature, use that fact
        for (int i = 0; i < numberOfTeams; i++) {
            String s = in.readString();
            teams.add(s);
            int p = Integer.parseInt(in.readString());
            wins[i] = p;
            p = Integer.parseInt(in.readString());
            losses[i] = p;
            p = Integer.parseInt(in.readString());
            remaining[i] = p;

            for (int j = 0; j < numberOfTeams; j++) {
                gamesAgainst[i][j] = Integer.parseInt(in.readString());
            }
        }
    }

    public Iterable<String> teams() {
        return teams;
    }

    public int wins(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        int index = teams.indexOf(team);
        return wins[index];
    }

    public int losses(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        int index = teams.indexOf(team);
        return losses[index];
    }

    public int remaining(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        int index = teams.indexOf(team);
        return remaining[index];
    }

    public int numberOfTeams() {
        return numberOfTeams;
    }

    public int against(String team1, String team2) {
        if (!teams.contains(team1)) throw new IllegalArgumentException();
        if (!teams.contains(team2)) throw new IllegalArgumentException();
        int index1 = teams.indexOf(team1);
        int index2 = teams.indexOf(team2);
        return gamesAgainst[index1][index2];
    }

    private int choose(int n) {
        return n * (n - 1) / 2;
    }

    private int[] getCoordinate(int serial, int k) {
        int c = 0;
        int[] coordinate = new int[2];
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == k) continue;
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (j == k) continue;
                c++;
                if (c == serial) {
                    coordinate[0] = i;
                    coordinate[1] = j;
                }
            }
        }
        return coordinate;
    }

    public boolean isEliminated(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        isCalled = true;
        int teamIndex = teams.indexOf(team);
        // gameVerts = factorial(numberOfTeams - 1) / (factorial(numberOfTeams - 3) * 2);
        // FlowNetwork flowNetwork = new FlowNetwork(1 + gameVerts + numberOfTeams -1  + 1);
        gameVerts = choose(numberOfTeams - 1);
        FlowNetwork flowNetwork = new FlowNetwork(1 + gameVerts + numberOfTeams + 1);
        // Now to add the flowEdges
        // firstly, the game edges
        for (int i = 1; i < gameVerts + 1; i++) {
            int[] coordinate = getCoordinate(i, teamIndex);
            flowNetwork.addEdge(new FlowEdge(0, i, gamesAgainst[coordinate[0]][coordinate[1]]));
            flowNetwork.addEdge(new FlowEdge(i, gameVerts + 1 + coordinate[0], Integer.MAX_VALUE));
            flowNetwork.addEdge(new FlowEdge(i, gameVerts + 1 + coordinate[1], Integer.MAX_VALUE));
        }

        boolean trivialElimination = false;
        cOfe = new ArrayList<>();
        for (int i = gameVerts + 1; i < gameVerts + numberOfTeams + 1; i++) {
            if (i - gameVerts - 1 == teamIndex) continue;
            int capacity = wins[teamIndex] + remaining[teamIndex] - wins[i - gameVerts - 1];
            if (capacity < 0) {
                cOfe.add(teams.get(i - gameVerts - 1));
                trivialElimination = true;
                continue;
            }
            flowNetwork.addEdge(new FlowEdge(i, gameVerts + numberOfTeams + 1,
                                             capacity));
        }

        fordFulkerson = new FordFulkerson(flowNetwork, 0, gameVerts + numberOfTeams + 1);
        if (trivialElimination) return true;
        for (FlowEdge e : flowNetwork.adj(0)) {
            if (e.flow() != e.capacity())
                return true;
        }
        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException();
        if (!isCalled) isEliminated(team);
        int k = teams.indexOf(team);
        // if (!cOfe.isEmpty()) return cOfe;
        // Queue<String> cOfe = new Queue<String>();
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == k) continue;
            if (fordFulkerson.inCut(i + gameVerts + 1)) {
                if (!cOfe.contains(teams.get(i)))
                    cOfe.add(teams.get(i));
            }
        }
        // toggle the switch
        isCalled = false;
        if (cOfe.isEmpty()) return null;
        return cOfe;
    }

    public static void main(String[] args) {

        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}


