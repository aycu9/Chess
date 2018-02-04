package ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridSquare implements Drawable, Clickable {
    private final Color background;
    private ChessPiece chessPiece;
    private final int gridSquareSize;
    private boolean gridSelected;
    private int borderWidth;
    private boolean gridSelectedForLegalMoves;

    public GridSquare(Color background, int gridSquareSize) {
        this.background = background;
        this.gridSquareSize = gridSquareSize;
    }

    public ChessPiece getChessPiece() {
        return chessPiece;
    }

    public void setChessPiece(ChessPiece chessPiece) {
        this.chessPiece = chessPiece;
    }


    @Override
    public void draw(GraphicsContext gc) {
        borderWidth = gridSquareSize / 10;
        gc.setFill(background);
        gc.fillRect(0, 0, gridSquareSize, gridSquareSize);
        if (chessPiece != null) {
            chessPiece.draw(gc);
        }
        if (isGridSelected()) {
            gc.setStroke(Color.PURPLE);
            gc.setLineWidth(borderWidth);
            gc.strokeRect(borderWidth / 2, borderWidth / 2, gridSquareSize - borderWidth, gridSquareSize - borderWidth);
        }
        if (isSelectedForLegalMoves()) {
            gc.setStroke(Color.GREEN);
            gc.setLineWidth(borderWidth);
            gc.strokeRect(borderWidth / 2, borderWidth / 2, gridSquareSize - borderWidth, gridSquareSize - borderWidth);
        }
    }

    public boolean hasChessPiece() {
        return getChessPiece() != null;
    }

    public void setGridSelectedForLegalMoves(boolean gridSelectedForLegalMoves) {
        this.gridSelectedForLegalMoves = gridSelectedForLegalMoves;
    }

    private boolean isSelectedForLegalMoves() {
        return gridSelectedForLegalMoves;

    }

    @Override
    public int getWidth() {
        return gridSquareSize;
    }

    @Override
    public int getHeight() {
        return gridSquareSize;
    }

    @Override
    public void click(int x, int y) {
    }

    public void setGridSelected(boolean gridSelected) {
        this.gridSelected = gridSelected;
    }

    private boolean isGridSelected() {
        return gridSelected;
    }
}


