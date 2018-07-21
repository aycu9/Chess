package api;

/**
 * Created by Libra on 2018-06-23.
 */
public class HostGameRequest {
    public String uuid;
    public Integer team;

    public HostGameRequest (String uuid, Integer team){
        this.uuid = uuid;
        this.team = team;
    }
}
