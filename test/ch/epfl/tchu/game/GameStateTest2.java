package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest2 {

    public static List<Station> stations() {
        return ALL_STATIONS;
    }

    public static List<Route> routes() {
        return ALL_ROUTES;
    }

    public static List<Ticket> tickets() {
        return ALL_TICKETS;
    }

    // Stations - cities
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

    // Stations - countries
    private static final Station DE1 = new Station(34, "Allemagne");
    private static final Station DE2 = new Station(35, "Allemagne");
    private static final Station DE3 = new Station(36, "Allemagne");
    private static final Station DE4 = new Station(37, "Allemagne");
    private static final Station DE5 = new Station(38, "Allemagne");
    private static final Station AT1 = new Station(39, "Autriche");
    private static final Station AT2 = new Station(40, "Autriche");
    private static final Station AT3 = new Station(41, "Autriche");
    private static final Station IT1 = new Station(42, "Italie");
    private static final Station IT2 = new Station(43, "Italie");
    private static final Station IT3 = new Station(44, "Italie");
    private static final Station IT4 = new Station(45, "Italie");
    private static final Station IT5 = new Station(46, "Italie");
    private static final Station FR1 = new Station(47, "France");
    private static final Station FR2 = new Station(48, "France");
    private static final Station FR3 = new Station(49, "France");
    private static final Station FR4 = new Station(50, "France");

    // Countries
    private static final List<Station> DE = List.of(DE1, DE2, DE3, DE4, DE5);
    private static final List<Station> AT = List.of(AT1, AT2, AT3);
    private static final List<Station> IT = List.of(IT1, IT2, IT3, IT4, IT5);
    private static final List<Station> FR = List.of(FR1, FR2, FR3, FR4);

    private static final List<Station> ALL_STATIONS = List.of(
            BAD, BAL, BEL, BER, BRI, BRU, COI, DAV, DEL, FRI, GEN, INT, KRE, LAU, LCF, LOC, LUC,
            LUG, MAR, NEU, OLT, PFA, SAR, SCE, SCZ, SIO, SOL, STG, VAD, WAS, WIN, YVE, ZOU, ZUR,
            DE1, DE2, DE3, DE4, DE5, AT1, AT2, AT3, IT1, IT2, IT3, IT4, IT5, FR1, FR2, FR3, FR4);

    // Routes
    private static final List<Route> ALL_ROUTES = List.of(
            new Route("AT1_STG_1", AT1, STG, 4, Route.Level.UNDERGROUND, null),
            new Route("AT2_VAD_1", AT2, VAD, 1, Route.Level.UNDERGROUND, Color.RED),
            new Route("BAD_BAL_1", BAD, BAL, 3, Route.Level.UNDERGROUND, Color.RED),
            new Route("BAD_OLT_1", BAD, OLT, 2, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("BAD_ZUR_1", BAD, ZUR, 1, Route.Level.OVERGROUND, Color.YELLOW),
            new Route("BAL_DE1_1", BAL, DE1, 1, Route.Level.UNDERGROUND, Color.BLUE),
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
            new Route("BRI_IT5_1", BRI, IT5, 3, Route.Level.UNDERGROUND, Color.GREEN),
            new Route("BRI_LOC_1", BRI, LOC, 6, Route.Level.UNDERGROUND, null),
            new Route("BRI_SIO_1", BRI, SIO, 3, Route.Level.UNDERGROUND, Color.BLACK),
            new Route("BRI_WAS_1", BRI, WAS, 4, Route.Level.UNDERGROUND, Color.RED),
            new Route("BRU_COI_1", BRU, COI, 5, Route.Level.UNDERGROUND, null),
            new Route("BRU_DAV_1", BRU, DAV, 4, Route.Level.UNDERGROUND, Color.BLUE),
            new Route("BRU_IT2_1", BRU, IT2, 2, Route.Level.UNDERGROUND, Color.GREEN),
            new Route("COI_DAV_1", COI, DAV, 2, Route.Level.UNDERGROUND, Color.VIOLET),
            new Route("COI_SAR_1", COI, SAR, 1, Route.Level.UNDERGROUND, Color.WHITE),
            new Route("COI_WAS_1", COI, WAS, 5, Route.Level.UNDERGROUND, null),
            new Route("DAV_AT3_1", DAV, AT3, 3, Route.Level.UNDERGROUND, null),
            new Route("DAV_IT1_1", DAV, IT1, 3, Route.Level.UNDERGROUND, null),
            new Route("DAV_SAR_1", DAV, SAR, 3, Route.Level.UNDERGROUND, Color.BLACK),
            new Route("DE2_SCE_1", DE2, SCE, 1, Route.Level.OVERGROUND, Color.YELLOW),
            new Route("DE3_KRE_1", DE3, KRE, 1, Route.Level.OVERGROUND, Color.ORANGE),
            new Route("DE4_KRE_1", DE4, KRE, 1, Route.Level.OVERGROUND, Color.WHITE),
            new Route("DE5_STG_1", DE5, STG, 2, Route.Level.OVERGROUND, null),
            new Route("DEL_FR4_1", DEL, FR4, 2, Route.Level.UNDERGROUND, Color.BLACK),
            new Route("DEL_LCF_1", DEL, LCF, 3, Route.Level.UNDERGROUND, Color.WHITE),
            new Route("DEL_SOL_1", DEL, SOL, 1, Route.Level.UNDERGROUND, Color.VIOLET),
            new Route("FR1_MAR_1", FR1, MAR, 2, Route.Level.UNDERGROUND, null),
            new Route("FR2_GEN_1", FR2, GEN, 1, Route.Level.OVERGROUND, Color.YELLOW),
            new Route("FR3_LCF_1", FR3, LCF, 2, Route.Level.UNDERGROUND, Color.GREEN),
            new Route("FRI_LAU_1", FRI, LAU, 3, Route.Level.OVERGROUND, Color.RED),
            new Route("FRI_LAU_2", FRI, LAU, 3, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("GEN_LAU_1", GEN, LAU, 4, Route.Level.OVERGROUND, Color.BLUE),
            new Route("GEN_LAU_2", GEN, LAU, 4, Route.Level.OVERGROUND, Color.WHITE),
            new Route("GEN_YVE_1", GEN, YVE, 6, Route.Level.OVERGROUND, null),
            new Route("INT_LUC_1", INT, LUC, 4, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("IT3_LUG_1", IT3, LUG, 2, Route.Level.UNDERGROUND, Color.WHITE),
            new Route("IT4_LOC_1", IT4, LOC, 2, Route.Level.UNDERGROUND, Color.ORANGE),
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


    private static final List<Ticket> ALL_TICKETS = List.of(
            // City-to-city tickets
            new Ticket(BAL, BER, 5),
            new Ticket(BAL, BRI, 10),
            new Ticket(BAL, STG, 8),
            new Ticket(BER, COI, 10),
            new Ticket(BER, LUG, 12),
            new Ticket(BER, SCZ, 5),
            new Ticket(BER, ZUR, 6),
            new Ticket(FRI, LUC, 5),
            new Ticket(GEN, BAL, 13),
            new Ticket(GEN, BER, 8),
            new Ticket(GEN, SIO, 10),
            new Ticket(GEN, ZUR, 14),
            new Ticket(INT, WIN, 7),
            new Ticket(KRE, ZUR, 3),
            new Ticket(LAU, INT, 7),
            new Ticket(LAU, LUC, 8),
            new Ticket(LAU, STG, 13),
            new Ticket(LCF, BER, 3),
            new Ticket(LCF, LUC, 7),
            new Ticket(LCF, ZUR, 8),
            new Ticket(LUC, VAD, 6),
            new Ticket(LUC, ZUR, 2),
            new Ticket(LUG, COI, 10),
            new Ticket(NEU, WIN, 9),
            new Ticket(OLT, SCE, 5),
            new Ticket(SCE, MAR, 15),
            new Ticket(SCE, STG, 4),
            new Ticket(SCE, ZOU, 3),
            new Ticket(STG, BRU, 9),
            new Ticket(WIN, SCZ, 3),
            new Ticket(ZUR, BAL, 4),
            new Ticket(ZUR, BRU, 11),
            new Ticket(ZUR, LUG, 9),
            new Ticket(ZUR, VAD, 6));


    @Test
    void initialRetrievesCorrectAmountOfCardsFromDeck() {
        GameState gameState = GameState.initial(SortedBag.of(tickets()), new Random());
      //  assertEquals(Constants.ALL_CARDS.size() - 8, gameState.cardState().totalSize());
    }

    @Test
    void initialDistributesCorrectlyCardsToPlayers() {
        GameState gameState = GameState.initial(SortedBag.of(tickets()), new Random());
        assertEquals(Constants.INITIAL_CARDS_COUNT, gameState.playerState(PlayerId.PLAYER_1).cardCount());
        assertEquals(Constants.INITIAL_CARDS_COUNT, gameState.playerState(PlayerId.PLAYER_2).cardCount());
        PlayerId.ALL.forEach(p -> System.out.println(gameState.playerState(p).cards()));
    }

    @Test
    void initialIsRandomizedAndImmutable() {
        GameState gameState = GameState.initial(SortedBag.of(tickets()), new Random());
        assertNotEquals(GameState.initial(SortedBag.of(tickets()), new Random()), gameState);
    }

    @Test
    void initialRandomizesFirstPlayer() {
        List<PlayerId> list = new ArrayList<>();
        for (int i = 0; i < 50; ++i) {
            list.add(GameState.initial(SortedBag.of(tickets()), new Random()).currentPlayerId());
            System.out.println(list.get(i).toString());
        }
        assertTrue(list.contains(PlayerId.PLAYER_1) && list.contains(PlayerId.PLAYER_2));
    }

    @Test
    void initialAddsAllTickets() {
        GameState gameState = GameState.initial(SortedBag.of(tickets()), new Random());
        assertEquals(tickets().size(), gameState.ticketsCount());
        assertEquals(0, GameState.initial(SortedBag.of(), new Random()).ticketsCount());
    }


    @Test
    void topTicketsThrowsIllegalArgumentException() {
        GameState gameState = GameState.initial(SortedBag.of(tickets()), new Random());
        assertThrows(IllegalArgumentException.class, () -> {
            gameState.topTickets(-1);
            gameState.topTickets(tickets().size()+1);
        });
    }

    @Test
    void topTicketsWithKnownExamples() {
        GameState gameState = GameState.initial(SortedBag.of(tickets()), new Random());
        assertEquals(3, gameState.topTickets(3).size());
        assertEquals(0, gameState.topTickets(0).size());
        assertEquals(15, gameState.topTickets(15).size());

        GameState gameState1 = GameState.initial(SortedBag.of(tickets().subList(0, 5)), new Random());
        assertTrue(gameState1.topTickets(5).contains(SortedBag.of(tickets().subList(0, 5))));
    }

    @Test
    void withoutTopTicketsWithKnownExamples() {
        GameState gameState = GameState.initial(SortedBag.of(tickets().subList(0, 5)), new Random());
        assertFalse(gameState.withoutTopTickets(3).topTickets(2).contains(gameState.topTickets(3)));
        gameState = GameState.initial(SortedBag.of(tickets().subList(2, 20)), new Random());
        assertFalse(gameState.withoutTopTickets(12).topTickets(6).contains(gameState.topTickets(12)));
    }

    @Test
    void withoutTopTicketsTest(){
        assertEquals(tickets().size()-5,
                GameState.initial(SortedBag.of(tickets()), new Random()).withoutTopTickets(5).ticketsCount());
    }
    @Test
    void withoutTopTicketsDoesNotWorkForWrongInt(){
        Random rng = new Random();
        assertThrows(IllegalArgumentException.class, () -> {
            GameState.initial(SortedBag.of(tickets()), rng).withoutTopTickets(-1);
        });
        assertThrows(IllegalArgumentException.class, () ->{
            GameState.initial(SortedBag.of(tickets()), rng).withoutTopTickets(1000);
        });
    }

    /**
     * a faire
     */
    @Test
    void topCardThrowsIllegalArgumentException() {

    }

    @Test
    void withoutTopCardWorksDoesNotWork() {
        GameState game1 = GameState.initial(SortedBag.of(ChMap.tickets()), new Random());
        for(int i = 0; i < 97; ++i){
            game1 = game1.withoutTopCard();
        }
        final GameState gameState = game1;
        assertThrows(IllegalArgumentException.class, () -> {
            gameState.withoutTopCard();
        });
    }
    @Test
    void withMoreDiscardedCardsWorksForKnownExample() {
        assertEquals(Card.ALL.size(),
                GameState.initial(SortedBag.of(ChMap.tickets()), new Random()).withMoreDiscardedCards(SortedBag.of(Card.ALL)).cardState().discardsSize());
    }
    //Trouver un moyen d'avoir un deck vide.
    @Test
    void withCardsDeckRecreatedIfNeeded() {
        assertNotEquals(GameState.initial(SortedBag.of(ChMap.tickets()), new Random()).withCardsDeckRecreatedIfNeeded(new Random()),
                GameState.initial(SortedBag.of(ChMap.tickets()), new Random()).withCardsDeckRecreatedIfNeeded(new Random()));
    }
    @Test
    void withInitiallyChosenTicketsWorksForKnownExample() {
        assertEquals(1,
                GameState.initial(SortedBag.of(ChMap.tickets()), new Random()).withInitiallyChosenTickets
                        (PlayerId.PLAYER_1, SortedBag.of(ChMap.tickets().get(1))).
                        playerState
                                (PlayerId.PLAYER_1).tickets().size());
    }
    @Test
    void withInitiallyChosenTicketsDoesNotWork() {

        GameState game1 = GameState.initial(SortedBag.of(ChMap.tickets()), new Random()).
                withInitiallyChosenTickets(PlayerId.PLAYER_1,
                        SortedBag.of(ChMap.tickets().get(1)));

        assertThrows(IllegalArgumentException.class, () -> {
            game1.withInitiallyChosenTickets(PlayerId.PLAYER_1, SortedBag.of(ChMap.tickets().get(2)));
        });
    }
    @Test
    void withChosenAdditionalTicketsWorksForKnownExample() {
        assertEquals(1, GameState.initial(SortedBag.of(ChMap.tickets()), new Random()).withChosenAdditionalTickets
                (SortedBag.of(tickets().get(0)), SortedBag.of(tickets().get(0))).currentPlayerState().tickets().size());
    }
    @Test
    void withChosenAdditionalTicketsDoesNotWork() {
        assertThrows(IllegalArgumentException.class, () -> {
            GameState.initial(SortedBag.of(ChMap.tickets()), new Random()).withChosenAdditionalTickets
                    (SortedBag.of(tickets().get(1)), SortedBag.of(tickets().get(0))).currentPlayerState();
        });
    }
    @Test
    void withBlindlyDrawnCardWorksForKnownExample() {
        assertEquals(5, GameState.initial(SortedBag.of(ChMap.tickets()), new Random()).withBlindlyDrawnCard().currentPlayerState().cards().size());
    }











}