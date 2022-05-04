package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Copy;

import java.util.*;

import static ch.epfl.tchu.game.Card.*;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;
import static org.junit.jupiter.api.Assertions.*;

class SerdeTest2 {

    private final int NUMBER_OF_TESTS = 100;

    private <T> void serdeOneOfTestForGivenType(List<T> list, int elementToRemove, boolean printActualObject) {
        Preconditions.checkArgument(!list.isEmpty() && 0 <= elementToRemove && elementToRemove < list.size());
        if (printActualObject) {
            System.out.println("Loading test for " + list.get(0).getClass().getName() + " and testing by removing element number " + elementToRemove);
        }
        Serde<T> serde = Serde.oneOf(list);
        for (int i = 0; i < list.size(); ++i) {
            if (printActualObject) {
                System.out.println(i + " - " + serde.serialize(list.get(i)));
                System.out.println(list.get(i) + " - " + serde.deserialize(String.valueOf(i)));
                System.out.println();
            }
            assertEquals(String.valueOf(i), serde.serialize(list.get(i)));
            assertEquals(list.get(i), serde.deserialize(String.valueOf(i)));
        }

        List<T> listWithMissingElement = new ArrayList<>(list);
        listWithMissingElement.remove(elementToRemove);

        Serde<T> serdeWithMissingElement = Serde.oneOf(listWithMissingElement);
        for (int i = 0; i < elementToRemove; ++i) {
            assertEquals(serde.serialize(list.get(i)), serdeWithMissingElement.serialize(listWithMissingElement.get(i)));
        }

        for (int i = elementToRemove; i < listWithMissingElement.size(); ++i) {
            assertNotEquals(serde.deserialize(String.valueOf(i)), serdeWithMissingElement.deserialize(String.valueOf(i)));
        }
    }

    private <T> void randomizedListOfTestsForGivenType(List<T> list, CharSequence separator, Random rng) {
        Serde<List<T>> serde = Serde.listOf(Serde.oneOf(list), separator);
        List<String> serializeList = new ArrayList<>();
        List<T> deserializeList = new ArrayList<>();
        for (int i = 0; i < 1 + rng.nextInt(99); ++i) {
            int k = 1 + rng.nextInt(list.size() - 1);
            deserializeList.add(list.get(k));
            serializeList.add(String.valueOf(k));
        }

        assertEquals(String.join(separator, serializeList), serde.serialize(deserializeList));
        assertEquals(deserializeList, serde.deserialize(String.join(separator, serializeList)));
    }

    private void randomizedIntegerSerdeTests(Random rng) {
        int i = rng.nextInt();
        assertEquals(String.valueOf(i), Serdes.intSerde.serialize(i));
    }


    private <T> void randomizedOneOfTests(Serde<T> serde, List<T> list, boolean testForNullObject, boolean printOut, Random rng) {
        int i = testForNullObject ? rng.nextInt(list.size()+1) : rng.nextInt(list.size());
        T testedObject = i == list.size() ? null : list.get(i);
        if (testForNullObject && i == list.size()) {
            assertEquals("", serde.serialize(testedObject));
            assertNull(serde.deserialize(""));
        } else {
            assertEquals(String.valueOf(i), serde.serialize(testedObject));
            assertEquals(testedObject, serde.deserialize(String.valueOf(i)));
        }

        if (printOut) {
            System.out.println("Tested object: " + testedObject);
            System.out.println("Serialized object = " + serde.serialize(testedObject) + " | Deserialized object = " +
                    serde.deserialize(serde.serialize(testedObject)) + "\n");
        }
    }

    private <T> void randomizedListOfTests(Serde<List<T>> serde, List<T> list, boolean testForEmptyList, boolean printOut, Random rng) {
        List<String> serializedList = new ArrayList<>();
        List<T> deserializedList = new ArrayList<>();
        int k = testForEmptyList ? 2 + rng.nextInt(10) : 1 + rng.nextInt(10);



        if (k == 11) {
            assertEquals("", serde.serialize(List.of()));
            assertEquals(List.of(), serde.deserialize(""));
        } else {
            for (int i = 0; i < k; ++i) {
                int r = rng.nextInt(list.size());
                serializedList.add(String.valueOf(r));
                deserializedList.add(list.get(r));
            }
            assertEquals(String.join(",", serializedList), serde.serialize(deserializedList));
            assertEquals(deserializedList, serde.deserialize(String.join(",", serializedList)));
        }

        if (printOut) {
            System.out.println("Tested list: " + deserializedList.toString());
            System.out.println("Serialized list: " + serde.serialize(deserializedList) + " | " + "Deserialized list: " +
                    serde.deserialize(serde.serialize(deserializedList)) + "\n");
        }
    }

    private <T extends Comparable<T>> void randomizedBagOfTests(Serde<SortedBag<T>> serde, List<T> list, boolean testForEmptyBag, boolean printOut, Random rng) {
        List<T> deserializedBag = new ArrayList<>();
        List<String> serializedBag = new ArrayList<>();

        int k = testForEmptyBag ? 2 + rng.nextInt(10) : 1 + rng.nextInt(10);



        if (k == 11) {
            assertEquals("", serde.serialize(SortedBag.of()));
            assertEquals(SortedBag.of(), serde.deserialize(""));
        } else {
            for (int i = 0; i < k; ++i) {
                int r = rng.nextInt(list.size());
                serializedBag.add(String.valueOf(r));
                deserializedBag.add(list.get(r));
            }

            assertEquals(String.join(",", SortedBag.of(serializedBag)), serde.serialize(SortedBag.of(deserializedBag)));
            assertEquals(SortedBag.of(deserializedBag), serde.deserialize(String.join(",", SortedBag.of(serializedBag))));
        }

        if (printOut) {
            System.out.println("Tested bag: " + SortedBag.of(deserializedBag).toString());
            System.out.println("Serialized list: " + serde.serialize(SortedBag.of(deserializedBag)) + " | " + "Deserialized list: " +
                    serde.deserialize(serde.serialize(SortedBag.of(deserializedBag))) + "\n");
        }
    }

    private static void serdePCS(PublicCardState pcs, String str) {
        assertEquals(str, Serdes.publicCardStateSerde.serialize(pcs));
        assertEquals(pcs.faceUpCards(), Serdes.publicCardStateSerde.deserialize(str).faceUpCards());
        assertEquals(pcs.deckSize(), Serdes.publicCardStateSerde.deserialize(str).deckSize());
        assertEquals(pcs.discardsSize(), Serdes.publicCardStateSerde.deserialize(str).discardsSize());
    }

    private static void serdesPS(PlayerState ps, String str) {
        assertEquals(str, Serdes.playerStateSerde.serialize(ps));
        assertEquals(ps.tickets(), Serdes.playerStateSerde.deserialize(str).tickets());
        assertEquals(ps.cards(), Serdes.playerStateSerde.deserialize(str).cards());
        assertEquals(ps.routes(), Serdes.playerStateSerde.deserialize(str).routes());
    }


    @Test
    void SortedBagOfTicketsTest() {
        List<Ticket> list = new ArrayList<>();
        list.add(ChMap.tickets().get(25));
        list.add(ChMap.tickets().get(2));
        list.add(ChMap.tickets().get(5));
        list.add(ChMap.tickets().get(12));

        System.out.println(SortedBag.of(list));
        System.out.println(Serdes.ticketBagSerde.serialize(SortedBag.of(list)));
        System.out.println(Serdes.ticketBagSerde.deserialize("5,2,12,25"));
    }

    @Test
    void oneOfWorksOnRandomExample() {

        //Couleur
        for (int i = 0; i < Color.ALL.size(); ++i) {
            serdeOneOfTestForGivenType(Color.ALL, i, false);
        }

        //Card
        for (int i = 0; i < Card.ALL.size(); ++i) {
            serdeOneOfTestForGivenType(Card.ALL, i, false);
        }

        //PlayerId
        for (int i = 0; i < PlayerId.ALL.size(); ++i) {
            serdeOneOfTestForGivenType(PlayerId.ALL, i, false);
        }

        //Routes
        for (int i = 0; i < ChMap.routes().size(); ++i) {
            serdeOneOfTestForGivenType(ChMap.routes(), i, false);
        }

        //Tickets
        for (int i = 0; i < ChMap.tickets().size()-8; ++i) {
            serdeOneOfTestForGivenType(ChMap.tickets().subList(0, ChMap.tickets().size()-8), i, true);
        }


    }


    @Test
    void oneOfReturnsEmptyStringForNullElement() {
        Serde<Color> serde = Serde.oneOf(Color.ALL);
        assertEquals("", serde.serialize(null));
        assertNull(serde.deserialize(""));
    }

    @Test
    void listOfReturnsEmptyStringForEmptyList() {
        Serde<List<Route>> serde = Serde.listOf(Serde.oneOf(ChMap.routes()), ",");
        assertEquals("", serde.serialize(List.of()));
        assertEquals(List.of(), serde.deserialize(""));
    }

    @Test
    void bagOfReturnsEmptyStringForEmptySortedBag() {
        Serde<SortedBag<Card>> serde = Serde.bagOf(Serde.oneOf(Card.ALL), ",");
        assertEquals("", serde.serialize(SortedBag.of()));
        assertEquals(SortedBag.of(), serde.deserialize(""));
    }

    @Test
    void listOfWorksOnKnownExample() {
        Serde<Color> color = Serde.oneOf(Color.ALL);



        Serde<List<Color>> listOfColor = Serde.listOf(color, ",");
        assertEquals("5,4,1,3,1", listOfColor.serialize(List.of(Color.ORANGE, Color.YELLOW, Color.VIOLET, Color.GREEN, Color.VIOLET)));
        assertEquals("3,3,3", listOfColor.serialize(List.of(Color.GREEN, Color.GREEN, Color.GREEN)));

        assertEquals(List.of(Color.BLUE, Color.BLUE, Color.BLACK), listOfColor.deserialize("2,2,0"));

        Serde<PlayerId> playerId = Serde.oneOf(PlayerId.ALL);
        Serde<List<PlayerId>> listOfPlayerId = Serde.listOf(playerId, "-");
        assertEquals("0-0-1-0-1-1", listOfPlayerId.serialize(List.of(PLAYER_1, PLAYER_1, PLAYER_2, PLAYER_1, PLAYER_2, PLAYER_2)));



    }

    @Test
    void listOfWorksOnRandomExample() {
        for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
            randomizedListOfTestsForGivenType(Color.ALL, "/", new Random());
        }

        for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
            randomizedListOfTestsForGivenType(Card.ALL, "?", new Random());
        }

        for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
            randomizedListOfTestsForGivenType(PlayerId.ALL, "separator", new Random());
        }

        for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
            randomizedListOfTestsForGivenType(ChMap.routes(), "+", new Random());
        }

        for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
            randomizedListOfTestsForGivenType(ChMap.tickets().subList(0, ChMap.tickets().size()-8), ",", new Random());
        }
    }

    @Test
    void bagOfWorksOnKnownExample() {
        Serde<Color> color = Serde.oneOf(Color.ALL);

        Serde<SortedBag<Color>> bagOfColor = Serde.bagOf(color, ",");

        assertEquals("1,1,2", bagOfColor.serialize(SortedBag.of(2, Color.VIOLET, 1, Color.BLUE)));
        assertEquals("1,4,4,5", bagOfColor.serialize(SortedBag.of(List.of(Color.VIOLET, Color.YELLOW, Color.ORANGE, Color.YELLOW))));

        Serde<PlayerId> playerId = Serde.oneOf(PlayerId.ALL);
        Serde<SortedBag<PlayerId>> bagOfPlayerId = Serde.bagOf(playerId, "(");

        assertEquals("0(0(0(0(1(1(1", bagOfPlayerId.serialize(SortedBag.of(3, PLAYER_2, 4, PLAYER_1)));
        assertEquals("0(0(1", bagOfPlayerId.serialize(SortedBag.of(List.of(PLAYER_2, PLAYER_1, PLAYER_1))));
    }

    @Test
    void ofPublicCardStateWorksOnKnownExample() {
        PublicCardState pcs = new PublicCardState(List.of(VIOLET, BLUE, BLUE, LOCOMOTIVE, RED), 12, 40);
        assertEquals("1,2,2,8,6;12;40", Serdes.publicCardStateSerde.serialize(pcs));
        assertEquals(pcs.faceUpCards(), Serdes.publicCardStateSerde.deserialize("1,2,2,8,6;12;40").faceUpCards());
        assertEquals(pcs.deckSize(), Serdes.publicCardStateSerde.deserialize("1,2,2,8,6;12;40").deckSize());
        assertEquals(pcs.discardsSize(), Serdes.publicCardStateSerde.deserialize("1,2,2,8,6;12;40").discardsSize());
    }

    @Test
    void ofPublicPlayerStateWorksWithEmptyList() {
        PublicPlayerState pps = new PublicPlayerState(2, 5, List.of());
        assertEquals(List.of(), Serdes.publicPlayerStateSerde.deserialize("2;5;").routes());
    }

    @Test
    void integerSerdeWorksOnKnownExample() {
        assertEquals("-59", Serdes.intSerde.serialize(-59));
        assertEquals("15748", Serdes.intSerde.serialize(15748));
        assertEquals(-3619, Serdes.intSerde.deserialize("-3619"));
        assertEquals(0, Serdes.intSerde.deserialize("0"));
    }

    @Test
    void integerSerdeWorksOnRandomExamples() {
        for (int i = 0; i <NUMBER_OF_TESTS; ++i) {
            randomizedIntegerSerdeTests(new Random());
        }
    }

    @Test
    void stringSerdeWorksOnKnownExample() {
        assertEquals("Q2hhcmxlcw==", Serdes.stringSerde.serialize("Charles"));
        assertEquals("Charles", Serdes.stringSerde.deserialize("Q2hhcmxlcw=="));
        assertEquals("dENIdQ==", Serdes.stringSerde.serialize("tCHu"));
        assertEquals("tC\nHu", Serdes.stringSerde.deserialize("dEMKSHU="));
        assertEquals("tchutchu", Serdes.stringSerde.deserialize("dGNodXRjaHU="));
        assertNotEquals(Serdes.stringSerde.serialize("elyes"), Serdes.stringSerde.serialize("ELYES"));
    }

    @Test
    void oneOfPlayerIdWorksOnRandomExample() {
        for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
            randomizedOneOfTests(Serdes.playerIdSerde, PlayerId.ALL, true, false, new Random());
        }
    }

    @Test
    void oneOfTurnKindWorksOnRandomExample() {
        for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
            randomizedOneOfTests(Serdes.turnKindSerde, Player.TurnKind.ALL, false, false, new Random());
        }
    }

    @Test
    void oneOfCardWorksOnRandomExample() {
        for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
            randomizedOneOfTests(Serdes.cardSerde, Card.ALL, false, false, new Random());
        }
    }

    @Test
    void oneOfRouteWorksOnRandomExample() {
        for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
            randomizedOneOfTests(Serdes.routeSerde, ChMap.routes(), false, false, new Random());
        }
    }

    @Test
    void oneOfTicketWorksOnRandomExample() {
        for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
            randomizedOneOfTests(Serdes.ticketSerde, ChMap.tickets().subList(0, ChMap.tickets().size() - 8), false, false, new Random());
        }
    }

    @Test
    void listOfSerdeWorksOnKnownExample() {
        assertEquals("Qm9i,QWxpY2U=,RWx5ZXM=,UmFzYW4=", Serdes.stringListSerde.serialize(List.of("Bob","Alice","Elyes","Rasan")));
        assertEquals(List.of("Bob","Alice","Elyes","Rasan"), Serdes.stringListSerde.deserialize("Qm9i,QWxpY2U=,RWx5ZXM=,UmFzYW4="));
        assertEquals("", Serdes.stringListSerde.serialize(List.of()));
        assertEquals(List.of(), Serdes.stringListSerde.deserialize(""));
    }

    @Test
    void listOfCardWorksOnRandomExample() {
        for (int i = 0; i < NUMBER_OF_TESTS; ++i)  {
            randomizedListOfTests(Serdes.cardListSerde, Card.ALL, true, false, new Random());
        }
    }

    @Test
    void listOfRouteWorksOnRandomExample() {
        for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
            randomizedListOfTests(Serdes.routeListSerde, ChMap.routes(), true, false, new Random());
        }
    }

    @Test
    void bagOfCardWorksOnRandomExample() {
        for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
            randomizedBagOfTests(Serdes.cardBagSerde, Card.ALL, true, false, new Random());
        }
    }

    @Test
    void bagOfTicketWorksOnKnownExample() {
        List<Ticket> list = new ArrayList<>();
        list.add(ChMap.tickets().get(4));
        list.add(ChMap.tickets().get(8));
        list.add(ChMap.tickets().get(2));
        list.add(ChMap.tickets().get(17));
        list.add(ChMap.tickets().get(21));
        SortedBag<Ticket> sb = SortedBag.of(list);
        assertEquals("4,2,8,17,21", Serdes.ticketBagSerde.serialize(sb));
        assertEquals("5,0,1,12,15", Serdes.ticketBagSerde.serialize(SortedBag.of(List.of(ChMap.tickets().get(5),
                ChMap.tickets().get(15), ChMap.tickets().get(12), ChMap.tickets().get(1), ChMap.tickets().get(0)))));


    }

    @Test
    void listOfCardBagsWorksOnRandomExample() {
        for (int p = 0; p < NUMBER_OF_TESTS; ++p) {
            List<SortedBag<Card>> deserializedList = new ArrayList<>();
            List<String> serializedList = new ArrayList<>();
            Random rng = new Random();
            int k = 1 + rng.nextInt(15);
            int n = 2 + rng.nextInt(7);

            if (n == 8) {
                assertEquals("", Serdes.cardBagListSerde.serialize(List.of()));
                assertEquals(List.of(), Serdes.cardBagListSerde.deserialize(""));
            }

            for (int i = 0; i < n - 1; ++i) {
                List<Card> sb = new ArrayList<>();
                List<String> str = new ArrayList<>();
                for (int j = 0; j < k; ++j) {
                    int m = rng.nextInt(Card.ALL.size());
                    sb.add(Card.ALL.get(m));
                    str.add(String.valueOf(m));
                }
                deserializedList.add(SortedBag.of(sb));
                serializedList.add(String.join(",", SortedBag.of(str)));
            }
            assertEquals(String.join(";", serializedList), Serdes.cardBagListSerde.serialize(deserializedList));
            assertEquals(deserializedList, Serdes.cardBagListSerde.deserialize(String.join(";", serializedList)));

            System.out.println("Tested object: " + deserializedList);
            System.out.println("Serialized object = " + Serdes.cardBagListSerde.serialize(deserializedList) + " | Deserialized object = " +
                    Serdes.cardBagListSerde.deserialize(Serdes.cardBagListSerde.serialize(deserializedList)) + "\n");

        }
    }

    @Test
    void oneOfTurnKindWorksOnKnownExample() {
        assertEquals("1", Serdes.turnKindSerde.serialize(Player.TurnKind.ALL.get(1)));
        assertEquals("2", Serdes.turnKindSerde.serialize(Player.TurnKind.ALL.get(2)));

    }

    @Test
    void ofPublicGameStateWorksOnKnownExample(){
        PublicGameState pgs = new PublicGameState(5,
                new PublicCardState(
                        List.of(BLUE, RED, Card.YELLOW, Card.ORANGE, WHITE), 10, 0),
                PLAYER_1,
                Map.of(PLAYER_1,
                        new PublicPlayerState(5, 5, ChMap.routes().subList(0, 3)),
                        PLAYER_2,
                        new PublicPlayerState(5, 5, List.of())),
                null
        );
        assertEquals("5:2,6,4,5,7;10;0:0:5;5;0,1,2:5;5;:", Serdes.publicGameStateSerde.serialize(pgs));
        System.out.println("check");
        List<Card> fu = List.of(RED, WHITE, BLUE, BLACK, RED);
        PublicCardState cs = new PublicCardState(fu, 30, 31);
        List<Route> rs1 = ChMap.routes().subList(0, 2);
        Map<PlayerId, PublicPlayerState> ps = Map.of(
                PLAYER_1, new PublicPlayerState(10, 11, rs1),
                PLAYER_2, new PublicPlayerState(20, 21, List.of()));
        PublicGameState gs =
                new PublicGameState(40, cs, PLAYER_2, ps, null);
        assertEquals("40:6,7,2,0,6;30;31:1:10;11;0,1:20;21;:", Serdes.publicGameStateSerde.serialize(gs));
    }

    @Test
    void testWorksForMultipleExamplesPS() {
        PlayerState ps = new PlayerState(SortedBag.of(ChMap.tickets().subList(0, 3)),
                SortedBag.of(ALL.subList(0, 2)),
                ChMap.routes().subList(0, 2));
        assertEquals("0,1,2;0,1;0,1", Serdes.playerStateSerde.serialize(ps));
        serdesPS(ps, "0,1,2;0,1;0,1");
        PlayerState ps2 = new PlayerState(SortedBag.of(ChMap.tickets().subList(0, 4)),
                SortedBag.of(ALL.subList(1, 4)),
                ChMap.routes().subList(4, 6));
        assertEquals("3,0,1,2;1,2,3;4,5", Serdes.playerStateSerde.serialize(ps2));
        serdesPS(ps2, "3,0,1,2;1,2,3;4,5");
        PlayerState ps3 = new PlayerState(SortedBag.of(ChMap.tickets().subList(5, 6)),
                SortedBag.of(ALL.subList(6, 8)),
                ChMap.routes().subList(3, 7));
        assertEquals("5;6,7;3,4,5,6", Serdes.playerStateSerde.serialize(ps3));
        serdesPS(ps3, "5;6,7;3,4,5,6");
    }

    @Test
    void testWorksForMultipleExamplesPCS() {
        PublicCardState cs = new PublicCardState(ALL.subList(0, 5),
                10,
                2);
        assertEquals("0,1,2,3,4;10;2", Serdes.publicCardStateSerde.serialize(cs));
        serdePCS(cs, "0,1,2,3,4;10;2");
        PublicCardState cs1 = new PublicCardState(ALL.subList(3, 8),
                19,
                34);
        assertEquals("3,4,5,6,7;19;34", Serdes.publicCardStateSerde.serialize(cs1));
        serdePCS(cs1, "3,4,5,6,7;19;34");
        PublicCardState cs2 = new PublicCardState(ALL.subList(2, 7),
                3,
                29);
        assertEquals("2,3,4,5,6;3;29", Serdes.publicCardStateSerde.serialize(cs2));
        serdePCS(cs2, "2,3,4,5,6;3;29");
    }

}