package game;

import ui.GridSquare;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017-11-04.
 */
public class BoardState {
    public GridSquare selectedSquare;
    public GridSquare destinationSquare;
    public Team currentTeam;
    public List<Move> pastMoves = new ArrayList<>();
}
