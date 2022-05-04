package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * game element, a trip is a path between a departure and an arrival station with a value of points
 *
 * @author Mathieu Faure (328086)
 * @author Clement HUSLER (328105)
 */
public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    /**
     * Trip class constructor
     *
     * @param from   is the departure station of the trip
     * @param to     is the arrival station or country of the trip
     * @param points is the value of the trip
     */
    public Trip(Station from, Station to, int points) {
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        Preconditions.checkArgument(points > 0);
        this.points = points;
    }

    /**
     * other constructor for the Trip class
     *
     * @param from   is a list containing one or multiple departure stations
     * @param to     is a list containing one or multiple arrival stations
     * @param points is the value of the trips
     * @return a list containing all the trip combinations of the departure and arrival station
     * @throws IllegalArgumentException if the amount of points is not positive or one of the given Lists is empty
     */
    public static List<Trip> all(List<Station> from, List<Station> to, int points) {
        Preconditions.checkArgument(!(from.isEmpty()) && !(to.isEmpty()) && points > 0);
        List<Trip> allTrips = new ArrayList<Trip>();
        for (Station s : from) {
            for (Station t : to) {
                allTrips.add(new Trip(s, t, points));
            }
        }
        return allTrips;
    }

    /**
     * getter for the departure station
     *
     * @return the departure Station from
     */
    public Station from() {
        return from;
    }

    /**
     * getter for the arrival station
     *
     * @return the departure Station to
     */
    public Station to() {
        return to;
    }

    /**
     * getter for the point value of the trip
     *
     * @return points
     */
    public int points() {
        return points;
    }

    /**
     * returns an amount of points depending on the connectivity established by the player
     *
     * @param connectivity true if the two stations are connected by the player, false else
     * @return a positive value of points of the trip if the stations are connected, a negative value else
     */
    public int points(StationConnectivity connectivity) {
        if (connectivity.connected(from, to)) {
            return points;
        } else return (-points);
    }

}
