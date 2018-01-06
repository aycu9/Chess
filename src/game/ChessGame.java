package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ui.ChessBoard;
import ui.Clickable;
import ui.GridSquare;

import java.util.List;

/**
 * Created by Libra on 2017-09-30.
 */
public class ChessGame implements Clickable {
    private ChessBoard board;
    private GraphicsContext gc;
    private final int windowWidth;
    private final int windowHeight;
    private final Team team1;
    private final Team team2;

    public ChessGame(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        team1 = new Team(Color.WHITE);
        team2 = new Team(Color.BLACK);
    }

    public void startGame(GraphicsContext gc) {
        this.gc = gc;
        board = new ChessBoard(windowHeight, team1, team2);
        drawUiElements();
        board.getCurrentState().currentTeam = team1;

    }

    public void drawUiElements() {
        int boardOrigin = (windowWidth - board.getWidth()) / 2;
        gc.translate(boardOrigin, 0);
        board.draw(gc);
        gc.translate(-boardOrigin, 0);
    }


    @Override
    public void click(int x, int y) {
        int boardOriginX = (windowWidth - board.getWidth()) / 2;  //80
        int boardEndX = boardOriginX + board.getWidth();  //720
        int boardOriginY = windowHeight - board.getHeight();  //0
        int boardEndY = boardOriginY + board.getHeight();    //640
        if (x >= boardOriginX && x <= boardEndX) {
            if (y >= boardOriginY && y <= boardEndY) {
                board.click(x - boardOriginX, y - boardOriginY);
            }
        }
        updateGameState();
        drawUiElements();
    }

    private void updateGameState() {
        if (board.getCurrentState().selectedSquare != null) {
            if (this.allowedToSelectSquare()) {
                board.getCurrentState().selectedSquare.setGridSelected(true);
            } else {
                board.getCurrentState().selectedSquare = null;
            }
            if (board.getCurrentState().destinationSquare != null) {
                if (allowedToMoveHere()) {
                    Move recentMove = new Move(board.getCurrentState().selectedSquare, board.getCurrentState().destinationSquare);
                    board.getCurrentState().destinationSquare.setChessPiece(board.getCurrentState().selectedSquare.getChessPiece());
                    board.getCurrentState().selectedSquare.setChessPiece(null);
                    board.getCurrentState().selectedSquare.setGridSelected(false);
                    board.getCurrentState().selectedSquare = null;
                    board.getCurrentState().destinationSquare = null;
                    this.swapCurrentTeam();
                    board.getCurrentState().pastMoves.add(recentMove);
                } else {
                    board.getCurrentState().destinationSquare = null;
                }
            }

        }

    }

    private boolean allowedToSelectSquare() {
        if (!board.getCurrentState().selectedSquare.hasChessPiece()) { //if false
            return false;
        }
        if (!board.getCurrentState().selectedSquare.getChessPiece().getTeam().isSameTeam(board.getCurrentState().currentTeam)) { //if not same team
            return false;
        }
        if(board.getCurrentState().selectedSquare.getChessPiece().getLegalMoves(board).isEmpty()){ //if no legal moves
            return false;
        }
        return true;
    }

    private boolean allowedToMoveHere() {
        List<GridSquare> legalMoves = board.getCurrentState().selectedSquare.getChessPiece().getLegalMoves(board);
        if (legalMoves.contains(board.getCurrentState().destinationSquare)) {
            return true;
        } else {
            return false;
        }
    }

    private void swapCurrentTeam() {
        if (board.getCurrentState().currentTeam == team1) {
            board.getCurrentState().currentTeam = team2;
        } else
            board.getCurrentState().currentTeam = team1;
    }

}
