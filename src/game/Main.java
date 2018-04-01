package game;

import api.ChessAPI;
import api.UserState;
import game.player.LocalPlayer;
import game.player.Player;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
        GraphicsContext gc = canvas.getGraphicsContext2D();

        game.startGame(gc);
        Player player1 = new LocalPlayer(game, game.getTeam1(), scene);
        player1.start();
        Player player2 = new LocalPlayer(game, game.getTeam2(), scene);
        player2.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.0.157:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ChessAPI api = retrofit.create(ChessAPI.class);

    }

}
