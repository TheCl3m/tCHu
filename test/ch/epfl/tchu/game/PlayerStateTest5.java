package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PlayerStateTest5 {

    @Test
    void initialTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            PlayerState.initial(SortedBag.of());
        });
        SortedBag.Builder<Card> CardsB = new SortedBag.Builder<>();
        CardsB.add(4, Card.RED);
        PlayerState player2 = new PlayerState(new SortedBag.Builder<Ticket>().build(), CardsB.build(), List.of());
        assertEquals(player2.cards(),
                PlayerState.initial(CardsB.build()).cards());
    }
    @Test
    void withAddedTicketsTest() {
        SortedBag.Builder<Ticket> ticketsB = new SortedBag.Builder<>();
        SortedBag.Builder<Ticket> ticketsB2 = new SortedBag.Builder<>();
        SortedBag.Builder<Card> cardsB = new SortedBag.Builder<>();
        ticketsB.add(new Ticket(Map.DAV, Map.LCF, 5));
        ticketsB2.add(new Ticket(Map.DAV, Map.LCF, 5));
        for(Ticket t : Map.ALL_TICKETS) {
            ticketsB.add(t);
        }
        for(Card c : Map.CARDS) {
            cardsB.add(c);
            cardsB.add(c);
        }
        PlayerState player2 = new PlayerState(ticketsB.build(), cardsB.build(), Map.ALL_ROUTES);
        assertEquals(player2.tickets(), Map.player().withAddedTickets(ticketsB2.build()).tickets());
    }
    @Test
    void withAddedCardTest() {
        SortedBag.Builder<Card> cardsB = new SortedBag.Builder<>();
        SortedBag.Builder<Ticket> ticketsB = new SortedBag.Builder<>();
        for(Ticket t : Map.ALL_TICKETS) {
            ticketsB.add(t);
        }
        for(Card c : Map.CARDS) {
            cardsB.add(c);
            cardsB.add(c);
        }
        cardsB.add(Card.GREEN);
        PlayerState player2 = new PlayerState(ticketsB.build(), cardsB.build(), List.of());
        assertEquals(player2.cards(), Map.player().withAddedCard(Card.GREEN).cards());
    }
    @Test
    void withClaimedRouteTest() {
        Route route = new Route("BAD_ZUR_1", Map.BAD, Map.BER, 1, Route.Level.OVERGROUND, Color.YELLOW);
        SortedBag.Builder<Card> cardsB = new SortedBag.Builder<>();
        cardsB.add(Card.YELLOW);
        Map.player().withClaimedRoute(route, cardsB.build());
    }

    @Test
    void ticketPointsTest() {
        SortedBag.Builder<Ticket> ticketsB = new SortedBag.Builder<>();
        SortedBag.Builder<Card> cardsB = new SortedBag.Builder<>();
        ticketsB.add(new Ticket(Map.BAL, Map.BER, 1));
        PlayerState player = new PlayerState(ticketsB.build(), cardsB.build(),
                List.of(new Route("BAL_BER_1", Map.BAL, Map.BER, 1, Route.Level.UNDERGROUND, Color.RED)));
        assertEquals(1, player.ticketPoints());
    }
    private static class Map{

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

        private static final List<Card> CARDS = List.of(
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

        private static PlayerState player() {

            SortedBag.Builder<Ticket> ticketsB = new SortedBag.Builder<>();
            SortedBag.Builder<Card> cardsB = new SortedBag.Builder<>();

            for(Ticket t : ALL_TICKETS) {
                ticketsB.add(t);
            }
            for(Card c : CARDS) {
                cardsB.add(c);
                cardsB.add(c);
            }
            PlayerState player = new PlayerState(ticketsB.build(), cardsB.build(), ALL_ROUTES);
            return player;
        }
    }
}
