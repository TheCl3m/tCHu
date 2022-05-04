package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public final class ClientMain2 extends Application {
    /**
     * test class for etape 12, please ignore.
     *
     * @param args
     */

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        List<String> args = getParameters().getRaw();
        Preconditions.checkArgument(args.size() >= 2, "Not enough information to establish connection");
        GraphicalPlayerAdapter player = new GraphicalPlayerAdapter();
        RemotePlayerClient client = new RemotePlayerClient(player, args.get(0), Integer.parseInt(args.get(1)));
        new Thread(client::run).start();
    }

}
