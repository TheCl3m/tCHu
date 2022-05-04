package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StationPartitionTest {

    private final Station Berne = new Station(0, "Berne");
    private final Station Delemont = new Station(1, "Délémeont");
    private final Station Fribourg = new Station(2, "Fribourg");
    private final Station Interlaken = new Station(3, "Interlaken");
    private final Station LaChauxDeFonds = new Station(4, "La Chaux-de-Fonds");
    private final Station Lausanne = new Station(5, "Lausanne");
    private final Station Lucerne = new Station(6, "Lucerne");
    private final Station Neuchatel = new Station(7, "Neuchâtel");
    private final Station Olten = new Station(8, "Olten");
    private final Station Schwyz = new Station(9, "Schwyz");
    private final Station Soleure = new Station(10, "Soleure");
    private final Station Wassen = new Station(11, "Wassen");
    private final Station Yverdon = new Station(12, "Yverdon");
    private final Station Zoug = new Station(13, "Zoug");
    private final Station Zurich = new Station(14, "Zürich");

    @Test
    void connectedTrivial() {
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Station BER = new Station(3, "Berne");
        Station BRI = new Station(4, "Brigue");
        Station BRU = new Station(5, "Brusio");
        Station COI = new Station(6, "Coire");
        Station DAV = new Station(7, "Davos");
        Station DEL = new Station(8, "Delémont");
        Station FRI = new Station(9, "Fribourg");
        StationPartition.Builder builder = new StationPartition.Builder(10);
        builder.connect(BAD, BAL);
        builder.connect(BAL, BEL);
        builder.connect(BER, BEL);
        builder.connect(FRI, DEL);
        builder.connect(BRI, DEL);
        builder.connect(BRU, COI);
        // 2,2,2,2,8,6,6,7,8,8
        StationPartition partition = builder.build();
        assertTrue(partition.connected(FRI, BRI));
        assertFalse(partition.connected(FRI, DAV));
        assertFalse(partition.connected(BAD, BRU));
    }

    @Test
    void connectedButOutOfBounds(){
        Station BAD = new Station(0, "Baden");
        Station BAL = new Station(1, "Bâle");
        Station BEL = new Station(2, "Bellinzone");
        Station BER = new Station(3, "Berne");
        Station BRI = new Station(4, "Brigue");
        Station BRU = new Station(5, "Brusio");
        Station COI = new Station(6, "Coire");
        Station DAV = new Station(7, "Davos");
        Station DEL = new Station(8, "Delémont");
        Station FRI = new Station(9, "Fribourg");
        Station POS = new Station(100, "Positive");
        Station NEG = new Station(50, "Negative");
        Station COUNT = new Station(10, "Count");
        StationPartition.Builder builder = new StationPartition.Builder(10);
        builder.connect(BAD, BAL);
        builder.connect(BAL, BEL);
        builder.connect(BER, BEL);
        builder.connect(FRI, DEL);
        builder.connect(BRI, DEL);
        builder.connect(BRU, COI);
        StationPartition partition = builder.build();
        assertTrue(partition.connected(POS, POS));
        assertTrue(partition.connected(NEG, NEG));
        assertTrue(partition.connected(COUNT, COUNT));
        assertFalse(partition.connected(POS, NEG));
        assertFalse(partition.connected(COUNT, NEG));
        assertFalse(partition.connected(COUNT, POS));
    }

    @Test

    void errorThrownBuilderIllegal(){
        assertThrows(IllegalArgumentException.class, ()-> {new StationPartition.Builder(-5);});
    }

    @Test
    void enonceTest1Trivial(){
        StationPartition.Builder builder = new StationPartition.Builder(15);
        builder.connect(Lausanne, Interlaken);
        builder.connect(Berne, Interlaken);
        builder.connect(Fribourg, Interlaken);
        builder.connect(Soleure, Neuchatel);
        builder.connect(Olten, Neuchatel);
        builder.connect(Schwyz, Zoug);
        builder.connect(Lucerne, Zoug);
        builder.connect(Wassen, Zoug);
        StationPartition partition = builder.build();
        assertTrue(partition.connected(Lausanne, Berne));
        assertTrue(partition.connected(Berne, Interlaken));
        assertTrue(partition.connected(Neuchatel, Olten));
        assertTrue(partition.connected(Schwyz, Wassen));
        assertFalse(partition.connected(Delemont, Zoug));
        assertFalse(partition.connected(Olten, Lausanne));
    }

    @Test
    void enonceTest1NewOrder(){
        StationPartition.Builder builder = new StationPartition.Builder(15);
        builder.connect(Lausanne, Interlaken);
        builder.connect(Interlaken, Berne);
        builder.connect(Fribourg, Interlaken);
        builder.connect(Soleure, Neuchatel);
        builder.connect(Neuchatel, Olten);
        builder.connect(Zoug, Schwyz);
        builder.connect(Lucerne, Zoug);
        builder.connect(Wassen, Zoug);
        StationPartition partition = builder.build();
        assertTrue(partition.connected(Lausanne, Berne));
        assertTrue(partition.connected(Berne, Interlaken));
        assertTrue(partition.connected(Neuchatel, Olten));
        assertTrue(partition.connected(Schwyz, Wassen));
        assertFalse(partition.connected(Delemont, Zoug));
        assertFalse(partition.connected(Olten, Lausanne));
    }


}