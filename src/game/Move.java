package game;

import ui.ChessPiece;
import ui.GridSquare;


public class Move {
    private final GridSquare startingSquare;
    private final GridSquare destinationSquare;
    private final ChessPiece movedPiece;

    public Move(GridSquare startingSquare, GridSquare destinationSquare) {
        this.startingSquare = startingSquare;
        this.destinationSquare = destinationSquare;
        this.movedPiece = startingSquare.getChessPiece();
    }

    public GridSquare getStartingSquare() {
        return startingSquare;
    }

    public GridSquare getDestinationSquare() {
        return destinationSquare;
    }

    public ChessPiece getMovedPiece() {
        return movedPiece;
    }
}
