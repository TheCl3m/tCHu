package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Map;

/**
 * The InfoViewCreator class is used to create the view on the game's information
 *
 * @author Mathieu Faure (328086)
 * @author Clement HUSLER (328105)
 */

final class InfoViewCreator {

    private final static int CIRCLE_RADIUS = 5;

    private InfoViewCreator() {
    }

    /**
     * the only public method of the InfoViewCreator class
     * creates the view of the game's information
     *
     * @param playerId    the ID of the player to who the graphical interface corresponds
     * @param playerNames the map associating the players' IDs with their names
     * @param gameState   the current ObservableGameState
     * @param textList    an ObservableList containing the information on the game, as Texts
     * @return the created VBox
     */
    public static VBox createInfoView(PlayerId playerId, Map<PlayerId, String> playerNames, ObservableGameState gameState, ObservableList<Text> textList, ObservableList<Text> chat, ObjectProperty<ActionHandlers.chatHandler> chatHandler) {
        VBox infoBox = new VBox();
        infoBox.getStylesheets().addAll("info.css", "colors.css");
        VBox playerStatsBox = createPlayerStatsBox(playerId, playerNames, gameState);
        Separator separator = new Separator();
        Separator separator1 = new Separator();
        TextFlow messages = createMessagesFlow(textList);
        TextFlow chatFlow = createMessagesFlow(chat);
        GridPane reactions = createReactionButtons(chatHandler, playerNames.get(playerId));
        TextField textField = new TextField();
        textField.setPromptText("Entrez un message");
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (textField.getText().length() > 75) {
                    String s = textField.getText().substring(0, 75);
                    textField.setText(s);
                }
            }
        });
        Button sendButton = new Button("Envoyer");
        sendButton.disableProperty().bind(Bindings.isEmpty(textField.textProperty()));
        sendButton.setId("send-button");
        sendButton.setOnAction(e -> {
            chatHandler.get().onReaction(playerNames.get(playerId) + ": " + textField.getText().strip() + "\n");
            textField.clear();
        });
        HBox chatInput = new HBox(textField, sendButton);
        chatInput.setSpacing(10);
        chatInput.disableProperty().bind(chatHandler.isNull());
        infoBox.getChildren().addAll(playerStatsBox, separator, messages, separator1, chatFlow, new Separator(), reactions, chatInput);
        infoBox.setSpacing(5);
        return infoBox;
    }

    /**
     * creates the VBox containing the player's stats, child of the Vbox created in the createInfoView method
     *
     * @param pId         the ID of the player to who the graphical interface corresponds
     * @param playerNames the map associating the players' IDs with their names
     * @param gameState   the current ObservableGameState
     * @return the created VBox
     */
    private static VBox createPlayerStatsBox(PlayerId pId, Map<PlayerId, String> playerNames, ObservableGameState gameState) {
        VBox box = new VBox();
        box.setId("player-stats");
        TextFlow playerTextFlow = createPlayerStats(pId, playerNames.get(pId), gameState);
        TextFlow opponentTextFlow = createPlayerStats(pId.next(), playerNames.get(pId.next()), gameState);
        box.getChildren().addAll(playerTextFlow, opponentTextFlow);
        return box;
    }

    /**
     * creates the player's statistics, method called in the createPlayerStatsBox method
     *
     * @param pId        the ID of the player to who the graphical interface corresponds
     * @param playerName the map associating the players' IDs with their names
     * @param gS         the current ObservableGameState
     * @return the created stats as a TextFlow
     */
    private static TextFlow createPlayerStats(PlayerId pId, String playerName, ObservableGameState gS) {
        TextFlow textFlow = new TextFlow();
        textFlow.getStyleClass().add(pId.name());
        Circle circle = new Circle(CIRCLE_RADIUS);
        circle.getStyleClass().add("filled");
        Text text = new Text();
        text.textProperty().bind(Bindings.format(StringsFr.PLAYER_STATS, playerName, gS.getPlayerTicketCount(pId),
                gS.getPlayerCardCount(pId), gS.getPlayerWagonCount(pId), gS.getPlayerBuildPoints(pId)));
        textFlow.getChildren().addAll(circle, text);
        return textFlow;
    }

    /**
     * creates the TextFlow with a given List of Texts
     *
     * @param textList the given List of Texts
     * @return the created TextFlow
     */
    private static TextFlow createMessagesFlow(ObservableList<Text> textList) {
        TextFlow textFlow = new TextFlow();
        textFlow.setId("game-info");
        Bindings.bindContent(textFlow.getChildren(), textList);
        return textFlow;
    }

    private static GridPane createReactionButtons(ObjectProperty<ActionHandlers.chatHandler> chatHandler, String name) {
        Button b1 = createReactionButton("Bien joué!", name, chatHandler);
        Button b2 = createReactionButton("Arghh!", name, chatHandler);
        Button b3 = createReactionButton("Bien essayé!", name, chatHandler);
        GridPane grid = new GridPane();
        grid.setId("reaction-grid");
        grid.add(b1, 0, 0);
        grid.add(b2, 1, 0);
        grid.add(b3, 2, 0);
        grid.setHgap(5);

        return grid;
    }

    private static Button createReactionButton(String reaction, String name, ObjectProperty<ActionHandlers.chatHandler> chatHandler) {
        Button b = new Button(reaction);
        b.setId("reaction-button");
        b.disableProperty().bind(chatHandler.isNull());
        b.setOnAction(e -> chatHandler.get().onReaction(name + ": " + reaction + "\n"));
        return b;
    }
}
