package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.Objects;

/**
 * a Station is a game element, a point of the rail network of the game
 *
 * @author Mathieu Faure (328086)
 * @author Clement HUSLER (328105)
 */
public final class Station {
    private final int id;
    private final String name;

    /**
     * constructor of the Station class, checks if the given id is valid before affecting it
     *
     * @param id   is the id of the station as an int
     * @param name is the name of the station as a String
     * @throws IllegalArgumentException if the id is negative
     */
    public Station(int id, String name) {
        Preconditions.checkArgument(id >= 0);
        Objects.requireNonNull(name);
        this.id = id;
        this.name = name;
    }

    /**
     * getter for the id of the station
     *
     * @return id
     */
    public int id() {
        return this.id;
    }

    /**
     * getter for the name of the station
     *
     * @return name
     */
    public String name() {
        return this.name;
    }

    /**
     * Returns the name of the station
     *
     * @return name the name of the station
     */
    @Override
    public String toString() {
        return this.name;
    }
}
