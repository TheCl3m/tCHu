package ch.epfl.tchu.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

/**
 * The ServerMain class contains the principal program of the tCHu server,
 * extends Application as it is a JavaFX application
 * its attributes are the default players' names
 *
 * @author Clément Husler 328105
 * @author Mathieu Faure 328086
 */
public final class ServerMain extends Application {

    private static final String NAME_1 = "Ada";
    private static final String NAME_2 = "Charles";


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
     * starts the server by analysing the given arguments to determine the players' name, waits for a connection by the
     * client on the port (5108 by default), creating the two players, a graphical player and a proxy
     * for the distant player located on the client, and starts the game's thread
     *
     * @param primaryStage is ignored
     */
    @Override
    public void start(Stage primaryStage) {
        List<String> args = getParameters().getRaw();


    }

}
