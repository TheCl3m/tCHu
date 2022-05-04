package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import static javafx.application.Platform.isFxApplicationThread;

/**
 * The GraphicalPlayer class represents the graphical interface for a player of tCHu
 *
 * @author Mathieu FAURE (328086)
 * @author Clement HUSLER (328105)
 */
public final class GraphicalPlayer {


    private final ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandlerProperty = new SimpleObjectProperty<>(null);
    private final ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandlerProperty = new SimpleObjectProperty<>(null);
    private final ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandlerProperty = new SimpleObjectProperty<>(null);
    private final ObjectProperty<ActionHandlers.chatHandler> chatHandlerProperty = new SimpleObjectProperty<>(null);
    private final ObservableList<Text> infos = FXCollections.observableArrayList();
    private final ObservableList<Text> chat = FXCollections.observableArrayList();
    private final ArrayBlockingQueue<String> reaction = new ArrayBlockingQueue<>(1);
    private final ObservableGameState gameState;
    private final Stage principalStage;
    private static final int MAX_INFO = 5;


    /**
     * public constructor for the GraphicalPlayer class
     * creates the graphical interface
     *
     * @param playerId    the player's ID
     * @param playerNames the map associating the player's ID with his name
     */
    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames) {
        assert isFxApplicationThread();
        gameState = new ObservableGameState(playerId);
        Stage stage = new Stage();
        Node mapView = MapViewCreator.createMapView(gameState, claimRouteHandlerProperty, this::chooseClaimCards);
        Node cardsView = DecksViewCreator.createCardsView(gameState, drawTicketsHandlerProperty, drawCardHandlerProperty);
        Node handView = DecksViewCreator.createHandView(gameState);
        Node infoView = InfoViewCreator.createInfoView(playerId, playerNames, gameState, infos, chat, chatHandlerProperty);
        BorderPane mainPane = new BorderPane(mapView, null, cardsView, handView, infoView);
        stage.setScene(new Scene(mainPane));
        stage.setTitle("tCHu \u2014 " + playerNames.get(playerId));
        this.principalStage = stage;
        stage.setResizable(false);

        stage.show();
    }

    /**
     * calls the setState method of the ObservableGameState attribute with its own parameters as parameters
     *
     * @param newGameState   the new PublicGameState
     * @param newPlayerState the new PlayerState
     */
    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        assert isFxApplicationThread();
        gameState.setState(newGameState, newPlayerState);
    }

    /**
     * takes a message as a String in parameter and adds it at the bottom of the information section,
     * which only contains the last five messages
     *
     * @param message the message to add at the bottom of the information section
     */
    public void receiveInfo(String message) {
        assert isFxApplicationThread();
        Text m = new Text(message);
        if (infos.size() == MAX_INFO) {
            infos.remove(0);
        }
        infos.add(m);
    }

    public void receiveMessage(String message) {
        assert isFxApplicationThread();
        Text m = new Text(message);
        if (chat.size() == MAX_INFO) {
            chat.remove(0);
        }
        chat.add(m);
    }

    public String sendMessage() {
        assert isFxApplicationThread();
        if (!reaction.isEmpty()) {
            return reaction.remove();
        }
        return "NO_REACTION";
    }

    /**
     * allows the player to execute one of the actions he is able to execute : draw a card, draw tickets, claim a route
     *
     * @param drawCardHandler    the ActionsHandler used to draw a card
     * @param drawTicketsHandler the ActionsHandler used to draw tickets
     * @param claimRouteHandler  the ActionsHandler used to claim a route
     */
    public void startTurn(ActionHandlers.DrawCardHandler drawCardHandler, ActionHandlers.DrawTicketsHandler drawTicketsHandler, ActionHandlers.ClaimRouteHandler claimRouteHandler) {
        assert isFxApplicationThread();
        ActionHandlers.DrawTicketsHandler ticketH = () -> {
            drawTicketsHandler.onDrawTickets();
            emptyAllProperties();
        };
        drawTicketsHandlerProperty.set(gameState.canDrawTickets() ? ticketH : null);

        ActionHandlers.DrawCardHandler cardH = (slot) -> {
            drawCardHandler.onDrawCard(slot);
            emptyAllProperties();
        };
        drawCardHandlerProperty.set(gameState.canDrawCards() ? cardH : null);

        ActionHandlers.ClaimRouteHandler routeH = (route, cards) -> {
            claimRouteHandler.onClaimRoute(route, cards);
            emptyAllProperties();
        };
        claimRouteHandlerProperty.set(routeH);
        ActionHandlers.chatHandler chatH = (message) -> {
            reaction.offer(message);
            chatHandlerProperty.set(null);
        };
        chatHandlerProperty.set(chatH);
    }

    /**
     * empties all the properties used to contain the handlers given in the startTurn method
     */
    private void emptyAllProperties() {
        drawTicketsHandlerProperty.set(null);
        drawCardHandlerProperty.set(null);
        claimRouteHandlerProperty.set(null);
        chatHandlerProperty.set(null);
    }

    /**
     * opens a window allowing the player to choose the tickets he wants in a bag of either three, during the game, either five tickets,
     * at the beginning of the game. In these situations he can choose respectively one or three tickets to keep
     *
     * @param ticketsBag           the SortedBag containing the tickets the player can choose from
     * @param chooseTicketsHandler the ActionHandler used to choose the tickets
     * @throws IllegalArgumentException if the given bag has a size different from 3 or 5
     */
    public void chooseTickets(SortedBag<Ticket> ticketsBag, ActionHandlers.ChooseTicketsHandler chooseTicketsHandler) {
        assert isFxApplicationThread();
        Preconditions.checkArgument(ticketsBag.size() == Constants.INITIAL_TICKETS_COUNT || ticketsBag.size() == Constants.IN_GAME_TICKETS_COUNT);
        int numberOfTicketsToChoose = ticketsBag.size() - Constants.DISCARDABLE_TICKETS_COUNT;
        Stage stage = createModalStage(StringsFr.TICKETS_CHOICE);
        ListView<Ticket> ticketView = createTicketListView(ticketsBag);
        ticketView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TextFlow flow = createTextFlow(String.format(StringsFr.CHOOSE_TICKETS, numberOfTicketsToChoose, StringsFr.plural(numberOfTicketsToChoose)));
        Button button = createButton(Bindings.size(ticketView.getSelectionModel().getSelectedItems()), numberOfTicketsToChoose);
        button.setOnAction(e -> {
            closeModal(button);
            ObservableList<Ticket> selected = ticketView.getSelectionModel().getSelectedItems();
            SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
            for (Ticket t : selected) {
                builder.add(t);
            }
            chooseTicketsHandler.onChooseTickets(builder.build());
        });
        VBox vBox = new VBox(flow, ticketView, button);
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * allows the player to choose one of the five face up cards
     *
     * @param drawCardHandler the handler allowing to draw a card
     */
    public void drawCard(ActionHandlers.DrawCardHandler drawCardHandler) {
        assert isFxApplicationThread();
        ActionHandlers.DrawCardHandler cardH = (slot) -> {
            drawCardHandler.onDrawCard(slot);
            emptyAllProperties();
        };
        drawCardHandlerProperty.set(cardH);
    }

    /**
     * opens a window allowing the player to choose between the different bags of cards he can use to claim a route
     *
     * @param sortedBagList      a list containing all the SortedBag representing the different card sets
     *                           the player can use to claim a route
     * @param chooseCardsHandler the ActionHandler allowing to choose these cards
     */
    public void chooseClaimCards(List<SortedBag<Card>> sortedBagList, ActionHandlers.ChooseCardsHandler chooseCardsHandler) {
        assert isFxApplicationThread();
        Stage stage = createModalStage(StringsFr.CARDS_CHOICE);
        ListView<SortedBag<Card>> cardBagsView = createListView(sortedBagList);
        TextFlow flow = createTextFlow(StringsFr.CHOOSE_CARDS);
        Button button = createButton(Bindings.size(cardBagsView.getSelectionModel().getSelectedItems()), 1);
        createCardsModal(chooseCardsHandler, stage, cardBagsView, flow, button);
    }

    /**
     * opens a window allowing the player to choose between the possible bags of cards
     * that can be used as additional cards to claim an underground route
     *
     * @param sortedBagList      a list of SortedBags representing each of the card sets the player can choose
     *                           to use as additional cards in the case the tunnel claiming required additional cards
     * @param chooseCardsHandler the ActionHandler allowing to choose these cards
     */
    public void chooseAdditionalCards(List<SortedBag<Card>> sortedBagList, ActionHandlers.ChooseCardsHandler chooseCardsHandler) {
        assert isFxApplicationThread();
        Stage stage = createModalStage(StringsFr.CARDS_CHOICE);
        ListView<SortedBag<Card>> cardBagsView = createListView(sortedBagList);
        TextFlow flow = createTextFlow(StringsFr.CHOOSE_ADDITIONAL_CARDS);
        Button button = createButton();
        createCardsModal(chooseCardsHandler, stage, cardBagsView, flow, button);
    }

    /**
     * Puts together all the parts of the modal that deals with cards
     *
     * @param chooseCardsHandler the handler
     * @param stage              the stage which will allow the modal to be displayed
     * @param cardBagsView       the view allowing to see the possible cards
     * @param flow               the text informing the player what to do
     * @param button             allowing the player to validate their choice
     */
    private void createCardsModal(ActionHandlers.ChooseCardsHandler chooseCardsHandler, Stage stage, ListView<SortedBag<Card>> cardBagsView, TextFlow flow, Button button) {
        button.setOnAction(e -> {
            closeModal(button);
            ObservableList<SortedBag<Card>> selected = cardBagsView.getSelectionModel().getSelectedItems();
            if (selected.size() == 0) {
                chooseCardsHandler.onChooseCards(SortedBag.of());
            } else {
                chooseCardsHandler.onChooseCards(selected.get(0));
            }
        });
        VBox vBox = new VBox(flow, cardBagsView, button);
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");
        stage.setScene(scene);
        stage.show();
    }

    public void gameEnded(String message) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXMLS/EndMenu.fxml"));
            AnchorPane pane = loader.load();
            EndScreenController controller = (EndScreenController) loader.getController();
            controller.setText(message);
            Scene scene = new Scene(pane);
            Stage newWindow = new Stage();
            newWindow.setResizable(false);
            newWindow.setTitle("Fin de partie");
            newWindow.setScene(scene);
            newWindow.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error in end screen");
        }
    }


    /**
     * Allows to close the modal where the given button is present
     *
     * @param button contained in the modal to be closed
     */
    private void closeModal(Button button) {
        button.getScene().getWindow().hide();
    }

    /**
     * creates a stage for a modal box
     *
     * @param title the title of the window
     * @return the configured stage
     */
    private Stage createModalStage(String title) {
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(this.principalStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setOnCloseRequest(Event::consume);
        return stage;
    }

    /**
     * creates a TextFlow with the given text String
     *
     * @param text the given text as a String
     * @return the created TextFlow
     */
    private TextFlow createTextFlow(String text) {
        TextFlow flow = new TextFlow();
        flow.getChildren().add(new Text(text));
        return flow;
    }

    /**
     * @return the button created with the inscription "Choisir"
     */
    private Button createButton() {
        return new Button(StringsFr.CHOOSE);
    }

    /**
     * creates a button that is disabled when the players chooses too few of the given possibilities
     *
     * @param selectionSize the amount of possibilities the player selected
     * @param minChoices    the minimum amount of possibilities the player can choose
     * @return the created button
     */
    private Button createButton(IntegerBinding selectionSize, int minChoices) {
        Button button = new Button(StringsFr.CHOOSE);
        button.disableProperty().bind(Bindings.greaterThanOrEqual(selectionSize, minChoices).not());
        return button;
    }

    /**
     * creates a ListView with the given List of cards bags, for the methods creating a window used to choose
     * between cards bags
     *
     * @param bags the given cards bags
     * @return the created ListView
     */
    private ListView<SortedBag<Card>> createListView(List<SortedBag<Card>> bags) {
        ListView<SortedBag<Card>> view = new ListView<>(FXCollections.observableList(bags));
        view.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
        return view;
    }

    /**
     * creates a ListView with the given bag of tickets, for the method creating a window used to choose
     * between tickets
     *
     * @param tickets the given bag of tickets
     * @return the created ListView
     */
    private ListView<Ticket> createTicketListView(SortedBag<Ticket> tickets) {
        ListView<Ticket> view = new ListView<>(FXCollections.observableList(tickets.toList()));
        view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        return view;
    }

}
