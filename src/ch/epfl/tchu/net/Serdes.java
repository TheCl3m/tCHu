package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.SoundID;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;


/**
 * the Serdes class contains all the Serdes used in the project
 */

public final class Serdes {

    /**
     * Serde used for Integers
     */
    public static final Serde<Integer> intSerde = Serde.of(i -> Integer.toString(i), Integer::parseInt);

    /**
     * Serde used for Strings
     */
    public static final Serde<String> stringSerde = Serde.of((String s) -> Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)), Serdes::getStringFromB64);

    private static String getStringFromB64(String b64string) {
        return new String(Base64.getDecoder().decode(b64string), StandardCharsets.UTF_8);
    }

    /**
     * Serde used for PlayerIds
     */
    public static final Serde<PlayerId> playerIdSerde = Serde.oneOf(PlayerId.ALL);

    /**
     * Serde used for turnKinds
     */
    public static final Serde<Player.TurnKind> turnKindSerde = Serde.oneOf(Player.TurnKind.ALL);

    /**
     * Serde used for Cards
     */
    public static final Serde<Card> cardSerde = Serde.oneOf(Card.ALL);

    /**
     * Serde used for Sounds
     */
    public static final Serde<SoundID> soundSerde = Serde.oneOf(SoundID.ALL);

    /**
     * Serde used for Routes
     */
    public static final Serde<Route> routeSerde = Serde.oneOf(ChMap.routes());

    /**
     * Serde used for Tickets
     */
    public static final Serde<Ticket> ticketSerde = Serde.oneOf(ChMap.tickets());

    /**
     * Serde used for Lists of Strings
     */
    public static final Serde<List<String>> stringListSerde = Serde.listOf(stringSerde, ",");

    /**
     * Serde used for Lists of Cards
     */
    public static final Serde<List<Card>> cardListSerde = Serde.listOf(cardSerde, ",");

    /**
     * Serde used for Lists of Routes
     */
    public static final Serde<List<Route>> routeListSerde = Serde.listOf(routeSerde, ",");

    /**
     * Serde used for SortedBag of Cards
     */
    public static final Serde<SortedBag<Card>> cardBagSerde = Serde.bagOf(cardSerde, ",");

    /**
     * Serde used for SortedBag of Tickets
     */
    public static final Serde<SortedBag<Ticket>> ticketBagSerde = Serde.bagOf(ticketSerde, ",");

    /**
     * Serde used for Lists of SortedBags of Cards
     */
    public static final Serde<List<SortedBag<Card>>> cardBagListSerde = Serde.listOf(Serde.bagOf(cardSerde, ","), ";");

    /**
     * Serde used for PublicCardStates
     */
    public static final Serde<PublicCardState> publicCardStateSerde = new Serde<>() {
        @Override
        public String serialize(PublicCardState state) {
            List<String> strings = new ArrayList<>(3);
            strings.add(cardListSerde.serialize(state.faceUpCards()));
            strings.add(intSerde.serialize(state.deckSize()));
            strings.add(intSerde.serialize(state.discardsSize()));
            return String.join(";", strings);
        }

        @Override
        public PublicCardState deserialize(String string) {
            String[] strings = splitString(string, ";");
            Preconditions.checkArgument(strings.length == 3);
            return new PublicCardState(cardListSerde.deserialize(strings[0]), intSerde.deserialize(strings[1]), intSerde.deserialize(strings[2]));
        }
    };

    /**
     * Serde used for PublicPlayerStates
     */
    public static final Serde<PublicPlayerState> publicPlayerStateSerde = new Serde<>() { // separator point-virgule
        @Override
        public String serialize(PublicPlayerState pps) {
            List<String> strings = new ArrayList<>(3);
            strings.add(intSerde.serialize(pps.ticketCount()));
            strings.add(intSerde.serialize(pps.cardCount()));
            strings.add(routeListSerde.serialize(pps.routes()));
            return String.join(";", strings);
        }

        @Override
        public PublicPlayerState deserialize(String string) {
            String[] strings = splitString(string, ";");
            Preconditions.checkArgument(strings.length == 3);
            return new PublicPlayerState(intSerde.deserialize(strings[0]), intSerde.deserialize(strings[1]), routeListSerde.deserialize(strings[2]));
        }
    };

    /**
     * Serde used for PlayerStates
     */
    public static final Serde<PlayerState> playerStateSerde = new Serde<>() { // separator point-virgule
        @Override
        public String serialize(PlayerState ps) {
            List<String> strings = new ArrayList<>(3);
            strings.add(ticketBagSerde.serialize(ps.tickets()));
            strings.add(cardBagSerde.serialize(ps.cards()));
            strings.add(routeListSerde.serialize(ps.routes()));
            return String.join(";", strings);
        }

        @Override
        public PlayerState deserialize(String string) {
            String[] strings = splitString(string, ";");
            Preconditions.checkArgument(strings.length == 3);
            return new PlayerState(ticketBagSerde.deserialize(strings[0]), cardBagSerde.deserialize(strings[1]), routeListSerde.deserialize(strings[2]));
        }
    };

    /**
     * Serde used for PublicGameStates
     */
    public static final Serde<PublicGameState> publicGameStateSerde = new Serde<>() { // separator deux-points
        @Override
        public String serialize(PublicGameState pgs) {
            List<String> strings = new ArrayList<>(6);
            strings.add(intSerde.serialize(pgs.ticketsCount()));
            strings.add(publicCardStateSerde.serialize(pgs.cardState()));
            strings.add(playerIdSerde.serialize(pgs.currentPlayerId()));
            strings.add(publicPlayerStateSerde.serialize(pgs.playerState(PLAYER_1)));
            strings.add(publicPlayerStateSerde.serialize(pgs.playerState(PLAYER_2)));
            if (!(pgs.lastPlayer() == null)) {
                strings.add(playerIdSerde.serialize(pgs.lastPlayer()));
            } else {
                strings.add("");
            }
            return String.join(":", strings);
        }

        @Override
        public PublicGameState deserialize(String string) {
            String[] s = splitString(string, ":");
            Preconditions.checkArgument(s.length == 6);
            PlayerId lastPId = null;
            if (!s[5].equals("")) {
                lastPId = playerIdSerde.deserialize(s[5]);
            }
            Map<PlayerId, PublicPlayerState> map = Map.of(PLAYER_1, publicPlayerStateSerde.deserialize(s[3]),
                    PLAYER_2, publicPlayerStateSerde.deserialize(s[4]));
            return new PublicGameState(intSerde.deserialize(s[0]), publicCardStateSerde.deserialize(s[1]),
                    playerIdSerde.deserialize(s[2]), map, lastPId);
        }
    };

    /**
     * @param string    the String to split
     * @param separator the separator used to split it
     * @return the split String
     */
    private static String[] splitString(String string, String separator) {
        return string.split(Pattern.quote(separator), -1);
    }

}
