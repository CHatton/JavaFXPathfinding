package pathanimations;

import graph.Graph;
import graph.Point;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AnimationDisplay {
    private final Canvas displayArea;
    private final Graph graph;
    private final double size;
    private boolean animationPlaying = false;


    public AnimationDisplay(Canvas displayArea, Graph graph, int size) {
        this.displayArea = displayArea;
        this.graph = graph;
        this.size = size;
        renderNewGraph(graph);
    }

    private void drawPoint(Point point, Color color) {

        double x = point.x();
        double y = point.y();

        double sizeOfOne = size / graph.size();
        double margin = sizeOfOne * (0.05);

        GraphicsContext gc = displayArea.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(x * sizeOfOne, y * sizeOfOne, sizeOfOne - margin, sizeOfOne - margin);
    }


    private void clearCanvas() {
        GraphicsContext gc = displayArea.getGraphicsContext2D();
        gc.clearRect(0, 0, displayArea.getWidth(), displayArea.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, displayArea.getWidth(), displayArea.getHeight());
    }

    public void renderNewGraph(Graph graph) {
        if (!animationPlaying) {
            clearCanvas();
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

    private void renderFrame(Frame frame) {
        Point point = frame.point();
        State state = frame.state();

        switch (state) {
            case OPEN:
                drawPoint(point, Colours.OPENLIST);
                break;
            case FRONTIER:
                drawPoint(point, Colours.FRONTIER);
                break;
            case PATH:
                drawPoint(point, Colours.PATH);
        }
    }
}
