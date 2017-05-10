package pathanimations;

import graph.Point;

public class Frame {

    private final Point point;
    private final State state;

    public Frame(Point point, State state) {
        this.point = point;
        this.state = state;
    }

    public Point point() {
        return point;
    }

    public State state() {
        return state;
    }
}
