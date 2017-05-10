package main;

import graph.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pathanimations.AnimationDisplay;
import pathanimations.astar.AstarAnimator;
import pathanimations.astar.diagonals.NoDiagonals;
import pathanimations.astar.heuristics.Euclidean;
import pathanimations.astar.heuristics.Manhattan;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        int GRAPH_SIZE = 30;
        Graph graph = new Graph(GRAPH_SIZE);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("pathfinding.fxml"));
        HBox root = loader.load();

        Controller controller = loader.getController();
        controller.setAnimator(new AstarAnimator(graph, new Manhattan(), new NoDiagonals(graph)));
        controller.setGraph(graph);
        controller.init();

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
