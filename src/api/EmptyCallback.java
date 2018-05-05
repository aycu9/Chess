package api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Libra on 2018-05-05.
 */
public class EmptyCallback implements Callback<Void> {
    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {

    }

    @Override
    public void onFailure(Call<Void> call, Throwable throwable) {

    }
}
