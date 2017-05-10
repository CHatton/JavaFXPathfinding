package pathanimations.astar;

import graph.Graph;
import graph.Point;
import pathanimations.Animator;
import pathanimations.Frame;
import pathanimations.GraphAnimation;
import pathanimations.State;
import pathanimations.astar.diagonals.DiagonalRule;
import pathanimations.astar.heuristics.Heuristic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class AstarAnimator implements Animator {

    private final Graph graph;
    private Heuristic heuristic;
    private DiagonalRule diagonalRule;

    public AstarAnimator(Graph graph, Heuristic heuristic, DiagonalRule diagonalRule) {
        this.graph = graph;
        this.heuristic = heuristic;
        this.diagonalRule = diagonalRule;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    public void setDiagonalRule(DiagonalRule diagonalRule) {
        this.diagonalRule = diagonalRule;
    }

    private double calcHeuristic(Node n1, Node n2) {
        return heuristic.getFor(n1.point, n2.point);
    }

    private List<Node> getNeighbours(Node node) {
        List<Point> neighbours = this.diagonalRule.getNeighbours(node.point);
        return neighbours.stream().map(Node::new).collect(Collectors.toList());
    }

    private boolean passable(Node node) {
        return graph.isPassable(node.point);
    }

    @Override
    public GraphAnimation animate() {

        Set<Node> closedSet = new HashSet<>();
        Queue<Node> frontier = new PriorityQueue<>(Comparator.comparingDouble(node -> node.fCost));

        Node startNode = new Node(graph.start());
        Node dest = new Node(graph.dest());

        frontier.offer(new Node(graph.start()));

        GraphAnimation anim = new GraphAnimation();

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();

            if (current.equals(dest)) {
                dest = current; // reassign to keep the full path held by current
                break;
            }

            if (current.equals(startNode)) {
                anim.addFrame(new Frame(current.point, State.START));
            } else {
                anim.addFrame(new Frame(current.point, State.OPEN));
            }

            closedSet.add(current);

            List<Node> neighbours = getNeighbours(current);

            for (Node next : neighbours) {
                if (closedSet.contains(next) || !passable(next)) {
                    continue;
                }

                double hCost = calcHeuristic(current, next);
                double gCost = current.gCost + hCost;

                if (!frontier.contains(next)) {
                    frontier.offer(next);
                } else if (gCost >= next.gCost) {
                    continue;
                }

                next.previous = current;
                next.gCost = gCost;
                next.fCost = next.gCost + calcHeuristic(next, dest);

                if (!next.equals(dest)) {
                    anim.addFrame(new Frame(next.point, State.FRONTIER));
                }

            } // foreach neighbour

        } // while not empty

        constructPath(dest).forEach(node -> anim.addFrame(new Frame(node.point, State.PATH)));

        return anim;
    }

    private List<Node> constructPath(Node dest) {
        /*
        create the path between start and destination nodes
         */
        List<Node> path = new ArrayList<>();
        Node node = dest;
        boolean atHead = true;
        while (node != null) {
            if (atHead && node.previous == null) {
                // there was no path found, don't want to highlight the point
                return new ArrayList<>();
            }
            atHead = false;
            path.add(node);
            node = node.previous;
        }
        Collections.reverse(path);
        return path;
    }

    private class Node {

        private final Point point;
        private double gCost = 0;
        private double fCost = Double.MAX_VALUE;
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
            return point.toString() + "\n" +
                    "fCost " + fCost + "\n" +
                    "gCost " + gCost + "\n";
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Node)) {
                return false;
            }
            Node otherNode = (Node) o;
            return point.equals(otherNode.point);
        }


    } // Node
} // Astar
