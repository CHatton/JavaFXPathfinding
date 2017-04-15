package pathanimations.astar.directions;

import graph.Graph;
import graph.Point;

import java.util.ArrayList;
import java.util.List;

public class AllowDiagonals implements Directional {

    private final Graph graph;

    public AllowDiagonals(Graph graph) {
        this.graph = graph;
    }

    @Override
    public List<Point> getNeighbours(Point point) {
        List<Point> neighbours = new ArrayList<>();
        for (int y = point.y() - 1; y <= point.y() + 1; y++) {
            for (int x = point.x() - 1; x <= point.x() + 1; x++) {
                Point current = new Point(x, y);
                if (current.equals(point)) { // don't want itself as neighbour
                    continue;
                }
                if (graph.contains(current)) {
                    neighbours.add(current);
                }
            }
        }
        return neighbours;
    }
}
