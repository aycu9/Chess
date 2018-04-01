package game.player;

import game.ChessGame;
import game.Team;

/**
 * Created by Libra on 2018-03-31.
 */
public abstract class Player {
    private final ChessGame chessGame;
    private final Team team;

    public Player(ChessGame chessGame, Team team) {
        this.chessGame = chessGame;
        this.team = team;
    }

    public abstract void start();

    public abstract void stop();

    public ChessGame getChessGame() {
        return chessGame;
    }

    public Team getTeam() {
        return team;
    }
}

