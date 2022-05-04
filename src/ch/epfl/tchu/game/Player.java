package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.SoundID;

import java.util.List;
import java.util.Map;

/**
 * The interface Player, implemented by the players.
 *
 * @author Clement HUSLER (328105)
 * @author Mathieu FAURE (328086)
 */
public interface Player {

    /**
     * Initialises the players
     *
     * @param ownId       the own id
     * @param playerNames the player names
     */
    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * Sends the given info to the player
     *
     * @param info the info to send to the player
     */
    void receiveInfo(String info);

    /**
     * Updates the state of the player
     *
     * @param newState the player's new state
     * @param ownState the player's old state
     */
    void updateState(PublicGameState newState, PlayerState ownState);

    /**
     * Sends to the player a bag of tickets at the beginning of the game, he then chooses the ones he wants to keep
     *
     * @param tickets the tickets to choose from
     */
    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * Chooses the initial tickets the player wants to keep out of the ones he got offered
     *
     * @return the sorted bag of the initially chosen tickets
     */
    SortedBag<Ticket> chooseInitialTickets();

    /**
     * @return the turn kind the player wants to do next
     */
    TurnKind nextTurn();

    /**
     * Lets the player choose the tickets he wants out of the ones available in the option SortedBag
     *
     * @param options the options
     * @return the sorted bag
     */
    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     * The player selects which slot he wants to draw a card from.
     *
     * @return the slot the player chose
     */
    int drawSlot();

    /**
     * @return the route the player is willing to claim
     */
    Route claimedRoute();

    /**
     * @return the sorted bag containing the cards the player chose to use to claim the route initially
     */
    SortedBag<Card> initialClaimCards();

    /**
     * Called if the player needs to play more cards
     * when claiming a tunnel
     *
     * @param options the options the player has to choose from
     * @return the sorted bag of cards the player decided to use
     */
    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);

    /**
     * Plays a sound
     *
     * @param sound the sound to be played
     */
    void playSound(SoundID sound);

    /**
     * Triggers the game ended event
     *
     * @param message the message to display on the end screen
     */
    void gameEnded(String message);

    /**
     * Called to receive a chat
     *
     * @param message the message to add to the chatbox
     */
    void receiveChat(String message);

    /**
     * Called by the server to get the potential message to send
     *
     * @return the message to send
     */
    String sendChat();

    /**
     * The enumeration of the kind of turns the player can choose from every turn
     */
    enum TurnKind {
        /**
         * Draw tickets turn kind.
         */
        DRAW_TICKETS,
        /**
         * Draw cards turn kind.
         */
        DRAW_CARDS,
        /**
         * Claim route turn kind.
         */
        CLAIM_ROUTE;

        /**
         * The list of all turn kinds.
         */
        public static final List<TurnKind> ALL = List.of(TurnKind.values());
    }

}
