package ui;

import game.BoardState;
import game.Location;
import game.Team;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ui.pieces.Knight;
import ui.pieces.Pawn;

import java.util.List;


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
        resetLegalMoves();
        if (currentState.selectedSquare != null) {
            setLegalMoves();
        }
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

    public GridSquare getGridSquare(int column, int row) {
        if(column >= GRID_SIZE || column < 0 || row >= GRID_SIZE || row < 0) {
            return null;
        }
        else {
            return boardGrid[column][row];
        }
    }

    public Location getPiecePosition(ChessPiece chessPiece) {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (boardGrid[x][y].hasChessPiece() && boardGrid[x][y].getChessPiece() == chessPiece) {
                    return new Location(x, y);
                }
            }
        }
        return null; //Piece not found on board
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
        //create pawns
        for (int x = 0; x < GRID_SIZE; x++) {
            boardGrid[x][6].setChessPiece(new Pawn(team1.getTeamColor(), team1, boardSize / GRID_SIZE));
            boardGrid[x][1].setChessPiece(new Pawn(team2.getTeamColor(), team2, boardSize / GRID_SIZE));
        }
        //create knights
        boardGrid[1][7].setChessPiece(new Knight(team1.getTeamColor(), team1, boardSize / GRID_SIZE));
        boardGrid[6][7].setChessPiece(new Knight(team1.getTeamColor(), team1, boardSize / GRID_SIZE));
        boardGrid[6][0].setChessPiece(new Knight(team2.getTeamColor(), team2, boardSize / GRID_SIZE));
        boardGrid[1][0].setChessPiece(new Knight(team2.getTeamColor(), team2, boardSize / GRID_SIZE));
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
                        currentState.selectedSquare = boardGrid[xCor][yCor];
                    } else {
                        currentState.destinationSquare = boardGrid[xCor][yCor];
                    }
                }
            }
        }
    }

    public int getForwardDirection(Team team) {
        if (team == team1) {
            return -1;
        } else {
            return 1;
        }
    }

    public void setLegalMoves() {
        this.getCurrentState().selectedSquare.setGridSelectedForLegalMoves(false);
        List<GridSquare> legalMoves = this.getCurrentState().selectedSquare.getChessPiece().getLegalMoves(this);
        for (int i = 0; i < legalMoves.size(); i++) {
            legalMoves.get(i).setGridSelectedForLegalMoves(true);
        }
    }

    public void resetLegalMoves() {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                boardGrid[x][y].setGridSelectedForLegalMoves(false);
            }
        }
    }
}

