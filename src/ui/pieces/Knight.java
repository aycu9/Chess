package ui.pieces;

import game.Location;
import game.Team;
import javafx.scene.paint.Color;
import ui.ChessBoard;
import ui.ChessPiece;
import ui.GridSquare;

import java.util.ArrayList;
import java.util.List;


public class Knight extends ChessPiece {
    public Knight(Color pieceColor, Team team, int size) {
        super(pieceColor, team, size);
    }

    @Override
    public String getName() {
        return "Kn";
    }

    @Override
    public List<GridSquare> getLegalMoves(ChessBoard chessBoard) {

        List<GridSquare> moves = new ArrayList<>();

        moves.addAll(getThreateningSquares(chessBoard)); //check

        return moves;
    }

    @Override
    public List<GridSquare> getThreateningSquares(ChessBoard chessBoard) {
        List<GridSquare> squares;
        squares = new ArrayList<>();
        Location currentLocation = chessBoard.getPiecePosition(this);
        int currentColumn = currentLocation.getColumn();
        int currentRow = currentLocation.getRow();


        GridSquare[] possibleMoves = new GridSquare[]{
                chessBoard.getGridSquare(currentColumn + 1, currentRow + 2),
                chessBoard.getGridSquare(currentColumn - 1, currentRow + 2),
                chessBoard.getGridSquare(currentColumn + 2, currentRow + 1),
                chessBoard.getGridSquare(currentColumn + 2, currentRow - 1),
                chessBoard.getGridSquare(currentColumn - 2, currentRow + 1),
                chessBoard.getGridSquare(currentColumn - 2, currentRow - 1),
                chessBoard.getGridSquare(currentColumn + 1, currentRow - 2),
                chessBoard.getGridSquare(currentColumn - 1, currentRow - 2)
        };

        for (int i = 0; i < possibleMoves.length; i++) {
            legalMovesAdder(possibleMoves[i], squares);
        }
        return squares;
    }
}
