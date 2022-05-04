package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StationPartitionTest2 {

    private static final Station BAD = new Station(0, "Baden");
    private static final Station BAL = new Station(1, "Bâle");
    private static final Station BEL = new Station(2, "Bellinzone");
    private static final Station BER = new Station(3, "Berne");
    private static final Station BRI = new Station(4, "Brigue");
    private static final Station BRU = new Station(5, "Brusio");
    private static final Station COI = new Station(6, "Coire");
    private static final List<Station> list = List.of(BAD, BAL, BEL, BER, BRI, BRU, COI);

    @Test
    void builderWithNegativeStationCount() {
        assertThrows(IllegalArgumentException.class, () -> {
            StationPartition.Builder builder = new StationPartition.Builder(-1);
        });
    }

    @Test
    void stationPartitionTest() {
        StationPartition.Builder stationPartitionB = new StationPartition.Builder(list.size());
        stationPartitionB.connect(BRI, BRU);
        stationPartitionB.connect(BAL, BRI);
        stationPartitionB.connect(BAD, BAL);
        stationPartitionB.connect(BAL, BEL);
        assertTrue(stationPartitionB.build().connected(BAD, BAL));
        assertTrue(stationPartitionB.build().connected(BAD, BEL));
        assertTrue(stationPartitionB.build().connected(BAD, BRU));
        assertFalse(stationPartitionB.build().connected(BAD, COI));


    }






}