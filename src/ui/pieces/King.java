package ui.pieces;

import game.Location;
import game.Team;
import javafx.scene.paint.Color;
import ui.ChessBoard;
import ui.ChessPiece;
import ui.GridSquare;

import java.util.ArrayList;
import java.util.List;


public class King extends ChessPiece {
    public King(Color pieceColor, Team team, int size) {
        super(pieceColor, team, size);
    }

    @Override
    public String getName() {
        return "K";
    }

    @Override
    public List<GridSquare> getLegalMoves(ChessBoard chessBoard) {
        List<GridSquare> moves;
        moves = new ArrayList<>();
        Location currentLocation = chessBoard.getPiecePosition(this);
        int currentColumn = currentLocation.getColumn();
        int currentRow = currentLocation.getRow();


        moves.addAll(getThreateningSquares(chessBoard));


        if (!this.hasPieceMoved(chessBoard)) { //if king has not moved
            if (!chessBoard.isSquareThreatened(chessBoard.getGridSquare(currentColumn, currentRow))) { //if king is not threatened
                this.addCastlingMoveIfLegal(0, chessBoard, moves);
                this.addCastlingMoveIfLegal(7, chessBoard, moves);
            }
        }

        return moves;
    }

    @Override
    public List<GridSquare> getThreateningSquares(ChessBoard chessBoard) {
        Location currentLocation = chessBoard.getPiecePosition(this);
        int currentColumn = currentLocation.getColumn();
        int currentRow = currentLocation.getRow();

        List<GridSquare> squares = new ArrayList<>();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                legalMovesAdder(chessBoard.getGridSquare(currentColumn + x, currentRow + y), squares);
            }
        }
        return squares;
    }

    private void addCastlingMoveIfLegal(int rookColumn, ChessBoard chessBoard, List<GridSquare> legalMoves) {

        Location currentLocation = chessBoard.getPiecePosition(this);
        int currentRow = currentLocation.getRow();

        List<GridSquare> spaceBetween = new ArrayList<GridSquare>();

        if (rookColumn == 0) {
            spaceBetween.add(chessBoard.getGridSquare(rookColumn + 1, currentRow));
            spaceBetween.add(chessBoard.getGridSquare(rookColumn + 2, currentRow));
            spaceBetween.add(chessBoard.getGridSquare(rookColumn + 3, currentRow));
        }
        if (rookColumn == 7) {
            spaceBetween.add(chessBoard.getGridSquare(rookColumn - 1, currentRow));
            spaceBetween.add(chessBoard.getGridSquare(rookColumn - 2, currentRow));
        }

        ChessPiece rookPiece = chessBoard.getGridSquare(rookColumn, currentRow).getChessPiece();

        if (rookPiece instanceof Rook) {  //returns boolean after comparing (if checked piece is a Rook)
            if (!rookPiece.hasPieceMoved(chessBoard)) {//if Rook hasn't moved

                boolean isOccupied = false;
                for (GridSquare gridSquare : spaceBetween) {
                    if (chessBoard.isSquareThreatened(gridSquare)) {//if space in between not threatened
                        isOccupied = true;
                        break;
                    }
                    if (gridSquare.hasChessPiece()) {
                        isOccupied = true;
                        break;
                    }
                }
                if(isOccupied == false){
                    legalMoves.add(chessBoard.getGridSquare(rookColumn, currentRow));
                }
            }
        }
    }
}
