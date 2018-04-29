package game;

import api.UserState;

/**
 * Created by Libra on 2018-04-14.
 */
public interface UserStateListener {
    /**
     * Notifies when a new UserState has been dispatched.
     * @param userState the new UserState
     * @param team the team that created the new UserState
     */
    void onReceiveUserState (UserState userState, Team team);
}
