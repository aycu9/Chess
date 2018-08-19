package api;

/**
 * Created by Libra on 2018-08-04.
 */
public class UpdateUserStateRequest {
    String uuid;
    UserState userState;
    public UpdateUserStateRequest(String uuid, UserState userState){
        this.uuid = uuid;
        this.userState = userState;
    }
}
