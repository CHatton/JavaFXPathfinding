package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    private final Map<Point, Location> graph;
    private Point start;
    private Point dest;
    private int size;

    public List<Point> allPoints() {
        return new ArrayList<>(graph.keySet());
    }

    public int size() {
        return size;
    }

    public Graph(int size) { // graph will always be a square
        this.size = size;
        this.graph = new HashMap<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // fill up whole graph with open spaces
                this.graph.put(new Point(i, j), new Location(true));
            }
        }
        this.start = new Point(0, 0); // default to top left
        this.dest = new Point(size - 1, size - 1); // and bottom right
    }

    public boolean contains(Point point) {
        return graph.keySet().contains(point);
    }

    private void addLocation(Point point, Location location) {
        this.graph.put(point, location);
    }

    public boolean isPassable(Point point) {
        return graph.get(point).isPassable();
    }

    public void open(Point point) {
        if (!point.equals(dest)) {
            graph.get(point).open();
        }
    }

    public void close(Point point) {
        if (!point.equals(start)) {
            graph.get(point).close();
        }
    }

    public void assignStart(Point startingPoint) {
        this.start = startingPoint;
        addLocation(startingPoint, new Location(true));
    }

    public void assignDest(Point destination) {
        this.dest = destination;
        this.graph.put(destination, new Location(true));
    }

    public Point start() {
        return start;
    }

    public Point dest() {
        return dest;
    }

    private class Location {
        /*
        keep track of locations in the graph, provides functionality to mark locations
        as open/closed.
         */
        private boolean passable;

        Location(boolean passable) {
            this.passable = passable;
        }

        private boolean isPassable() {
            return passable;
        }

        private void open() {
            this.passable = true;
        }

        private void close() {
            this.passable = false;
        }
    } // Location

} // Graph
