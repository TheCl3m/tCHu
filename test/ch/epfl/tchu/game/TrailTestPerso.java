package ch.epfl.tchu.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrailTestPerso {

    @Test
    void longestTrivialCase() {
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
        System.out.println(Trail.longest(routes));
    }

    @Test
    void longestEdgeCase1() {
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Station BER = new Station(3, "Berne");
        Station BRI = new Station(4, "Brigue");
        Station BRU = new Station(5, "Brusio");
        Station CRU = new Station(6, "Crusier");
        Route BadBel = new Route("1", BAD, BAL, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BalBel = new Route("2", BAL, BEL, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route CruBer = new Route("3", CRU, BER, 1, Route.Level.OVERGROUND, Color.BLACK);
        Route BerBri = new Route("4", BER, BRI, 1, Route.Level.OVERGROUND, Color.BLACK);
        Route BriBru = new Route("5", BRI, BRU, 1, Route.Level.OVERGROUND, Color.BLACK);
        List<Route> routes = List.of(BadBel, BalBel, CruBer, BerBri, BriBru);
        assertEquals("Baden - Bâle - Bellinzone (10)", Trail.longest(routes).toString());
    }

    @Test
    void longestStarTest1(){
        Station Center = new Station(0, "Center");
        Station A = new Station(1, "A");
        Station B = new Station(2, "B");
        Station C = new Station(3, "C");
        Station D = new Station(4, "D");
        Station E = new Station(5, "E");
        Station F = new Station(6, "F");
        Station G = new Station(7, "G");
        Station H = new Station(8, "H");
        Route centerA = new Route("0", Center, A, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerB = new Route("1", B, Center, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerC = new Route("2", Center, C, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerD = new Route("3", Center, D, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerE = new Route("4", Center, E, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerF = new Route("5", Center, F, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerG = new Route("6", Center, G, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerH = new Route("7", Center, H, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route AB = new Route("8", A, B, 1, Route.Level.OVERGROUND, Color.BLACK);
        Route CD = new Route("9", C, D, 1, Route.Level.OVERGROUND, Color.BLACK);
        Route EF = new Route("10", E, F, 1, Route.Level.OVERGROUND, Color.BLACK);
        Route GH = new Route("11", G, H, 1, Route.Level.OVERGROUND, Color.BLACK);
        List<Route> routes = List.of(centerA, centerE, centerB, centerC, centerF, centerG,centerH,centerD, AB, CD, EF, GH);
        System.out.println(Trail.longest(routes));
        assertEquals(52, Trail.longest(routes).length());
    }
    @Test
    void longestStarTest2(){
        Station Center = new Station(0, "Center");
        Station A = new Station(1, "A");
        Station B = new Station(2, "B");
        Station C = new Station(3, "C");
        Station D = new Station(4, "D");
        Station E = new Station(5, "E");
        Station F = new Station(6, "F");
        Station G = new Station(7, "G");
        Station H = new Station(8, "H");
        Route centerA = new Route("0", Center, A, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerB = new Route("1", Center, B, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerC = new Route("2", Center, C, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerD = new Route("3", Center, D, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerE = new Route("4", Center, E, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerF = new Route("5", Center, F, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerG = new Route("6", Center, G, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route centerH = new Route("7", Center, H, 6, Route.Level.OVERGROUND, Color.BLACK);
        Route AB = new Route("8", A, B, 1, Route.Level.OVERGROUND, Color.BLACK);
        Route CD = new Route("9", C, D, 1, Route.Level.OVERGROUND, Color.BLACK);
        Route EF = new Route("10", E, F, 1, Route.Level.OVERGROUND, Color.BLACK);
        Route GH = new Route("11", G, H, 1, Route.Level.OVERGROUND, Color.BLACK);
        Route AH = new Route("12", A, H, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BC = new Route("13", B, C, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route DE = new Route("14", D, E, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route FG = new Route("15", F, G, 5, Route.Level.OVERGROUND, Color.BLACK);
        List<Route> routes = List.of(centerA, centerE, centerB, centerC, centerF, centerG,centerH,centerD, AB, CD, EF, GH, AH, BC, DE, FG);
        System.out.println(Trail.longest(routes));
        assertEquals(69, Trail.longest(routes).length());
    }


    @Test
    void longestNonTrivialCase1() {
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
        List<Route> routes = List.of(BadBel, BalBel, BelBer, BerBri, BriBru);
        System.out.println(Trail.longest(routes));
    }

    @Test
    void longestNonTrivialCase2() {
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
        List<Route> routes = List.of(BadBel, BalBel, BelBer, BerBri, BriBru, BruBad);
        System.out.println(Trail.longest(routes));
    }

    @Test
    void longestNonTrivialCase3() {
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
        List<Route> routes = List.of(BadBel, BalBel, BelBer, BerBri, BriBru, BruBad);
        System.out.println(Trail.longest(routes));
    }

    @Test
    void longestGiantCase(){
        System.out.println(Trail.longest(Data.ALL_ROUTES.subList(0, 25)));
    }
    @Test
    void longestLengthSingleRouteCase() {
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Station BER = new Station(3, "Berne");
        Station BRI = new Station(4, "Brigue");
        Station BRU = new Station(5, "Brusio");
        Route BadBel = new Route("1", BAD, BAL, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BelBer = new Route("3", BEL, BER, 6, Route.Level.OVERGROUND, Color.BLACK);
        List<Route> routes = List.of(BadBel, BelBer);
        assertEquals("Bellinzone - Berne (6)", Trail.longest(routes).toString());
    }

    @Test
    void longestLoopTrail1(){
        Station LACH = new Station(0, "La Chaux");
        Station DEL = new Station(1, "Délémont");
        Station SOL = new Station(2, "Soleure");
        Station NEU = new Station(3, "Neuchâtel");
        Station BER = new Station(4, "Berne");
        Station LUC = new Station(5, "Lucerne");
        Station OLT = new Station(6, "Olten");
        Route LacDel = new Route("1", LACH, DEL, 3, Route.Level.OVERGROUND, Color.WHITE);
        Route DelSol = new Route("2", DEL, SOL, 1, Route.Level.OVERGROUND, Color.GREEN);
        Route SolNeu = new Route("3", SOL, NEU, 4, Route.Level.UNDERGROUND, Color.WHITE);
        Route NeuBer = new Route("4", NEU, BER, 2, Route.Level.UNDERGROUND, Color.GREEN);
        Route BerSol = new Route("5", BER, SOL, 2, Route.Level.UNDERGROUND, Color.GREEN);
        Route SolOlt = new Route("6", SOL, OLT, 1, Route.Level.OVERGROUND, Color.BLUE);
        Route OltLuc = new Route("7", OLT, LUC, 3, Route.Level.OVERGROUND, Color.GREEN);
        Route LucBer = new Route("8", LUC, BER, 4, Route.Level.UNDERGROUND, Color.GREEN);
        List<Route> routes = List.of(LacDel, DelSol, SolNeu, NeuBer, BerSol, SolOlt, OltLuc, LucBer);
        System.out.println(Trail.longest(routes));
        assertEquals(20, Trail.longest(routes).length());
    }
    @Test
    void longestLoopTrail2(){
        Station LACH = new Station(0, "La Chaux");
        Station DEL = new Station(1, "Délémont");
        Station SOL = new Station(2, "Soleure");
        Station NEU = new Station(3, "Neuchâtel");
        Station BER = new Station(4, "Berne");
        Station LUC = new Station(5, "Lucerne");
        Station OLT = new Station(6, "Olten");
        Station INT = new Station(7, "Interlaken");
        Station FRI = new Station(8, "MathieuVille");

        Route LacDel = new Route("1", LACH, DEL, 1, Route.Level.OVERGROUND, Color.WHITE);
        Route DelSol = new Route("2", DEL, SOL, 1, Route.Level.OVERGROUND, Color.GREEN);
        Route SolNeu = new Route("3", SOL, NEU, 1, Route.Level.UNDERGROUND, Color.WHITE);
        Route NeuBer = new Route("4", NEU, BER, 1, Route.Level.UNDERGROUND, Color.GREEN);
        Route BerSol = new Route("5", BER, SOL, 1, Route.Level.UNDERGROUND, Color.GREEN);
        Route SolOlt = new Route("6", SOL, OLT, 1, Route.Level.OVERGROUND, Color.BLUE);
        Route OltLuc = new Route("7", OLT, LUC, 1, Route.Level.OVERGROUND, Color.GREEN);
        Route LucBer = new Route("8", LUC, BER, 1, Route.Level.UNDERGROUND, Color.GREEN);
        Route NeuLach = new Route("9", NEU, LACH, 1, Route.Level.UNDERGROUND, Color.GREEN);
        Route IntFri = new Route("10", INT, FRI, 1, Route.Level.UNDERGROUND, Color.GREEN);

        List<Route> routes = List.of(LacDel, DelSol, SolNeu, NeuBer, BerSol, SolOlt, OltLuc, LucBer, NeuLach, IntFri);
        System.out.println(Trail.longest(routes));
        assertEquals(9, Trail.longest(routes).length());
    }

    @Test
    void longestCircularLoopCase() {
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Route BadBal = new Route("1", BAD, BAL, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BalBel = new Route("2", BAL, BEL, 5, Route.Level.OVERGROUND, Color.BLACK);
        Route BelBad = new Route("3", BEL, BAD, 5, Route.Level.OVERGROUND, Color.BLACK);
        List<Route> routes = List.of(BadBal, BalBel, BelBad);
        assertEquals(15, Trail.longest(routes).length());
    }
    @Test
    void longestLength1Case() {
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Station BER = new Station(3, "Berne");
        Station BRI = new Station(4, "Brigue");
        Station BRU = new Station(5, "Brusio");
        Route BadBel = new Route("1", BAD, BAL, 5, Route.Level.OVERGROUND, Color.BLACK);
        List<Route> routes = List.of(BadBel);
        assertEquals("Baden - Bâle (5)", Trail.longest(routes).toString());
    }

    @Test
    void longestOneRouteCase(){
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Route BadBel = new Route("1", BAD, BAL, 5, Route.Level.OVERGROUND, Color.BLACK);
        List<Route> routes = List.of(BadBel);
        System.out.println(Trail.longest(routes));
        assertEquals("Baden - Bâle (5)", Trail.longest(routes).toString());
    }

    @Test
    void longestNullCase(){
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Route BadBel = new Route("1", BAD, BAL, 5, Route.Level.OVERGROUND, Color.BLACK);
        List<Route> routes = List.of();
        System.out.println(Trail.longest(routes));
        assertEquals("Empty trail (0)", Trail.longest(routes).toString());
    }

    @Test
    void length() {
    }

    @Test
    void station1TrivialCase() {
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
        assertEquals("Baden", Trail.longest(routes).station1().toString());
    }

    @Test
    void station1NullCase(){
        List<Route> routes = new ArrayList<Route>();
        assertNull(Trail.longest(routes).station1());
    }

    @Test
    void station2NullCase(){
        List<Route> routes = new ArrayList<Route>();
        assertNull(Trail.longest(routes).station1());
    }

    @Test
    void station2TrivialCase() {
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
        System.out.println(Trail.longest(routes).station2());
    }

    @Test
    void testToString() {
    }
}