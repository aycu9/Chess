package api;

import game.Location;
import ui.ChessBoard;

import java.util.Objects;


public class UserState {
    public UserSelection selectedSquare;
    public UserSelection destinationSquare;
    public String promotionResult;

    public UserState(ChessBoard board) {
        if (board.getCurrentState().selectedSquare != null) {
            this.selectedSquare = new UserSelection(board.getGridSquareLocation(board.getCurrentState().selectedSquare));
        }
        if (board.getCurrentState().destinationSquare != null) {
            this.destinationSquare = new UserSelection(board.getGridSquareLocation(board.getCurrentState().destinationSquare));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserState){
            UserState other = (UserState) obj;
            return Objects.equals(this.selectedSquare, other.selectedSquare)
                    && Objects.equals(this.destinationSquare, other.destinationSquare)
                    && Objects.equals(this.promotionResult, other.promotionResult);
        }
        return false;
    }


    public static class UserSelection {
        public int column;
        public int row;

        public UserSelection(Location location) {
            this.column = location.getColumn();
            this.row = location.getRow();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof UserSelection){
                UserSelection other = (UserSelection) obj;
                return column == other.column && row == other.row;
            }
            return false;
        }
    }
}
