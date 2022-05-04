package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * a route is a game element consisting of two stations, a length, a level, a color and an id
 * it can be claimed by a player, which grants him points
 *
 * @author Clement HUSLER (328105)
 * @author Mathieu Faure (328086)
 */
public final class Route {

    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;

    /**
     * The enum Level.
     */
    public enum Level {
        /**
         * Overground level.
         */
        OVERGROUND,
        /**
         * Underground level.
         */
        UNDERGROUND
    }

    /**
     * Instantiates a new Route.
     *
     * @param id       the id of the route
     * @param station1 the departure station
     * @param station2 the arrival station
     * @param length   the length of the route
     * @param level    the level (UNDERGROUND or OVERGROUND)
     * @param color    the color of the route, what color is needed to claim it
     * @throws NullPointerException     if either the id, one of the stations or the level is null
     * @throws IllegalArgumentException if the stations 1 and 2 are the same or if the length of the route isn't an accepted size
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {
        Preconditions.checkArgument(!station1.equals(station2) && length <= Constants.MAX_ROUTE_LENGTH && length >= Constants.MIN_ROUTE_LENGTH);
        requireNonNull(id);
        requireNonNull(station1);
        requireNonNull(station2);
        requireNonNull(level);
        this.id = id;
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
        this.level = level;
        this.color = color;
    }

    /**
     * @return the string ID of the route
     */
    public String id() {
        return this.id;
    }

    /**
     * @return the first station of the route (departure)
     */
    public Station station1() {
        return this.station1;
    }

    /**
     * @return the second station of the route (arrival)
     */
    public Station station2() {
        return this.station2;
    }

    /**
     * @return the length of the route as an int
     */
    public int length() {
        return this.length;
    }

    /**
     * @return the level of the Route, OVERGROUND or UNDERGROUND
     */
    public Level level() {
        return this.level;
    }

    /**
     * @return the color of the route
     */
    public Color color() {
        return this.color;
    }

    /**
     * @return the list of the stations of the route
     */
    public List<Station> stations() {
        return List.of(station1, station2);
    }

    /**
     * Returns the departure station of the route if the parameter is the arrival, and inversely if the parameter is the departure
     *
     * @param station one of the two stations of the route
     * @return the other station of the route
     * @throws IllegalArgumentException if the station that is send isn't a station of the route
     */
    public Station stationOpposite(Station station) {
        Preconditions.checkArgument(station.equals(station1) || station.equals(station2));
        if (station.equals(station1)) {
            return station2;
        } else {
            return station1;
        }
    }

    /**
     * Possible claim cards list.
     *
     * @return the list of all the cards set that can be played to claim the route
     */
    public List<SortedBag<Card>> possibleClaimCards() {
        if (color != null) {
            return possibleClaimCardsWithColor();
        } else {
            return possibleClaimCardsWithoutColor();
        }
    }

    /**
     * used in the method possibleClaimCards, in the case where the route has a particular color
     *
     * @return the list of all the cards set that can be played to claim the route in this case
     */
    private List<SortedBag<Card>> possibleClaimCardsWithColor() {
        List<SortedBag<Card>> possibleCards = new ArrayList<>();
        if (this.level == Level.UNDERGROUND) {
            for (int i = length; i >= 0; i--) {
                SortedBag<Card> temp = SortedBag.of(i, Card.of(color), length - i, Card.LOCOMOTIVE);
                possibleCards.add(temp);
            }
        } else {
            possibleCards.add(SortedBag.of(length, Card.of(color)));
        }
        return Collections.unmodifiableList(possibleCards);
    }

    /**
     * used in the method possibleClaimCards, in the case where the route has no particular color
     *
     * @return the list of all the cards set that can be played to claim the route in this case
     */

    private List<SortedBag<Card>> possibleClaimCardsWithoutColor() {
        List<SortedBag<Card>> possibleCards = new ArrayList<>();
        if (this.level == Level.UNDERGROUND) {
            for (int i = length; i > 0; i--) {
                for (int j = 0; j < Card.CARS.size(); j++) {
                    SortedBag<Card> temp = SortedBag.of(i, Card.CARS.get(j), length - i, Card.LOCOMOTIVE);
                    possibleCards.add(temp);
                }
            }
            possibleCards.add(SortedBag.of(length, Card.LOCOMOTIVE));
        } else {
            for (int i = 0; i < Card.CARS.size(); i++) {
                SortedBag<Card> temp = SortedBag.of(length, Card.CARS.get(i));
                possibleCards.add(temp);
            }
        }
        return Collections.unmodifiableList(possibleCards);
    }

    /**
     * computes the number of cards that have to be played in addition of the claimCards to claim the route
     *
     * @param claimCards the claim cards previously used to claim the route
     * @param drawnCards the drawn cards
     * @return the number of additional cards that have to be played to claim the route
     * @throws IllegalArgumentException if the level is not UNDERGROUND or the size of drawnCards is not 3
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards) {
        Preconditions.checkArgument(this.level.equals(Level.UNDERGROUND) && (drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS));
        Set<Card> claimCardsTypeSet = claimCards.toSet();
        int count = 0;
        for (Card type : claimCardsTypeSet) {
            for (int j = 0; j < drawnCards.size(); j++) {
                if (type == drawnCards.get(j) && drawnCards.get(j) != Card.LOCOMOTIVE) {
                    ++count;
                }
            }
        }
        for (Card card : drawnCards) {
            if (card == Card.LOCOMOTIVE) {
                ++count;
            }
        }
        return count;
    }

    /**
     * @return the number of points the player gets for claiming the Route
     */
    public int claimPoints() {
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }
}
