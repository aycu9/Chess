package ui.pieces;

import game.Location;
import game.Team;
import javafx.scene.paint.Color;
import ui.ChessBoard;
import ui.ChessPiece;
import ui.GridSquare;

import java.util.ArrayList;
import java.util.List;


public class Rook extends ChessPiece {
    public Rook(Color pieceColor, Team team, int size) {
        super(pieceColor, team, size);
    }

    @Override
    public String getName() {
        return "R";
    }

    @Override
    public List<GridSquare> getLegalMoves(ChessBoard chessBoard) {
        List<GridSquare> moves;
        moves = new ArrayList<>();

        moves.addAll(getThreateningSquares(chessBoard));

        return moves;
    }

    @Override
    public List<GridSquare> getThreateningSquares(ChessBoard chessBoard) {
        List<GridSquare> squares;
        squares = new ArrayList<>();

        Location currentLocation = chessBoard.getPiecePosition(this);

        linearMoveAdder(currentLocation, chessBoard, squares, 0, 1);
        linearMoveAdder(currentLocation, chessBoard, squares, 0, -1);
        linearMoveAdder(currentLocation, chessBoard, squares, 1, 0);
        linearMoveAdder(currentLocation, chessBoard, squares, -1, 0);

        return squares;
    }
}
