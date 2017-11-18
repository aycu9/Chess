package ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Libra on 2017-10-14.
 */
public class GridSquare implements Drawable, Clickable {
    private final Color background;
    private ChessPiece chessPiece;
    private final int gridSquareSize;
    private boolean gridSelected;
    private int borderWidth;

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
        borderWidth = gridSquareSize/10;
        gc.setFill(background);
        gc.fillRect(0, 0, gridSquareSize, gridSquareSize);
        if (chessPiece != null) {
            chessPiece.draw(gc);
        }
        if(isGridSelected()){
            gc.setStroke(Color.GREEN);
            gc.setLineWidth(borderWidth);
            gc.strokeRect(borderWidth/2, borderWidth/2, gridSquareSize-borderWidth, gridSquareSize-borderWidth);
        }
    }

    public boolean thereIsChessPiece(){
        return getChessPiece() != null;
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

    public boolean isGridSelected() {
        return gridSelected;
    }
}


