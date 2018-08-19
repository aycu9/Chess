package game.player;

import api.*;
import com.sun.net.httpserver.HttpServer;
import game.ChessGame;
import game.Team;
import game.UserStateListener;
import javafx.application.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.UnknownHostException;


public class NetworkPlayer extends Player implements UserStateListener {
    private final ChessAPI api;
    private final String myUUID;
    private final String opponentUUID;
    private final UpdateUserStateListener updateUserStateListener = this::onReceiveUserStateFromNetwork;
    private final UserStateUpdateChecker userStateUpdateChecker;

    public NetworkPlayer(ChessGame chessGame, Team team, String apiBaseURL, String myUUID, String opponentUUID) {
        super(chessGame, team);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ChessAPI.class);
        this.myUUID = myUUID;
        this.opponentUUID = opponentUUID;
        userStateUpdateChecker = new UserStateUpdateChecker(myUUID, api, updateUserStateListener);
    }

    @Override
    public void start() {
        getChessGame().addListener(this);
        if (getTeam().isSameTeam(getChessGame().getBoardState().currentTeam)) {
            userStateUpdateChecker.start();
        }
    }

    @Override
    public void stop() {
        getChessGame().removeListener(this);
        userStateUpdateChecker.pause();
    }

    //receives UserState from LocalPlayer, sends UserState towards other player through api
    @Override
    public void onReceiveUserState(UserState userState, Team team) {
        if (team.isSameTeam(this.getTeam())) {
            return;
        }
        api.updateUserState(new UpdateUserStateRequest(opponentUUID, userState)).enqueue(new EmptyCallback());
        if (getTeam().isSameTeam(getChessGame().getBoardState().currentTeam)) {
            userStateUpdateChecker.start();
        }

    }

    private void onReceiveUserStateFromNetwork(UserState userState) {
        Platform.runLater(() -> {
            getChessGame().dispatchUserState(userState);
            if (!getTeam().isSameTeam(getChessGame().getBoardState().currentTeam)) {
                userStateUpdateChecker.pause();
            }
        });
    }
}
