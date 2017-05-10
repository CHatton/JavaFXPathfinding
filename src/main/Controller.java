package main;

import graph.Graph;
import graph.Point;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import pathanimations.AnimationDisplay;
import pathanimations.GraphAnimation;
import pathanimations.Animator;
import pathanimations.astar.AstarAnimator;
import pathanimations.astar.diagonals.AllowRule;
import pathanimations.astar.diagonals.DiagonalRule;
import pathanimations.astar.diagonals.DisallowRule;
import pathanimations.astar.heuristics.Euclidean;
import pathanimations.astar.heuristics.Heuristic;
import pathanimations.astar.heuristics.Manhattan;

public class Controller {

    @FXML
    private Canvas canvas;
    @FXML
    private Slider graphSize;

    private Graph graph;
    private Animator activeAnimator;
    private AnimationDisplay animationDisplay;


    @FXML
    void updateGraphSize() {
        int minSize = 10;
        int maxSize = 30;
        double percent = graphSize.getValue();
        double value = percent * (maxSize / 100.00) + minSize;
        graph.initAtSize((int) value);
        animationDisplay.renderNewGraph(graph);

    }

    void init() {
        this.animationDisplay = new AnimationDisplay(canvas, graph);
    }

    void setGraph(Graph graph) {
        this.graph = graph;
    }

    void setAnimator(Animator animator) {
        activeAnimator = animator;
    }

    @FXML
    private void playAnimation() throws InterruptedException {
        GraphAnimation anim = activeAnimator.animate();
        animationDisplay.playAnimation(anim);
    }

    @FXML
    private void clearScreen() {
        animationDisplay.renderNewGraph(graph);
    }


    @FXML
    private void setEuclidean() {
        setHeuristic(new Euclidean());
    }

    @FXML
    private void setManhattan() {
        setHeuristic(new Manhattan());
    }

    private void setHeuristic(Heuristic heuristic) {
        if (activeAnimator instanceof AstarAnimator) {
            ((AstarAnimator) activeAnimator).setHeuristic(heuristic);
        }
    }


    @FXML
    private void enableDiagonals() {
        setDiagonal(new AllowRule(graph));
    }

    @FXML
    private void disableDiagonals() {
        setDiagonal(new DisallowRule(graph));
    }

    private void setDiagonal(DiagonalRule diagonalRule) {
        if (activeAnimator instanceof AstarAnimator) {
            ((AstarAnimator) activeAnimator).setDiagonalRule(diagonalRule);
        }
    }

    @FXML
    private void clearWalls() {
        for (Point p : graph.allPoints()) {
            if (!graph.isPassable(p) && !p.equals(graph.start()) && !p.equals(graph.dest())) {
                graph.open(p);
            }
        }
        // don't want to lose start and destination points
        graph.initAtSize(graph.size(), graph.start(), graph.dest());
        animationDisplay.renderNewGraph(graph);
    }

}
