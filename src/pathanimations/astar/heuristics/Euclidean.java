package pathanimations.astar.heuristics;

import graph.Point;

public class Euclidean implements Heuristic {

    @Override
    public double getFor(Point p, Point q) {
        double xSquared = Math.pow(q.x() - p.x(), 2.0);
        double ySquared = Math.pow(q.y() - p.y(), 2.0);
        return (float) Math.sqrt(xSquared + ySquared);
    }
}
