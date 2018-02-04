package ui.pieces;

import game.Location;
import game.Team;
import javafx.scene.paint.Color;
import ui.ChessBoard;
import ui.ChessPiece;
import ui.GridSquare;

import java.util.ArrayList;
import java.util.List;


public class Bishop extends ChessPiece {
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

        moves.addAll(getThreateningSquares(chessBoard));

            return moves;
        }

    @Override
    public List<GridSquare> getThreateningSquares(ChessBoard chessBoard) {
        List<GridSquare> squares = new ArrayList<>();
        Location currentLocation = chessBoard.getPiecePosition(this);

        moveCheck(currentLocation, chessBoard, squares, 1, 1);
        moveCheck(currentLocation, chessBoard, squares, 1, -1);
        moveCheck(currentLocation, chessBoard, squares, -1, 1);
        moveCheck(currentLocation, chessBoard, squares, -1, -1);

        return squares;
    }

    private void moveCheck(Location currentLocation, ChessBoard chessBoard, List<GridSquare> moves, int columnChange, int rowChange) {
        int currentColumn = currentLocation.getColumn();
        int currentRow = currentLocation.getRow();

        while (true) {
            currentRow = currentRow + rowChange;
            currentColumn = currentColumn + columnChange;
            GridSquare current = chessBoard.getGridSquare(currentColumn, currentRow);
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

