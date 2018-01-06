package ui.pieces;

import game.Location;
import game.Move;
import game.Team;
import javafx.scene.paint.Color;
import ui.ChessBoard;
import ui.ChessPiece;
import ui.GridSquare;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017-10-28.
 */
public class Pawn extends ChessPiece {

    public Pawn(Color pieceColor, Team team, int size) {
        super(pieceColor, team, size);
    }

    @Override
    public String getName() {
        return "P";
    }

    @Override
    public List<GridSquare> getLegalMoves(ChessBoard chessBoard) {
        List<GridSquare> moves;
        moves = new ArrayList<>();
        Location currentLocation = chessBoard.getPiecePosition(this);
        int currentColumn = currentLocation.getColumn();
        int currentRow = currentLocation.getRow();
        int direction = chessBoard.getForwardDirection(this.getTeam());
        boolean isPieceMoved = false;

        for (Move pastMove : chessBoard.getCurrentState().pastMoves) {
            if (pastMove.getMovedPiece() == this) {
                isPieceMoved = true;
                break;
            }
        }

        GridSquare squareInFront1 = chessBoard.getGridSquare(currentColumn, currentRow + direction);
        if(squareInFront1 != null && !squareInFront1.hasChessPiece()) {
            moves.add(squareInFront1);
            GridSquare squareInFront2 = chessBoard.getGridSquare(currentColumn, currentRow + direction * 2);
            if (squareInFront2 != null && !isPieceMoved && !squareInFront2.hasChessPiece()) {
                moves.add(squareInFront2);
            }
        }




        if (diagonalOccupationStatus(chessBoard).equals("Both")) {
            moves.add(chessBoard.getGridSquare(currentColumn + 1, currentRow + direction));
            moves.add(chessBoard.getGridSquare(currentColumn - 1, currentRow + direction));
        } else if (diagonalOccupationStatus(chessBoard).equals("Left")) {
            moves.add(chessBoard.getGridSquare(currentColumn - 1, currentRow + direction));
        } else if (diagonalOccupationStatus(chessBoard).equals("Right")) {
            moves.add(chessBoard.getGridSquare(currentColumn + 1, currentRow + direction));

        }
        return moves;
    }


    public boolean thereIsEnemyDiagonally(Location currentLocation, ChessBoard chessBoard, int diagonalColumn) {
        int direction = chessBoard.getForwardDirection(this.getTeam());
        int column = currentLocation.getColumn() + diagonalColumn;
        int row = currentLocation.getRow() + direction;
        GridSquare columnGridSquare = chessBoard.getGridSquare(column, row);
        if (columnGridSquare != null) {
            if (columnGridSquare.hasChessPiece() && !columnGridSquare.getChessPiece().getTeam().isSameTeam(this.getTeam())) {
                return true;
            }
            return false;
        }
        return false;
    }


    public String diagonalOccupationStatus(ChessBoard chessBoard) {
        Location currentLocation = chessBoard.getPiecePosition(this);
        if (thereIsEnemyDiagonally(currentLocation, chessBoard, -1) && thereIsEnemyDiagonally(currentLocation, chessBoard, 1)) {
            return "Both";
        } else if (thereIsEnemyDiagonally(currentLocation, chessBoard, -1) && !thereIsEnemyDiagonally(currentLocation, chessBoard, 1)) {
            return "Left";
        } else if (!thereIsEnemyDiagonally(currentLocation, chessBoard, -1) && thereIsEnemyDiagonally(currentLocation, chessBoard, 1)) {
            return "Right";
        } else
            return "None";
    }
}
