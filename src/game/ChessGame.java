package game;

import api.UserState;
import com.google.gson.Gson;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import ui.ChessBoard;
import ui.ChessPiece;
import ui.Clickable;
import ui.GridSquare;
import ui.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ChessGame implements Clickable {
    private ChessBoard board;
    private GraphicsContext gc;
    private final int windowWidth;
    private final int windowHeight;
    private final Team team1;
    private final Team team2;
    private final List<UserStateListener> listeners;

    public ChessGame(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        team1 = new Team(Color.WHITE, "White");
        team2 = new Team(Color.BLACK, "Black");
        listeners = new ArrayList<>();  //initially no listeners
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
    }

    //Makes move for a Player, all listeners receive the move as a UserState
    public void dispatchUserState(UserState userState) {
        Team team = getBoardState().currentTeam;
        board.dispatchUserState(userState);
        updateGameState(userState);
        drawUiElements();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onReceiveUserState(userState, team);
        }
    }

    public UserState createUserState() {
        return new UserState(board);
    }

    private void updateGameState(UserState userState) {
        if (board.getCurrentState().selectedSquare != null) { //if selected a square
            if (this.allowedToSelectSquare()) {
                board.getCurrentState().selectedSquare.setGridSelected(true);
            } else {
                board.getCurrentState().selectedSquare = null;
            }
            if (board.getCurrentState().destinationSquare != null) {
                if (isLegalMove()) {
                    Move recentMove = new Move(board.getCurrentState().selectedSquare, board.getCurrentState().destinationSquare);
                    board.getCurrentState().pastMoves.add(recentMove);
                    if (!tryCastlingMove()) {// if did not castle, execute normal move
                        board.getCurrentState().destinationSquare.setChessPiece(board.getCurrentState().selectedSquare.getChessPiece());
                        board.getCurrentState().selectedSquare.setChessPiece(null);
                    }
                    //applies to both types of move
                    board.getCurrentState().selectedSquare.setGridSelected(false);
                    board.getCurrentState().selectedSquare = null;
                    if (pawnReachedOpposteSide()) {
                        drawUiElements();
                        promotionPopup(userState);
                    }
                    board.getCurrentState().destinationSquare = null;
                    this.swapCurrentTeam();
                    drawUiElements();
                    checkmate();
                } else {
                    board.getCurrentState().destinationSquare = null;
                }
            }
        }
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    private boolean allowedToSelectSquare() {
        if (!board.getCurrentState().selectedSquare.hasChessPiece()) { //if no chess piece
            return false;
        }
        if (!board.getCurrentState().selectedSquare.getChessPiece().getTeam().isSameTeam(board.getCurrentState().currentTeam)) { //if not same team
            return false;
        }
        if (board.getCurrentState().selectedSquare.getChessPiece().getLegalMoves(board).isEmpty()) { //if no legal moves
            return false;
        }
        return true;
    }

    private boolean isLegalMove() {
        List<GridSquare> legalMoves = board.getCurrentState().selectedSquare.getChessPiece().getLegalMoves(board);
        if (legalMoves.contains(board.getCurrentState().destinationSquare)) {
            return true;
        } else {
            return false;
        }
    }

    private void swapCurrentTeam() {
        board.getCurrentState().currentTeam = getOppositeTeam(board.getCurrentState().currentTeam);
    }


    private boolean tryCastlingMove() {
        ChessPiece king = board.getCurrentState().selectedSquare.getChessPiece();
        ChessPiece rook = board.getCurrentState().destinationSquare.getChessPiece();
        if (king instanceof King && rook instanceof Rook && king.getTeam().isSameTeam(rook.getTeam())) {
            Location rookLocation = board.getPieceLocation(rook);
            int rookRow = rookLocation.getRow();
            int rookColumn = rookLocation.getColumn();
            if (rookColumn == 0) {
                board.getGridSquare(2, rookRow).setChessPiece(king);
                board.getGridSquare(3, rookRow).setChessPiece(rook);
            }
            if (rookColumn == 7) {
                board.getGridSquare(6, rookRow).setChessPiece(king);
                board.getGridSquare(5, rookRow).setChessPiece(rook);
            }
            board.getCurrentState().destinationSquare.setChessPiece(null);
            board.getCurrentState().selectedSquare.setChessPiece(null);
            return true;
        }
        return false;
    }

    private void checkmate() {
        Team currentTeam = board.getCurrentState().currentTeam;
        GridSquare kingSquare = board.getKingSquare(currentTeam);
        if (!board.teamHasLegalMoves(currentTeam)) {    //if current team has no legal moves
            if (board.isSquareThreatenedByEnemy(kingSquare, currentTeam)) {   // if current team's king is threatened
                if (currentTeam.isSameTeam(team1)) {
                    gameoverPopup("Team Black has won!");
                } else {
                    gameoverPopup("Team White has won!");
                }
            } else {
                gameoverPopup("It's a draw!");
            }
        }
    }

    private boolean pawnReachedOpposteSide() {
        GridSquare gridSquare = board.getCurrentState().destinationSquare;
        if (gridSquare.getChessPiece() instanceof Pawn && board.pieceReachedOppositeSide(gridSquare)) {
            return true;
        }
        return false;
    }

    private void promotionPopup(UserState userState) {
        GridSquare pawnSquare = board.getCurrentState().destinationSquare;
        ChessPiece pawnPiece = pawnSquare.getChessPiece();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pawn Promotion");
        alert.setHeaderText("You may promote your pawn.");
        alert.setContentText("Choose a piece your pawn will be promoted to");

        ButtonType buttonQueen = new ButtonType("Queen");
        ButtonType buttonBishop = new ButtonType("Bishop");
        ButtonType buttonKnight = new ButtonType("Knight");
        ButtonType buttonRook = new ButtonType("Rook");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonQueen, buttonBishop, buttonKnight, buttonRook, buttonTypeCancel);

        ButtonType resultType;
        if (userState.promotionResult == null) {
            Optional<ButtonType> result = alert.showAndWait();
            resultType = result.get();
        } else {
            switch (userState.promotionResult) {
                case Queen.NAME:
                    resultType = buttonQueen;
                    break;
                case Bishop.NAME:
                    resultType = buttonBishop;
                    break;
                case Knight.NAME:
                    resultType = buttonKnight;
                    break;
                case Rook.NAME:
                    resultType = buttonRook;
                    break;
                default:
                    resultType = buttonTypeCancel;
                    break;
            }
        }


        if (resultType == buttonQueen) {
            pawnSquare.setChessPiece(new Queen(pawnPiece));
            userState.promotionResult = Queen.NAME;
        } else if (resultType == buttonBishop) {
            pawnSquare.setChessPiece(new Bishop(pawnPiece));
            userState.promotionResult = Bishop.NAME;
        } else if (resultType == buttonKnight) {
            pawnSquare.setChessPiece(new Knight(pawnPiece));
            userState.promotionResult = Knight.NAME;
        } else if (resultType == buttonRook) {
            pawnSquare.setChessPiece(new Rook(pawnPiece));
            userState.promotionResult = Rook.NAME;
        } else {
            userState.promotionResult = Pawn.NAME;
        }
    }

    private void gameoverPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setHeaderText(message);
        alert.setContentText("Click OK to start a new game.");

        Optional<ButtonType> result = alert.showAndWait();

        startGame(gc);
    }

    public BoardState getBoardState() {
        return board.getCurrentState();
    }

    public void addListener(UserStateListener listener) {
        listeners.add(listener);
    }

    public void removeListener(UserStateListener listener) {
        listeners.remove(listener);
    }

    public Team getOppositeTeam(Team team) {
        if (team.isSameTeam(team1)) {
            return team2;
        } else {
            return team1;
        }
    }

}
