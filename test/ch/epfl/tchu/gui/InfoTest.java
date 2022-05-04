package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InfoTest {

    private final Info info = new Info("Clément");

    @Test
    void cardNameBlackSingular() {
        assertEquals("noire", Info.cardName(Card.BLACK, 1));
    }
    @Test
    void cardNameBlackPlural() {
       assertEquals("noires", Info.cardName(Card.BLACK, 4));
    }

    @Test
    void cardNameVioletSingular() {
        assertEquals("violette", Info.cardName(Card.VIOLET, 1));
    }
    @Test
    void cardNameVioletPlural() {
        assertEquals("violettes", Info.cardName(Card.VIOLET, 4));
    }
    @Test
    void cardNameBlueSingular() {
        assertEquals("bleue", Info.cardName(Card.BLUE, 1));
    }
    @Test
    void cardNameBluePlural() {
        assertEquals("bleues", Info.cardName(Card.BLUE, 6));
    }
    @Test
    void cardNameGreenSingular() {
        assertEquals("verte", Info.cardName(Card.GREEN, 1));
    }
    @Test
    void cardNameGreenPlural() {
        assertEquals("vertes", Info.cardName(Card.GREEN, 4));
    }
    @Test
    void cardNameYellowSingular() {
        assertEquals("jaune", Info.cardName(Card.YELLOW, 1));
    }
    @Test
    void cardNameYellowPlural() {
        assertEquals("jaunes", Info.cardName(Card.YELLOW, 5));
    }
    @Test
    void cardNameOrangeSingular() {
        assertEquals("orange", Info.cardName(Card.ORANGE, 1));
    }
    @Test
    void cardNameOrangePlural() {
        assertEquals("oranges", Info.cardName(Card.ORANGE, 2));
    }
    @Test
    void cardNameRedSingular() {
        assertEquals("rouge", Info.cardName(Card.RED, 1));
    }
    @Test
    void cardNameRedPlural() {
        assertEquals("rouges", Info.cardName(Card.RED, 6));
    }
    @Test
    void cardNameWhiteSingular() {
        assertEquals("blanche", Info.cardName(Card.WHITE, 1));
    }
    @Test
    void cardNameWhitePlural() {
        assertEquals("blanches", Info.cardName(Card.WHITE, 5));
    }
    @Test
    void cardNameLocomotiveSingular() {
        assertEquals("locomotive", Info.cardName(Card.LOCOMOTIVE, 1));
    }
    @Test
    void cardNameLocomotivePlural() {
        assertEquals("locomotives", Info.cardName(Card.LOCOMOTIVE, 3));
    }



    @Test
    void willPlayFirst() {
        assertEquals("Clément jouera en premier.\n\n", info.willPlayFirst());
    }

    @Test
    void keptTicketsSingular() {
        assertEquals("Clément a gardé 1 billet.\n", info.keptTickets(1));
    }
    @Test
    void keptTicketsPlural() {
        assertEquals("Clément a gardé 3 billets.\n", info.keptTickets(3));
    }

    @Test
    void canPlay() {
        assertEquals("\nC'est à Clément de jouer.\n", info.canPlay());
    }

    @Test
    void drewTicketsSingular() {
        assertEquals("Clément a tiré 1 billet...\n", info.drewTickets(1));
    }

    @Test
    void drewTicketsPlural() {
        assertEquals("Clément a tiré 5 billets...\n", info.drewTickets(5));
    }

    @Test
    void drewBlindCard() {
        assertEquals("Clément a tiré une carte de la pioche.\n", info.drewBlindCard());
    }

    @Test
    void drewVisibleCard() {
        assertEquals("Clément a tiré une carte noire visible.\n", info.drewVisibleCard(Card.BLACK));
    }

    @Test
    void claimedRoute() {
        Station B = new Station(0, "Bâle");
        Station L = new Station(1, "Lausanne");
        Route BalLau = new Route("BalLau", B, L, 6, Route.Level.OVERGROUND, null);
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(Card.LOCOMOTIVE);
        bag.add(Card.RED);
        bag.add(Card.BLUE);
        bag.add(Card.BLUE);
        bag.add(Card.BLUE);
        bag.add(Card.RED);
        assertEquals("Clément a pris possession de la route Bâle – Lausanne au moyen de 3 bleues, 2 rouges et 1 locomotive.\n",
                info.claimedRoute(BalLau, bag.build()));
    }

    @Test
    void attemptsTunnelClaim() {
        Station B = new Station(0, "Bâle");
        Station L = new Station(1, "Lausanne");
        Route BalLau = new Route("BalLau", B, L, 6, Route.Level.OVERGROUND, null);
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(2, Card.BLACK);
        bag.add(Card.RED);
        assertEquals("Clément tente de s'emparer du tunnel Bâle – Lausanne au moyen de 2 noires et 1 rouge !\n",
                info.attemptsTunnelClaim(BalLau, bag.build()));
    }

    @Test
    void drewAdditionalCardsNoAdditional() {
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(2, Card.BLACK);
        bag.add(Card.RED);
        assertEquals("Les cartes supplémentaires sont 2 noires et 1 rouge. Elles n'impliquent aucun coût additionnel.\n",
           info.drewAdditionalCards(bag.build(), 0));
    }

    @Test
    void drewAdditionalCardsAdditionalSingular() {
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(2, Card.BLACK);
        bag.add(Card.RED);
        assertEquals("Les cartes supplémentaires sont 2 noires et 1 rouge. Elles impliquent un coût additionnel de 1 carte.\n",
        info.drewAdditionalCards(bag.build(), 1));
    }

    @Test
    void drewAdditionalCardsAdditionalPlural() {
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(2, Card.BLACK);
        bag.add(Card.RED);
        assertEquals("Les cartes supplémentaires sont 2 noires et 1 rouge. Elles impliquent un coût additionnel de 3 cartes.\n",
                info.drewAdditionalCards(bag.build(), 3));
    }
    @Test
    void didNotClaimRoute() {
        Station B = new Station(0, "Bâle");
        Station L = new Station(1, "Lausanne");
        Route BalLau = new Route("BalLau", B, L, 6, Route.Level.OVERGROUND, null);
        assertEquals("Clément n'a pas pu (ou voulu) s'emparer de la route Bâle – Lausanne.\n", info.didNotClaimRoute(BalLau));
    }

    @Test
    void lastTurnBeginsPlural() {
        assertEquals( "\nClément n'a plus que 5 wagons, le dernier tour commence !\n",
                info.lastTurnBegins(5));
    }
    @Test
    void lastTurnBeginsSingular() {
        assertEquals( "\nClément n'a plus que 1 wagon, le dernier tour commence !\n",
                info.lastTurnBegins(1));
    }

    @Test
    void getsLongestTrailBonus() {
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Station BER = new Station(3, "Berne");
        Station BRI = new Station(4, "Brigue");
        Station BRU = new Station(5, "Brusio");
        Route BadBel = new Route("1", BAD, BAL, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BalBel = new Route("2", BAL, BEL, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BelBer = new Route("3", BEL, BER, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BerBri = new Route("4", BER, BRI, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BriBru = new Route("5", BRI, BRU, 5, Route.Level.OVERGROUND, Color.BLACK);
        List<Route> routes = List.of(BadBel, BalBel, BelBer, BerBri, BriBru);
        assertEquals( "\nClément reçoit un bonus de 10 points pour le plus long trajet (Baden – Brusio).\n", info.getsLongestTrailBonus(Trail.longest(routes)));
    }

    @Test
    void wonSingular1() {
        assertEquals("\nClément remporte la victoire avec 1 point, contre 10 points !\n",
                info.won(1,10));
    }
    @Test
    void wonSingular2() {
        assertEquals("\nClément remporte la victoire avec 1 point, contre 1 point !\n",
                info.won(1,1));
    }
    @Test
    void wonSingular3() {
        assertEquals("\nClément remporte la victoire avec 10 points, contre 1 point !\n",
                info.won(10,1));
    }
    @Test
    void wonSingular4() {
        assertEquals("\nClément remporte la victoire avec 10 points, contre 5 points !\n",
                info.won(10,5));
    }
}