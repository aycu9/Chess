package game;

import javafx.scene.paint.Color;

/**
 * Created by Libra on 2017-10-14.
 */
public class Team {
    private final Color teamColor;

    public Team(Color teamColor) {
        this.teamColor = teamColor;
    }

    public boolean isSameTeam (Team otherTeam){
        return this.teamColor.equals(otherTeam.teamColor);
    }

    public Color getTeamColor() {
        return teamColor;
    }
}
