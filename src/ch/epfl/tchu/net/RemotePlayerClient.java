package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicGameState;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public final class RemotePlayerClient {
    private final Player player;
    private final String host;
    private final int port;
    private static final String WRONG_SIZE = "WRONG SIZE";

    /**
     * public constructor of the RemotePlayerClient class
     *
     * @param player   the distant player the method has to give an access to the game
     * @param hostName the name used to connect to the proxy
     * @param port     the port used to connect to the proxy
     */
    public RemotePlayerClient(Player player, String hostName, int port) {
        this.player = player;
        this.host = hostName;
        this.port = port;
    }

    /**
     * This method waits until the proxy sends a message, splits it using space as a separator,
     * determines the type of the message following the first String resulting from the split,
     * deserializes the arguments and calls the player's corresponding method and if there is a return statement,
     * serializes it to send it back to the proxy
     */
    public void run() {
        try (Socket s = new Socket(host, port);
             BufferedReader r =
                     new BufferedReader(
                             new InputStreamReader(s.getInputStream(),
                                     StandardCharsets.US_ASCII));
             BufferedWriter w =
                     new BufferedWriter(
                             new OutputStreamWriter(s.getOutputStream(),
                                     StandardCharsets.US_ASCII))) {
            String message;
            while ((message = r.readLine()) != null) {
                String[] parts = message.split(Pattern.quote(" "));
                Preconditions.checkArgument(parts.length >= 1, WRONG_SIZE);
                MessageId id = MessageId.valueOf(parts[0]);
                String response = play(id, parts);
                if (response != null) {
                    w.write(response);
                    w.write('\n');
                    w.flush();
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Allows to call the right method of the player with the serialized message
     *
     * @param id    the message ID
     * @param parts the parts of the message
     * @return the answer to the message or null if no answer is needed
     */
    private String play(MessageId id, String[] parts) {
        switch (id) {
            case INIT_PLAYERS:
                return initPlayer(parts);
            case RECEIVE_INFO:
                return receiveInfo(parts);
            case UPDATE_STATE:
                return updateState(parts);
            case SET_INITIAL_TICKETS:
                return setInitialTickets(parts);
            case CHOOSE_TICKETS:
                return chooseTickets(parts);
            case CHOOSE_ADDITIONAL_CARDS:
                return chooseAdditionalCards(parts);
            case CHOOSE_INITIAL_TICKETS:
                return Serdes.ticketBagSerde.serialize(player.chooseInitialTickets());
            case NEXT_TURN:
                return Serdes.turnKindSerde.serialize(player.nextTurn());
            case DRAW_SLOT:
                return Serdes.intSerde.serialize(player.drawSlot());
            case ROUTE:
                return Serdes.routeSerde.serialize(player.claimedRoute());
            case CARDS:
                return Serdes.cardBagSerde.serialize(player.initialClaimCards());
            case PLAY_SOUND:
                return playSound(parts);
            case GAME_END:
                return gameEnd(parts);
            case RECEIVE_CHAT:
                player.receiveChat(Serdes.stringSerde.deserialize(parts[1]));
                return null;
            case SEND_CHAT:
                return sendChat();
        }
        return null;
    }

    /**
     * Initializes the player by deserializing it's own ID, and the player names
     *
     * @param parts the serialized data
     * @return null, no response is needed
     * @see Player#initPlayers(PlayerId, Map)
     */
    private String initPlayer(String[] parts) {
        Preconditions.checkArgument(parts.length == 3, WRONG_SIZE);
        PlayerId ownId = Serdes.playerIdSerde.deserialize(parts[1]);
        String[] names = parts[2].split(Pattern.quote(","));
        Map<PlayerId, String> playerNames = Map.of(PlayerId.PLAYER_1, Serdes.stringSerde.deserialize(names[0]),
                PlayerId.PLAYER_2, Serdes.stringSerde.deserialize(names[1]));
        player.initPlayers(ownId, playerNames);
        return null;
    }

    private String sendChat() {
        String message = player.sendChat();
        if (message == null) {
            return Serdes.stringSerde.serialize("NO_REACTION");
        } else {
            return Serdes.stringSerde.serialize(message);
        }
    }

    /**
     * Receives info
     *
     * @param parts the serialized message
     * @return null, no response needed
     * @see Player#receiveInfo(String)
     */
    private String receiveInfo(String[] parts) {
        Preconditions.checkArgument(parts.length == 2, WRONG_SIZE);
        player.receiveInfo(Serdes.stringSerde.deserialize(parts[1]));
        return null;
    }

    /**
     * @param parts the serialized gameState and PlayerState
     * @return null, no response is needed
     * @see Player#updateState(PublicGameState, PlayerState)
     */
    private String updateState(String[] parts) {
        Preconditions.checkArgument(parts.length == 3, WRONG_SIZE);
        player.updateState(Serdes.publicGameStateSerde.deserialize(parts[1]), Serdes.playerStateSerde.deserialize(parts[2]));
        return null;
    }

    /**
     * @param parts the serialized ticket bag
     * @return null, no response is needed
     * @see Player#setInitialTicketChoice(SortedBag)
     */
    private String setInitialTickets(String[] parts) {
        Preconditions.checkArgument(parts.length == 2, WRONG_SIZE);
        player.setInitialTicketChoice(Serdes.ticketBagSerde.deserialize(parts[1]));
        return null;
    }

    /**
     * @param parts the serialized possible tickets
     * @return the chosen tickets, serialized
     * @see Player#chooseTickets(SortedBag)
     */
    private String chooseTickets(String[] parts) {
        Preconditions.checkArgument(parts.length == 2, WRONG_SIZE);
        return Serdes.ticketBagSerde.serialize(player.chooseTickets(Serdes.ticketBagSerde.deserialize(parts[1])));
    }

    /**
     * @param parts the card bag list to choose from, serialized
     * @return the chosen bag, serialized
     * @see Player#chooseAdditionalCards(List)
     */
    private String chooseAdditionalCards(String[] parts) {
        Preconditions.checkArgument(parts.length == 2, WRONG_SIZE);
        return Serdes.cardBagSerde.serialize(player.chooseAdditionalCards(Serdes.cardBagListSerde.deserialize(parts[1])));
    }

    private String playSound(String[] parts) {
        Preconditions.checkArgument(parts.length == 2, WRONG_SIZE);
        player.playSound(Serdes.soundSerde.deserialize(parts[1]));
        return null;
    }

    private String gameEnd(String[] parts) {
        Preconditions.checkArgument(parts.length == 2, WRONG_SIZE);
        player.gameEnded(Serdes.stringSerde.deserialize(parts[1]));
        return null;
    }

}