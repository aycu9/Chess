package api;

/**
 * Created by Libra on 2018-06-30.
 */
public class StartGameRequest {
    public String hostUuid;
    public String otherUuid;

    public StartGameRequest (String hostUuid, String otherUuid){
        this.hostUuid = hostUuid;
        this.otherUuid = otherUuid;
    }
}
