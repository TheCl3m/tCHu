package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * PlayerState represents a player's total game state, including the cards and tickets he owns as well as the routes he claimed
 *
 * @author Mathieu Faure (328086)
 * @author Clément Husler (328105)
 */
public final class PlayerState extends PublicPlayerState {

    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;

    /**
     * public constructor for the PlayerState class
     *
     * @param tickets the player's tickets as a SortedBag
     * @param cards   the player's cards as a SortedBag
     * @param routes  the player's routes as a list
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {
        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards = cards;
    }

    /**
     * @param initialCards the player's cards at the start of the game
     * @return a new PlayerState with initialCards as the player's initial cards
     * @throws IllegalArgumentException if the size of initialCards is not 4
     */
    public static PlayerState initial(SortedBag<Card> initialCards) {
        Preconditions.checkArgument(initialCards.size() == Constants.INITIAL_CARDS_COUNT);
        return new PlayerState(SortedBag.of(), initialCards, List.of());
    }

    /**
     * @return the player's tickets SortedBag
     */
    public SortedBag<Ticket> tickets() {
        return tickets;
    }

    /**
     * @return the player's cards SortedBag
     */
    public SortedBag<Card> cards() {
        return cards;
    }

    /**
     * @param newTickets, the tickets to add to the player's ticket bag
     * @return a new PlayerState similar to the current one but with the tickets added to the player's bag
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets) {
        return new PlayerState(this.tickets.union(newTickets), this.cards, this.routes());
    }

    /**
     * @param card the card to add to the player's bag
     * @return a new PlayerState similar to this one but with the card added to the player's bag
     */
    public PlayerState withAddedCard(Card card) {
        return new PlayerState(this.tickets, this.cards.union(SortedBag.of(card)), this.routes());
    }


    /**
     * checks if the player can claim a route depending on his cards
     *
     * @param route the route the player would claim
     * @return true if the player can claim the route, false otherwise
     */
    public boolean canClaimRoute(Route route) {
        if (carCount() < route.length()) {
            return false;
        } else return !possibleClaimCards(route).isEmpty();
    }

    /**
     * @param route the route the player wants to claim
     * @return the list of all the cards sets that can be used to claim the route
     * @throws IllegalArgumentException if the player doesn't have enough wagons to claim the route
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        Preconditions.checkArgument(this.carCount() >= route.length());
        List<SortedBag<Card>> routeClaimBags = route.possibleClaimCards();
        List<SortedBag<Card>> possibleClaimBags = new ArrayList<>();
        for (SortedBag<Card> bag : routeClaimBags) {
            if (cards.contains(bag)) {
                possibleClaimBags.add(bag);
            }
        }
        return possibleClaimBags;
    }

    /**
     * @param additionalCardsCount the amount of additional cards to use to claim the route
     * @param initialCards         the cards initially used to claim the route
     * @return the list of all the combinations of additionalCardCount cards the player could use to claim the tunnel
     * @throws IllegalArgumentException if the amount of additional cards is not between 1 and 3 included,
     *                                  if the initialCards bag is empty or contains more than two card types,
     *                                  or if drawnCards does not contain exactly 3 cards
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards) {
        Preconditions.checkArgument(additionalCardsCount >= 1 && additionalCardsCount <= Constants.ADDITIONAL_TUNNEL_CARDS &&
                initialCards.size() > 0 && initialCards.toSet().size() <= 2);
        Card tunnelColor = tunnelColor(initialCards);
        SortedBag<Card> cardsAfterFirstClaim = cards.difference(initialCards);//all the player's cards without the ones he used to claim the route
        if (cardsAfterFirstClaim.size() < additionalCardsCount) { //the case where the player has no more cards after claiming the route for the first time
            return List.of();
        }
        SortedBag.Builder<Card> usableCards = new SortedBag.Builder<>();
        for (Card card : cardsAfterFirstClaim) {
            if (card == tunnelColor || card == Card.LOCOMOTIVE) {
                usableCards.add(card);
            }
        }
        SortedBag<Card> usableCardsBag = usableCards.build();
        if (usableCardsBag.size() < additionalCardsCount) {
            return List.of();
        } else {
            Set<SortedBag<Card>> subsets = usableCardsBag.subsetsOfSize(additionalCardsCount);
            List<SortedBag<Card>> options = new ArrayList<>(subsets);
            options.sort(Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));
            return options;
        }
    }

    /**
     * @param initialCards the cards used to claim the tunnel
     * @return the color of the claimed tunnel
     */
    private Card tunnelColor(SortedBag<Card> initialCards) {
        for (Card card : initialCards) {
            if (card != Card.LOCOMOTIVE) {
                return card;
            }
        }
        return Card.LOCOMOTIVE;
    }

    /**
     * @param route      the route to add to the player's routes list
     * @param claimCards the cards used to claim the route
     * @return a new PlayerState with the route added to its and without the cards used to claim the route
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards) {
        List<Route> newRoutes = new ArrayList<>(this.routes());
        newRoutes.add(route);
        return new PlayerState(this.tickets, this.cards.difference(claimCards), newRoutes);
    }

    /**
     * @return the amount of points the player has earned with his tickets, can be positive or negative
     */
    public int ticketPoints() {
        StationPartition partition = buildPartition();
        int totalPoints = 0;
        for (Ticket ticket : tickets) {
            totalPoints += ticket.points(partition);
        }
        return totalPoints;
    }

    /**
     * @return a map mapping each ticket to it's current points
     */
    public Map<Ticket, Integer> ticketsStatus() {
        StationPartition partition = buildPartition();
        HashMap<Ticket, Integer> status = new HashMap<>();
        tickets.forEach(ticket -> status.put(ticket, ticket.points(partition)));
        return status;
    }


    private StationPartition buildPartition() {
        StationPartition.Builder builder = new StationPartition.Builder(computeMaxStationId() + 1);
        for (Route route : this.routes()) {
            builder.connect(route.station1(), route.station2());
        }
        return builder.build();
    }

    /**
     * finds the highest id of the player's stations
     *
     * @return the highest id
     */
    private int computeMaxStationId() {
        int maxStationId = 0;
        for (Route route : this.routes()) {
            int id1 = route.station1().id();
            int id2 = route.station2().id();
            maxStationId = Math.max(maxStationId, id1);
            maxStationId = Math.max(maxStationId, id2);
        }
        return maxStationId;
    }

    /**
     * Computes the final points of the player adding his claimPoints and ticketPoints
     *
     * @return the final points of the player
     */
    public int finalPoints() {
        return claimPoints() + ticketPoints();
    }
}
