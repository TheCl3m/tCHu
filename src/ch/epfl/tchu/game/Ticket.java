package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * This class represents a ticket in the game
 *
 * @author Clement HUSLER (328105)
 * @author Mathieu Faure (328086)
 */
public final class Ticket implements Comparable<Ticket> {

    private final List<Trip> trips;
    private final String text;

    /**
     * ch.epfl.tchu.gui.Main constructor of the class, sets the attributes up after checking that
     * the list isn't empty and that all trips starts from the same station
     *
     * @param trips the trip list of the ticket
     * @throws IllegalArgumentException if the given list is empty or contains duplicates
     */
    public Ticket(List<Trip> trips) {
        Preconditions.checkArgument(!(trips.isEmpty()) && checkDuplicateFrom(trips));
        this.trips = List.copyOf(trips);
        this.text = computeText(trips);
    }

    /**
     * Secondary constructor of the class, constructs a list with a single trip element
     * before sending it to the main constructor
     *
     * @param from   the starting station of the trip
     * @param to     the destination station of the trip
     * @param points the number of points of the trip
     */
    public Ticket(Station from, Station to, int points) {
        this(Collections.singletonList(new Trip(from, to, points)));
    }


    /**
     * @param trips the list of trips
     * @return the computed string containing the trips from the lists
     * @throws IllegalArgumentException if the given List is empty
     */
    private static String computeText(List<Trip> trips) {
        Preconditions.checkArgument(!trips.isEmpty());
        TreeSet<String> data = new TreeSet<>();
        String from = trips.get(0).from().toString();
        for (Trip trip : trips) {
            String to = trip.to().toString();
            String points = "(" + trip.points() + ")";
            data.add(to + " " + points);
        }
        String tos = String.join(", ", data);
        String t;
        if (data.size() == 1) {
            t = String.format("%s - %s", from, tos);
        } else {
            t = String.format("%s - {%s}", from, tos);
        }
        return t;
    }

    /**
     * @param trips the list of the trips
     * @return true if there are no duplicates in the given list, false if there are
     */
    private static boolean checkDuplicateFrom(List<Trip> trips) {
        String from = trips.get(0).from().toString();
        for (Trip trip : trips) {
            String check = trip.from().toString();
            if (!from.equals(check)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Getter for the text representation of the ticket
     *
     * @return the text representation
     */
    public String text() {
        return text;
    }

    /**
     * @return the text representation of the ticket
     */
    @Override
    public String toString() {
        return text();
    }

    /**
     * This method calculates the amount of points to be given to the player for the ticket
     * it goes trough all the trips contained in the ticket, checks for the connection
     * and returns the right value :
     * The maximum points if any of the trips are connected
     * -1*the minimum amount of points of the ticket if there isn't any connection
     *
     * @param connectivity the connectivity to be checked
     * @return the amount of points to add or subtract from the player's score
     */
    public int points(StationConnectivity connectivity) {
        int max = Integer.MIN_VALUE;
        for (Trip trip : trips) {
            max = Math.max(max, trip.points(connectivity));
        }
        return max;
    }

    /**
     * compares the text of two tickets
     *
     * @param that the ticket to compare to
     * @return 0 if the text is the same, a positive or a negative number following the difference
     * in the alphabetic order between this ticket and the ticket taken as a parameter
     */
    @Override
    public int compareTo(Ticket that) {
        return this.text.compareTo(that.text);
    }

    /**
     * @return the list of trips contained in the ticket
     */
    public List<Trip> getTrips() {
        return trips;
    }

}
