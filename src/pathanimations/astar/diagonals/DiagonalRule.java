package pathanimations.astar.diagonals;

import graph.Point;

import java.util.List;

public interface DiagonalRule {
    List<Point> getNeighbours(Point point);
}
