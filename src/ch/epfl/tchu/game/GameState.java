package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * GameState represents the current sate of the game in all it's aspects, it extends PublicGameState
 *
 * @author Mathieu Faure (328086)
 * @author Clément Husler (328105)
 */
public final class GameState extends PublicGameState {

    private final Deck<Ticket> tickets;
    private final CardState cardState;
    private final Map<PlayerId, PlayerState> playerState;

    /**
     * private constructor for the GameState class, used in the "initial" class
     *
     * @param cardState       the game's card state
     * @param tickets         the game's deck of tickets
     * @param currentPlayerId the ID of the current player
     * @param playerState     a map associating each Player ID with a PlayerState
     * @param lastPlayer      the last player to play in the game, null if the last round hasn't begun
     */
    private GameState(CardState cardState, Deck<Ticket> tickets, PlayerId currentPlayerId, Map<PlayerId, PlayerState> playerState, PlayerId lastPlayer) {
        super(tickets.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);
        Objects.requireNonNull(cardState);
        Objects.requireNonNull(tickets);
        this.tickets = tickets;
        this.cardState = cardState;
        this.playerState = Map.copyOf(playerState);
    }

    /**
     * public building method to initialize the game
     *
     * @param tickets a SortedBag of the tickets we want to have in our tickets deck at the beginning of the game
     * @param rng     Random used to shuffle the cards and tickets
     * @return a GameState built using the private constructor
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng) {
        Deck<Ticket> ticketsDeck = Deck.of(tickets, rng);
        Deck<Card> cards = Deck.of(Constants.ALL_CARDS, rng);
        PlayerState playerState1 = PlayerState.initial(cards.topCards(Constants.INITIAL_CARDS_COUNT));
        cards = cards.withoutTopCards(Constants.INITIAL_CARDS_COUNT);
        PlayerState playerState2 = PlayerState.initial(cards.topCards(Constants.INITIAL_CARDS_COUNT));
        cards = cards.withoutTopCards(Constants.INITIAL_CARDS_COUNT);
        Map<PlayerId, PlayerState> map = new EnumMap<>(PlayerId.class);
        map.put(PlayerId.PLAYER_1, playerState1);
        map.put(PlayerId.PLAYER_2, playerState2);
        CardState cardState = CardState.of(cards);
        PlayerId firstPlayer = PlayerId.ALL.get(rng.nextInt(2));
        return new GameState(cardState, ticketsDeck, firstPlayer, map, null);
    }

    /**
     * @param playerId the Id of the player we want to get the public state
     * @return the public state associated to the given ID in the Map
     */
    @Override
    public PlayerState playerState(PlayerId playerId) {
        return playerState.get(playerId);
    }

    /**
     * @return the the current player's complete PlayerState
     */
    @Override
    public PlayerState currentPlayerState() {
        return playerState.get(currentPlayerId());
    }

    /**
     * @param count the amount of tickets we want to get
     * @return the tickets deck count top cards as a SortedBag
     * @throws IllegalArgumentException if the count is not between 0 and the ticket's deck size included
     */
    public SortedBag<Ticket> topTickets(int count) {
        Preconditions.checkArgument(count >= 0 && count <= ticketsCount());
        return tickets.topCards(count);
    }

    /**
     * @param count the amount of tickets to be removed
     * @return an identical GameState, just the tickets deck is changed, we remove it's count top cards
     * @throws IllegalArgumentException if the count is not between 0 and the ticket's deck size included
     */
    public GameState withoutTopTickets(int count) {
        Preconditions.checkArgument(count >= 0 && count <= ticketsCount());
        return new GameState(cardState, tickets.withoutTopCards(count), currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * @return the cards deck's top card
     * @throws IllegalArgumentException if the card deck is empty
     */
    public Card topCard() {
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return cardState.topDeckCard();
    }

    /**
     * @return a new GameState identical to this one, but the top card of the cards deck is removed
     * @throws IllegalArgumentException if the card deck is empty
     */
    public GameState withoutTopCard() {
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return new GameState(cardState.withoutTopDeckCard(), tickets, currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * @param discardedCards the cards to be add dto the discards
     * @return a new GameState identical to this one, but the given cards are added to the cardState's discard
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards) {
        return new GameState(cardState.withMoreDiscardedCards(discardedCards), tickets, currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * @param rng used to shuffle the cards and tickets if a new GameState is created
     * @return an object identical to this one, except if the cards deck is empty, in which case a new state with a deck recreated from discards is created
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng) {
        if (cardState.isDeckEmpty()) {
            return new GameState(cardState.withDeckRecreatedFromDiscards(rng), tickets, currentPlayerId(), playerState, lastPlayer());
        }
        return this;
    }

    /**
     * @param playerId      the ID of the player
     * @param chosenTickets the tickets to add to the player's State
     * @return a new GameState identical to this one, but the map is replaced with a new one containig the player's new state
     * @throws IllegalArgumentException if the player already owns at least one ticket
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets) {
        Preconditions.checkArgument(playerState.get(playerId).ticketCount() == 0);
        PlayerState newState = playerState.get(playerId).withAddedTickets(chosenTickets);
        Map<PlayerId, PlayerState> newMap = new EnumMap<>(playerState);
        newMap.put(playerId, newState);
        return new GameState(cardState, tickets, currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * @param drawnTickets  the tickets drawn by the player
     * @param chosenTickets the tickets the player chose to keep
     * @return a new state identical to the current one but the tickets the player chose are removed from the deck, a new map is added,
     * containing the refreshed PlayerState
     * @throws IllegalArgumentException if the chosen tickets are not contained in the drawn tickets
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets) {
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));
        PlayerState newState = playerState.get(currentPlayerId()).withAddedTickets(chosenTickets);
        Map<PlayerId, PlayerState> newMap = new EnumMap<>(playerState);
        newMap.put(currentPlayerId(), newState);
        return new GameState(cardState, tickets.withoutTopCards(drawnTickets.size()), currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * throws IllegalArgumentException if there are currently no cards to be drawn
     *
     * @param slot the slot of the card to be removed from the face up cards and put in the current player's hand
     * @return a new GameState identical to this one but the face up card at the given slot has benn replaced with
     * the deck's top card and placed in the player's hand
     * @throws IllegalArgumentException if it is not possible to draw cards
     */
    public GameState withDrawnFaceUpCard(int slot) {
        PlayerState newState = playerState.get(currentPlayerId()).withAddedCard(cardState.faceUpCard(slot));
        Map<PlayerId, PlayerState> newMap = new EnumMap<>(playerState);
        newMap.put(currentPlayerId(), newState);
        return new GameState(cardState.withDrawnFaceUpCard(slot), tickets, currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * @return a new state identical to this one but the card at the top of the deck has benn put in the current player's hand
     * @throws IllegalArgumentException if there are currently no cards to be drawn
     */
    public GameState withBlindlyDrawnCard() {
        PlayerState newState = playerState.get(currentPlayerId()).withAddedCard(cardState.topDeckCard());
        Map<PlayerId, PlayerState> newMap = new EnumMap<>(playerState);
        newMap.put(currentPlayerId(), newState);
        return new GameState(cardState.withoutTopDeckCard(), tickets, currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * @param route the route the current player has claimed
     * @param cards the cards he used to claim the route
     * @return a new state identical to this one but the route has benn added to the player's state
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards) {
        PlayerState newState = playerState.get(currentPlayerId()).withClaimedRoute(route, cards);
        Map<PlayerId, PlayerState> newMap = new EnumMap<>(playerState);
        newMap.put(currentPlayerId(), newState);
        CardState newCardState = cardState.withMoreDiscardedCards(cards);
        return new GameState(newCardState, tickets, currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * @return true if the last turn begins, which means that the last player is unknown but the current player has two or less cars left
     */
    public boolean lastTurnBegins() {
        if (lastPlayer() == null) return playerState.get(currentPlayerId()).carCount() <= 2;
        return false;
    }

    /**
     * @return a new state identical to this one, except that the current player is the one following the actual current player,
     * if lastTurnBegins returns true, the actual current player becomes the last player
     */
    public GameState forNextTurn() {
        if (lastTurnBegins()) {
            return new GameState(cardState, tickets, currentPlayerId().next(), Map.copyOf(playerState), currentPlayerId());
        }
        return new GameState(cardState, tickets, currentPlayerId().next(), Map.copyOf(playerState), lastPlayer());
    }
}
