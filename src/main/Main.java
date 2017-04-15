package main;

import graph.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pathanimations.astar.AstarAnimator;
import pathanimations.astar.diagonals.NoDiagonals;
import pathanimations.astar.heuristics.Euclidean;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        int GRAPH_SIZE = 50;
        Graph graph = new Graph(GRAPH_SIZE);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("pathfinding.fxml"));
        BorderPane root = loader.load();
        RectanglePainter painter = new RectanglePainter(root, graph);
        Controller controller = loader.getController();
        controller.setGraph(graph);
        controller.setAnimator(new AstarAnimator(graph, new Euclidean(), new NoDiagonals(graph)));
        controller.setPainter(painter);
        painter.paintCleanGraph(graph);

        primaryStage.setTitle("JFX Pathfinding Visualiser");
        int WIDTH = 800;
        int HEIGHT = 630;
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
