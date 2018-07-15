package game;

import api.ChessAPI;
import api.UserState;
import game.lobby.OnlineLobby;
import game.player.LocalPlayer;
import game.player.NetworkPlayer;
import game.player.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Optional;

import static game.Main.GameType.LOCAL_GAME;
import static game.Main.GameType.ONLINE_GAME;


public class Main extends Application {

    private int windowWidth = 800;
    private int windowHeight = 640;
    private ChessGame game = new ChessGame(windowWidth, windowHeight);
    private String apiBaseURL = "https://aqueous-crag-60434.herokuapp.com";


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

        if (askUserToChooseGameType() == ONLINE_GAME) {
            OnlineLobby onlineLobby = new OnlineLobby(apiBaseURL);
            onlineLobby.launch(primaryStage);
//            Team playerChosenTeam = askUserToPickTeam();
//            Player player1 = new LocalPlayer(game, playerChosenTeam, scene);
//            player1.start();
//            Player player2;
//            String address = askUserForHostIPAddress();
//            if (address == null) {
//                player2 = new NetworkPlayer(game, game.getOppositeTeam(playerChosenTeam));
//            } else {
//                player2 = new NetworkPlayer(game, game.getOppositeTeam(playerChosenTeam), address);
//            }
//            player2.start();
        } else {
            Player player1 = new LocalPlayer(game, game.getTeam1(), scene);
            player1.start();
            Player player2 = new LocalPlayer(game, game.getTeam2(), scene);
            player2.start();
        }
    }

    public GameType askUserToChooseGameType() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Online or Local");
        alert.setHeaderText("Please choose whether you will play online or locally:");
        ButtonType buttonLocal = new ButtonType("Local");
        ButtonType buttonOnline = new ButtonType("Online");
        alert.getButtonTypes().setAll(buttonLocal, buttonOnline);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonLocal) {
            return LOCAL_GAME;
        } else if (result.get() == buttonOnline) {
            return ONLINE_GAME;
        }
        return null;
    }

    public Team askUserToPickTeam() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pick Team");
        alert.setHeaderText("Please pick the team you want to play as:");
        ButtonType buttonTeam1 = new ButtonType(game.getTeam1().getTeamName());
        ButtonType buttonTeam2 = new ButtonType(game.getTeam2().getTeamName());
        alert.getButtonTypes().setAll(buttonTeam1, buttonTeam2);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTeam1) {
            return game.getTeam1();
        } else if (result.get() == buttonTeam2) {
            return game.getTeam2();
        }
        return null;
    }

    public String askUserForHostIPAddress() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Host or client?");
        alert.setHeaderText("Do you wish to be the host or the client? ");
        ButtonType buttonTeam1 = new ButtonType("Client");
        ButtonType buttonTeam2 = new ButtonType("Host");
        alert.getButtonTypes().setAll(buttonTeam1, buttonTeam2);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTeam1) {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Enter Host Address");
            dialog.setHeaderText("Enter the IP Address of the host that you are connecting to:");
            dialog.setContentText("IP Address: ");
            Optional<String> address = dialog.showAndWait();
            return address.get();
        } else if (result.get() == buttonTeam2) {
            return null;
        }
        return null;
    }


    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    enum GameType {
        LOCAL_GAME, ONLINE_GAME
    }


}
