package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

/**
 * the PublicPlayerState class represents the state of a player the other player's also know,
 * such as the list of his routes, his ticket count, his card count, his points and his car count
 *
 * @author Mathieu Faure (328086)
 * @author Clément Husler (328105)
 */
public class PublicPlayerState {
    private final List<Route> routes;
    private final int ticketCount;
    private final int cardCount;
    private final int points;
    private final int carCount;

    /**
     * public constructor of the PublicPlayerState class
     *
     * @param ticketCount the player's ticket count
     * @param cardCount   the player's card count
     * @param routes      the routes the player claimed as a List
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes) {
        Preconditions.checkArgument(ticketCount >= 0 && cardCount >= 0);
        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = List.copyOf(routes);
        this.points = computePoints();
        this.carCount = computeCarCount();
    }

    /**
     * @return the amount of points the player earned
     */
    private int computePoints() {
        int total = 0;
        for (Route route : routes) {
            total += route.claimPoints();
        }
        return total;
    }

    /**
     * @return the player's count of cards
     */
    private int computeCarCount() {
        int total = 0;
        for (Route route : routes) {
            total += route.length();
        }
        return Constants.INITIAL_CAR_COUNT - total;
    }

    /**
     * @return the player's ticket count
     */
    public int ticketCount() {
        return ticketCount;
    }

    /**
     * @return the player's card count
     */
    public int cardCount() {
        return cardCount;
    }

    /**
     * @return the player's routes as a List
     */
    public List<Route> routes() {
        return routes;
    }

    /**
     * @return the player's car count
     */
    public int carCount() {
        return carCount;
    }

    /**
     * @return the player's point
     */
    public int claimPoints() {
        return points;
    }
}
