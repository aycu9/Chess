package game.lobby;

import api.ChessAPI;
import api.Host;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2018-07-14.
 */
public class HostList {
    private final List<Host> hostList = new ArrayList<>();
    private final ObservableList<String> data = FXCollections.observableArrayList();
    private final ListView<String> listView = new ListView<>(data);
    private final ChessAPI api;
    private final Callback<List<Host>> getHostListCallback = new Callback<List<Host>>() {
        @Override
        public void onResponse(Call<List<Host>> call, Response<List<Host>> response) {
            hostList.clear();
            hostList.addAll(response.body());
            data.clear();
            for (int i = 0; i < hostList.size(); i++) {
                data.add("Name: " + hostList.get(i).name + "          " + "Team: " + hostList.get(i).team.getName());
            }
        }

        @Override
        public void onFailure(Call<List<Host>> call, Throwable throwable) {
            throwable.printStackTrace();
        }
    };

    public HostList(int listWidth, ChessAPI api) {
        this.api = api;
        listView.setPrefSize(listWidth, 200);
        listView.setEditable(true);
        updateHostList();
    }


    public Node getNode() {
        return listView;
    }

    public void updateHostList() {
        api.getHostList().enqueue(getHostListCallback);
    }

}
