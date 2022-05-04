package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class PublicGameStateTest2 {
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
    private static final Station NEU = new Station(19, "Neuchâtel");
    private static final Station OLT = new Station(20, "Olten");
    private static final Station PFA = new Station(21, "Pfäffikon");
    private static final Station SAR = new Station(22, "Sargans");
    private static final Station SCE = new Station(23, "Schaffhouse");
    private static final Station SCZ = new Station(24, "Schwyz");
    private static final Station SIO = new Station(25, "Sion");
    private static final Station SOL = new Station(26, "Soleure");
    private static final Station STG = new Station(27, "Saint-Gall");
    private static final Station VAD = new Station(28, "Vaduz");
    private static final Station WAS = new Station(29, "Wassen");
    private static final Station WIN = new Station(30, "Winterthour");
    private static final Station YVE = new Station(31, "Yverdon");
    private static final Station ZOU = new Station(32, "Zoug");
    private static final Station ZUR = new Station(33, "Zürich");

    private static final List<Route> ALL_ROUTES = List.of(
            new Route("BAD_BAL_1", BAD, BAL, 3, Route.Level.UNDERGROUND, Color.RED),
            new Route("BAD_OLT_1", BAD, OLT, 2, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("BAD_ZUR_1", BAD, ZUR, 1, Route.Level.OVERGROUND, Color.YELLOW),
            new Route("BAL_DEL_1", BAL, DEL, 2, Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("BAL_OLT_1", BAL, OLT, 2, Route.Level.UNDERGROUND, Color.ORANGE),
            new Route("BEL_LOC_1", BEL, LOC, 1, Route.Level.UNDERGROUND, Color.BLACK),
            new Route("BEL_LUG_1", BEL, LUG, 1, Route.Level.UNDERGROUND, Color.RED),
            new Route("BEL_LUG_2", BEL, LUG, 1, Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("BEL_WAS_1", BEL, WAS, 4, Route.Level.UNDERGROUND, null),
            new Route("BEL_WAS_2", BEL, WAS, 4, Route.Level.UNDERGROUND, null),
            new Route("BER_FRI_1", BER, FRI, 1, Route.Level.OVERGROUND, Color.ORANGE),
            new Route("BER_FRI_2", BER, FRI, 1, Route.Level.OVERGROUND, Color.YELLOW),
            new Route("BER_INT_1", BER, INT, 3, Route.Level.OVERGROUND, Color.BLUE),
            new Route("BER_LUC_1", BER, LUC, 4, Route.Level.OVERGROUND, null),
            new Route("BER_LUC_2", BER, LUC, 4, Route.Level.OVERGROUND, null),
            new Route("BER_NEU_1", BER, NEU, 2, Route.Level.OVERGROUND, Color.RED),
            new Route("BER_SOL_1", BER, SOL, 2, Route.Level.OVERGROUND, Color.BLACK),
            new Route("BRI_INT_1", BRI, INT, 2, Route.Level.UNDERGROUND, Color.WHITE),
            new Route("BRI_LOC_1", BRI, LOC, 6, Route.Level.UNDERGROUND, null),
            new Route("BRI_SIO_1", BRI, SIO, 3, Route.Level.UNDERGROUND, Color.BLACK),
            new Route("BRI_WAS_1", BRI, WAS, 4, Route.Level.UNDERGROUND, Color.RED),
            new Route("BRU_COI_1", BRU, COI, 5, Route.Level.UNDERGROUND, null),
            new Route("BRU_DAV_1", BRU, DAV, 4, Route.Level.UNDERGROUND, Color.BLUE),
            new Route("COI_DAV_1", COI, DAV, 2, Route.Level.UNDERGROUND, Color.VIOLET),
            new Route("COI_SAR_1", COI, SAR, 1, Route.Level.UNDERGROUND, Color.WHITE),
            new Route("COI_WAS_1", COI, WAS, 5, Route.Level.UNDERGROUND, null),
            new Route("DAV_SAR_1", DAV, SAR, 3, Route.Level.UNDERGROUND, Color.BLACK),
            new Route("DEL_LCF_1", DEL, LCF, 3, Route.Level.UNDERGROUND, Color.WHITE),
            new Route("DEL_SOL_1", DEL, SOL, 1, Route.Level.UNDERGROUND, Color.VIOLET),
            new Route("FRI_LAU_1", FRI, LAU, 3, Route.Level.OVERGROUND, Color.RED),
            new Route("FRI_LAU_2", FRI, LAU, 3, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("GEN_LAU_1", GEN, LAU, 4, Route.Level.OVERGROUND, Color.BLUE),
            new Route("GEN_LAU_2", GEN, LAU, 4, Route.Level.OVERGROUND, Color.WHITE),
            new Route("GEN_YVE_1", GEN, YVE, 6, Route.Level.OVERGROUND, null),
            new Route("INT_LUC_1", INT, LUC, 4, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("KRE_SCE_1", KRE, SCE, 3, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("KRE_STG_1", KRE, STG, 1, Route.Level.OVERGROUND, Color.GREEN),
            new Route("KRE_WIN_1", KRE, WIN, 2, Route.Level.OVERGROUND, Color.YELLOW),
            new Route("LAU_MAR_1", LAU, MAR, 4, Route.Level.UNDERGROUND, Color.ORANGE),
            new Route("LAU_NEU_1", LAU, NEU, 4, Route.Level.OVERGROUND, null),
            new Route("LCF_NEU_1", LCF, NEU, 1, Route.Level.UNDERGROUND, Color.ORANGE),
            new Route("LCF_YVE_1", LCF, YVE, 3, Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("LOC_LUG_1", LOC, LUG, 1, Route.Level.UNDERGROUND, Color.VIOLET),
            new Route("LUC_OLT_1", LUC, OLT, 3, Route.Level.OVERGROUND, Color.GREEN),
            new Route("LUC_SCZ_1", LUC, SCZ, 1, Route.Level.OVERGROUND, Color.BLUE),
            new Route("LUC_ZOU_1", LUC, ZOU, 1, Route.Level.OVERGROUND, Color.ORANGE),
            new Route("LUC_ZOU_2", LUC, ZOU, 1, Route.Level.OVERGROUND, Color.YELLOW),
            new Route("MAR_SIO_1", MAR, SIO, 2, Route.Level.UNDERGROUND, Color.GREEN),
            new Route("NEU_SOL_1", NEU, SOL, 4, Route.Level.OVERGROUND, Color.GREEN),
            new Route("NEU_YVE_1", NEU, YVE, 2, Route.Level.OVERGROUND, Color.BLACK),
            new Route("OLT_SOL_1", OLT, SOL, 1, Route.Level.OVERGROUND, Color.BLUE),
            new Route("OLT_ZUR_1", OLT, ZUR, 3, Route.Level.OVERGROUND, Color.WHITE),
            new Route("PFA_SAR_1", PFA, SAR, 3, Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("PFA_SCZ_1", PFA, SCZ, 1, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("PFA_STG_1", PFA, STG, 3, Route.Level.OVERGROUND, Color.ORANGE),
            new Route("PFA_ZUR_1", PFA, ZUR, 2, Route.Level.OVERGROUND, Color.BLUE),
            new Route("SAR_VAD_1", SAR, VAD, 1, Route.Level.UNDERGROUND, Color.ORANGE),
            new Route("SCE_WIN_1", SCE, WIN, 1, Route.Level.OVERGROUND, Color.BLACK),
            new Route("SCE_WIN_2", SCE, WIN, 1, Route.Level.OVERGROUND, Color.WHITE),
            new Route("SCE_ZUR_1", SCE, ZUR, 3, Route.Level.OVERGROUND, Color.ORANGE),
            new Route("SCZ_WAS_1", SCZ, WAS, 2, Route.Level.UNDERGROUND, Color.GREEN),
            new Route("SCZ_WAS_2", SCZ, WAS, 2, Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("SCZ_ZOU_1", SCZ, ZOU, 1, Route.Level.OVERGROUND, Color.BLACK),
            new Route("SCZ_ZOU_2", SCZ, ZOU, 1, Route.Level.OVERGROUND, Color.WHITE),
            new Route("STG_VAD_1", STG, VAD, 2, Route.Level.UNDERGROUND, Color.BLUE),
            new Route("STG_WIN_1", STG, WIN, 3, Route.Level.OVERGROUND, Color.RED),
            new Route("STG_ZUR_1", STG, ZUR, 4, Route.Level.OVERGROUND, Color.BLACK),
            new Route("WIN_ZUR_1", WIN, ZUR, 1, Route.Level.OVERGROUND, Color.BLUE),
            new Route("WIN_ZUR_2", WIN, ZUR, 1, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("ZOU_ZUR_1", ZOU, ZUR, 1, Route.Level.OVERGROUND, Color.GREEN),
            new Route("ZOU_ZUR_2", ZOU, ZUR, 1, Route.Level.OVERGROUND, Color.RED));

    private static Map<PlayerId, PublicPlayerState> createNormalMap() {
        Map<PlayerId, PublicPlayerState> playerState = new TreeMap<>();
        playerState.put(PlayerId.PLAYER_1, new PublicPlayerState(0, 2, List.of(ALL_ROUTES.get(0), ALL_ROUTES.get(1))));
        playerState.put(PlayerId.PLAYER_2, new PublicPlayerState(0, 1, List.of(ALL_ROUTES.get(2), ALL_ROUTES.get(3))));
        return playerState;
    }

    private static PublicCardState createNormalCardState() {
        return new PublicCardState(List.of(Card.BLUE, Card.LOCOMOTIVE, Card.RED, Card.BLUE, Card.WHITE), 10, 2);
    }

    @Test
    void publicGameStateWithWrongArguments() {
        Map<PlayerId, PublicPlayerState> playerState = new HashMap<>();
        playerState.put(PlayerId.PLAYER_2, new PublicPlayerState(0, 1, List.of()));

        PublicCardState cardState = new PublicCardState(List.of(Card.BLUE, Card.LOCOMOTIVE, Card.RED, Card.BLUE, Card.WHITE), 10, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicGameState(1, cardState, PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);

            playerState.put(PlayerId.PLAYER_1, new PublicPlayerState(0, 2, List.of()));
            new PublicGameState(-1, cardState, PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);
        });

        playerState.put(PlayerId.PLAYER_1, new PublicPlayerState(0, 2, List.of()));
        assertThrows(NullPointerException.class, () -> {
            new PublicGameState(1, cardState, null, playerState, PlayerId.PLAYER_2);
            new PublicGameState(1, null, PlayerId.PLAYER_1, playerState, PlayerId.PLAYER_2);
        });
    }

    @Test
    void ticketsCountWithNullTicketCount() {
        PublicGameState pgs = new PublicGameState(0, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2);
        assertEquals(0, pgs.ticketsCount());
    }

    @Test
    void ticketsCountWithKnownExample() {
        assertEquals(2, new PublicGameState(2, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2).ticketsCount());
        assertEquals(7, new PublicGameState(7, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2).ticketsCount());
        assertEquals(157489,  new PublicGameState(157489, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2).ticketsCount());
    }

    @Test
    void ticketsCountIsImmuable() {
        int ticketsCount = 3;
        PublicGameState pgs = new PublicGameState(ticketsCount, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2);
        assertEquals(3, pgs.ticketsCount());
        ticketsCount = 10;
        assertNotEquals(10, pgs.ticketsCount());
    }

    @Test
    void canDrawTicketsTest() {
        PublicGameState pgs = new PublicGameState(0, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2);
        assertFalse(pgs.canDrawTickets());
        PublicGameState pgs1 = new PublicGameState(1, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2);
        assertTrue(pgs1.canDrawTickets());
    }

    @Test
    void cardStateReturnsIdenticalCardState() {
        PublicGameState pgs = new PublicGameState(0, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2);
        PublicCardState cardState = createNormalCardState();
        for (int i = 0; i < pgs.cardState().faceUpCards().size(); ++i) {
            assertEquals(cardState.faceUpCards().get(i),pgs.cardState().faceUpCards().get(i));
        }
        assertEquals(cardState.deckSize(), pgs.cardState().deckSize());
        assertEquals(cardState.discardsSize(), pgs.cardState().discardsSize());
    }

    @Test
    void cardStateIsImmuable() {
        PublicGameState pgs = new PublicGameState(0, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2);
        PublicCardState cardState = createNormalCardState();
        assertNotEquals(cardState, pgs.cardState());
    }

    @Test
    void canDrawCardsOnKnownExample() {
        PublicGameState pgs = new PublicGameState(0, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2);
        assertTrue(pgs.canDrawCards());

        PublicCardState pcs = new PublicCardState(createNormalCardState().faceUpCards(), 2, 2);
        PublicGameState pgs1 = new PublicGameState(0, pcs, PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2);
        assertFalse(pgs1.canDrawCards());
    }

    @Test
    void canDrawCardsWithImmuability() {
        int deckSize = 5;
        int discardSize = 4;
        PublicCardState cardState = new PublicCardState(createNormalCardState().faceUpCards(), deckSize, discardSize);
        PublicGameState pgs = new PublicGameState(0, cardState, PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2);
        assertTrue(pgs.canDrawCards());
        deckSize = 3;
        discardSize = 1;
        assertTrue(pgs.canDrawCards());
    }

    @Test
    void currentPlayerIdTest() {
        PublicGameState pgs = new PublicGameState(0, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2);
        assertEquals(PlayerId.PLAYER_1, pgs.currentPlayerId());

        PlayerId currentPlayer = PlayerId.PLAYER_1;
        PublicGameState pgs1 = new PublicGameState(0, createNormalCardState(), currentPlayer, createNormalMap(), PlayerId.PLAYER_2);
        currentPlayer = PlayerId.PLAYER_2;
        assertNotEquals(PlayerId.PLAYER_2, pgs1.currentPlayerId());
    }

    @Test
    void lastPlayerTest() {
        PublicGameState pgs = new PublicGameState(0, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2);
        assertEquals(PlayerId.PLAYER_2, pgs.lastPlayer());
    }

    /**
     * Marche mieux quand la map est une Treemap car elle est triée
     */
    @Test
    void claimedRoutesTest() {
        PublicGameState pgs = new PublicGameState(0, createNormalCardState(), PlayerId.PLAYER_1, createNormalMap(), PlayerId.PLAYER_2);
        List<Route> routes = List.of(
                ALL_ROUTES.get(0),
                ALL_ROUTES.get(1),
                ALL_ROUTES.get(2),
                ALL_ROUTES.get(3)
        );
        assertEquals(routes, pgs.claimedRoutes());

        List<Route> routez = List.of(
                ALL_ROUTES.get(0),
                ALL_ROUTES.get(1),
                ALL_ROUTES.get(2),
                ALL_ROUTES.get(3),
                ALL_ROUTES.get(4)
        );
        assertNotEquals(routez, pgs.claimedRoutes());
    }
}
