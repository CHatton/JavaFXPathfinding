package main;

import graph.Graph;
import javafx.fxml.FXML;
import pathanimations.Animation;
import pathanimations.Animator;
import pathanimations.astar.AstarAnimator;
import pathanimations.astar.diagonals.AllowDiagonals;
import pathanimations.astar.diagonals.DiagonalsAllowed;
import pathanimations.astar.diagonals.NoDiagonals;
import pathanimations.astar.heuristics.Euclidean;
import pathanimations.astar.heuristics.Heuristic;
import pathanimations.astar.heuristics.Manhattan;

public class Controller {


    private Graph graph;
    private Animator activeAnimator;
    private RectanglePainter painter;


    void setGraph(Graph graph) {
        this.graph = graph;
    }

    void setAnimator(Animator animator) {
        activeAnimator = animator;
    }

    void setPainter(RectanglePainter painter) {
        this.painter = painter;
    }

    @FXML
    private void playAnimation() throws InterruptedException {
        Animation anim = activeAnimator.animate();
        painter.paintAnimation(anim);
    }

    @FXML
    private void clearScreen() {
        painter.paintCleanGraph(graph);
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
        setDiagonal(new AllowDiagonals(graph));
    }

    @FXML
    private void disableDiagonals() {
        setDiagonal(new NoDiagonals(graph));
    }

    private void setDiagonal(DiagonalsAllowed diagonalsAllowed) {
        if (activeAnimator instanceof AstarAnimator) {
            ((AstarAnimator) activeAnimator).setDiagonalsAllowed(diagonalsAllowed);
        }
    }

}
