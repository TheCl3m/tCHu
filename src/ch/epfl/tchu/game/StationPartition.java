package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

/**
 * The station partition class allows to have a representation
 * of a network of stations. It allows to know which stations are connected with each other.
 *
 * @author Clement HUSLER (328105)
 * @author Mathieu Faure (328086)
 */

public final class StationPartition implements StationConnectivity {

    private final int[] stationsLinks;

    /**
     * private constructor that is called after using a StationPartition.Builder
     *
     * @param stationsLinksFromBuilder the array given by the builder
     */
    private StationPartition(int[] stationsLinksFromBuilder) {
        stationsLinks = new int[stationsLinksFromBuilder.length];
        System.arraycopy(stationsLinksFromBuilder, 0, stationsLinks, 0, stationsLinks.length);
    }


    /**
     * Method of the implemented interface
     * Allows to know if two stations of a network are connected to each other
     *
     * @param s1 the first station
     * @param s2 the second station
     * @return true if the stations are in the network and connected
     * if one or two of the stations are not in the network, returns true if station1 == station2
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        int s1Id = s1.id();
        int s2Id = s2.id();
        if (s1Id < 0 || s1Id >= stationsLinks.length || s2Id < 0 || s2Id >= stationsLinks.length) {
            return s1Id == s2Id;
        }
        return stationsLinks[s1Id] == stationsLinks[s2Id];
    }

    /**
     * The station partition builder nested class allows to build a representation
     * of a network of stations.
     *
     * @author Clement HUSLER (328105)
     * @author Mathieu Faure (328086)
     */

    public static final class Builder {

        private final int[] stationsLinks;

        /**
         * Constructor of the builder
         * initializes every station as it's own representative
         *
         * @param stationCount the highest station id of the network
         */
        public Builder(int stationCount) {
            Preconditions.checkArgument(stationCount >= 0);
            stationsLinks = new int[stationCount];
            for (int i = 0; i < stationCount; ++i) {
                stationsLinks[i] = i;
            }
        }

        /**
         * Connects two stations together
         *
         * @param s1 the first station
         * @param s2 the second station
         * @return the same builder, with the modified array
         */
        public Builder connect(Station s1, Station s2) {
            stationsLinks[representative(s1.id())] = representative(s2.id());
            return this;
        }

        /**
         * Builds the station partition by connecting every station to it's representative
         *
         * @return the build StationPartition
         */
        public StationPartition build() {
            for (int i = 0; i < stationsLinks.length; ++i) {
                stationsLinks[i] = representative(i);
            }
            return new StationPartition(stationsLinks);
        }

        /**
         * Travels to the array to find the representative of a station
         *
         * @param stationId the stationId for which the representative is searched
         * @return the representative of the given station
         */
        private int representative(int stationId) {
            int representative = stationsLinks[stationId];
            int index = stationId;
            while (index != representative) {
                index = stationsLinks[index];
                representative = stationsLinks[index];
            }
            return representative;
        }

    }
}
