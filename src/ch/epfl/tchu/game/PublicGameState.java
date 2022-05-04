package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PublicGameState is the public part of the game's state, which includes the ticket count, the public card state,
 * the ID of the current player, the ID of the game's last player and a map associating each playerID and the public player state of each player
 *
 * @author Mathieu Faure (328086)
 * @author Clément Husler (328105)
 */
public class PublicGameState {

    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final Map<PlayerId, PublicPlayerState> playerState;
    private final PlayerId lastPlayerId;

    /**
     * public constructor for the PublicGameState class
     *
     * @param ticketsCount    the count of tickets currently available
     * @param cardState       the game's public card state
     * @param currentPlayerId the ID of the player currently playing
     * @param playerState     the map associating the ID of a player with his public state
     * @param lastPlayer      the ID of the game's last player
     * @throws IllegalArgumentException if the ticket count is negative or if the size of the playerState Map isn't equal to 2
     * @throws NullPointerException     if cardState or currentPlayerId is null (or both)
     */
    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer) {
        Preconditions.checkArgument(ticketsCount >= 0 && playerState.size() == 2);
        if (cardState == null || currentPlayerId == null) {
            throw new NullPointerException("One or more of the arguments is null");
        }
        this.ticketsCount = ticketsCount;
        this.cardState = cardState;
        this.currentPlayerId = currentPlayerId;
        this.playerState = Map.copyOf(playerState);
        this.lastPlayerId = lastPlayer;
    }

    /**
     * @return the game's current ticketCount
     */
    public int ticketsCount() {
        return ticketsCount;
    }

    /**
     * @return true if it's possible for the players to draw tickets at the moment, false if it is not,
     * which means that the ticketCount is 0
     */
    public boolean canDrawTickets() {
        return ticketsCount != 0;
    }

    /**
     * @return the game's public card state
     */
    public PublicCardState cardState() {
        return cardState;
    }

    /**
     * @return true if it's possible for the players to draw cards at the moment, false if it is not
     */
    public boolean canDrawCards() {
        return ((cardState.deckSize() + cardState.discardsSize()) >= Constants.FACE_UP_CARDS_COUNT);
    }

    /**
     * @return the the current player's Id
     */
    public PlayerId currentPlayerId() {
        return currentPlayerId;
    }

    /**
     * @param playerId the Id of the player we want to get the public state
     * @return the public state associated to this Id in the map
     */
    public PublicPlayerState playerState(PlayerId playerId) {
        return playerState.get(playerId);
    }

    /**
     * @return the PublicPlayerState of the current player
     */
    public PublicPlayerState currentPlayerState() {
        return playerState(currentPlayerId);
    }

    /**
     * @return the routes that have already been claimed by all players
     */
    public List<Route> claimedRoutes() {
        ArrayList<Route> claimedRoutes = new ArrayList<>(currentPlayerState().routes());
        claimedRoutes.addAll(playerState(currentPlayerId.next()).routes());
        return claimedRoutes;
    }

    /**
     * @return the identity of the last player, null if he is unknown because the last turn hasn't begun at the moment
     */
    public PlayerId lastPlayer() {
        return lastPlayerId;
    }

}
