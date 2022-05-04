package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerStateTest {
    @Test
    void initialThrowsException(){
        PlayerState test = new PlayerState(SortedBag.of(), SortedBag.of(), List.of());
        assertThrows(IllegalArgumentException.class, ()-> {test.initial(SortedBag.of(2, Card.BLUE));});
    }

    @Test
    void canClaimRouteValidRouteCase(){
        Station station1 = new Station(1, "Bern");
        Station station2 = new Station(2, "Lausanne");
    PlayerState test = new PlayerState(SortedBag.of(), SortedBag.of(3, Card.BLUE), List.of());
    Route route  = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, Color.BLUE);
    assertTrue(test.canClaimRoute(route));
    }

    @Test
    void canClaimRouteInvalidRouteColorCase(){
        Station station1 = new Station(1, "Bern");
        Station station2 = new Station(2, "Lausanne");
        PlayerState test = new PlayerState(SortedBag.of(), SortedBag.of(3, Card.BLUE), List.of());
    Route route  = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, Color.ORANGE);
        assertFalse(test.canClaimRoute(route));
    }

    @Test
    void canClaimRouteInvalidRouteLengthCase(){
        Station station1 = new Station(1, "Bern");
        Station station2 = new Station(2, "Lausanne");
        PlayerState test = new PlayerState(SortedBag.of(), SortedBag.of(2, Card.BLUE), List.of());
        Route route  = new Route("Route", station1, station2, 4, Route.Level.OVERGROUND, Color.BLUE);
        assertFalse(test.canClaimRoute(route));
    }

    @Test
    void canClaimRouteNotEnoughCarsCase(){
        Station station1 = new Station(1, "Bern");
        Station station2 = new Station(2, "Lausanne");
        Route route  = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route1  = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route3 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route4 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route5 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route6 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);

        PlayerState test = new PlayerState(SortedBag.of(), SortedBag.of(10, Card.BLUE), List.of(route, route1, route3, route4, route5, route6));
        Route route2  = new Route("Route", station1, station2, 5, Route.Level.OVERGROUND, Color.BLUE);
        assertFalse(test.canClaimRoute(route2));
    }

    @Test
    void possibleClaimCardThrowsIllegalArgumentException(){
        Station station1 = new Station(1, "Bern");
        Station station2 = new Station(2, "Lausanne");
        Route route  = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route1  = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route3 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route4 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route5 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route6 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        PlayerState test = new PlayerState(SortedBag.of(), SortedBag.of(10, Card.BLUE), List.of(route, route1, route3, route4, route5, route6));
        Route route2  = new Route("Route", station1, station2, 5, Route.Level.OVERGROUND, Color.BLUE);
        assertThrows(IllegalArgumentException.class, ()-> {test.possibleClaimCards(route2);});
    }

    @Test
    void possibleClaimCards(){
        Station station1 = new Station(1, "Bern");
        Station station2 = new Station(2, "Lausanne");
        Route route2  = new Route("Route", station1, station2, 2, Route.Level.UNDERGROUND, Color.BLUE);
        PlayerState test = new PlayerState(SortedBag.of(), SortedBag.of(10, Card.BLUE, 5, Card.LOCOMOTIVE), List.of());
        List<SortedBag<Card>> list= test.possibleClaimCards(route2);
        for (SortedBag<Card> bag: list) {
            System.out.println("-------------------------");
            for (Card card : bag){
                System.out.println(card);
            }
        }
    }

    @Test
    void ticketPointsTrivialConnected(){
        Station BER = new Station(0, "Berne");
        Station LAU = new Station(1, "Lausanne");
        Ticket ticket = new Ticket(BER, LAU, 10);
        Route BerLau = new Route("route", BER, LAU, 6, Route.Level.OVERGROUND, null);
        PlayerState playerState = new PlayerState(SortedBag.of(ticket), SortedBag.of(), List.of(BerLau));
        assertEquals(10, playerState.ticketPoints());
    }

    @Test
    void ticketPointsTrivialDisconnected(){
        Station BER = new Station(0, "Berne");
        Station LAU = new Station(1, "Lausanne");
        Station YVE = new Station(2, "Yverdon");
        Station ZUR = new Station(3, "Zürich");
        Ticket ticket = new Ticket(BER, YVE, 10);
        Route YveZur = new Route("route", YVE, ZUR, 6, Route.Level.OVERGROUND, null);
        Route BerLau = new Route("route", BER, LAU, 6, Route.Level.OVERGROUND, null);
        PlayerState playerState = new PlayerState(SortedBag.of(ticket), SortedBag.of(), List.of(BerLau));
        assertEquals(-10, playerState.ticketPoints());
    }

    @Test
    void ticketPointsNormal(){
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Station BER = new Station(3, "Berne");
        Station BRI = new Station(4, "Brigue");
        Station BRU = new Station(5, "Brusio");
        Route BadBel = new Route("1", BAD, BAL, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BalBel = new Route("2", BAL, BEL, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BelBer = new Route("3", BEL, BER, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BerBri = new Route("4", BAL, BRI, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BriBru = new Route("5", BAL, BRU, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BruBad = new Route("6", BRU, BAD, 5, Route.Level.OVERGROUND, Color.BLACK);
        Ticket t1 = new Ticket(BAD, BAL, 10);
        Ticket t2 = new Ticket(BAD, BRU, 10);
        Ticket t3 = new Ticket(BEL, BER, 10);
        Ticket t4 = new Ticket(BRI, BAD, 10);
        SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
        builder.add(t1);
        builder.add(t2);
        builder.add(t3);
        builder.add(t4);
        PlayerState playerState = new PlayerState(builder.build(), SortedBag.of(), List.of(BadBel, BalBel,BelBer, BerBri, BriBru, BruBad));
        assertEquals(40, playerState.ticketPoints());
    }

    @Test
    void withAddedTicketNormal(){
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Station BER = new Station(3, "Berne");
        Station BRI = new Station(4, "Brigue");
        Station BRU = new Station(5, "Brusio");
        Ticket t1 = new Ticket(BAD, BAL, 10);
        Ticket t2 = new Ticket(BAD, BRU, 10);
        Ticket t3 = new Ticket(BEL, BER, 10);
        Ticket t4 = new Ticket(BRI, BAD, 10);
        SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
        builder.add(t1);
        builder.add(t2);
        builder.add(t3);
        builder.add(t4);
        PlayerState playerState = new PlayerState(builder.build(), SortedBag.of(), List.of());
        SortedBag.Builder<Ticket> builder2 = new SortedBag.Builder<>();
        Ticket toAdd1 = new Ticket(BRI, BRU, 1);
        Ticket toAdd2 = new Ticket(BAD, BER, 1);
        builder2.add(toAdd1);
        builder2.add(toAdd2);
        builder.add(toAdd1);
        builder.add(toAdd2);
        PlayerState finalState = playerState.withAddedTickets(builder2.build());
        assertEquals(builder.build(), finalState.tickets());
        assertEquals(playerState.cards(), finalState.cards());
    }

    @Test
    void withNoAddedTickets(){
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Station BER = new Station(3, "Berne");
        Station BRI = new Station(4, "Brigue");
        Station BRU = new Station(5, "Brusio");
        Ticket t1 = new Ticket(BAD, BAL, 10);
        Ticket t2 = new Ticket(BAD, BRU, 10);
        Ticket t3 = new Ticket(BEL, BER, 10);
        Ticket t4 = new Ticket(BRI, BAD, 10);
        SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
        builder.add(t1);
        builder.add(t2);
        builder.add(t3);
        builder.add(t4);
        PlayerState playerState = new PlayerState(builder.build(), SortedBag.of(), List.of());
        SortedBag.Builder<Ticket> builder2 = new SortedBag.Builder<>();
        Ticket toAdd1 = new Ticket(BRI, BRU, 1);
        Ticket toAdd2 = new Ticket(BAD, BER, 1);
        PlayerState finalState = playerState.withAddedTickets(builder2.build());
        assertEquals(builder.build(), finalState.tickets());
        assertEquals(playerState.cards(), finalState.cards());
    }

    @Test
    void withAddedCardTrivial(){
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Station BER = new Station(3, "Berne");
        Station BRI = new Station(4, "Brigue");
        Station BRU = new Station(5, "Brusio");
        Ticket t1 = new Ticket(BAD, BAL, 10);
        Ticket t2 = new Ticket(BAD, BRU, 10);
        Ticket t3 = new Ticket(BEL, BER, 10);
        Ticket t4 = new Ticket(BRI, BAD, 10);
        SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
        builder.add(t1);
        builder.add(t2);
        builder.add(t3);
        builder.add(t4);
        SortedBag<Card> bag = SortedBag.of(10, Card.BLUE, 20, Card.BLACK);
        PlayerState playerState = new PlayerState(builder.build(), bag, List.of());
        SortedBag.Builder<Card> builder2 = new SortedBag.Builder<>();
        builder2.add(bag);
        builder2.add(Card.RED);
        PlayerState finalState = playerState.withAddedCard(Card.RED);
        assertEquals(builder2.build(), finalState.cards());
    }


    @Test
    void withAddedCardsTrivial(){
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Station BER = new Station(3, "Berne");
        Station BRI = new Station(4, "Brigue");
        Station BRU = new Station(5, "Brusio");
        Ticket t1 = new Ticket(BAD, BAL, 10);
        Ticket t2 = new Ticket(BAD, BRU, 10);
        Ticket t3 = new Ticket(BEL, BER, 10);
        Ticket t4 = new Ticket(BRI, BAD, 10);
        SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
        builder.add(t1);
        builder.add(t2);
        builder.add(t3);
        builder.add(t4);
        SortedBag<Card> bag = SortedBag.of(10, Card.BLUE, 20, Card.BLACK);
        PlayerState playerState = new PlayerState(builder.build(), bag, List.of());
        SortedBag.Builder<Card> builder2 = new SortedBag.Builder<>();
        builder2.add(5,Card.RED);
        builder2.add(10, Card.LOCOMOTIVE);
        //PlayerState finalState = playerState.withAddedCards(builder2.build());
        SortedBag.Builder<Card> builder3 = new SortedBag.Builder<>();
        builder3.add(bag);
        builder3.add(builder2.build());
        //assertEquals(builder3.build(), finalState.cards());
    }

    @Test
    void withAddedCardsNoCards(){
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Station BER = new Station(3, "Berne");
        Station BRI = new Station(4, "Brigue");
        Station BRU = new Station(5, "Brusio");
        Ticket t1 = new Ticket(BAD, BAL, 10);
        Ticket t2 = new Ticket(BAD, BRU, 10);
        Ticket t3 = new Ticket(BEL, BER, 10);
        Ticket t4 = new Ticket(BRI, BAD, 10);
        SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
        builder.add(t1);
        builder.add(t2);
        builder.add(t3);
        builder.add(t4);
        SortedBag<Card> bag = SortedBag.of(10, Card.BLUE, 20, Card.BLACK);
        PlayerState playerState = new PlayerState(builder.build(), bag, List.of());
        SortedBag.Builder<Card> builder2 = new SortedBag.Builder<>();
        builder2.add(5,Card.RED);
        builder2.add(10, Card.LOCOMOTIVE);
        //PlayerState finalState = playerState.withAddedCards(SortedBag.of());
        SortedBag.Builder<Card> builder3 = new SortedBag.Builder<>();
        builder3.add(bag);
        //assertEquals(builder3.build(), finalState.cards());
    }

    @Test
    void withClaimedRouteTrivial(){
        Station station1 = new Station(1, "Bern");
        Station station2 = new Station(2, "Lausanne");
        Route route  = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route1  = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route3 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route4 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route5 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route6 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        PlayerState test = new PlayerState(SortedBag.of(), SortedBag.of(70, Card.ORANGE), List.of(route, route1, route3, route4, route5));
        PlayerState finalState = test.withClaimedRoute(route6, SortedBag.of(10, Card.ORANGE));
        assertEquals(SortedBag.of(60, Card.ORANGE), finalState.cards());
        assertEquals(List.of(route, route1, route3, route4, route5, route6), finalState.routes());
    }

    @Test
    void possibleAdditionalClaimCardsNormalNoLocomotive(){
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(6, Card.BLUE);
        PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
        SortedBag.Builder<Card> bag = new SortedBag.Builder();
        bag.add(3, Card.BLUE);
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
        drawnCards.add(Card.LOCOMOTIVE);
        drawnCards.add(2, Card.BLUE);
        assertEquals(List.of(bag.build()), test.possibleAdditionalCards(3, bag.build()));
    }

    @Test
    void possibleAdditionalClaimCardsNormalWithLocomotive(){
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(6, Card.BLUE);
        playersCards.add(1, Card.LOCOMOTIVE);
        PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
        SortedBag.Builder<Card> bag = new SortedBag.Builder();
        bag.add(3, Card.BLUE);
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
        drawnCards.add(Card.LOCOMOTIVE);
        drawnCards.add(2, Card.BLUE);
        List<SortedBag<Card>> finalList = test.possibleAdditionalCards(3, bag.build());
        for (SortedBag<Card> b : finalList ){
            System.out.println(b);
        }
    }

    @Test
    void possibleAdditionalClaimCardsNormalFullLocomotive(){
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(6, Card.BLUE);
        playersCards.add(10, Card.LOCOMOTIVE);
        PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
        SortedBag.Builder<Card> bag = new SortedBag.Builder();
        bag.add(3, Card.LOCOMOTIVE);
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
        drawnCards.add(3, Card.LOCOMOTIVE);
        List<SortedBag<Card>> finalList = test.possibleAdditionalCards(3, bag.build());
        for (SortedBag<Card> b : finalList ){
            System.out.println(b);
        }
    }

    @Test
    void possibleAdditonalClaimCardsThrowsIllegalAdditionalCardsNumberIllegal1(){
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(6, Card.BLUE);
        playersCards.add(10, Card.LOCOMOTIVE);
        PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
        SortedBag.Builder<Card> bag = new SortedBag.Builder();
        bag.add(3, Card.LOCOMOTIVE);
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
        drawnCards.add(3, Card.LOCOMOTIVE);
        assertThrows(IllegalArgumentException.class, ()-> {test.possibleAdditionalCards(-5, bag.build());});
    }
    @Test
    void possibleAdditonalClaimCardsThrowsIllegalAdditionalCardsNumberIllegal2(){
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(6, Card.BLUE);
        playersCards.add(10, Card.LOCOMOTIVE);
        PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
        SortedBag.Builder<Card> bag = new SortedBag.Builder();
        bag.add(3, Card.LOCOMOTIVE);
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
        drawnCards.add(3, Card.LOCOMOTIVE);
        assertThrows(IllegalArgumentException.class, ()-> {test.possibleAdditionalCards(10, bag.build());});
    }
    @Test
    void possibleAdditonalClaimCardsThrowsIllegalAdditionalCardsMoreThan2Colors(){
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(6, Card.BLUE);
        playersCards.add(10, Card.LOCOMOTIVE);
        PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
        SortedBag.Builder<Card> bag = new SortedBag.Builder();
        bag.add(3, Card.LOCOMOTIVE);
        bag.add(1, Card.GREEN);
        bag.add(1, Card.BLACK);
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
        drawnCards.add(3, Card.LOCOMOTIVE);
        assertThrows(IllegalArgumentException.class, ()-> {test.possibleAdditionalCards(3, bag.build());});
    }

    @Test
    void possibleAdditonalClaimCardsThrowsIllegalDrawnCardsLessThan3(){
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(6, Card.BLUE);
        playersCards.add(10, Card.LOCOMOTIVE);
        PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
        SortedBag.Builder<Card> bag = new SortedBag.Builder();
        bag.add(3, Card.LOCOMOTIVE);
        bag.add(1, Card.GREEN);
        bag.add(1, Card.BLACK);
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
        drawnCards.add(2, Card.LOCOMOTIVE);
        assertThrows(IllegalArgumentException.class, ()-> {test.possibleAdditionalCards(3, bag.build());});
    }

    @Test
    void possibleAdditonalClaimCardsThrowsIllegalDrawnCardsMoreThan3(){
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(6, Card.BLUE);
        playersCards.add(10, Card.LOCOMOTIVE);
        PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
        SortedBag.Builder<Card> bag = new SortedBag.Builder();
        bag.add(3, Card.LOCOMOTIVE);
        bag.add(1, Card.GREEN);
        bag.add(1, Card.BLACK);
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
        drawnCards.add(4, Card.LOCOMOTIVE);
        assertThrows(IllegalArgumentException.class, ()-> {test.possibleAdditionalCards(3, bag.build());});
    }
    @Test
    void possibleAdditionalClaimCardsNormalRankedLocomotive(){
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(10, Card.BLUE);
        playersCards.add(10, Card.LOCOMOTIVE);
        PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
        SortedBag.Builder<Card> bag = new SortedBag.Builder();
        bag.add(3, Card.BLUE);
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
        drawnCards.add(3, Card.BLUE);
        List<SortedBag<Card>> finalList = test.possibleAdditionalCards(3, bag.build());
        for (SortedBag<Card> b : finalList ){
            System.out.println(b);
        }
    }

    @Test
    void possibleAdditionalClaimCardsNormalWithLocomotiveAdvanced(){
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(6, Card.BLUE);
        playersCards.add(6, Card.LOCOMOTIVE);
        PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
        SortedBag.Builder<Card> bag = new SortedBag.Builder();
        bag.add(3, Card.BLUE);
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
        drawnCards.add(2, Card.LOCOMOTIVE);
        drawnCards.add(1, Card.BLUE);
        List<SortedBag<Card>> finalList = test.possibleAdditionalCards(3, bag.build());
        for (SortedBag<Card> b : finalList ){
            System.out.println(b);
        }
    }

    @Test
    void possibleAdditionalClaimCardsWrongCardsLeft(){
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(3, Card.BLUE);
        PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
        SortedBag.Builder<Card> bag = new SortedBag.Builder();
        bag.add(3, Card.BLUE);
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
        drawnCards.add(Card.LOCOMOTIVE);
        drawnCards.add(2, Card.BLUE);
        assertEquals(List.of(), test.possibleAdditionalCards(3, bag.build()));
    }

    @Test
    void possibleAdditionalClaimCardsNotEnoughCardsLeft(){
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(5, Card.BLUE);
        PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
        SortedBag.Builder<Card> bag = new SortedBag.Builder();
        bag.add(3, Card.BLUE);
        SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
        drawnCards.add(Card.LOCOMOTIVE);
        drawnCards.add(2, Card.BLUE);
        assertEquals(List.of(), test.possibleAdditionalCards(3, bag.build()));
    }

    @Test
    void possibleAdditionalCardsCountThrowsIllegalArgumentEmptyInitial(){
            SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
            playersCards.add(5, Card.ORANGE);
            playersCards.add(6, Card.BLUE);
            playersCards.add(10, Card.LOCOMOTIVE);
            PlayerState test = new PlayerState(SortedBag.of(), playersCards.build(), List.of());
            SortedBag.Builder<Card> bag = new SortedBag.Builder();
            SortedBag.Builder<Card> drawnCards = new SortedBag.Builder();
            drawnCards.add(2, Card.LOCOMOTIVE);
            assertThrows(IllegalArgumentException.class, ()-> {test.possibleAdditionalCards(3, bag.build());});
    }

}

