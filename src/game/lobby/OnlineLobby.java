package game.lobby;

import api.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.swing.text.LabelView;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Created by Libra on 2018-06-30.
 */
public class OnlineLobby {
    private final Retrofit retrofit;
    private final StartGameCallback startGameCallback;
    private final ChessAPI api;
    private User user;
    private final int windowWidth = 600;
    private final int windowHeight = 300;
    private final Label usernameLabel = new Label();
    private final HostList hostList;
    private GameStartChecker gameStartChecker;
    private final Callback<String> registerCallback = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            String uuid = response.body();
            System.out.println(uuid);
            GetUserRequest getUserRequest = new GetUserRequest(uuid);
            api.getUser(getUserRequest).enqueue(getUserCallback);
        }

        @Override
        public void onFailure(Call<String> call, Throwable throwable) {
            throwable.printStackTrace();
        }
    };
    private final Callback<User> getUserCallback = new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            user = response.body();
            System.out.println(user);
            gameStartChecker = new GameStartChecker(user, api, startGameCallback);
            Platform.runLater(() -> createLobbyUI());
        }

        @Override
        public void onFailure(Call<User> call, Throwable throwable) {
            throwable.printStackTrace();
        }
    };
    private final Callback<Void> waitForGameToStartCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            hostList.updateHostList();
            System.out.println("succeeded");
            if(!gameStartChecker.isGameStarted()) {
                gameStartChecker.start();
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable throwable) {
            throwable.printStackTrace();
        }
    };
    private Stage primaryStage;


    public OnlineLobby(String apiBaseURL, StartGameCallback startGameCallback) {
        retrofit = new Retrofit.Builder()
                .baseUrl(apiBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.startGameCallback = startGameCallback;
        api = retrofit.create(ChessAPI.class);
        this.hostList = new HostList(windowWidth, api);
    }

    public void launch(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        NewUser newUser = new NewUser(askUserForNickname());
        Call<String> call = api.registerNewUser(newUser);
        call.enqueue(registerCallback);
    }

    private String askUserForNickname() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Enter Nickname");
        dialog.setHeaderText("Enter the nickname you wish to use as a Chess game player");
        dialog.setContentText("Nickname: ");
        Optional<String> address = dialog.showAndWait();
        return address.get();
    }

    private void createLobbyUI() {
        usernameLabel.setText("Nickname: " + user.getName());
        usernameLabel.setPadding(new Insets(8));
        VBox root = new VBox();
        root.getChildren().addAll(usernameLabel, hostList.getNode(), createButton());
        primaryStage.setScene(new Scene(root, windowWidth, windowHeight));
    }

    private Node createButton() {

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                hostList.updateHostList();
            }
        });
        Button hostButton = new Button("Host Game");
        hostButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Integer chosenTeam = askUserToPickTeam();
                if (chosenTeam != null) {
                    Call<Void> call = api.hostGame(new HostGameRequest(user.getUuid(), chosenTeam));
                    call.enqueue(waitForGameToStartCallback);
                }
            }
        });
        Button joinButton = new Button("Join");
        joinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Host currentlySelectedHost = hostList.getCurrentlySelectedHost();
                if(currentlySelectedHost != null) {
                    Call<Void> call = api.startGame(new StartGameRequest(currentlySelectedHost.uuid, user.getUuid()));
                    call.enqueue(waitForGameToStartCallback);
                }
                else{
                    System.out.println("Please select a host first.");
                }
            }
        });
        FlowPane flowPane = new FlowPane(refreshButton, hostButton, joinButton);
        return flowPane;
    }

    private Integer askUserToPickTeam() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pick Team");
        alert.setHeaderText("Please pick the team you want to play as:");
        ButtonType buttonTeam1 = new ButtonType(new Team(1).getName());
        ButtonType buttonTeam2 = new ButtonType(new Team(2).getName());
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTeam1, buttonTeam2, buttonCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTeam1) {
            return 1;
        } else if (result.get() == buttonTeam2) {
            return 2;
        }
        return null;
    }




}
