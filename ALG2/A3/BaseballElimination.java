import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import java.util.Arrays;

/**
 *
 * @author Ghaith
 */
public class BaseballElimination {

    private final int[] w, l, r;                                //w[i] wins, l[i] losses, r[i] remaining games,ie[i] is elimenated. 
    private final int[][] g;                                  //g[i][j] games left to play against team j.
    private final int n;
    private final String teams[];
    private final boolean[] ie;
    private SET<Integer>[] ce;
    private FlowNetwork net;

    public BaseballElimination(String filename) {                   // create a baseball division from given filename in format specified below
        In input = new In(filename);
        n = input.readInt();
        w = new int[n];
        l = new int[n];
        r = new int[n];
        ie = new boolean[n];
        g = new int[n][n];
        teams = new String[n];
        for (int i = 0; i < n; i++) {
            teams[i] = input.readString();
            w[i] = input.readInt();
            l[i] = input.readInt();
            r[i] = input.readInt();
            for (int j = 0; j < n; j++) {
                g[i][j] = input.readInt();
            }

        }
        ce = (SET<Integer>[]) new SET[n];
        for (int i = 0; i < n; i++) {
            ce[i] = new SET<>();
            int t = 1 + (n - 1) + (n - 1) * (n - 1) / 2;
            net = new FlowNetwork(t + 1);
            for (int ig = 0; ig < n; ig++) {
                if (ig == i) {
                    continue;
                }
                for (int jg = 0; jg < ig; jg++) {
                    if (jg == i) {
                        continue;
                    }
                    //System.out.println(ig + ":" + jg + "," + getMatchVertex(ig, jg, i) + "," + i);
                    FlowEdge fe = new FlowEdge(0, getMatchVertex(ig, jg, i), g[ig][jg]);
                    net.addEdge(fe);
                    FlowEdge fe1 = new FlowEdge(getMatchVertex(ig, jg, i), getTeamVertex(ig, i), Double.POSITIVE_INFINITY);
                    FlowEdge fe2 = new FlowEdge(getMatchVertex(ig, jg, i), getTeamVertex(jg, i), Double.POSITIVE_INFINITY);
                    net.addEdge(fe1);
                    net.addEdge(fe2);
                }
                FlowEdge fe;
                if (w[i] + r[i] >= w[ig]) {
                    fe = new FlowEdge(getTeamVertex(ig, i), t, w[i] + r[i] - w[ig]);
                } else {
                    fe = new FlowEdge(getTeamVertex(ig, i), t, 0);
                    ie[i] = true;
                    ce[i].add(getTeamIndex(fe.other(t), i));
                }
                net.addEdge(fe);
            }
            FordFulkerson FF = new FordFulkerson(net, 0, t);

            for (FlowEdge fe : net.adj(t)) {

                if (FF.inCut(fe.other(t))) {
                    ie[i] = true;
                    ce[i].add(getTeamIndex(fe.other(t), i));
                }
            }

        }

    }

    public int numberOfTeams() {                                    // number of teams
        return n;

    }

    public Iterable<String> teams() {                               // all teams
        return () -> Arrays.stream(teams).iterator();
    }

    public int wins(String team) {                                  // number of wins for given team
        int index = indexOf(teams, team);
        if (index == -1) {
            throw new IllegalArgumentException("team not found");
        }
        return w[index];
    }

    public int losses(String team) {                                // number of losses for given team
        int index = indexOf(teams, team);
        if (index == -1) {
            throw new IllegalArgumentException("team not found");
        }
        return l[index];
    }

    public int remaining(String team) {                             // number of remaining games for given team
        int index = indexOf(teams, team);
        if (index == -1) {
            throw new IllegalArgumentException("team not found");
        }
        return r[index];
    }

    public int against(String team1, String team2) {                // number of remaining games between team1 and team2
        int index1 = indexOf(teams, team1);
        if (index1 == -1) {
            throw new IllegalArgumentException("team1 not found");
        }
        int index2 = indexOf(teams, team2);
        if (index2 == -1) {
            throw new IllegalArgumentException("team2 not found");
        }
        return g[index1][index2];
    }

    public boolean isEliminated(String team) {                      // is given team eliminated?

        int index = indexOf(teams, team);
        if (index == -1) {
            throw new IllegalArgumentException("team not found");
        }
        return ie[index];
    }

    public Iterable<String> certificateOfElimination(String team) { // subset R of teams that eliminates given team; null if not eliminated
        int index = indexOf(teams, team);
        if (index == -1) {
            throw new IllegalArgumentException("team not found");
        }
        if (!ie[index]) {
            return null;
        }
        return () -> {
            String[] ceArr = new String[ce[indexOf(teams, team)].size()];
            int i = 0;
            for (int x : ce[index]) {
                ceArr[i] = teams[x];
                i++;
            }
            return Arrays.stream(ceArr).iterator();
        };
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams4.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    private static int indexOf(String[] a, String key) {
        for (int i = 0; i < a.length; i++) {
            if (key.equals(a[i])) {
                return i;
            }
        }
        return -1;
    }

    private int getTeamVertex(int index, int graphIndex) {
        if (index > graphIndex) {
            index--;
        }
        return (index + 1 + (n - 1) * (n - 1) / 2);
    }

    private int getTeamIndex(int vertex, int graphIndex) {
        // System.out.println(vertex);
        int index = vertex - 1 - (n - 1) * (n - 1) / 2;
        if (index >= graphIndex) {
            index++;
        }
        //  System.out.println(index + ",,,");
        return index;
    }

    private int getMatchVertex(int indexA, int indexB, int graphIndex) {
        if (indexA > graphIndex) {
            indexA--;
        }
        if (indexB > graphIndex) {
            indexB--;
        }
        int v = indexA - indexB;// + indexB * (n - 1) ;
        for (int i = 1; i <= indexB; i++) {
            v += (n - i - 1);

        }
        return v;
    }

//    private int[] getMatchTeams(int vertex, int graphIndex) {
//        //System.out.println(vertex-1);
//        int indexA = 0;
//        int indexB = vertex - 1;
//        int h = n - 2;
//        while (indexB >= h) {
//            indexB -= h;
//            indexA++;
//            h--;
//
//        }
//        indexB += (n - h - 1);
//        if (indexA >= graphIndex) {
//            indexA++;
//        }
//        if (indexB >= graphIndex) {
//            indexB++;
//        }
//        // System.out.println(indexA+","+indexB+"..."+graphIndex);
//        return new int[]{indexA, indexB};
//    }
}
