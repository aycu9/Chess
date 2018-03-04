package ui.pieces;

import game.Location;
import game.Team;
import javafx.scene.paint.Color;
import ui.ChessBoard;
import ui.ChessPiece;
import ui.GridSquare;

import java.util.ArrayList;
import java.util.List;


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
        Location currentLocation = chessBoard.getPieceLocation(this);
        int currentColumn = currentLocation.getColumn();
        int currentRow = currentLocation.getRow();
        int direction = chessBoard.getForwardDirection(this.getTeam());
        boolean isPawnMoved = this.hasPieceMoved(chessBoard);


        int sqr1Column = currentColumn;
        int sqr1Row = currentRow + direction;
        int sqr2Column = currentColumn;
        int sqr2Row = currentRow + direction * 2;
        GridSquare squareInFront1 = chessBoard.getGridSquare(sqr1Column, sqr1Row);
        GridSquare squareInFront2 = chessBoard.getGridSquare(sqr2Column, sqr2Row);

        if (squareInFront1 != null && !squareInFront1.hasChessPiece()) {// if able to move forward 1
            if (!this.moveResultsInCheck(chessBoard, sqr1Column, sqr1Row)) {//if front 1 doesn't result in check
                moves.add(squareInFront1);
            }
            if (squareInFront2 != null && !isPawnMoved && !squareInFront2.hasChessPiece()
                    && !this.moveResultsInCheck(chessBoard, sqr2Column, sqr2Row)) {
                moves.add(squareInFront2);
            }
        }


        for (GridSquare move : getThreateningSquares(chessBoard)) {
            Location moveLocation = chessBoard.getGridSquareLocation(move);
            int moveColumn = moveLocation.getColumn();
            int moveRow = moveLocation.getRow();
            if (move.hasChessPiece()) {
                if (!move.getChessPiece().getTeam().isSameTeam(this.getTeam())
                        && !this.moveResultsInCheck(chessBoard, moveColumn, moveRow)) {
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    @Override
    public List<GridSquare> getThreateningSquares(ChessBoard chessBoard) {
        List<GridSquare> squares;
        squares = new ArrayList<>();
        Location currentLocation = chessBoard.getPieceLocation(this);
        int currentColumn = currentLocation.getColumn();
        int currentRow = currentLocation.getRow();
        int direction = chessBoard.getForwardDirection(this.getTeam());

        GridSquare leftThreat = chessBoard.getGridSquare(currentColumn - 1, currentRow + direction);
        GridSquare rightThreat = chessBoard.getGridSquare(currentColumn + 1, currentRow + direction);
        if (leftThreat != null) {
            squares.add(leftThreat);
        }
        if (rightThreat != null) {
            squares.add(rightThreat);
        }

        return squares;
    }
}
