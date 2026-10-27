import java.util.Arrays;
import java.util.PriorityQueue;
/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
record Wall(int index,int from, int to,int cost){}
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet disjointSet = new DisjointSet(w.cells());
        int[] brokenWalls = new int[w.count()];
        int brokenCountWalls = 0;

        for (int i = 0; i < w.count();i++){
            if (disjointSet.union(w.a()[i],w.b()[i])){
                brokenWalls[brokenCountWalls] = i;
                brokenCountWalls++;

            }

        }

        return Arrays.copyOf(brokenWalls,brokenCountWalls);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        DisjointSet disjointSet = new DisjointSet(w.cells());
        int[] brokenWalls = new int[w.count()];
        int countBrokenWalls = 0;
        Integer[] wallIndexes = new Integer[w.count()];

        for (int i = 0; i < w.count(); i++){
            wallIndexes[i] = i;
        }
        Arrays.sort(wallIndexes,(a,b) ->
                Integer.compare(w.cost()[a],w.cost()[b])
        );

        for (int index : wallIndexes){
            if (disjointSet.union(w.a()[index],w.b()[index])){
                brokenWalls[countBrokenWalls] = index;
                countBrokenWalls++;

            }
        }
        return Arrays.copyOf(brokenWalls,countBrokenWalls);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells();
        int m = w.count();

        if (n <= 1) {
            return 0;
        }

        int[] a = w.a();
        int[] b = w.b();
        int[] cost = w.cost();

        int[] degree = new int[n];

        for (int k = 0; k < m; k++) {
            degree[a[k]]++;
            degree[b[k]]++;
        }

        int[][] incident = new int[n][];

        for (int i = 0; i < n; i++) {
            incident[i] = new int[degree[i]];
        }

        Arrays.fill(degree, 0);

        for (int k = 0; k < m; k++) {
            incident[a[k]][degree[a[k]]++] = k;
            incident[b[k]][degree[b[k]]++] = k;
        }

        boolean[] inTree = new boolean[n];

        PriorityQueue<Integer> pq = new PriorityQueue<>(
                (left, right) -> Integer.compare(cost[left], cost[right])
        );

        inTree[0] = true;
        int visitedCount = 1;
        long totalCost = 0;

        for (int edge : incident[0]) {
            pq.offer(edge);
        }

        while (visitedCount < n && !pq.isEmpty()) {
            int k = pq.poll();

            int u = a[k];
            int v = b[k];

            if (inTree[u] && inTree[v]) {
                continue;
            }

            int next = inTree[u] ? v : u;

            inTree[next] = true;
            visitedCount++;
            totalCost += cost[k];

            for (int edge : incident[next]) {
                int other = a[edge] == next ? b[edge] : a[edge];

                if (!inTree[other]) {
                    pq.offer(edge);
                }
            }
        }

        return totalCost;
    }
}
