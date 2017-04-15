package main;

import graph.Graph;
import graph.Point;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import pathanimations.Animation;
import pathanimations.Frame;
import pathanimations.State;

import java.util.HashMap;
import java.util.Map;

class RectanglePainter {
    private final int HEIGHT = 600;
    private final BorderPane root;
    private final Graph graph;
    private final Map<Point, Rectangle> rectangles;
    private final Color startColour = Color.GREEN;
    private final Color wallColour = Color.rgb(71, 81, 71);
    private final Color passableColour = Color.BEIGE;
    private final Color destColour = Color.RED;
    private final Color openColour = Color.rgb(127, 167, 232);
    private final Color frontierColour = Color.rgb(132, 224, 132);
    private final Color pathColour = Color.YELLOW;
    private boolean animationPlaying = false;

    RectanglePainter(BorderPane root, Graph graph) {
        this.graph = graph;
        this.root = root;
        this.rectangles = new HashMap<>();
    }

    void paintCleanGraph(Graph graph) {
        if (!animationPlaying) {
            for (Point point : graph.allPoints()) {
                if (point.equals(graph.start())) {
                    drawPoint(point, startColour);
                } else if (point.equals(graph.dest())) {
                    drawPoint(point, destColour);
                } else if (!graph.isPassable(point)) {
                    drawPoint(point, wallColour);
                } else if (graph.isPassable(point)) {
                    drawPoint(point, passableColour);
                }
            }
        }
    }


    void paintAnimation(Animation animation) throws InterruptedException {
        paintCleanGraph(graph);

        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (animation.hasNext()) {
                    Platform.runLater(() -> renderFrame(animation.next()));
                    Thread.sleep(2);
                }
                animationPlaying = false;
                return null;

            }
        };
        if (!animationPlaying) {
            animationPlaying = true;
            Thread t = new Thread(task);
            t.start();
        }

    }


    private void renderFrame(Frame frame) {
        Point point = frame.point();
        State state = frame.state();
        Rectangle rect = rectangles.get(point);

        ScaleTransition st = new ScaleTransition();
        st.setNode(rect);
        st.setDuration(Duration.seconds(0.1));
        st.setByX(0.15);
        st.setByY(0.15);
        st.setAutoReverse(true);
        st.setCycleCount(2);

        switch (state) {
            case OPEN:
                rect.setFill(openColour);
                break;
            case FRONTIER:
                rect.setFill(frontierColour);
                break;
            case PATH:
                rect.setFill(pathColour);
                break;
        }

        st.play();
    }

    private void drawPoint(Point point, Color color) {
        Rectangle rect = rectangles.computeIfAbsent(point, this::createRectangle);
        alignRectangleWithPoint(rect, point);
        rect.setFill(color);
    }

    private void alignRectangleWithPoint(Rectangle rect, Point point) {
        double size = HEIGHT / graph.size();
        double margin = size * (0.05);
        rect.setX(point.x() * size);
        rect.setY(point.y() * size);
        rect.setHeight(size - margin);
        rect.setWidth(size - margin);
    }

    private Rectangle createRectangle(Point point) {
        Rectangle rect = new Rectangle();
        alignRectangleWithPoint(rect, point);
        rectangles.put(point, rect);
        root.getChildren().add(rect);
        return rect;
    }
}
