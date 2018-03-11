package ui;

import game.Location;
import game.Move;
import game.Team;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;


public abstract class ChessPiece implements Drawable {
    private final Color pieceColor;
    private final Color outlineColor;
    private final Team team;
    private final int size;
    private final Font font;

    public ChessPiece(Color pieceColor, Team team, int size) {
        this.pieceColor = pieceColor;
        this.outlineColor = Color.DARKSLATEBLUE;
        this.team = team;
        this.size = size;
        this.font = Font.font(null, FontWeight.BOLD, size / 2);
    }
    //clone
    public ChessPiece(ChessPiece chessPiece){
        this(chessPiece.pieceColor, chessPiece.team, chessPiece.size);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(pieceColor);
        gc.setStroke(outlineColor);
        gc.setLineWidth(3);
        gc.setFont(font);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.fillText(getName(), size / 2, size / 2);
        gc.strokeText(getName(),size / 2, size / 2 );
    }

    public abstract String getName();

    @Override
    public int getWidth() {
        return size;
    }

    @Override
    public int getHeight() {
        return size;
    }

    public Team getTeam() {
        return team;
    }

    public abstract List<GridSquare> getLegalMoves (ChessBoard chessBoard);

    public abstract List<GridSquare> getThreateningSquares (ChessBoard chessBoard);

    protected void legalMovesAdder(GridSquare gridSquare, List<GridSquare> moves) {
        if (gridSquare != null && !gridSquare.hasChessPiece()) { //Not null and Not occupied by a chess piece
            moves.add(gridSquare);
        } else if (gridSquare != null && gridSquare.hasChessPiece() && !gridSquare.getChessPiece().getTeam().isSameTeam(this.getTeam())) { //Not null and enemy
            moves.add(gridSquare);
        }
    }

    public boolean hasPieceMoved (ChessBoard chessBoard) {
        List<Move> pastMoves = chessBoard.getCurrentState().pastMoves;
        for (Move pastMove : pastMoves) {
            if (pastMove.getMovedPiece() == this) {
                return true;
            }
        }
        return false;
    }

    protected void linearMoveAdder (Location currentLocation, ChessBoard chessBoard, List<GridSquare> moves, int columnChange, int rowChange){
        int column = currentLocation.getColumn();
        int row = currentLocation.getRow();

        while (true) {
            row = row + rowChange;
            column = column + columnChange;
            GridSquare current = chessBoard.getGridSquare(column, row);
            if (current != null && !current.hasChessPiece()) { // does not have a piece on it
                moves.add(current);
            } else if (current != null && current.hasChessPiece() && !current.getChessPiece().getTeam().isSameTeam(this.getTeam())) {//Has piece but not same team
                moves.add(current);
                break;
            } else {
                break;
            }
        }
    }

    protected boolean moveResultsInCheck (ChessBoard chessBoard, int destinationColumn, int destinationRow){
        ChessBoard tempBoard = new ChessBoard(chessBoard);

        Location originalLocation = tempBoard.getPieceLocation(this);
        int originalColumn = originalLocation.getColumn();
        int originalRow = originalLocation.getRow();
        tempBoard.getGridSquare(originalColumn, originalRow).setChessPiece(null);
        tempBoard.getGridSquare(destinationColumn, destinationRow).setChessPiece(this);

        GridSquare kingSquare = tempBoard.getKingSquare(this.getTeam());
        if(kingSquare != null){
            if (tempBoard.isSquareThreatenedByEnemy(kingSquare, this.getTeam())){
                return true;
            }
        }
        return false;
    }

}






