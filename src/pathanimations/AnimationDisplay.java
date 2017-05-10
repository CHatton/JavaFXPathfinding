package pathanimations;

import graph.Graph;
import graph.Point;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class AnimationDisplay {
    private final Canvas displayArea;
    private final Graph graph;
    private final double size;
    private boolean animationPlaying = false;
    private boolean draggingDest = false;
    private boolean draggingStart = false;


    public AnimationDisplay(Canvas displayArea, Graph graph) {
        this.displayArea = displayArea;
        this.size = displayArea.getHeight();
        this.graph = graph;
        setCanvasListener();
        renderNewGraph(graph);
    }

    private void setCanvasListener() {
        displayArea.addEventFilter(MouseEvent.ANY, e -> {
            if (!animationPlaying) { // so we can't alter graph mid-animation

                Point current = getPointFromMousePosition(e);
                if (!graph.contains(current)) {
                    return;  // outside of the canvas, avoid NPE
                }
                EventType type = e.getEventType();

                if (current.equals(graph.dest()) && current.equals(graph.start())) {
                    return;
                }

                if (current.equals(graph.dest()) && !draggingStart) {
                    draggingDest = (type == MouseEvent.MOUSE_PRESSED || type == MouseEvent.MOUSE_DRAGGED);
                } else if (current.equals(graph.start()) && !draggingDest) {
                    draggingStart = (type == MouseEvent.MOUSE_PRESSED || type == MouseEvent.MOUSE_DRAGGED);
                }


                if (draggingDest) {
                    if (!current.equals(graph.start())) {
                        drawPoint(graph.dest(), Colours.PASSABLE);
                        graph.assignDest(current);
                        drawPoint(graph.dest(), Colours.DESTINATION);
                    }
                    return;
                }

                if (draggingStart) {
                    if (!current.equals(graph.dest())) {
                        drawPoint(graph.start(), Colours.PASSABLE);
                        graph.assignStart(current);
                        drawPoint(graph.start(), Colours.START);
                    }
                    return;
                }

                boolean closingPoints = (type == MouseEvent.MOUSE_DRAGGED
                        || type == MouseEvent.MOUSE_PRESSED)
                        && e.isPrimaryButtonDown(); // left click makes walls

                boolean openingPoints = (type == MouseEvent.MOUSE_DRAGGED
                        || type == MouseEvent.MOUSE_PRESSED)
                        && e.isSecondaryButtonDown(); // right click removes walls


                if (closingPoints) {
                    closePoint(current);
                } else if (openingPoints) {
                    openPoint(current);
                }

            } // event filter
        });
    }

    private void closePoint(Point p) {
        graph.close(p);
        drawPoint(p, Colours.WALL);
    }

    private void openPoint(Point p) {
        graph.open(p);
        drawPoint(p, Colours.PASSABLE);
    }


    private void drawPoint(Point point, Color color) {

        int x = point.x();
        int y = point.y();

        double graphSize = graph.size();
        double sizeOfOne = size / graphSize;

        GraphicsContext gc = displayArea.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(x * sizeOfOne, y * sizeOfOne, sizeOfOne, sizeOfOne);
        gc.setFill(color);
        gc.fillRect(x * sizeOfOne, y * sizeOfOne, sizeOfOne, sizeOfOne);
    }


    private Point getPointFromMousePosition(MouseEvent e) {
        double x = e.getSceneX();
        double y = e.getSceneY();
        double sizeOfOne = size / graph.size();
        return new Point((int) (x / sizeOfOne), (int) (y / sizeOfOne));
    }


    private void clearCanvas() {
        GraphicsContext gc = displayArea.getGraphicsContext2D();
        gc.clearRect(0, 0, displayArea.getWidth(), displayArea.getHeight());
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
        if (frame != null) {
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
}
