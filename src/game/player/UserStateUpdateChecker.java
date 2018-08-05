package game.player;

import api.ChessAPI;
import api.GetUserRequest;
import api.User;
import api.UserState;
import game.lobby.StartGameCallback;
import javafx.application.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Objects;

/**
 * Created by Libra on 2018-07-21.
 */
public class UserStateUpdateChecker {
    private final String uuid;
    private UserState userState;
    private final ChessAPI api;
    private final UpdateUserStateListener updateUserStateListener;
    private boolean hasStarted = false;
    private final Callback<User> callback = new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if (!Objects.equals(response.body().getUserState(), userState)) {
                updateUserStateListener.onReceiveUserState(response.body().getUserState());
            }
            userState = response.body().getUserState();
            scheduleNextCall();
        }

        @Override
        public void onFailure(Call<User> call, Throwable throwable) {
            throwable.printStackTrace();
            scheduleNextCall();
        }
    };

    public UserStateUpdateChecker(String uuid, ChessAPI api, UpdateUserStateListener updateUserStateListener) {
        this.uuid = uuid;
        this.api = api;
        this.updateUserStateListener = updateUserStateListener;
    }

    private void scheduleNextCall() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        api.getUser(new GetUserRequest(uuid)).enqueue(callback);
    }

    public void start() {
        if (!hasStarted) {
            api.getUser(new GetUserRequest(uuid)).enqueue(callback);
        }
        hasStarted = true;
    }
}
