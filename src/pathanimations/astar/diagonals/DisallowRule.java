package pathanimations.astar.diagonals;

import graph.Graph;
import graph.Point;

import java.util.ArrayList;
import java.util.List;

public class DisallowRule implements DiagonalRule {

    private final Graph graph;

    public DisallowRule(Graph graph) {
        this.graph = graph;
    }

    @Override
    public List<Point> getNeighbours(Point point) {
        List<Point> neighbours = new ArrayList<>();

        Point[] points = {
                new Point(point.x(), point.y() + 1),
                new Point(point.x() + 1, point.y()),
                new Point(point.x() - 1, point.y()),
                new Point(point.x(), point.y() - 1)
        };

        for (Point p : points) {
            if (graph.contains(p)) {
                neighbours.add(p);
            }
        }
        return neighbours;
    }
}
