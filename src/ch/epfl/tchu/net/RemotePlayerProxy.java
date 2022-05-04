package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.SoundID;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * This class represents the game's proxy, representing the distant player on the server's machine
 *
 * @author Clément Husler 328105
 * @author Mathieu Faure
 */
public final class RemotePlayerProxy implements Player {
    private static final String SPACE_CHAR = " ";
    private final BufferedWriter writer;
    private final BufferedReader reader;

    /**
     * Public constructor for the RemotePlayerProxy class, creates the BufferedReader and BufferedWriter attributes
     *
     * @param socket the Socket used by the proxy to communicate with the client
     * @throws UncheckedIOException if needed
     */
    public RemotePlayerProxy(Socket socket) {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * @param ownId       player's own id
     * @param playerNames the map associating each player's name with his id
     * @see Player#initPlayers(PlayerId, Map)
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        String names = Serdes.stringSerde.serialize(playerNames.get(PlayerId.PLAYER_1)) + "," + Serdes.stringSerde.serialize(playerNames.get(PlayerId.PLAYER_2));
        sendMessage(MessageId.INIT_PLAYERS, Serdes.playerIdSerde.serialize(ownId), names);
    }

    /**
     * @param info the info to send to the player
     * @see Player#receiveInfo(String)
     */
    @Override
    public void receiveInfo(String info) {
        String infoSer = Serdes.stringSerde.serialize(info);
        sendMessage(MessageId.RECEIVE_INFO, infoSer);
    }

    /**
     * @param newState the player's new state
     * @param ownState the player's old state
     * @see Player#updateState(PublicGameState, PlayerState)
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        String newStateSer = Serdes.publicGameStateSerde.serialize(newState);
        String ownStateSer = Serdes.playerStateSerde.serialize(ownState);
        sendMessage(MessageId.UPDATE_STATE, newStateSer, ownStateSer);
    }

    /**
     * @param tickets the tickets to choose from
     * @see Player#setInitialTicketChoice(SortedBag)
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        String ticketsSer = Serdes.ticketBagSerde.serialize(tickets);
        sendMessage(MessageId.SET_INITIAL_TICKETS, ticketsSer);
    }

    /**
     * @return the chosen tickets
     * @see Player#chooseInitialTickets()
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        sendMessage(MessageId.CHOOSE_INITIAL_TICKETS);
        return Serdes.ticketBagSerde.deserialize(receiveMessage());
    }

    /**
     * @return the chosen turn kind
     * @see Player#nextTurn()
     */
    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN);
        return Serdes.turnKindSerde.deserialize(receiveMessage());
    }

    /**
     * @param options the tickets to choose from
     * @return the chosen tickets
     * @see Player#chooseTickets(SortedBag)
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        String optionsSer = Serdes.ticketBagSerde.serialize(options);
        sendMessage(MessageId.CHOOSE_TICKETS, optionsSer);
        return Serdes.ticketBagSerde.deserialize(receiveMessage());
    }

    /**
     * @return the chosen slot to draw a card from
     * @see Player#drawSlot()
     */
    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT);
        return Serdes.intSerde.deserialize(receiveMessage());
    }

    /**
     * @return the route that the player is currently trying to claim
     * @see Player#claimedRoute()
     */
    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE);
        return Serdes.routeSerde.deserialize(receiveMessage());
    }

    /**
     * @return the initial claim cards
     * @see Player#initialClaimCards()
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        sendMessage(MessageId.CARDS);
        return Serdes.cardBagSerde.deserialize(receiveMessage());
    }

    /**
     * @param options the options the player has to choose from
     * @return the chosen additional cards
     * @see Player#chooseAdditionalCards(List)
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        String optionsSer = Serdes.cardBagListSerde.serialize(options);
        sendMessage(MessageId.CHOOSE_ADDITIONAL_CARDS, optionsSer);
        return Serdes.cardBagSerde.deserialize(receiveMessage());
    }

    /**
     * @param sound the sound to be played
     * @see Player#playSound(SoundID)
     */
    @Override
    public void playSound(SoundID sound) {
        sendMessage(MessageId.PLAY_SOUND, Serdes.soundSerde.serialize(sound));
    }

    /**
     * @param message the message to display on the end screen
     * @see Player#gameEnded(String)
     */
    @Override
    public void gameEnded(String message) {
        sendMessage(MessageId.GAME_END, Serdes.stringSerde.serialize(message));
    }

    /**
     * @param message the message to add to the chatbox
     * @see Player#receiveChat(String)
     */
    @Override
    public void receiveChat(String message) {
        sendMessage(MessageId.RECEIVE_CHAT, Serdes.stringSerde.serialize(message));
    }

    /**
     * @return the message to send
     * @see Player#sendChat()
     */
    @Override
    public String sendChat() {
        sendMessage(MessageId.SEND_CHAT);
        String message = receiveMessage();
        if (!message.isEmpty()) {
            return Serdes.stringSerde.deserialize(message);
        }
        return null;
    }


    /**
     * Allows to receive a message from the remote client
     *
     * @return the received message
     */
    private String receiveMessage() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Allows to send a message to the remote client
     *
     * @param id      the type of message
     * @param strings the strings that needs to be sent to the client
     */
    private void sendMessage(MessageId id, String... strings) {
        String messageID = id.name() + SPACE_CHAR;
        String message = String.join(SPACE_CHAR, strings);
        try {
            writer.write(messageID);
            writer.write(message);
            writer.write('\n');
            writer.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
