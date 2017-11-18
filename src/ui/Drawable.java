package ui;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Libra on 2017-10-21.
 */
public interface Drawable {
    void draw (GraphicsContext gc);
    int getWidth();
    int getHeight();
}
