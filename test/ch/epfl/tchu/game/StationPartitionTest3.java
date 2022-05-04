package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StationPartitionTest3 {

    private static final Station PARIS = new Station(0, "Paris");
    private static final Station LYON = new Station(1, "Lyon");
    private static final Station BORDEAUX = new Station(2, "Bordeaux");
    private static final Station MARSEILLE = new Station(3, "Marseille");
    private static final Station TOULOUSE = new Station(4, "Toulouse");
    private static final Station NICE = new Station(5, "Nice");
    private static final Station BREST = new Station(6, "Brest");
    private static final Station NANTES = new Station(7, "Nantes");

    private static final List<Station> France = List.of(
            PARIS, LYON, BORDEAUX, MARSEILLE, TOULOUSE, NICE, BREST, NANTES);

    @Test
    public void stationPartition() {
        StationPartition.Builder b = new StationPartition.Builder(France.size());
        b.connect(PARIS, NICE);
        b.connect(PARIS, BREST);
        b.connect(BORDEAUX, NANTES);
        b.connect(NANTES, NICE);

        b.connect(MARSEILLE, LYON);

        StationPartition partition = b.build();

        var net1 = List.of(PARIS, NICE, BORDEAUX, BREST, NANTES);
        var net2 = List.of(LYON, MARSEILLE);
        var net3 = List.of(TOULOUSE);

        var nets = List.of(net1, net2, net3);
        for (int i = 0; i < nets.size(); ++i) {
            for (int j = 0; j < nets.size(); ++j) {
                if (i == j) {
                    for (Station s1 : nets.get(i)) {
                        for (Station s2 : nets.get(j)) {
                            assertTrue(partition.connected(s1, s2));
                        }
                    }

                } else {
                    for (Station s1 : nets.get(i)) {
                        for (Station s2 : nets.get(j)) {
                            assertFalse(partition.connected(s1, s2));

                        }
                    }
                }
            }

        }
    }
}
