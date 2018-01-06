package ui.pieces;

import game.Team;
import javafx.scene.paint.Color;
import ui.ChessBoard;
import ui.ChessPiece;
import ui.GridSquare;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2018-01-06.
 */
public class Knight extends ChessPiece{
    public Knight(Color pieceColor, Team team, int size) {
        super(pieceColor, team, size);
    }

    @Override
    public String getName() {
        return "Kn";
    }

    @Override
    public List<GridSquare> getLegalMoves(ChessBoard chessBoard) {
        List <GridSquare> legalMoves = new ArrayList<>();
        return legalMoves;
    }


}
