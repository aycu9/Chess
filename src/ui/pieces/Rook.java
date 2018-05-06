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
    public static final String NAME = "R";

    public Rook(Color pieceColor, Team team, int size) {
        super(pieceColor, team, size);
    }

    public Rook(ChessPiece chessPiece) {
        super(chessPiece);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<GridSquare> getLegalMoves(ChessBoard chessBoard) {
        List<GridSquare> moves;
        moves = new ArrayList<>();

        for (GridSquare move : getThreateningSquares(chessBoard)) {
            Location moveLocation = chessBoard.getGridSquareLocation(move);
            int moveColumn = moveLocation.getColumn();
            int moveRow = moveLocation.getRow();

            if (!this.moveResultsInCheck(chessBoard, moveColumn, moveRow)) {
                moves.add(move);
            }
        }

        return moves;
    }

    @Override
    public List<GridSquare> getThreateningSquares(ChessBoard chessBoard) {
        List<GridSquare> squares;
        squares = new ArrayList<>();

        Location currentLocation = chessBoard.getPieceLocation(this);

        linearMoveAdder(currentLocation, chessBoard, squares, 0, 1);
        linearMoveAdder(currentLocation, chessBoard, squares, 0, -1);
        linearMoveAdder(currentLocation, chessBoard, squares, 1, 0);
        linearMoveAdder(currentLocation, chessBoard, squares, -1, 0);

        return squares;
    }
}
