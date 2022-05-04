package ch.epfl.tchu.game;

import java.util.List;

/**
 * This enumeration represents the different player Ids
 *
 * @author Clement HUSLER (328105)
 * @author Mathieu FAURE (328086)
 */
public enum PlayerId {
    /**
     * Player 1 player id.
     */
    PLAYER_1,
    /**
     * Player 2 player id.
     */
    PLAYER_2;

    /**
     * The list of both playerIds
     */
    public static final List<PlayerId> ALL = List.of(PlayerId.values());

    /**
     * The count of player Ids
     */
    public static final int COUNT = ALL.size();

    /**
     * Gives the player Id that is coming next
     *
     * @return the player id that follows this one
     */
    public PlayerId next() {
        if (this.equals(PLAYER_1)) {
            return PLAYER_2;
        }
        return PLAYER_1;
    }
}
