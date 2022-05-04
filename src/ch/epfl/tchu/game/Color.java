package ch.epfl.tchu.game;

import java.util.List;

/**
 * This enumeration represents the 8 colors used in the game
 * for the wagon card and the roads
 *
 * @author Clement HUSLER (328105)
 * @author Mathieu Faure (328086)
 */
public enum Color {
    BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE;

    /**
     * The list of all the 8 colors
     */
    public static final List<Color> ALL = List.of(Color.values());

    /**
     * The number of colors available
     */
    public static final int COUNT = ALL.size();


}
