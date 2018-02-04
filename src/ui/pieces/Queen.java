package ui.pieces;

import game.Location;
import game.Team;
import javafx.scene.paint.Color;
import ui.ChessBoard;
import ui.ChessPiece;
import ui.GridSquare;

import java.util.ArrayList;
import java.util.List;


public class Queen extends ChessPiece {
    public Queen(Color pieceColor, Team team, int size) {
        super(pieceColor, team, size);
    }

    @Override
    public String getName() {
        return "Q";
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

        moveCheck(currentLocation, chessBoard, squares, 0, 1);
        moveCheck(currentLocation, chessBoard, squares, 0, -1);
        moveCheck(currentLocation, chessBoard, squares, 1, 0);
        moveCheck(currentLocation, chessBoard, squares, -1, 0);
        moveCheck(currentLocation, chessBoard, squares, 1, 1);
        moveCheck(currentLocation, chessBoard, squares, 1, -1);
        moveCheck(currentLocation, chessBoard, squares, -1, 1);
        moveCheck(currentLocation, chessBoard, squares, -1, -1);

        return squares;
    }

    private void moveCheck(Location currentLocation, ChessBoard chessBoard, List<GridSquare> moves, int columnChange, int rowChange) {
        int column = currentLocation.getColumn();
        int row = currentLocation.getRow();

        while (true) {
            row = row + rowChange;
            column = column + columnChange;
            GridSquare current = chessBoard.getGridSquare(column, row);
            if (current != null && !current.hasChessPiece()) {
                moves.add(current);
            } else if (current != null && current.hasChessPiece() && !current.getChessPiece().getTeam().isSameTeam(this.getTeam())) {
                moves.add(current);
                break;
            } else {
                break;
            }
        }
    }
}