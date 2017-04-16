package pathanimations.astar.heuristics;

import graph.Point;

public interface Heuristic {
    double getFor(Point p1, Point p2);
}
