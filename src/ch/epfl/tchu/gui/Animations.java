package ch.epfl.tchu.gui;

import javafx.animation.PathTransition;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;


public final class Animations {

    private Animations() {
    }

    private static final double x_spacement = -71;


    public static PathTransition getPath(int slot, int ordinal) {
        slot = slot == -1 ? 5 : slot;
        Path path = new Path();
        path.setMouseTransparent(true);
        path.getElements().add(new MoveTo(0, -100 * (5 - slot)));
        path.getElements().add(new CubicCurveTo(-100, 100, -200, -150, -303 + x_spacement * (8 - ordinal), 147.5));
        path.getElements().add(new MoveTo(3000, 1000));
        path.getElements().add(new CubicCurveTo(0, 0, 0, 0, 1000, 100));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(2000));
        pathTransition.setPath(path);
        return pathTransition;
    }


}
