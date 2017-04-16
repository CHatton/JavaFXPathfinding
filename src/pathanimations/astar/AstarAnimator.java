package pathanimations.astar;

import graph.Graph;
import graph.Point;
import pathanimations.GraphAnimation;
import pathanimations.Animator;
import pathanimations.Frame;
import pathanimations.State;
import pathanimations.astar.diagonals.DiagonalsAllowed;
import pathanimations.astar.heuristics.Heuristic;

import java.util.*;
import java.util.stream.Collectors;

public class AstarAnimator implements Animator {
    private final Graph graph;

    private Heuristic heuristic;
    private DiagonalsAllowed diagonalsAllowed;

    public AstarAnimator(Graph graph, Heuristic heuristic, DiagonalsAllowed diagonalsAllowed) {
        this.graph = graph;
        this.heuristic = heuristic;
        this.diagonalsAllowed = diagonalsAllowed;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    public void setDiagonalsAllowed(DiagonalsAllowed diagonalsAllowed) {
        this.diagonalsAllowed = diagonalsAllowed;
    }

    private double calcHeuristic(Node n1, Node n2) {
        return heuristic.getFor(n1.point, n2.point);
    }

    private List<Node> getNeighbours(Node node) {
        List<Point> neighbours = this.diagonalsAllowed.getNeighbours(node.point);
        return neighbours.stream().map(Node::new).collect(Collectors.toList());
    }

    private boolean passable(Node node) {
        return graph.isPassable(node.point);
    }

    @Override
    public GraphAnimation animate() {
        GraphAnimation anim = new GraphAnimation();

        Set<Node> closedSet = new HashSet<>();
        Queue<Node> frontier = new PriorityQueue<>(Comparator.comparingDouble(o -> o.fCost));
        frontier.offer(new Node(graph.start()));
        Node dest = new Node(graph.dest());

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();

            current.hCost = calcHeuristic(current, dest);
            if (current.equals(dest)) {
                dest = current; // reassign to keep the full path
                break;
            }

            anim.addFrame(new Frame(current.point, State.OPEN));

            closedSet.add(current);

            List<Node> neighbours = getNeighbours(current);

            for (Node next : neighbours) {
                if (closedSet.contains(next) || !passable(next)) {
                    continue;
                }

                double gCost = current.gCost + Math.abs(
                        current.point.x() - next.point.x()) + Math.abs(current.point.y() - next.point.y());

                if (!frontier.contains(next)) {
                    frontier.offer(next);
                } else if (gCost >= next.gCost) {
                    continue;
                }

                next.previous = current;
                next.hCost = calcHeuristic(next, dest);
                next.gCost = gCost;
                next.fCost = next.gCost + next.hCost;

                anim.addFrame(new Frame(next.point, State.FRONTIER));
            } // foreach neighbour

        } // while not empty

        List<Node> path = constructPath(dest);
        for (Node node : path) {
            anim.addFrame(new Frame(node.point, State.PATH));
        }
        return anim;
    }

    private List<Node> constructPath(Node dest) {
        /*
        create the path between start and destination nodes
         */
        List<Node> path = new ArrayList<>();
        Node node = dest;
        while (node != null) {
            path.add(node);
            node = node.previous;
        }
        Collections.reverse(path);
        return path;
    }

    private class Node {

        private final Point point;
        private double gCost = 0;
        private double hCost = 0;
        private double fCost = Float.MAX_VALUE;
        private Node previous = null;


        private Node(Point point) {
            this.point = point;
        }

        @Override
        public int hashCode() {
            return point.hashCode();
        }

        @Override
        public String toString() {
            return point.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Node)) {
                return false;
            }
            Node otherNode = (Node) o;
            return this.point.equals(otherNode.point);
        }


    } // Node
} // Astar
