package ui;

import game.BoardState;
import game.Team;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ui.pieces.Pawn;

/**
 * Created by Libra on 2017-09-30.
 */
public class ChessBoard implements Drawable, Clickable {
    private final Color background = Color.FLORALWHITE;
    private final GridSquare[][] boardGrid;
    private static final int GRID_SIZE = 8;
    private final int boardSize;
    private final Team team1;
    private final Team team2;
    private BoardState currentState;

    public ChessBoard(int boardSize, Team team1, Team team2) {
        this.currentState = new BoardState();
        this.boardSize = boardSize;
        this.team1 = team1;
        this.team2 = team2;
        boardGrid = new GridSquare[GRID_SIZE][GRID_SIZE];
        this.createGridSquares();
        this.createChessPieces();
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(background);
        gc.fillRect(0, 0, boardSize, boardSize);
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                boardGrid[x][y].draw(gc);
                gc.translate(0, boardGrid[x][y].getHeight());
            }
            gc.translate(0, -boardGrid[x][x].getHeight() * GRID_SIZE);
            gc.translate(boardGrid[x][x].getWidth(), 0);
        }
        gc.translate(-boardGrid[0][0].getWidth() * GRID_SIZE, 0);
    }

    public BoardState getCurrentState() {
        return currentState;
    }

    @Override
    public int getWidth() {
        return boardSize;
    }

    @Override
    public int getHeight() {
        return boardSize;
    }


    private void createGridSquares() {
        Color a = Color.GOLDENROD;
        Color b = Color.BROWN;
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                Color current;
                if ((x + y) % 2 == 1)
                    current = a;
                else
                    current = b;
                boardGrid[x][y] = new GridSquare(current, boardSize / GRID_SIZE);
            }
        }
    }

    private void createChessPieces() {
        for (int x = 0; x < GRID_SIZE; x++) {
            boardGrid[x][6].setChessPiece(new Pawn(team1.getTeamColor(), team1, boardSize / GRID_SIZE));
            boardGrid[x][1].setChessPiece(new Pawn(team2.getTeamColor(), team2, boardSize / GRID_SIZE));
        }
    }

    @Override
    public void click(int x, int y) {
        System.out.println("x: " + x + " , y: " + y);
        int gridSize = this.boardSize / GRID_SIZE;    //80
        for (int xCor = 0; xCor < GRID_SIZE; xCor++) {
            for (int yCor = 0; yCor < GRID_SIZE; yCor++) {
                if (x / gridSize == xCor && y / gridSize == yCor) {
                    boardGrid[xCor][yCor].click(x, y);
                    if (currentState.selectedSquare == null) {
                        if (boardGrid[xCor][yCor].thereIsChessPiece()) {
                            currentState.selectedSquare = boardGrid[xCor][yCor];
                            boardGrid[xCor][yCor].setGridSelected(true);
                        }
                    } else {
                        currentState.destinationSquare = boardGrid[xCor][yCor];
                        currentState.destinationSquare.setChessPiece(currentState.selectedSquare.getChessPiece());
//                        currentState.selectedSquare.setChessPiece(null);
                        currentState.selectedSquare.setGridSelected(false);
                    }
                }
            }
        }
    }
}
