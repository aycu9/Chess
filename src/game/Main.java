package game;

import api.ChessAPI;
import api.UserState;
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
        Team playerChosenTeam = askUserToPickTeam();;
        Player player1 = new LocalPlayer(game, playerChosenTeam, scene);
        player1.start();
        Player player2;
        String address = askUserForHostIPAddress();
        if(address == null){
            player2 = new NetworkPlayer(game, game.getOppositeTeam(playerChosenTeam));
        }
        else{
            player2 = new NetworkPlayer(game, game.getOppositeTeam(playerChosenTeam), address);
        }
        player2.start();
    }

    public Team askUserToPickTeam (){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pick Team");
        alert.setHeaderText("Please pick the team you want to play as:");
        ButtonType buttonTeam1 = new ButtonType(game.getTeam1().getTeamName());
        ButtonType buttonTeam2 = new ButtonType(game.getTeam2().getTeamName());
        alert.getButtonTypes().setAll(buttonTeam1, buttonTeam2);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTeam1){
            return game.getTeam1();
        }else if(result.get() == buttonTeam2){
            return game.getTeam2();
        }
        return null;
    }

    public String askUserForHostIPAddress (){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Host or client?");
        alert.setHeaderText("Do you wish to be the host or the client? ");
        ButtonType buttonTeam1 = new ButtonType("Client");
        ButtonType buttonTeam2 = new ButtonType("Host");
        alert.getButtonTypes().setAll(buttonTeam1, buttonTeam2);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTeam1){
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Enter Host Address");
            dialog.setHeaderText("Enter the IP Address of the host that you are connecting to:");
            dialog.setContentText("IP Address: ");
            Optional<String> address = dialog.showAndWait();
            return address.get();
        }else if(result.get() == buttonTeam2){
            return null;
        }
        return null;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
}
