package ch.epfl.tchu.game;

/**
 * @author Mathieu Faure (328086)
 * @author Clement HUSLER (328105)
 */
public interface StationConnectivity {

    /**
     * @param s1 the first station
     * @param s2 the second station
     * @return true if they are connected, false if they are not
     */
    boolean connected(Station s1, Station s2);
}
