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
        boolean[] visited = new boolean[w.cells()];
        PriorityQueue<Wall> pq = new PriorityQueue<>(
                (a,b) -> Integer.compare(a.cost(),b.cost())
        );
        visited[0] = true;
        for (int i = 0; i < w.count(); i++) {
            if (((visited[w.a()[i]]) && !(visited[w.b()[i]]))
                    || (!(visited[w.a()[i]]) && (visited[w.b()[i]]))) {
                pq.add(new Wall(i, w.a()[i], w.b()[i], w.cost()[i]));
            }
        }
        long totalCost = 0;

        while(!pq.isEmpty()){
            Wall wall = pq.poll();

            if (visited[wall.from()] && visited[wall.to()]){
                continue;
            }

            int newCell;

            if (!visited[wall.from()]) {
                newCell = wall.from();
            } else {
                newCell = wall.to();
            }

            visited[newCell] = true;
            totalCost += wall.cost();

            for (int i = 0; i < w.count(); i++) {
                if (visited[w.a()[i]] != visited[w.b()[i]]) {
                    pq.add(new Wall(i, w.a()[i], w.b()[i], w.cost()[i]));
                }
            }
        }

        return totalCost;
    }
}
