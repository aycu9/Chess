package api;

import game.Location;
import ui.ChessBoard;

/**
 * Created by Libra on 2018-03-24.
 */
public class UserState {
    public UserSelection selectedSquare;
    public UserSelection destinationSquare;

    public UserState(ChessBoard board) {
        if(board.getCurrentState().selectedSquare != null){
            this.selectedSquare = new UserSelection(board.getGridSquareLocation(board.getCurrentState().selectedSquare));
        }
        if(board.getCurrentState().destinationSquare != null){
            this.destinationSquare = new UserSelection(board.getGridSquareLocation(board.getCurrentState().destinationSquare));
        }
    }

    public static class UserSelection {
        public int column;
        public int row;

        public UserSelection(Location location) {
            this.column = location.getColumn();
            this.row = location.getRow();
        }
    }
}
