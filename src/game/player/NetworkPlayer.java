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

    public NetworkPlayer(ChessGame chessGame, Team team, String apiBaseURL, String myUUID, String opponentUUID) {
        super(chessGame, team);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ChessAPI.class);
        this.myUUID = myUUID;
        this.opponentUUID = opponentUUID;
    }

    @Override
    public void start() {
        getChessGame().addListener(this);
    }

    @Override
    public void stop() {
        getChessGame().removeListener(this);
    }

    //receives UserState from LocalPlayer, sends UserState towards other player through api
    @Override
    public void onReceiveUserState(UserState userState, Team team) {
        if (team.isSameTeam(this.getTeam())) {
            return;
        }
        api.updateUserState(new UpdateUserStateRequest(opponentUUID, userState)).enqueue(new EmptyCallback());
    }

    public void receiveUserStateFromNetwork(UserState state) {
        Platform.runLater(() -> getChessGame().dispatchUserState(state));
    }
}
