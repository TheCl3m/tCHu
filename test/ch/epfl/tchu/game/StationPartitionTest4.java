package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StationPartitionTest4 {

    private static final Station BAD = new Station(0, "Baden");
    private static final Station BAL = new Station(1, "Bâle");
    private static final Station BEL = new Station(2, "Bellinzone");
    private static final Station BER = new Station(3, "Berne");
    private static final Station BRI = new Station(4, "Brigue");
    private static final Station FR4 = new Station(50, "France");

    @Test
    void constructorThrowsOnNegativeLengthForArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            StationPartition.Builder stationArrayBuilder = new StationPartition.Builder(-1);
        });
    }

    @Test
    void PartitionBuilderOnTrivialArrayThatHasNothingConnected() {
        StationPartition.Builder stationArrayBuilder = new StationPartition.Builder(5);
        StationPartition dawg = stationArrayBuilder.build();
        assertFalse(dawg.connected(BAD, BAL));
    }

    @Test
    void PartitionBuilderOnTrivialArray() {
        StationPartition.Builder stationArrayBuilder = new StationPartition.Builder(5);
        StationPartition dawg = stationArrayBuilder.connect(BEL , BER).connect(BRI, BAD).connect(BEL, BAD).build();
        assertTrue(dawg.connected(BEL,BER));
        assertTrue(dawg.connected(BER,BER));
        assertTrue(dawg.connected(BER,BEL));
        assertFalse(dawg.connected(BEL,BAL));

    }

    @Test
    void connectedWorksOnOutOfBoundIdStation() {
        StationPartition.Builder stationArrayBuilder = new StationPartition.Builder(5);
        StationPartition dawg = stationArrayBuilder.connect(BEL , BER).connect(BRI, BAD).connect(BEL, BAD).build();
        assertTrue(dawg.connected(FR4 , FR4));
        assertFalse(dawg.connected(BAD, FR4));
    }
}