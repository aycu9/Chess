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
        Location currentLocation = chessBoard.getPiecePosition(this);
        int currentColumn = currentLocation.getColumn();
        int currentRow = currentLocation.getRow();
        int direction = chessBoard.getForwardDirection(this.getTeam());
        boolean isPawnMoved = this.hasPieceMoved(chessBoard);


        GridSquare squareInFront1 = chessBoard.getGridSquare(currentColumn, currentRow + direction);
        if(squareInFront1 != null && !squareInFront1.hasChessPiece()) {
            moves.add(squareInFront1);
            GridSquare squareInFront2 = chessBoard.getGridSquare(currentColumn, currentRow + direction * 2);
            if (squareInFront2 != null && !isPawnMoved && !squareInFront2.hasChessPiece()) {
                moves.add(squareInFront2);
            }
        }


        for (GridSquare gridSquare : getThreateningSquares(chessBoard)) {
            if(gridSquare.hasChessPiece()){
                if (!gridSquare.getChessPiece().getTeam().isSameTeam(this.getTeam())){
                    moves.add(gridSquare);
                }
            }
        }

        return moves;
    }

    @Override
    public List<GridSquare> getThreateningSquares(ChessBoard chessBoard) {
        List<GridSquare> squares;
        squares = new ArrayList<>();
        Location currentLocation = chessBoard.getPiecePosition(this);
        int currentColumn = currentLocation.getColumn();
        int currentRow = currentLocation.getRow();
        int direction = chessBoard.getForwardDirection(this.getTeam());

        GridSquare leftThreat = chessBoard.getGridSquare(currentColumn - 1, currentRow + direction);
        GridSquare rightThreat = chessBoard.getGridSquare(currentColumn + 1, currentRow + direction);
        if(leftThreat != null) {
            squares.add(leftThreat);
        }
        if(rightThreat != null){
            squares.add(rightThreat);
        }

        return squares;
    }
}
