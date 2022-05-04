package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * This class represents the lowest level AI of our tCHu game
 *
 * @author Mathieu Faure 328086
 * @author Clément Husler 328105
 */
public class SimpleAI implements AI {
    private final Random random = new Random();
    private PublicGameState gameState;
    private PlayerState playerState;
    private SortedBag<Ticket> ticketChoice;
    private Route route;
    private PublicCardState cardState;
    private List<Card> faceUpCards;
    private final List<Route> availableRoutes = new ArrayList<>();
    private final List<Station> ticketStations = new ArrayList<>();
    private final List<Ticket> ownedTickets = new ArrayList<>();
    private final int turnIndex = 0;
    private Route routeClaimedLastTurn;

    /**
     * @param newState the player's new state
     * @param ownState the player's old state
     * @see Player#updateState(PublicGameState, PlayerState)
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        this.gameState = newState;
        this.playerState = ownState;
        cardState = newState.cardState();
        faceUpCards = cardState.faceUpCards();
        availableRoutes.clear();
        for (Ticket ticket : ownedTickets) {
            for (Trip trip : ticket.getTrips()) {
                ticketStations.add(trip.from());
                ticketStations.add(trip.to());
            }
        }
        HashSet<List<Station>> twinChecker = new HashSet<>();
        gameState.claimedRoutes().forEach(route -> twinChecker.add(route.stations()));
        for (Route r : ChMap.routes()) {
            if (!gameState.claimedRoutes().contains(r) && !twinChecker.contains(r.stations()) && playerState.canClaimRoute(r)) {
                availableRoutes.add(r);
            }
        }
    }

    /**
     * @param tickets the tickets to choose from
     * @see Player#setInitialTicketChoice(SortedBag)
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        this.ticketChoice = tickets;
    }

    /**
     * @return a bag containing the three first tickets the bot can choose from at the start of the game
     * @see Player#chooseInitialTickets()
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        SortedBag.Builder<Ticket> bag = new SortedBag.Builder<>();
        bag.add(ticketChoice.get(0));
        ownedTickets.add(ticketChoice.get(0));
        bag.add(ticketChoice.get(1));
        ownedTickets.add(ticketChoice.get(1));
        bag.add(ticketChoice.get(2));
        ownedTickets.add(ticketChoice.get(2));
        return bag.build();
    }

    /**
     * chooses randomly an integer between 0 and 99 and following which one it is and the possibilities,
     * chooses a TurnKind for the next turn
     *
     * @return the chosen TurnKind
     */
    @Override
    public TurnKind nextTurn() {
        int t = random.nextInt(100);
        if (gameState.canDrawTickets() && gameState.canDrawCards() && !availableRoutes.isEmpty()) {
            if (t < 5) {
                return TurnKind.DRAW_TICKETS;
            } else if (t < 35) {
                return TurnKind.CLAIM_ROUTE;
            }
            return TurnKind.DRAW_CARDS;
        } else if (gameState.canDrawTickets() && gameState.canDrawCards() && availableRoutes.isEmpty()) {

            if (t < 5) {
                return TurnKind.DRAW_TICKETS;
            }
            return TurnKind.DRAW_CARDS;
        } else if (!gameState.canDrawTickets() && gameState.canDrawCards() && !availableRoutes.isEmpty()) {
            if (t < 35) {
                return TurnKind.CLAIM_ROUTE;
            }
            return TurnKind.DRAW_CARDS;
        } else if (gameState.canDrawTickets() && !gameState.canDrawCards() && !availableRoutes.isEmpty()) {
            if (t < 5) {
                return TurnKind.DRAW_TICKETS;
            }
            return TurnKind.CLAIM_ROUTE;
        } else if (gameState.canDrawTickets() && !gameState.canDrawCards() && availableRoutes.isEmpty()) {
            return TurnKind.DRAW_TICKETS;
        } else if (!gameState.canDrawTickets() && gameState.canDrawCards() && availableRoutes.isEmpty()) {
            return TurnKind.DRAW_CARDS;
        } else if (!gameState.canDrawTickets() && !gameState.canDrawCards() && !availableRoutes.isEmpty()) {
            return TurnKind.CLAIM_ROUTE;
        }
        return TurnKind.DRAW_CARDS;
    }

    /**
     * if the player already completed the ticket, chooses this ticket
     * else, chooses the one that makes him lose the least points
     *
     * @param options the options to choose from
     * @return the ticket that makes lose the least points if none of the tickets are already connected by the player
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        int minimalPoints = Integer.MAX_VALUE;
        ;
        PlayerState pS;
        Ticket minimalTicket = options.get(0);
        SortedBag.Builder<Ticket> connectedTickets = new SortedBag.Builder<>();
        for (Ticket ticket : options) {
            pS = playerState.withAddedTickets(SortedBag.of(ticket));
            if (pS.ticketsStatus().get(ticket) > 0) {
                connectedTickets.add(ticket);
                ownedTickets.add(ticket);
            }
            int ticketPoints = pS.ticketsStatus().get(ticket);
            if (ticketPoints < minimalPoints) {
                minimalPoints = ticketPoints;
                minimalTicket = ticket;
            }
        }
        if (!connectedTickets.isEmpty()) {
            return connectedTickets.build();
        }
        ownedTickets.add(minimalTicket);
        return SortedBag.of(minimalTicket);
    }

    /**     *chooses a slot too draw a card from at random
     * @return the chosen slot
     */
    @Override
    public int drawSlot() {int s = random.nextInt(6);
        if (s == 5) {
            return Constants.DECK_SLOT;
        }
        return s;
    }

    /**
     * @return the route that, if claimed, would connect with the route claimed the previous time
     */
    @Override
    public Route claimedRoute() {


        if (turnIndex == 0) {
            Station station = ticketStations.get(random.nextInt(ticketStations.size()));
            ticketStations.remove(station);
            for (Route route1 : availableRoutes) {
                if (route1.station1() == station || route1.station2() == station) {
                    routeClaimedLastTurn = route1;

                    route = route1;
                    return route1;
                }
            }
            routeClaimedLastTurn = availableRoutes.get(random.nextInt(availableRoutes.size()));


            route = routeClaimedLastTurn;
            return routeClaimedLastTurn;
        } else {
            List<Route> connectedToLast = new ArrayList<>();
            for (Route r : availableRoutes) {
                if (routeClaimedLastTurn.stations().contains(r.station1()) || routeClaimedLastTurn.stations().contains(r.station2())) {
                    connectedToLast.add(r);
                }
            }
            if (connectedToLast.isEmpty()) {
                routeClaimedLastTurn = availableRoutes.get(random.nextInt(availableRoutes.size()));

                route = routeClaimedLastTurn;
                return routeClaimedLastTurn;
            } else {
                for (Route route2 : connectedToLast) {
                    if (ticketStations.contains(route2.station1()) && ticketStations.contains(route2.station2())) {
                        routeClaimedLastTurn = route2;

                        route = route2;
                        return route2;
                    }
                }

                for (Route route2 : connectedToLast) {
                    if (ticketStations.contains(route2.station1()) || ticketStations.contains(route2.station1())) {
                        routeClaimedLastTurn = route2;

                        route = route2;
                        return route2;
                    }
                }
                route = connectedToLast.get(random.nextInt(connectedToLast.size()));
                return route;

            }


        }
    }

    /**
     * chooses the first SortedBag of initial claim cards
     *
     * @return this bag
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        return playerState.possibleClaimCards(route).get(0);
    }

    /**
     * @param options the options the player has to choose from
     * @return the first bag of the list
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        if (options.isEmpty()) {
            return SortedBag.of();
        }
        return options.get(0);
    }


}
