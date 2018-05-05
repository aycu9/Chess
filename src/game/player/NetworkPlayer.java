package game.player;

import api.ChessAPI;
import api.ChessAPIServer;
import api.ConnectionRequest;
import api.UserState;
import com.sun.net.httpserver.HttpServer;
import game.ChessGame;
import game.Team;
import game.UserStateListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkPlayer extends Player implements UserStateListener, ChessAPIServer.Delegate {
    private ChessAPI api;
    private final ChessAPIServer server;
    private static final int PORT = 8000;

    //client that connects to a host
    public NetworkPlayer(ChessGame chessGame, Team team, String hostIPAddress) {
        this(chessGame, team);
        connectToNetworkPlayer(hostIPAddress);
    }

    //hosting
    public NetworkPlayer(ChessGame chessGame, Team team) {
        super(chessGame, team);
        server = new ChessAPIServer(PORT);
    }

    @Override
    public void start() {
        getChessGame().addListener(this);
        server.setDelegate(this);
        try {
            server.start();
            System.out.println("My IP Address = " + server.getIPAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        getChessGame().removeListener(this);
        server.stop();
        server.setDelegate(null);
    }

    //receives UserState from LocalPlayer, sends UserState towards other player through api
    @Override
    public void onReceiveUserState(UserState userState, Team team) {
        if (team.isSameTeam(this.getTeam())) {
            return;
        }
        api.sendUserState(userState).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public void receiveConnectionRequest(ConnectionRequest request) {
        connectToNetworkPlayer(request.ipAddress);
        System.out.println("Other Player's IP Address = " + request.ipAddress);
    }

    @Override
    public void receiveUserStateFromNetwork(UserState state) {
        getChessGame().dispatchUserState(state);
    }

    private void connectToNetworkPlayer(String ipAddress) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + ":" + PORT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ChessAPI.class);
    }
}
