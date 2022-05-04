package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerStateTest4 {

    private SortedBag<Ticket> playerTickets;
    private List<Route> playerRoutes;
    private SortedBag<Card> playerCards;

    @Test
    void testingPossibleClaimCards() {
        SortedBag.Builder<Card> B = new SortedBag.Builder<>();
        B.add(2, Card.GREEN);
        B.add(2, Card.BLACK);
        B.add(3, Card.RED);
        B.add(1, Card.LOCOMOTIVE);
        SortedBag<Card> C1 = B.build();

        List<SortedBag<Card>> C2 = new ArrayList<>();
        C2.add(SortedBag.of(2, Card.GREEN));
        C2.add(SortedBag.of(1, Card.GREEN, 1, Card.LOCOMOTIVE));
        C2.add(SortedBag.of(2, Card.LOCOMOTIVE));

        PlayerState ps = new PlayerState(SortedBag.of(), C1, List.of());
        Station s1 = new Station(0, "Station 1");
        Station s2 = new Station(1, "Station 2");
        Route r = new Route("Tariq Michel Aoun", s1, s2, 2, Route.Level.UNDERGROUND, Color.GREEN);

        List<SortedBag<Card>> C3 = r.possibleClaimCards();

        List<SortedBag<Card>> lstp = ps.possibleClaimCards(r);

        //assertEquals(2,lstp.size());
        assertEquals(C2, C3);
        assertEquals(true, ps.canClaimRoute(r));
    }

    @Test
    void testingPossibleAdditionalCards() {
        int additionalCardsCount;
        SortedBag<Card> initialCards;
        SortedBag<Card> drawnCards;


        // Tickets
        SortedBag.Builder<Ticket> T = new SortedBag.Builder<>();
        T.add(ChMap.tickets().get(3));
        T.add(ChMap.tickets().get(15));
        T.add(ChMap.tickets().get(9));
        playerTickets = T.build();

        // Routes
        playerRoutes = new ArrayList<>();
        playerRoutes.add(ChMap.routes().get(2));
        playerRoutes.add(ChMap.routes().get(3));
        playerRoutes.add(ChMap.routes().get(4));
        playerRoutes.add(ChMap.routes().get(13));
        playerRoutes.add(ChMap.routes().get(15));

        SortedBag.Builder<Card> B = new SortedBag.Builder<>();
        B.add(1, Card.GREEN);
        B.add(1, Card.BLUE);
        B.add(1, Card.VIOLET);
        B.add(1, Card.LOCOMOTIVE);
        playerCards = B.build();

        PlayerState ps = new PlayerState(playerTickets, playerCards, playerRoutes);


        ps = ps.initial(playerCards);

        additionalCardsCount = 2;
        initialCards = SortedBag.of(3, Card.GREEN);

        B = new SortedBag.Builder<>();
        B.add(1, Card.GREEN);
        B.add(1, Card.VIOLET);
        B.add(1, Card.LOCOMOTIVE);
        drawnCards = B.build();

        List<SortedBag<Card>> lpac = ps.possibleAdditionalCards(additionalCardsCount, initialCards);
        assertEquals(0, lpac.size());
    }

    @Test
    void testingTickets() {
        Ticket addTicket = ChMap.tickets().get(12);// 7
        Card addCard = Card.GREEN;
        Route addRoute = ChMap.routes().get(10); // 1

        SortedBag.Builder<Ticket> T = new SortedBag.Builder<>();
        T.add(ChMap.tickets().get(3)); // 10
        T.add(ChMap.tickets().get(15)); // 8
        T.add(ChMap.tickets().get(9)); // 8
        playerTickets = T.build();
        SortedBag.Builder<Card> B = new SortedBag.Builder<>();
        B.add(1, Card.GREEN);
        B.add(1, Card.BLUE);
        B.add(1, Card.YELLOW);
        B.add(1, Card.LOCOMOTIVE);
        playerCards = B.build();
        playerRoutes = new ArrayList<>();
        PlayerState ps = new PlayerState(playerTickets, playerCards, playerRoutes);

        assertEquals(playerTickets, ps.tickets());
        assertEquals(playerCards, ps.cards());
        assertEquals(playerRoutes, ps.routes());

        T.add(addTicket);
        playerTickets = T.build();
        B.add(addCard);
        playerCards = B.build();
        playerRoutes.add(addRoute);

        PlayerState ps2 = new PlayerState(playerTickets, playerCards, playerRoutes);

        assertEquals(ps2.tickets(), ps.withAddedTickets(SortedBag.of(1, addTicket)).tickets());
        assertEquals(ps2.cards(), ps.withAddedCard(addCard).cards());
        assertTrue(ps.canClaimRoute(addRoute));

        Set<SortedBag<Card>> S = SortedBag.of(1, Card.YELLOW, 1, Card.LOCOMOTIVE).subsetsOfSize(1);
        List<SortedBag<Card>> L = new ArrayList<>(S);
        L.sort(Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));

        assertEquals(L, ps.possibleClaimCards(addRoute));
        assertEquals(ps2.routes(), ps.withClaimedRoute(addRoute, L.get(0)).routes());

        playerRoutes.add(ChMap.routes().get(13));
        playerRoutes.add(ChMap.routes().get(44));
        playerRoutes.add(ChMap.routes().get(46));

        ps = new PlayerState(playerTickets, playerCards, playerRoutes);

        assertEquals(-17, ps.ticketPoints());
        assertEquals(-4, ps.finalPoints());

        playerRoutes.add(ChMap.routes().get(16));
        ps = new PlayerState(playerTickets, playerCards, playerRoutes);

        assertEquals(-1, ps.ticketPoints());
        assertEquals(19, ps.finalPoints());
    }

}