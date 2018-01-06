package game;

/**
 * Created by Libra on 2017-12-09.
 */
public class Location {
    private final int column;
    private final int row;

    public Location(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
