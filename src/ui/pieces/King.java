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
 * Created by Libra on 2018-01-13.
 */
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

        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                legalMovesAdder(chessBoard.getGridSquare(currentColumn + x, currentRow + y), moves);
            }
        }
//        this.addCastlingMoveIfLegal(0, chessBoard, moves);
//        this.addCastlingMoveIfLegal(7, chessBoard, moves);
        return moves;
    }

//    private void addCastlingMoveIfLegal(int rookColumn, ChessBoard chessBoard, List<GridSquare> legalMoves) {
//
//        Location currentLocation = chessBoard.getPiecePosition(this);
//        int currentColumn = currentLocation.getColumn();
//        int currentRow = currentLocation.getRow();
//
//        List<GridSquare> spaceBetween = new ArrayList<GridSquare>();
//
//        if (rookColumn == 0) {
//            spaceBetween.add(chessBoard.getGridSquare(rookColumn + 1, currentRow));
//            spaceBetween.add(chessBoard.getGridSquare(rookColumn + 2, currentRow));
//            spaceBetween.add(chessBoard.getGridSquare(rookColumn + 3, currentRow));
//            spaceBetween.add(chessBoard.getGridSquare(currentColumn, currentRow));//king
//        } else {
//            spaceBetween.add(chessBoard.getGridSquare(rookColumn - 1, currentRow));
//            spaceBetween.add(chessBoard.getGridSquare(rookColumn - 2, currentRow));
//            spaceBetween.add(chessBoard.getGridSquare(currentColumn, currentRow));//king
//        }
//
//        ChessPiece rookPiece = chessBoard.getGridSquare(rookColumn, currentRow).getChessPiece();
//
//        if (rookPiece instanceof Rook) {  //returns boolean after comparing
//            if (!rookPiece.hasPieceMoved(chessBoard) && !this.hasPieceMoved(chessBoard)) {//if rook hasn't moved
//                for (int i = 0; i < spaceBetween.size(); i++) {
//                    if (!spaceBetween.get(i).hasChessPiece()) //if empty in between
//                        if (chessBoard.isSquareThreatened(spaceBetween.get(i))) {//if space in between not threatened
//                            legalMoves.add(chessBoard.getGridSquare(rookColumn, currentRow));
//                        }
//                }
//            }
//        }
//    }
}
