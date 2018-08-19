package api;

/**
 * Created by Libra on 2018-06-23.
 */
public class Host {
    public String uuid;
    public String name;
    public Team team;
    public Host (User user){
        uuid = user.getUuid();
        name = user.getName();
        team = user.getTeam();
    }
}
