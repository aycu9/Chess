package game.player;

import game.ChessGame;
import game.Team;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/**
 * Created by Libra on 2018-03-31.
 */
public class LocalPlayer extends Player {
    private final Scene scene;

    public LocalPlayer(ChessGame chessGame, Team team, Scene scene) {
        super(chessGame, team);
        this.scene = scene;
    }

    private final EventHandler<MouseEvent> mouseClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            System.out.println(event.getSceneX() + " " + event.getSceneY());
            if(!getChessGame().getBoardState().currentTeam.isSameTeam(getTeam())){
                return;
            }
            getChessGame().click((int) event.getSceneX(), (int) event.getSceneY());
            getChessGame().dispatchUserState(getChessGame().createUserState());
        }
    };

    @Override
    public void start() {
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickHandler);
    }

    @Override
    public void stop() {
        scene.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickHandler);
    }
}
