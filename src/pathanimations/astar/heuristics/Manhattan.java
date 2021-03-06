package pathanimations.astar.heuristics;

import graph.Point;

public class Manhattan implements Heuristic {

    @Override
    public double getFor(Point p1, Point p2) {
        return Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y());
    }
}
