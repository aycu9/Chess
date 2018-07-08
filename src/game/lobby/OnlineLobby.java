package game.lobby;

import api.ChessAPI;
import api.Host;
import api.NewUser;
import api.User;
import javafx.scene.control.TextInputDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Created by Libra on 2018-06-30.
 */
public class OnlineLobby{
    private final Retrofit retrofit;
    private final ChessAPI api;
    private final List<Host> hostList = new ArrayList<>();
    private User user;
    private final Callback<String> registerCallback = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            String uuid = response.body();
            System.out.println(uuid);
        }

        @Override
        public void onFailure(Call<String> call, Throwable throwable) {
            throwable.printStackTrace();
        }
    };

    public OnlineLobby (String apiBaseURL){
        retrofit = new Retrofit.Builder()
                .baseUrl(apiBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ChessAPI.class);
    }

    public void launchLobby (){
        NewUser newUser = new NewUser(askUserForNickname());
        Call<String> call = api.registerNewUser(newUser);
        call.enqueue(registerCallback);

    }

    private String askUserForNickname() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Enter Nickname");
        dialog.setHeaderText("Enter the nickname you wish to use as a Chess game player");
        dialog.setContentText("Nickname: ");
        Optional<String> address = dialog.showAndWait();
        return address.get();
    }



}
