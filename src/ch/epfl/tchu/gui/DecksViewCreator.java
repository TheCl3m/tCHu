package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.animation.PathTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.List;

/**
 * class used to create a view on the game's deck
 *
 * @author Clément Husler 328105
 * @author Mathieu Faure 328086
 */
public final class DecksViewCreator {

    private static final int CARD_OUTSIDE_WIDTH = 60;
    private static final int CARD_OUTSIDE_HEIGHT = 90;
    private static final int CARD_INSIDE_WIDTH = 40;
    private static final int CARD_INSIDE_HEIGHT = 70;
    private static final int GAUGE_WIDTH = 50;
    private static final int GAUGE_HEIGHT = 5;
    private static final String DEFAULT_CONTROL_INNER_BACKGROUND = "derive(-fx-base,80%)";
    private static final String HIGHLIGHTED_CONTROL_INNER_BACKGROUND = "derive(palegreen, 50%)";


    /**
     * @param gameState the current ObservableGameState
     * @return a view of the player's hand
     */
    public static HBox createHandView(ObservableGameState gameState) {
        HBox hBox = new HBox();
        hBox.getStylesheets().addAll("decks.css", "colors.css");
        ListView<Ticket> ticketView = ticketListView(gameState);
        hBox.getChildren().add(ticketView);
        HBox handPane = createHandPane(gameState);
        hBox.getChildren().add(handPane);
        return hBox;
    }

    /**
     * @param gameState     the current ObservableGameState
     * @param ticketHandler a property containing an action handler, handling the tickets draws
     * @param cardHandler   a property containing an action handler, handling the cards draws
     * @return a view of the cards and tickets decks, with their respective buttons and gauges,
     * and of the five face up cards
     */
    public static VBox createCardsView(ObservableGameState gameState, ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketHandler, ObjectProperty<ActionHandlers.DrawCardHandler> cardHandler) {
        VBox vBox = new VBox();
        vBox.getStylesheets().addAll("decks.css", "colors.css");
        vBox.setId("card-pane");
        Button ticketsButton = createButton(StringsFr.TICKETS, gameState.ticketPercentage());
        ticketsButton.setOnMouseClicked(e -> ticketHandler.get().onDrawTickets());
        ticketsButton.disableProperty().bind(ticketHandler.isNull());
        Rectangle block = new Rectangle(5, 20);
        block.setVisible(false);
        vBox.getChildren().add(block);
        vBox.getChildren().add(ticketsButton);
        StackPane animationCardPane = new StackPane();
        animationCardPane.getStyleClass().addAll(null, "card");
        animationCardPane.getChildren().addAll(createCard());
        animationCardPane.setVisible(false);
        animationCardPane.setMouseTransparent(true);
        BooleanProperty animationPlaying = new SimpleBooleanProperty(false);
        IntegerProperty slotDrawn = new SimpleIntegerProperty(0);
        createDrawnAnimation(animationCardPane, slotDrawn, animationPlaying, gameState.getLastDrawnCard());
        for (int i : Constants.FACE_UP_CARD_SLOTS) {
            StackPane cardPane = createCardsStackPane(i, gameState);
            cardPane.setOnMouseClicked(e -> {
                slotDrawn.set(i);
                cardHandler.get().onDrawCard(i);
            });
            cardPane.disableProperty().bind(cardHandler.isNull());
            cardPane.setId("face-up-card");
            vBox.getChildren().add(cardPane);
        }
        Button cardsButton = createButton(StringsFr.CARDS, gameState.cardPercentage());
        cardsButton.setOnMouseClicked(e -> {
            slotDrawn.set(Constants.DECK_SLOT);
            cardHandler.get().onDrawCard(Constants.DECK_SLOT);
        });
        cardsButton.disableProperty().bind(cardHandler.isNull());
        vBox.getChildren().add(cardsButton);
        vBox.getChildren().add(animationCardPane);
        return vBox;
    }

    private static void createDrawnAnimation(StackPane animationCardPane, IntegerProperty slotDrawn, BooleanProperty animationPlaying, ObjectProperty<Card> lastDrawnCard) {
        lastDrawnCard.addListener((p, o, card) -> {
            if (card != null) {
                if (animationCardPane.isVisible()) {
                    animationCardPane.setVisible(false);
                }
                String color = card.color() == null ? "NEUTRAL" : card.color().name();
                animationCardPane.getStyleClass().set(0, color);
                animationCardPane.setVisible(true);
                PathTransition pathTransition = Animations.getPath(slotDrawn.get(), card.ordinal());
                pathTransition.setNode(animationCardPane);
                pathTransition.play();
                animationPlaying.set(true);
                pathTransition.setOnFinished(k -> {
                    if (!animationPlaying.get()) {
                        animationCardPane.setVisible(false);
                        animationPlaying.set(false);
                    }
                });
            }
        });
    }


    /**
     * @param gameState the current ObservableGameState
     * @return the Hbox Pane that has been created, showing the current face up cards
     */
    private static HBox createHandPane(ObservableGameState gameState) {
        HBox box = new HBox();
        box.setId("hand-pane");
        for (Card card : Card.ALL) {
            StackPane cardPane = createHandStackPane(card, gameState);
            cardPane.visibleProperty().bind(Bindings.greaterThan(gameState.getCardCount(card), 0));
            box.getChildren().add(cardPane);
        }
        return box;
    }

    private static ListView<Ticket> ticketListView(ObservableGameState gameState) {
        ListView<Ticket> ticketView = new ListView<>(gameState.playersTicket());
        ticketView.setId("tickets");
        ticketView.setCellFactory(new Callback<ListView<Ticket>, ListCell<Ticket>>() {
            @Override
            public ListCell<Ticket> call(ListView<Ticket> param) {
                return new ListCell<Ticket>() {
                    @Override
                    protected void updateItem(Ticket item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            int points = gameState.ticketConnected(item).get();
                            setText(item + " -> " + points);
                            if (points > 0) {
                                setStyle("-fx-control-inner-background: " + HIGHLIGHTED_CONTROL_INNER_BACKGROUND + ";");
                            } else {
                                setStyle("-fx-control-inner-background: " + DEFAULT_CONTROL_INNER_BACKGROUND + ";");
                            }
                        }
                    }
                };
            }
        });
        return ticketView;
    }


    /**
     * @param slot      the slot of the card to display
     * @param gameState the current ObservableGameState
     * @return the created Pane
     */
    private static StackPane createCardsStackPane(int slot, ObservableGameState gameState) {
        StackPane pane = new StackPane();
        gameState.faceUpCard(slot).addListener((p, o, newCard) -> pane.getStyleClass().set(1, newCard.color() == null ? "NEUTRAL" : newCard.color().name()));
        pane.getStyleClass().addAll("card", null); //we add null to it so the style class has the space for the card color
        pane.getChildren().addAll(createCard());
        return pane;
    }

    /**
     * @param card      the card of the player's hand we want to create a StackPane for
     * @param gameState the current ObservableGameState
     * @return the created StackPane, showing the cards' type and the counter associated to it
     */
    private static StackPane createHandStackPane(Card card, ObservableGameState gameState) {
        StackPane pane = new StackPane();
        String color = card.color() == null ? "NEUTRAL" : card.color().name();
        pane.getStyleClass().addAll(color, "card");
        pane.getChildren().addAll(createCard());
        pane.getChildren().add(createCounter(card, gameState));
        return pane;
    }

    /**
     * creates the representation of the cards that are going to be displayed
     *
     * @return the created card
     */
    private static List<Node> createCard() {
        Rectangle r1 = new Rectangle(CARD_OUTSIDE_WIDTH, CARD_OUTSIDE_HEIGHT);
        r1.getStyleClass().add("outside");
        Rectangle r2 = new Rectangle(CARD_INSIDE_WIDTH, CARD_INSIDE_HEIGHT);
        r2.getStyleClass().addAll("filled", "inside");
        Rectangle r3 = new Rectangle(CARD_INSIDE_WIDTH, CARD_INSIDE_HEIGHT);
        r3.getStyleClass().add("train-image");
        return List.of(r1, r2, r3);
    }

    /**
     * @param card      the card type we want to create the counter for
     * @param gameState the game's current ObservableGameState
     * @return the created counter
     */
    private static Node createCounter(Card card, ObservableGameState gameState) {
        Text text = new Text();
        text.getStyleClass().add("count");
        ReadOnlyIntegerProperty count = gameState.getCardCount(card);
        text.textProperty().bind(Bindings.convert(count));
        text.visibleProperty().bind(Bindings.greaterThan(count, 1));
        return text;
    }

    /**
     * @param name       the name of the button, can be for the cards or the tickets
     * @param percentage the percentage of cards or tickets remaining in the pile
     * @return the created button
     */
    private static Button createButton(String name, ReadOnlyIntegerProperty percentage) {
        Button button = new Button(name);
        button.getStyleClass().add("gauged");
        Rectangle gaugeBackground = new Rectangle(GAUGE_WIDTH, GAUGE_HEIGHT);
        gaugeBackground.getStyleClass().add("background");
        Rectangle gaugeForeground = new Rectangle(GAUGE_WIDTH, GAUGE_HEIGHT);
        gaugeForeground.getStyleClass().add("foreground");
        gaugeForeground.widthProperty().bind(percentage.multiply(50).divide(100));
        button.setGraphic(new Group(gaugeBackground, gaugeForeground));
        return button;
    }
}
