package game.lobby;

import api.User;

/**
 * Created by Libra on 2018-07-28.
 */
public interface StartGameCallback {
    void  onGameStart (User user);
}
