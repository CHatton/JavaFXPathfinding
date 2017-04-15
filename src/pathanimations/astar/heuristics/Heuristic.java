package pathanimations.astar.heuristics;

import graph.Point;

public interface Heuristic {
    float getFor(Point p1, Point p2);
}
