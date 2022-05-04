package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.scene.media.AudioClip;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.application.Platform.runLater;

/**
 * The type Graphical player adapter.
 */
public final class GraphicalPlayerAdapter implements Player {

    private GraphicalPlayer graphicalPlayer;
    private String name;
    private final ArrayBlockingQueue<SortedBag<Ticket>> ticketsQueue = new ArrayBlockingQueue<>(1);
    private final ArrayBlockingQueue<Integer> drawCardQueue = new ArrayBlockingQueue<>(1);
    private final ArrayBlockingQueue<Route> routeQueue = new ArrayBlockingQueue<>(1);
    private final ArrayBlockingQueue<SortedBag<Card>> cardsQueue = new ArrayBlockingQueue<>(1);
    private final ArrayBlockingQueue<TurnKind> turnKindQueue = new ArrayBlockingQueue<>(1);

    /**
     * builds on the JavaFx thread the GraphicalPlayer and stocks it in the graphicalPlayer attribute
     *
     * @param ownId       the player's ID
     * @param playerNames the map associating each player's ID with his name
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        name = playerNames.get(ownId);
        runLater(() -> graphicalPlayer = new GraphicalPlayer(ownId, playerNames));
    }

    /**
     * calls the method receiveInfo from the GraphicalPlayer
     *
     * @param info the info to send to the
     * @see GraphicalPlayer#receiveInfo(String)
     */
    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    /**
     * Calls the method updateState from the GraphicalPlayer
     *
     * @param newState the player's new state
     * @param ownState the player's old state
     * @see GraphicalPlayer#setState(PublicGameState, PlayerState)
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() -> graphicalPlayer.setState(newState, ownState));
    }

    /**
     * Creates a chooseTicketsHandler that will add the chosen tickets to the ticketsQueue
     * Calls the chooseTicket method of the GraphicalPlayer
     *
     * @param tickets the tickets to choose from
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        setTicketChoice(tickets);
    }

    /**
     * Retrieves the tickets that the player choose
     *
     * @return the tickets that are stocked in the ticketsQueue in a SortedBag
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return getTicketChoice();
    }

    /**
     * Creates three action handlers and sends them to the graphical player by calling startTurn
     * All handlers, when used, store the choice of the player in the corresponding queue and the corresponding
     * turnKind in the turnKind queue
     *
     * @return the turn kind the player chose
     * @see GraphicalPlayer#startTurn(ActionHandlers.DrawCardHandler, ActionHandlers.DrawTicketsHandler, ActionHandlers.ClaimRouteHandler)
     */
    @Override
    public TurnKind nextTurn() {
        ActionHandlers.DrawCardHandler drawCardHandler = slot -> {
            drawCardQueue.add(slot);
            turnKindQueue.add(TurnKind.DRAW_CARDS);
        };
        ActionHandlers.ClaimRouteHandler claimRouteHandler = (route, initialClaimCards) -> {
            routeQueue.add(route);
            cardsQueue.add(initialClaimCards);
            turnKindQueue.add(TurnKind.CLAIM_ROUTE);
        };
        ActionHandlers.DrawTicketsHandler drawTicketsHandler = () -> turnKindQueue.add(TurnKind.DRAW_TICKETS);
        runLater(() -> graphicalPlayer.startTurn(drawCardHandler, drawTicketsHandler, claimRouteHandler));
        return takeQueue(turnKindQueue);
    }

    /**
     * Calls the setInitialTicketChoice and the chooseInitialTickets methods to create the action of choosing the tickets
     *
     * @param options the options the player has to choose from
     * @return the tickets the player chose
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        setTicketChoice(options);
        return getTicketChoice();
    }

    /**
     * Checks if the queue containing the card slots contains a value, if yes it means this method
     * for the first time at this turn, and it just needs to return what this queue contains, else it means
     * it is the second time, the player draws his second card and it needs to call the drawCard method of
     * graphicalPlayer on the thread and block until the slot of the second card chosen can be returned
     *
     * @return the slot of the card to draw
     * @see GraphicalPlayer#drawCard(ActionHandlers.DrawCardHandler)
     */
    @Override
    public int drawSlot() {
        if (drawCardQueue.isEmpty()) {
            ActionHandlers.DrawCardHandler cardHandler = drawCardQueue::add;
            runLater(() -> graphicalPlayer.drawCard(cardHandler));
        }
        return takeQueue(drawCardQueue);
    }

    /**
     * @return the claimed Route, the first element of the Queue containing the Routes
     */
    @Override
    public Route claimedRoute() {
        return takeQueue(routeQueue);
    }

    /**
     * @return the cards initially used to claim a Route, by extracting them of their Queue
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        return takeQueue(cardsQueue);
    }

    /**
     * Calls graphicalPlayer's chooseAdditionalCards method in the thread and waits until an element is placed
     * in the Queue containing the Cards SortedBags, and returns it
     *
     * @param options the options the player has to choose from
     * @return the additional cards used to claim a route stocked in a SortedBag extracted from the Queue
     * @see GraphicalPlayer#chooseAdditionalCards(List, ActionHandlers.ChooseCardsHandler)
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        ActionHandlers.ChooseCardsHandler cardsHandler = cardsQueue::add;
        runLater(() -> graphicalPlayer.chooseAdditionalCards(options, cardsHandler));
        return takeQueue(cardsQueue);
    }

    @Override
    public void playSound(SoundID sound) {
        new AudioClip(getClass().getResource(sound.filename()).toExternalForm()).play();
    }

    @Override
    public void gameEnded(String message) {
        runLater(() -> graphicalPlayer.gameEnded(message));
    }

    @Override
    public void receiveChat(String message) {
        runLater(() -> graphicalPlayer.receiveMessage(message));
    }

    @Override
    public String sendChat() {
        AtomicReference<String> m = new AtomicReference<>();
        runLater(() -> m.set(graphicalPlayer.sendMessage()));
        while (m.get() == null) {
            //waiting for message to come in
        }
        return m.get();
    }

    /**
     * method used to extract the elements of a queue
     *
     * @param queue the queue to extract the elements from
     * @param <T>   the generic type
     * @return the extracted elements
     */
    private <T> T takeQueue(ArrayBlockingQueue<T> queue) {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    /**
     * Allows to reuse the code of the ticket choice setter
     * without calling setInitialTicketChoice() in the chooseTicket() method
     *
     * @param options the tickets to choose from
     */
    private void setTicketChoice(SortedBag<Ticket> options) {
        ActionHandlers.ChooseTicketsHandler ticketsHandler = ticketsQueue::add;
        runLater(() -> graphicalPlayer.chooseTickets(options, ticketsHandler));
    }

    /**
     * Allows to reuse the code of the ticket choice setter
     * without calling chooseInitialTickets() in the chooseTicket() method
     *
     * @return the sorted bag containing the chosen tickets
     */
    private SortedBag<Ticket> getTicketChoice() {
        return takeQueue(ticketsQueue);
    }
}
