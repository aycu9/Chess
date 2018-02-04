package game;

import javafx.scene.paint.Color;


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
