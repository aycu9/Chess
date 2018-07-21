package game.lobby;

import api.*;
import javafx.application.Application;
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
    private final ChessAPI api;
    private User user;
    private final int windowWidth = 600;
    private final int windowHeight = 300;
    private final Label usernameLabel = new Label();
    private final HostList hostList;
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
        }

        @Override
        public void onFailure(Call<User> call, Throwable throwable) {
            throwable.printStackTrace();
        }
    };
    private final Callback<Void> hostCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            hostList.updateHostList();
        }

        @Override
        public void onFailure(Call<Void> call, Throwable throwable) {
            throwable.printStackTrace();
        }
    };


    public OnlineLobby(String apiBaseURL) {
        retrofit = new Retrofit.Builder()
                .baseUrl(apiBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ChessAPI.class);
        this.hostList = new HostList(windowWidth, api);
    }

    public void launch(Stage primaryStage) throws Exception {
        NewUser newUser = new NewUser(askUserForNickname());
        createLobbyUI(primaryStage, newUser);
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

    private void createLobbyUI(Stage stage, NewUser newUser) {

        usernameLabel.setText("Nickname: " + newUser.name);
        usernameLabel.setPadding(new Insets(8));
        VBox root = new VBox();
        root.getChildren().addAll(usernameLabel, hostList.getNode(), createButton());
        stage.setScene(new Scene(root, windowWidth, windowHeight));
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
                    call.enqueue(hostCallback);
                }
            }
        });
        FlowPane flowPane = new FlowPane(refreshButton, hostButton);
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
