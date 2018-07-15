package api;

/**
 * Created by Libra on 2018-05-26.
 */
public class Team {
    private final int teamNumber;

    public Team(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public Team getOppositeTeam() {
        if (teamNumber == 1) {
            return new Team(2);
        }
        return new Team(1);
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamNumber=" + teamNumber +
                '}';
    }

    public String getName() {
        return teamNumber == 1 ? "White" : "Black";
    }
}
