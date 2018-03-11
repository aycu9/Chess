package game;

import javafx.scene.paint.Color;


public class Team {
    private final Color teamColor;
    private final String teamName;

    public Team(Color teamColor, String teamName) {
        this.teamColor = teamColor;
        this.teamName = teamName;
    }

    public boolean isSameTeam (Team otherTeam){
        return this.teamColor.equals(otherTeam.teamColor);
    }

    public Color getTeamColor() {
        return teamColor;
    }

    public String getTeamName() {
        return teamName;
    }
}
