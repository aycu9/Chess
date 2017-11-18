package game;

import javafx.application.Application;
import javafx.event.*;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    private int windowWidth = 800;
    private int windowHeight = 640;
    private ChessGame game = new ChessGame(windowWidth, windowHeight);


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Application starts.");
        primaryStage.setTitle("Chess Game");
        Canvas canvas = new Canvas(windowWidth, windowHeight);
        Group root = new Group();
        Scene scene = new Scene(root, windowWidth, windowHeight, Color.BEIGE);
        root.getChildren().add(canvas);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(event.getSceneX() + " " + event.getSceneY());
                game.click((int) event.getSceneX(), (int) event.getSceneY());
            }
        });

        GraphicsContext gc = canvas.getGraphicsContext2D();
        game.startGame(gc);

//        gc.setFill(Color.BLACK);
//        gc.rect(10, 10, 100, 200);

    }
}
