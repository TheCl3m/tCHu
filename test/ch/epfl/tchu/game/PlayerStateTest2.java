package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest2 {
    private static final Station BAD = new Station(0, "Baden");
    private static final Station BAL = new Station(1, "Bâle");
    private static final Station BEL = new Station(2, "Bellinzone");
    private static final Station BER = new Station(3, "Berne");
    private static final Station BRI = new Station(4, "Brigue");
    private static final Station BRU = new Station(5, "Brusio");
    private static final Station COI = new Station(6, "Coire");
    private static final Station DAV = new Station(7, "Davos");
    private static final Station DEL = new Station(8, "Delémont");
    private static final Station FRI = new Station(9, "Fribourg");
    private static final Station GEN = new Station(10, "Genève");
    private static final Station INT = new Station(11, "Interlaken");
    private static final Station KRE = new Station(12, "Kreuzlingen");
    private static final Station LAU = new Station(13, "Lausanne");
    private static final Station LCF = new Station(14, "La Chaux-de-Fonds");
    private static final Station LOC = new Station(15, "Locarno");
    private static final Station LUC = new Station(16, "Lucerne");
    private static final Station LUG = new Station(17, "Lugano");
    private static final Station MAR = new Station(18, "Martigny");

    private static final List<Ticket> ALL_TICKETS = List.of(
            // City-to-city tickets
            new Ticket(BAL, BER, 5),
            new Ticket(BAL, BRI, 10),
            new Ticket(BAL, DAV, 8),
            new Ticket(BER, COI, 10),
            new Ticket(BER, LUG, 12),
            new Ticket(BER, LAU, 5),
            new Ticket(BER, LUG, 6),
            new Ticket(FRI, LUC, 5),
            new Ticket(GEN, BAL, 13),
            new Ticket(GEN, BER, 8),
            new Ticket(GEN, COI, 10),
            new Ticket(GEN, DAV, 14),
            new Ticket(INT, MAR, 7));

    private static final List<Card> cards = List.of(
            Card.BLACK,
            Card.BLUE,
            Card.GREEN,
            Card.ORANGE,
            Card.YELLOW,
            Card.LOCOMOTIVE,
            Card.WHITE,
            Card.VIOLET,
            Card.RED,
            Card.BLACK
    );

    private static final List<Route> ALL_ROUTES = List.of(
            new Route("AT1_STG_1", BER, DAV, 4, Route.Level.UNDERGROUND, null),
            new Route("AT2_VAD_1", DAV, LUG, 1, Route.Level.UNDERGROUND, Color.RED),
            new Route("BAD_BAL_1", BAD, BAL, 3, Route.Level.UNDERGROUND, Color.RED),
            new Route("BAD_OLT_1", BAD, LUG, 2, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("BAD_ZUR_1", BAD, BER, 1, Route.Level.OVERGROUND, Color.YELLOW),
            new Route("BAL_DE1_1", BAL, DAV, 1, Route.Level.UNDERGROUND, Color.BLUE),
            new Route("BAL_DEL_1", BAL, DEL, 2, Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("BAL_OLT_1", BAL, BER, 2, Route.Level.UNDERGROUND, Color.ORANGE),
            new Route("BEL_LOC_1", BEL, LOC, 1, Route.Level.UNDERGROUND, Color.BLACK),
            new Route("BEL_LUG_1", BEL, LUG, 1, Route.Level.UNDERGROUND, Color.RED),
            new Route("BEL_LUG_2", BEL, LUG, 1, Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("BEL_WAS_1", BEL, GEN, 4, Route.Level.UNDERGROUND, null));


    private static SortedBag.Builder<Ticket> TICKETS = new SortedBag.Builder<>();
    private static SortedBag.Builder<Card> CARD = new SortedBag.Builder<>();
    private static SortedBag.Builder<Card> drawnCards = new SortedBag.Builder<>();


    private static SortedBag<Ticket> createTickets() {
        for (Ticket t : ALL_TICKETS) {
            TICKETS.add(t);
        }
        return TICKETS.build();
    }

    private static SortedBag<Card> createCards(int nbrOfDuplicates) {
        for (int i = 0; i < nbrOfDuplicates; ++i) {
            for (Card t : cards) {
                CARD.add(t);
            }
        }
        return CARD.build();
    }

    private static SortedBag<Card> createRandomDrawnCards() {
        SortedBag.Builder<Card> dc = new SortedBag.Builder<>();
        Random r = new Random();
        dc.add(cards.get(r.nextInt(9)));
        dc.add(cards.get(r.nextInt(9)));
        dc.add(cards.get(r.nextInt(9)));
        return dc.build();
    }

    private static SortedBag<Card> createDrawnCards(Card c1, Card c2, Card c3) {
        SortedBag.Builder<Card> dc = new SortedBag.Builder<>();
        dc.add(c1);
        dc.add(c2);
        dc.add(c3);
        return dc.build();
    }

    private static SortedBag<Card> createCardsBag(Card c1, int c1Count, Card c2, int c2Count) {
        SortedBag.Builder<Card> dc = new SortedBag.Builder<>();
        dc.add(c1Count, c1);
        dc.add(c2Count, c2);
        return dc.build();
    }

    private static PlayerState ps = new PlayerState(createTickets(), createCards(6), ALL_ROUTES);


    @Test
    void possibleClaimCardsTest() {
        assertEquals("[{RED}, {LOCOMOTIVE}]", ps.possibleClaimCards(ALL_ROUTES.get(1)).toString());
        assertEquals("[{3×RED}, {2×RED, LOCOMOTIVE}, {RED, 2×LOCOMOTIVE}, {3×LOCOMOTIVE}]", ps.possibleClaimCards(ALL_ROUTES.get(2)).toString());
        assertEquals("[{4×BLACK}, {4×VIOLET}, {4×BLUE}, {4×GREEN}, {4×YELLOW}, {4×ORANGE}, {4×RED}, {4×WHITE}, {3×BLACK, LOCOMOTIVE}, {3×VIOLET, LOCOMOTIVE}, {3×BLUE, LOCOMOTIVE}, {3×GREEN, LOCOMOTIVE}, {3×YELLOW, LOCOMOTIVE}, {3×ORANGE, LOCOMOTIVE}, {3×RED, LOCOMOTIVE}, {3×WHITE, LOCOMOTIVE}, {2×BLACK, 2×LOCOMOTIVE}, {2×VIOLET, 2×LOCOMOTIVE}, {2×BLUE, 2×LOCOMOTIVE}, {2×GREEN, 2×LOCOMOTIVE}, {2×YELLOW, 2×LOCOMOTIVE}, {2×ORANGE, 2×LOCOMOTIVE}, {2×RED, 2×LOCOMOTIVE}, {2×WHITE, 2×LOCOMOTIVE}, {BLACK, 3×LOCOMOTIVE}, {VIOLET, 3×LOCOMOTIVE}, {BLUE, 3×LOCOMOTIVE}, {GREEN, 3×LOCOMOTIVE}, {YELLOW, 3×LOCOMOTIVE}, {ORANGE, 3×LOCOMOTIVE}, {RED, 3×LOCOMOTIVE}, {WHITE, 3×LOCOMOTIVE}, {4×LOCOMOTIVE}]", ps.possibleClaimCards(ALL_ROUTES.get(0)).toString());
    }

    @Test
    void possibleAdditionalCardsWithWrongArguments() {
        assertThrows(IllegalArgumentException.class, () -> {
            ps.possibleAdditionalCards(0, createCards(6));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ps.possibleAdditionalCards(1, createCards(6));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ps.possibleAdditionalCards(1, createCards(6));
        });
    }

    @Test
    void possibleAdditionalCardsTest() {
        assertEquals("[{3×BLACK}, {2×BLACK, LOCOMOTIVE}, {BLACK, 2×LOCOMOTIVE}, {3×LOCOMOTIVE}]", ps.possibleAdditionalCards(3, createCardsBag(Card.BLACK, 3, Card.LOCOMOTIVE, 1)).toString());
        assertEquals("[{2×BLACK}, {BLACK, LOCOMOTIVE}, {2×LOCOMOTIVE}]", ps.possibleAdditionalCards(2, createCardsBag(Card.BLACK, 3, Card.BLACK, 1)).toString());
        assertEquals("[{LOCOMOTIVE}]", ps.possibleAdditionalCards(1, createCardsBag(Card.LOCOMOTIVE, 3, Card.LOCOMOTIVE, 1)).toString());
        assertEquals("[{LOCOMOTIVE}]", ps.possibleAdditionalCards(1, createCardsBag(Card.LOCOMOTIVE, 0, Card.LOCOMOTIVE, 1)).toString());
        assertEquals("[{2×GREEN}, {GREEN, LOCOMOTIVE}, {2×LOCOMOTIVE}]", ps.possibleAdditionalCards(2, createCardsBag(Card.LOCOMOTIVE, 0, Card.GREEN, 1)).toString());
        System.out.println(createCardsBag(Card.LOCOMOTIVE, 0, Card.LOCOMOTIVE, 1));
    }
}