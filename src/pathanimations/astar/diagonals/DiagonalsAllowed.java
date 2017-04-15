package pathanimations.astar.directions;

import graph.Point;

import java.util.List;

public interface Directional {
    List<Point> getNeighbours(Point point);
}
