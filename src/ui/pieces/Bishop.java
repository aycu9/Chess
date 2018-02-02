package ui.pieces;

import game.Location;
import game.Team;
import javafx.scene.paint.Color;
import ui.ChessBoard;
import ui.ChessPiece;
import ui.GridSquare;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2018-02-01.
 */
public class Bishop extends ChessPiece { //Unfinished
    public Bishop(Color pieceColor, Team team, int size) {
        super(pieceColor, team, size);
    }

    @Override
    public String getName() {
        return "B";
    }

    @Override
    public List<GridSquare> getLegalMoves(ChessBoard chessBoard) {
        List<GridSquare> moves;
        moves = new ArrayList<>();
        Location currentLocation = chessBoard.getPiecePosition(this);
        int currentColumn = currentLocation.getColumn();
        int currentRow = currentLocation.getRow();

            return moves;
        }
    }

