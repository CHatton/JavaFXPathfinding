package pathanimations;

import graph.Graph;
import graph.Point;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;


public class AnimationDisplay {
    private final Group displayArea;
    private final Graph graph;
    private final Map<Point, Rectangle> rectangles;
    private final int size;
    private boolean animationPlaying = false;


    public AnimationDisplay(Group displayArea, Graph graph, int size) {
        this.displayArea = displayArea;
        this.graph = graph;
        this.rectangles = new HashMap<>();
        this.size = size;
        renderNewGraph(graph);
    }

    private void drawPoint(Point point, Color color) {
        Rectangle rect = rectangles.computeIfAbsent(point, this::createRectangle);
        alignRectangleWithPoint(rect, point);
        rect.setFill(color);
    }

    public void renderNewGraph(Graph graph) {
        if (!animationPlaying) {
            for (Point point : graph.allPoints()) {
                if (point.equals(graph.start())) {
                    drawPoint(point, Colours.START);
                } else if (point.equals(graph.dest())) {
                    drawPoint(point, Colours.DESTINATION);
                } else if (!graph.isPassable(point)) {
                    drawPoint(point, Colours.WALL);
                } else if (graph.isPassable(point)) {
                    drawPoint(point, Colours.PASSABLE);
                }
            }
        }
    }

    public void playAnimation(GraphAnimation animation) throws InterruptedException {
        renderNewGraph(graph);
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

    private void alignRectangleWithPoint(Rectangle rect, Point point) {
        double sizeOfOne = size / graph.size();
        double margin = sizeOfOne * (0.05);
        rect.setX(point.x() * sizeOfOne);
        rect.setY(point.y() * sizeOfOne);
        rect.setHeight(sizeOfOne - margin);
        rect.setWidth(sizeOfOne - margin);
    }

    private Rectangle createRectangle(Point point) {
        Rectangle rect = new Rectangle();
        alignRectangleWithPoint(rect, point);
        rectangles.put(point, rect);
        displayArea.getChildren().add(rect);
        return rect;
    }

    private void renderFrame(Frame frame) {
        Point point = frame.point();
        State state = frame.state();
        Rectangle rect = rectangles.get(point);

        ScaleTransition anim = FXAnimationFactory.makeRectangleAnimation();
        anim.setNode(rect);


        switch (state) {
            case OPEN:
                rect.setFill(Colours.OPENLIST);
                break;
            case FRONTIER:
                rect.setFill(Colours.FRONTIER);
                break;
            case PATH:
                rect.setFill(Colours.PATH);
                break;
        }

//        anim.play();
    }
}
