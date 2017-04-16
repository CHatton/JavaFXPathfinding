package main;

import graph.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pathanimations.AnimationDisplay;
import pathanimations.astar.AstarAnimator;
import pathanimations.astar.diagonals.NoDiagonals;
import pathanimations.astar.heuristics.Euclidean;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        int GRAPH_SIZE = 30;
        Graph graph = new Graph(GRAPH_SIZE);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("pathfinding.fxml"));
        BorderPane root = loader.load();

//        Group displayArea = new Group();
//        root.getChildren().add(displayArea);

        Canvas canvas = new Canvas(600, 600);
        root.getChildren().add(canvas);

        AnimationDisplay display = new AnimationDisplay(canvas, graph, 600);

        Controller controller = loader.getController();
        controller.setGraph(graph);
        controller.setAnimator(new AstarAnimator(graph, new Euclidean(), new NoDiagonals(graph)));
        controller.setAnimationDisplay(display);

        primaryStage.setTitle("JFX Pathfinding Visualiser");
        int WIDTH = 800;
        int HEIGHT = 660;
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
