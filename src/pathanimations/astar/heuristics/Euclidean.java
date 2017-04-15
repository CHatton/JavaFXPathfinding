package pathanimations.astar.heuristics;

import graph.Point;

public class Euclidian implements Heuristic {

    @Override
    public float getFor(Point p1, Point p2) {
        return Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y());
    }
}
