package ch.epfl.tchu.game;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.Info;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ch.epfl.tchu.game.Color.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InfoTest2 {

    static String playerName = "Micho";
    static Info playerInfo = new Info(playerName);



    @Test
    void testingCardNames(){
        assertEquals("bleues", Info.cardName(Card.of(BLUE),3));
        assertEquals("locomotive", Info.cardName(Card.of(null),1));
        assertEquals("violettes", Info.cardName(Card.of(VIOLET),-2));
    }

    @Test
    void testingWillPlayFirst(){
        assertEquals("Micho jouera en premier.\n\n",playerInfo.willPlayFirst());
    }

    @Test
    void testingKeptTickets(){
        assertEquals("Micho a gardé 5 billets.\n",playerInfo.keptTickets(5));
        assertEquals("Micho a gardé 1 billet.\n",playerInfo.keptTickets(1));
    }

    @Test
    void testingCanPlay(){
        assertEquals("\nC'est à Micho de jouer.\n",playerInfo.canPlay());
    }

    @Test
    void testingDrewTickets(){
        assertEquals("Micho a tiré 5 billets...\n",playerInfo.drewTickets(5));
        assertEquals("Micho a tiré 1 billet...\n",playerInfo.drewTickets(1));
    }

    @Test
    void testingDrewBlindCard(){
        assertEquals("Micho a tiré une carte de la pioche.\n",playerInfo.drewBlindCard());

    }

    @Test
    void testingDrewVisibleCard(){
        assertEquals("Micho a tiré une carte bleue visible.\n",playerInfo.drewVisibleCard(Card.of(BLUE)));
    }

    @Test
    void testingClaimedRoute(){

        SortedBag<Card> cards = SortedBag.of(2, Card.YELLOW);


        Station SCZ = new Station(24, "Schwyz");
        Station WAS = new Station(29, "Wassen");
        Route yo = new Route("SCZ_WAS_2", SCZ, WAS, 2, Route.Level.OVERGROUND, Color.YELLOW);
        assertEquals("Micho a pris possession de la route Schwyz – Wassen au moyen de 2 jaunes.\n",playerInfo.claimedRoute(yo,cards));
    }

    @Test
    void testingAttemptsTunnelClaim(){
        SortedBag<Card> cards = SortedBag.of(1,Card.YELLOW,1,Card.LOCOMOTIVE);
        Station SCZ = new Station(24, "Schwyz");
        Station WAS = new Station(29, "Wassen");
        Route yo1 = new Route("SCZ_WAS_2", SCZ, WAS, 2, Route.Level.UNDERGROUND, Color.YELLOW);

        assertEquals("Micho tente de s'emparer du tunnel Schwyz – Wassen au moyen de 1 jaune et 1 locomotive !\n",playerInfo.attemptsTunnelClaim(yo1,cards));
    }

    @Test
    void testingDrewAdditionalCards(){
        SortedBag<Card> cards = SortedBag.of(2,Card.YELLOW,1,Card.LOCOMOTIVE);
        SortedBag.Builder sb = new SortedBag.Builder();
        SortedBag<Card> s;
        sb.add(1,Card.YELLOW);
        sb.add(1,Card.LOCOMOTIVE);
        sb.add(1,Card.BLACK);
        s = sb.build();

        assertEquals("Les cartes supplémentaires sont 2 jaunes et 1 locomotive. Elles impliquent un coût additionnel de 3 cartes.\n", playerInfo.drewAdditionalCards(cards,3));
        assertEquals("Les cartes supplémentaires sont 1 noire, 1 jaune et 1 locomotive. Elles n'impliquent aucun coût additionnel.\n",playerInfo.drewAdditionalCards(s,0));

    }

    @Test
    void testingDidNotClaimRoute(){
        Station SCZ = new Station(24, "Schwyz");
        Station WAS = new Station(29, "Wassen");
        Route yo2 = new Route("SCZ_WAS_2", SCZ, WAS, 2, Route.Level.OVERGROUND, Color.YELLOW);
        assertEquals("Micho n'a pas pu (ou voulu) s'emparer de la route Schwyz – Wassen.\n",playerInfo.didNotClaimRoute(yo2));
    }

    @Test
    void testingLastTurnBegins(){
        assertEquals("\nMicho n'a plus que 2 wagons, le dernier tour commence !\n",playerInfo.lastTurnBegins(2));
        assertEquals("\nMicho n'a plus que 1 wagon, le dernier tour commence !\n",playerInfo.lastTurnBegins(1));
        assertEquals("\nMicho n'a plus que 0 wagon, le dernier tour commence !\n",playerInfo.lastTurnBegins(0));
        assertEquals("\nMicho n'a plus que 3 wagons, le dernier tour commence !\n",playerInfo.lastTurnBegins(3));
    }

    @Test
    void testingGetsLongestTrailBonus(){
        Station BER = new Station(3, "Berne");
        Station SOL = new Station(26, "Soleure");
        Station COI = new Station(6, "Coire");
        Station BRU = new Station(5, "Brusio");
        Station OLT = new Station(20, "Olten");

        var D = new Route("BER_SOL_1", BER, SOL, 2, Route.Level.OVERGROUND, Color.BLACK);
        var G = new Route("OLT_SOL_1", OLT, SOL, 1, Route.Level.OVERGROUND, Color.BLUE);
        var H = new Route("BRU_COI_1", BRU, COI, 5, Route.Level.UNDERGROUND, null);

        List<Route> routes =List.of(D,G,H);
        Trail t2 = Trail.longest(routes);
        assertEquals("\nMicho reçoit un bonus de 10 points pour le plus long trajet (Brusio – Coire).\n",playerInfo.getsLongestTrailBonus(t2));

    }

    @Test
    void testingWon(){
        assertEquals("\nMicho remporte la victoire avec 10 points, contre 3 points !\n",playerInfo.won(10,3));
    }




}