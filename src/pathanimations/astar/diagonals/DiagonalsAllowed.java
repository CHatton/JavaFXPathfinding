package pathanimations.astar.diagonals;

import graph.Point;

import java.util.List;

public interface DiagonalsAllowed {
    List<Point> getNeighbours(Point point);
}
