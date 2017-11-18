package ui;

import game.Team;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Created by Libra on 2017-10-14.
 */
public abstract class ChessPiece implements Drawable {
    private final Color pieceColor;
    private final Color outlineColor;
    private final Team team;
    private final int size;
    private final Font font;

    public ChessPiece(Color pieceColor, Team team, int size) {
        this.pieceColor = pieceColor;
        this.outlineColor = Color.DARKSLATEBLUE;
        this.team = team;
        this.size = size;
        this.font = Font.font(null, FontWeight.BOLD, size / 2);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(pieceColor);
        gc.setStroke(outlineColor);
        gc.setLineWidth(3);
        gc.setFont(font);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.fillText(getName(), size / 2, size / 2);
        gc.strokeText(getName(),size / 2, size / 2 );
    }

    public abstract String getName();

    @Override
    public int getWidth() {
        return size;
    }

    @Override
    public int getHeight() {
        return size;
    }

    public Team getTeam() {
        return team;
    }
}
