package game.player;

import api.UserState;

/**
 * Created by Libra on 2018-08-04.
 */
public interface UpdateUserStateListener {
    void onReceiveUserState(UserState userState);
}
