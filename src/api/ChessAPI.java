package api;

import game.BoardState;
import game.Team;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Libra on 2018-03-24.
 */
public interface ChessAPI {
    String CONNECT_PATH = "connect";
    String USERSTATE_PATH = "sendUserState";

    @POST(CONNECT_PATH)
    Call<Void> connectToHost(@Body ConnectionRequest request);

    @POST(USERSTATE_PATH)
    Call<Void> sendUserState(@Body UserState state);
}
