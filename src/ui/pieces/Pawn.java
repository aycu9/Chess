package ui.pieces;

import game.Team;
import javafx.scene.paint.Color;
import ui.ChessPiece;

/**
 * Created by Libra on 2017-10-28.
 */
public class Pawn extends ChessPiece{

    public Pawn(Color pieceColor, Team team, int size) {
        super(pieceColor, team, size);
    }

    @Override
    public String getName() {
        return "P";
    }
}
