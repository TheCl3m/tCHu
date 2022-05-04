package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerClient;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main extends Application {

    private final MediaPlayer music = new MediaPlayer(new Media(getClass().getResource(SoundID.MAIN_MENU.filename()).toExternalForm()));
    private static BooleanProperty musicStoped;

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    Slider slider;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXMLS/MainMenu.fxml"));
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("tCHu" + StringsFr.EN_DASH_SEPARATOR + "Menu Principal");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("logo.png"));
        primaryStage.show();
        music.setCycleCount(MediaPlayer.INDEFINITE);
        music.play();
        musicStoped = new SimpleBooleanProperty(false);
        musicStoped.addListener(e -> music.pause());
        slider = (Slider) pane.getChildren().get(5);
        slider.valueProperty().addListener((p, o, n) -> {
            music.setVolume(n.doubleValue() / 100);
        });
    }

    private void stopMusic() {
        musicStoped.set(true);
    }

    @FXML
    private void createNamesInputModal(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("Ada");
        dialog.setHeaderText(null);
        dialog.setTitle("Votre nom");
        dialog.setContentText("Entrez votre nom:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name1 -> {
            TextInputDialog dialog1 = new TextInputDialog("Charles");
            dialog1.setContentText("Entrez le nom de l'adversaire:");
            dialog1.setTitle("Nom de l'adversaire");
            dialog1.setHeaderText(null);
            Optional<String> result1 = dialog1.showAndWait();
            result1.ifPresent(name2 -> {
                new Thread(() -> startServer(name1, name2)).start();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Serveur lancé");
                alert.setHeaderText(null);
                alert.setContentText("Le serveur est lancé ! Veuillez patienter jusqu'à ce que l'adversaire rejoigne.");
                alert.showAndWait();
            });
        });
    }


    private void startServer(String name1, String name2) {
        try (ServerSocket serverSocket = new ServerSocket(5108)) {
            Socket socket = serverSocket.accept();
            stopMusic();
            RemotePlayerProxy player2 = new RemotePlayerProxy(socket);
            GraphicalPlayerAdapter player1 = new GraphicalPlayerAdapter();
            Map<PlayerId, Player> playerMap = Map.of(PlayerId.PLAYER_1, player1, PlayerId.PLAYER_2, player2);
            Map<PlayerId, String> playerNames = Map.of(PlayerId.PLAYER_1, name1, PlayerId.PLAYER_2, name2);
            new Thread(() -> Game.play(playerMap,
                    playerNames, SortedBag.of(ChMap.tickets()), new Random()))
                    .start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void promptServerIp(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("XXX.XXX.XX.X");
        dialog.setHeaderText(null);
        dialog.setTitle("Rejoindre une partie de tCHu");
        dialog.setContentText("Entrez l'addresse du serveur:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(adr -> {
            TextInputDialog dialog1 = new TextInputDialog("5108");
            dialog1.setHeaderText(null);
            dialog1.setTitle("Rejoindre une partie de tCHu");
            dialog1.setContentText("Entrez l'addresse du serveur:");
            Optional<String> result1 = dialog1.showAndWait();
            result1.ifPresent(port -> {
                GraphicalPlayerAdapter player = new GraphicalPlayerAdapter();
                RemotePlayerClient client = new RemotePlayerClient(player, adr, Integer.parseInt(port));
                new Thread(client::run).start();
            });
        });
    }

    @FXML
    private void showRules(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Règles du jeu de tCHu");
        WebView webView = new WebView();
        webView.getEngine().load("https://cs108.epfl.ch/p/00_introduction.html");
        webView.setPrefSize(750, 800);
        alert.getDialogPane().setContent(webView);
        alert.showAndWait();
    }

    @FXML
    private void offlineGame(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("Ada");
        dialog.setHeaderText(null);
        dialog.setTitle("Votre nom");
        dialog.setContentText("Entrez votre nom:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name1 -> {
            List<String> choices = new ArrayList<>();
            choices.add("Faible");
            choices.add("Moyen");
            ChoiceDialog<String> dialog2 = new ChoiceDialog<>("Faible", choices);
            dialog2.setTitle("Difficulté de l'adversaire");
            dialog2.setHeaderText("Choisissez le niveau de votre adversaire");
            dialog2.setContentText("Niveau:");
            Optional<String> result1 = dialog2.showAndWait();
            result1.ifPresent(level -> {
                AI robot = null;
                switch (choices.indexOf(level)) {
                    case 0:
                        robot = new SimpleAI();
                        break;
                    case 1:
                        robot = new MediumAI();
                        break;
                }
                stopMusic();
                GraphicalPlayerAdapter player1 = new GraphicalPlayerAdapter();
                AI finalRobot = robot;
                new Thread(() -> Game.play(Map.of(PlayerId.PLAYER_1, player1, PlayerId.PLAYER_2, finalRobot),
                        Map.of(PlayerId.PLAYER_1, name1, PlayerId.PLAYER_2, "Ordinateur"), SortedBag.of(ChMap.tickets()), new Random()))
                        .start();
            });
        });
    }

}
