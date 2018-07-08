package api;

import game.BoardState;
import game.Team;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ChessAPI {
    @POST("register")
    Call<String> registerNewUser (@Body NewUser newUser);

    @POST("get_user")
    Call<User> getUser (@Body GetUserRequest getUserRequest);
}
