package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * the ObservableGameState represents the observable state of a tCHu game
 * each of its instances is specific to a player
 * it contains the public state of the game, and the given player's total state
 *
 * @author Clément Husler 328105
 * @author Mathieu Faure 328086
 */
public final class ObservableGameState {
    private final PlayerId playerId;
    private PublicGameState gameState;
    private PlayerState playerState;
    private final IntegerProperty ticketPercentageRemaining = zeroIntProperty();
    private final IntegerProperty cardPercentageRemaining = zeroIntProperty();
    private final List<ObjectProperty<Card>> faceUpCards = createFaceUpCards();

    private final Map<Route, ObjectProperty<PlayerId>> routeOwner = createRouteOwners();
    private final Map<PlayerId, IntegerProperty> playersTicketsCount = createIntPropertyMap();
    private final Map<PlayerId, IntegerProperty> playersCardsCount = createIntPropertyMap();
    private final Map<PlayerId, IntegerProperty> playersWagonsCount = createIntPropertyMap();
    private final Map<PlayerId, IntegerProperty> playersBuildPoints = createIntPropertyMap();

    private final Map<Ticket, IntegerProperty> playerTicketsPoints = new HashMap<>();
    private final ObservableList<Ticket> playerTicketsList = FXCollections.observableArrayList();
    private final Map<Card, IntegerProperty> playerCardTypeCount = createCardTypeCount();
    private final Map<Route, BooleanProperty> claimableRoutes = createClaimableRoutes();

    private final ObjectProperty<Card> lastDrawnCard = new SimpleObjectProperty<>(null);

    /**
     * Public constructor of the ObservableGameState class
     *
     * @param playerId the ID of the player who the instance belongs to
     */
    public ObservableGameState(PlayerId playerId) {
        this.playerId = playerId;
    }

    /**
     * @return true if the player is able to draw tickets, false else
     */
    public boolean canDrawTickets() {
        return gameState.canDrawTickets();
    }

    /**
     * @return true if the player is able to draw cards, false else
     */
    public boolean canDrawCards() {
        return gameState.canDrawCards();
    }

    /**
     * @param route the route to check if it can be claimed by the player
     * @return true if the player can claim the given route
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        return playerState.possibleClaimCards(route);
    }

    /**
     * updates the ObservableGameState
     *
     * @param newGameState   the new GameState to be displayed
     * @param newPlayerState the new PlayerState to be displayed
     */
    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        gameState = newGameState;
        playerState = newPlayerState;
        updateFaceUpCards();
        updateClaimableRoutes(updateRouteOwners()); //updates the owners and informs the claimable routes of the twin routes that are claimed
        updateCardCount();
        updatePercentages();
        updatePlayerTickets();
        updatePlayersCounts();
    }

    /**
     * updates each of the properties representing the five face up cards
     */
    private void updateFaceUpCards() {
        for (int slot : Constants.FACE_UP_CARD_SLOTS) {
            Card newCard = gameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }
    }

    /**
     * updates each boolean property containing if a route is claimable or not
     */
    private void updateClaimableRoutes(Set<List<Station>> twinChecker) {
        ChMap.routes().forEach(route -> claimableRoutes.get(route).set(playerState.canClaimRoute(route) && !twinChecker.contains(route.stations())));
    }

    /**
     * updates the route owners
     *
     * @return the set containing the stations of all the claimed routes,
     * to check for twin routes and block the "claimability" of the twin
     */
    private Set<List<Station>> updateRouteOwners() {
        HashSet<List<Station>> twinRoutesCheck = new HashSet<>();
        for (Route route : gameState.claimedRoutes()) {
            if (playerState.routes().contains(route)) {
                routeOwner.get(route).set(playerId);
            } else {
                routeOwner.get(route).set(playerId.next());
            }
            twinRoutesCheck.add(route.stations());
        }
        return twinRoutesCheck;
    }

    /**
     * updates the percentages of the tickets and of the cards left in the decks
     */
    private void updatePercentages() {
        double ticketPercentage = (double) gameState.ticketsCount() / ChMap.tickets().size() * 100;
        double cardPercentage = (double) gameState.cardState().deckSize() / Constants.ALL_CARDS.size() * 100;
        ticketPercentageRemaining.set((int) ticketPercentage);
        cardPercentageRemaining.set((int) cardPercentage);
    }

    /**
     * updates the properties containing the amount of card the player owns for each card type
     */
    private void updateCardCount() {
        lastDrawnCard.set(null);
        for (Card c : Card.ALL) {
            int oldCount = playerCardTypeCount.get(c).get();
            int newCount = playerState.cards().countOf(c);
            if (newCount == oldCount + 1) {
                lastDrawnCard.set(c);
            }
            playerCardTypeCount.get(c).set(newCount);
        }
    }


    /**
     * adds the tickets contained in the PlayerState to the property concerning the tickets the player owns
     * if they are not already in it
     */
    private void updatePlayerTickets() {
        Map<Ticket, Integer> status = playerState.ticketsStatus();
        playerState.tickets().toList().forEach(t -> {
            if (playerTicketsPoints.containsKey(t)) {
                playerTicketsPoints.get(t).set(status.get(t));
            } else {
                playerTicketsPoints.put(t, new SimpleIntegerProperty(status.get(t)));
            }
        });
        playerTicketsList.setAll(playerState.tickets().toList());
    }

    private void updatePlayersCounts() {
        PlayerId otherId = playerId.next();
        PublicPlayerState otherState = gameState.playerState(otherId);
        playersTicketsCount.get(playerId).set(playerState.ticketCount());
        playersTicketsCount.get(otherId).set(otherState.ticketCount());
        playersCardsCount.get(playerId).set(playerState.cardCount());
        playersCardsCount.get(otherId).set(otherState.cardCount());
        playersWagonsCount.get(playerId).set(playerState.carCount());
        playersWagonsCount.get(otherId).set(otherState.carCount());
        playersBuildPoints.get(playerId).set(playerState.claimPoints());
        playersBuildPoints.get(otherId).set(otherState.claimPoints());
    }

    /**
     * creates the list of properties containing each of the face up cards
     *
     * @return the list of these properties
     */
    private static List<ObjectProperty<Card>> createFaceUpCards() {
        ArrayList<ObjectProperty<Card>> list = new ArrayList<>();
        for (int i = 0; i < Constants.FACE_UP_CARDS_COUNT; ++i) {
            list.add(new SimpleObjectProperty<>(null));
        }
        return list;
    }

    /**
     * creates the map associating each route with the ID of the player owning it
     *
     * @return this map
     */
    private static Map<Route, ObjectProperty<PlayerId>> createRouteOwners() {
        HashMap<Route, ObjectProperty<PlayerId>> map = new HashMap<>();
        ChMap.routes().forEach(route -> map.put(route, new SimpleObjectProperty<>(null)));
        return map;
    }

    /**
     * creates the map associating each of the game's route with a boolean
     * this boolean is true if and only if the player is able to claim this route, which means
     * that he is the current player, that he has the required cards, and that the
     * route and its twin are free
     *
     * @return the map associating each route with a boolean
     */
    private static Map<Route, BooleanProperty> createClaimableRoutes() {
        HashMap<Route, BooleanProperty> map = new HashMap<>();
        ChMap.routes().forEach(route -> map.put(route, new SimpleBooleanProperty(false)));
        return map;
    }

    /**
     * @return a simple integer property with a value of 0
     */
    private static IntegerProperty zeroIntProperty() {
        return new SimpleIntegerProperty(0);
    }

    /**
     * creates a map with each playerId as a key
     * associated with an integerProperty initialized to 0
     *
     * @return the created map
     */
    private static Map<PlayerId, IntegerProperty> createIntPropertyMap() {
        EnumMap<PlayerId, IntegerProperty> map = new EnumMap<>(PlayerId.class);
        map.put(PlayerId.PLAYER_1, zeroIntProperty());
        map.put(PlayerId.PLAYER_2, zeroIntProperty());
        return map;
    }

    /**
     * creates a map associating each card type with an integer property,
     * which corresponds to the count of cards of this type the player owns
     *
     * @return this map
     */
    private static Map<Card, IntegerProperty> createCardTypeCount() {
        HashMap<Card, IntegerProperty> map = new HashMap<>();
        for (Card c : Card.ALL) {
            map.put(c, zeroIntProperty());
        }
        return map;
    }

    /**
     * @return the property representing the percentage of tickets left in the deck
     */
    public ReadOnlyIntegerProperty ticketPercentage() {
        return ticketPercentageRemaining;
    }

    /**
     * @return the percentage of cards remaining in the card deck
     */
    public ReadOnlyIntegerProperty cardPercentage() {
        return cardPercentageRemaining;
    }

    /**
     * @param slot the slot of the face up card to get
     * @return the property corresponding to the card at the given slot
     */
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    /**
     * @param route the route we want to know the owner
     * @return the property corresponding to the playerId corresponding to the player who owns the given route
     */
    public ReadOnlyObjectProperty<PlayerId> getRouteOwner(Route route) {
        return routeOwner.get(route);
    }

    /**
     * @param route the Route to check if the player can claim it
     * @return the property of the boolean telling if the given route can be claimed by the player
     */
    public ReadOnlyBooleanProperty canClaimRoute(Route route) {
        return claimableRoutes.get(route);
    }

    /**
     * @return the list of the tickets the player owns
     */
    public ObservableList<Ticket> playersTicket() {
        return FXCollections.unmodifiableObservableList(playerTicketsList);
    }

    /**
     * @param ticket the ticket
     * @return the points that the ticket grants to the player
     */
    public ReadOnlyIntegerProperty ticketConnected(Ticket ticket) {
        return playerTicketsPoints.get(ticket);
    }

    /**
     * return the integer property corresponding to the amount of cards of a given type the player owns
     *
     * @param card the card type
     * @return the amount of cards of the type owned by the player
     */
    public ReadOnlyIntegerProperty getCardCount(Card card) {
        return playerCardTypeCount.get(card);
    }

    /**
     * method used to get the count of tickets associated to a player's ID
     *
     * @param playerId the player's ID
     * @return the player's ticket count
     */
    public ReadOnlyIntegerProperty getPlayerTicketCount(PlayerId playerId) {
        return playersTicketsCount.get(playerId);
    }

    /**
     * method used to get the count of cards associated to a player's ID
     *
     * @param playerId the player's ID
     * @return the player's card count
     */
    public ReadOnlyIntegerProperty getPlayerCardCount(PlayerId playerId) {
        return playersCardsCount.get(playerId);
    }

    /**
     * method used to get the wagon count associated with a player's ID
     *
     * @param playerId the player's ID
     * @return the player's count of wagons
     */
    public ReadOnlyIntegerProperty getPlayerWagonCount(PlayerId playerId) {
        return playersWagonsCount.get(playerId);
    }

    /**
     * method used to get the build points count associated with a player's ID
     *
     * @param playerId the player's ID
     * @return the player's count build points
     */
    public ReadOnlyIntegerProperty getPlayerBuildPoints(PlayerId playerId) {
        return playersBuildPoints.get(playerId);
    }

    public ObjectProperty<Card> getLastDrawnCard() {
        return lastDrawnCard;
    }
}
