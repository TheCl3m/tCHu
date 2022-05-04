package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

/**
 * The ServerMain class contains the principal program of the tCHu client,
 * extends Application as it is a JavaFX application
 *
 * @author Clément Husler 328105
 * @author Mathieu Faure 328086
 */
public final class ClientMain extends Application {


    /**
     * the method simply calls the launch method of the Application class
     *
     * @param args the program's arguments as a Strings array
     * @see Application#launch(String...)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * starts the client by analysing the arguments to find the host's name and the server's port number,
     * creating a distant client as an instance of RemotePlayerClient associated to a GraphicalPlayerAdapter instance,
     * and starts the thread handling the network access, executing the run method of the client previously created
     *
     * @param primaryStage not used
     * @see RemotePlayerClient#run()
     */
    @Override
    public void start(Stage primaryStage) {
        List<String> args = getParameters().getRaw();
        Preconditions.checkArgument(args.size() >= 2, "Not enough information to establish connection");
        GraphicalPlayerAdapter player = new GraphicalPlayerAdapter();
        RemotePlayerClient client = new RemotePlayerClient(player, args.get(0), Integer.parseInt(args.get(1)));
        new Thread(client::run).start();
    }

}
