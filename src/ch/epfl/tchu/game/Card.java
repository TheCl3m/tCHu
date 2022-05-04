package ch.epfl.tchu.game;


import java.util.List;

/**
 * This enumeration represents the different card types of the game
 * there are 8 different wagon kinds (one for each color) and one card for the locomotive
 *
 * @author Clement HUSLER (328105)
 * @author Mathieu Faure (328086)
 */

public enum Card {

    BLACK(Color.BLACK), VIOLET(Color.VIOLET), BLUE(Color.BLUE), GREEN(Color.GREEN), YELLOW(Color.YELLOW), ORANGE(Color.ORANGE), RED(Color.RED), WHITE(Color.WHITE), LOCOMOTIVE(null);

    /**
     * The list of all card kinds
     */
    public static final List<Card> ALL = List.of(Card.values());
    /**
     * The number of all card kinds
     */
    public static final int COUNT = ALL.size();
    /**
     * The list containing only the wagon cards (the locomotive is excluded)
     */
    public static final List<Card> CARS = ALL.subList(0, Color.COUNT);

    private final Color color;

    /**
     * The constructor assigning a color to each type
     *
     * @param color the color to be assigned to the card
     */
    Card(Color color) {
        this.color = color;
    }

    /**
     * This method returns the correct wagon card depending on it's color
     *
     * @param color the color of the card
     * @return the card corresponding to the color
     */
    public static Card of(Color color) {
        if (color == null) {
            return Card.LOCOMOTIVE;
        }
        switch (color) {
            case BLACK:
                return BLACK;
            case VIOLET:
                return VIOLET;
            case BLUE:
                return BLUE;
            case GREEN:
                return GREEN;
            case YELLOW:
                return YELLOW;
            case ORANGE:
                return ORANGE;
            case RED:
                return RED;
            case WHITE:
                return WHITE;
            default:
                return LOCOMOTIVE;
        }
    }

    /**
     * This method returns the card's color
     *
     * @return the card's color if it is a wagon, null otherwise
     */
    public Color color() {
        return this.color;
    }

}
