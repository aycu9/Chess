package api;

import game.BoardState;
import game.Team;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Libra on 2018-03-24.
 */
public interface ChessAPI {
    @POST("connect")
    Call<Team> connectToHost(@Body ConnectionRequest request);

    @POST("sendUserState")
    Call sendUserState(@Body UserState state);

}
