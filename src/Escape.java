import java.util.Arrays;

/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int[][] open = Graphs.open(w,broken);
        int cells = w.cells();
        int exit = cells - 1;

        int deadEnds = 0;

        for (int i = 0; i < w.cells();i++){
            if (open[i].length == 1){
                deadEnds++;
            }
        }

        if (cells == 0) {
            return new Result(new int[0], 0, deadEnds);
        }


        boolean[] visited = new boolean[w.cells()];
        int[] parent = new int[w.cells()];
        Arrays.fill(parent,-1);

        int[] stack = new int[cells];
        int[] nextDirection = new int[cells];
        int top = 0;
        int visitedCount = 0;

        stack[top++] = 0;
        visited[0] = true;
        visitedCount++;


        while (top > 0) {
            int cell = stack[top - 1];

            if (cell == exit) {
                break;
            }

            if (nextDirection[cell] == 4) {
                top--;
                continue;
            }

            int direction = nextDirection[cell];
            nextDirection[cell]++;

            int cols = w.cols();
            int next;

            if (direction == 0) {
                next = cell - cols;
                if (cell < cols) continue;
            } else if (direction == 1) {
                next = cell + 1;
                if (cell % cols == cols - 1) continue;
            } else if (direction == 2) {
                next = cell + cols;
                if (cell / cols == w.rows() - 1) continue;
            } else {
                next = cell - 1;
                if (cell % cols == 0) continue;
            }

            boolean isOpen = false;

            for (int neighbour : open[cell]) {
                if (neighbour == next) {
                    isOpen = true;
                    break;
                }
            }

            if (isOpen && !visited[next]) {
                visited[next] = true;
                visitedCount++;
                parent[next] = cell;
                stack[top++] = next;
            }
        }



        if (!visited[exit]) {
            return new Result(new int[0], visitedCount, deadEnds);
        }

        int pathLength = 0;
        for (int cell = exit; cell != -1; cell = parent[cell]) {
            pathLength++;
        }

        int[] path = new int[pathLength];
        int index = pathLength - 1;

        for (int cell = exit; cell != -1; cell = parent[cell]) {
            path[index--] = cell;
        }

        return new Result(path, visitedCount, deadEnds);
    }
}

