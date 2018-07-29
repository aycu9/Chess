package game.lobby;

import api.ChessAPI;
import api.GetUserRequest;
import api.User;
import javafx.application.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by Libra on 2018-07-21.
 */
public class GameStartChecker {
    private User user;
    private final ChessAPI api;
    private final StartGameCallback startGameCallback;
    private boolean hasStarted = false;
    private final Callback<User> callback = new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            user = response.body();
            System.out.println(user.toString());
            if (isGameStarted()) {
                Platform.runLater(() -> startGameCallback.onGameStart(user));
            } else {
                scheduleNextCall();
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable throwable) {
            throwable.printStackTrace();
            scheduleNextCall();
        }
    };

    public GameStartChecker(User user, ChessAPI api, StartGameCallback startGameCallback) {
        this.user = user;
        this.api = api;
        this.startGameCallback = startGameCallback;
    }

    public boolean isGameStarted() {
        return user.getOpponent() != null;
    }

    private void scheduleNextCall() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        api.getUser(new GetUserRequest(user.getUuid())).enqueue(callback);
    }

    public void start() {
        if (!hasStarted) {
            api.getUser(new GetUserRequest(user.getUuid())).enqueue(callback);
        }
        hasStarted = true;
    }
}
