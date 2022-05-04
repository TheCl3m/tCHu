package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Trail.
 *
 * @author Clement HUSLER (328105)
 * @author Mathieu Faure (328086)
 */
public final class Trail {

    private final Station start;
    private final Station end;
    private final int length;
    private final List<Route> routes;

    /**
     * Private and main constructor of a Trail
     *
     * @param routes the list of routes that the trail consists of
     */
    private Trail(Station start, Station end, List<Route> routes) {
        this.routes = routes;
        this.start = start;
        this.end = end;
        this.length = computeLength();
    }


    /**
     * Private method returning the length of a trail
     * iterates trough all the routes and adds up their lengths
     *
     * @return the length of the trail
     */
    private int computeLength() {
        int total = 0;
        for (Route route : routes) {
            total += route.length();
        }
        return total;
    }

    /**
     * @param routes the routes
     * @return the longest possible trail that can be built using the Routes contained in the routes List
     */
    public static Trail longest(List<Route> routes) {
        if (routes.size() == 0) {
            return new Trail(null, null, List.of());
        } else {
            List<Trail> cs = new ArrayList<>();
            for (Route route : routes) {
                cs.add(new Trail(route.station1(), route.station2(), List.of(route)));
                cs.add(new Trail(route.station2(), route.station1(), List.of(route)));
            }
            int maxLength = 0;
            Trail longest = new Trail(null, null, List.of());
            while (!cs.isEmpty()) {
                List<Trail> csP = new ArrayList<>();
                for (Trail c : cs) {
                    if (c.length > maxLength) {
                        longest = c;
                        maxLength = c.length;
                    }
                    for (Route route : routes) {
                        Trail temp = c.extendTrailIfPossible(route);
                        if (temp != null) {
                            csP.add(temp);
                        }
                    }
                    cs = csP;
                }
            }
            return longest;
        }
    }

    /**
     * Private method that extends a trail if it is possible
     *
     * @param route the route to add to the trail
     * @return if the trail has been extended, a new trail containing
     * the new route list, else the non-extended trail
     */
    private Trail extendTrailIfPossible(Route route) {
        if (!this.routes.contains(route)) {
            List<Route> routes = new ArrayList<Route>(this.routes);
            routes.add(route);
            if ((route.station1() == this.end)) {
                return new Trail(this.start, route.station2(), routes);
            } else if (route.station2() == this.end) {
                return new Trail(this.start, route.station1(), routes);
            }
            return null;
        }
        return null;
    }

    /**
     * @return the length of the trail as an int
     */
    public int length() {
        return this.length;
    }

    /**
     * @return the departure station of the trail
     */
    public Station station1() {
        if (this.length == 0) {
            return null;
        }
        return this.start;
    }

    /**
     * @return the end station of the trail
     */
    public Station station2() {
        if (this.length == 0) {
            return null;
        }
        return this.end;
    }

    /**
     * method used to get the list of the routes composing the trail, used to highlight the longest trail at the end of the game
     *
     * @return the list of the trail's routes
     */
    public List<Route> getRoutes() {
        return List.copyOf(routes);
    }


    /**
     * Public method returning the String representation of a Trail
     * It lists all the stations of the trail and it's length
     *
     * @return the String representation of the Trail
     */
    @Override
    public String toString() {
        if (this.length == 0) {
            return "Empty trail (0)";
        }
        StringBuilder stationsNames = new StringBuilder();
        Station lastAdded = start;
        for (Route route : routes) {
            Station toAdd = route.stationOpposite(lastAdded);
            stationsNames.append(" - ");
            stationsNames.append(toAdd.name());
            lastAdded = toAdd;
        }
        return start.name() + stationsNames + " (" + length + ")";

    }
}
