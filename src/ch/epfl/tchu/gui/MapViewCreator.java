package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Route;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.List;

/**
 * The MapViewCreator class creates a view on the game's map
 *
 * @author Mathieu Faure (328086)
 * @author Clement HUSLER (328105)
 */


final class MapViewCreator {

    private static final int RECTANGLE_WIDTH = 36;
    private static final int RECTANGLE_HEIGHT = 12;
    private static final int CIRCLE_RADIUS = 3;
    private static final int CIRCLE_SPACE = 6;

    private MapViewCreator() {
    }

    /**
     * creates a view on the game's map
     *
     * @param gameState         the game's current state
     * @param claimRouteHandler property containing the handler allowing a player to claim a route when necessary
     * @param cardChooser       has the CardChooser type, a functional interface containing a single method used when the
     *                          player has to choose the cards he wants to claim a route with
     * @return the Pane, a view of the game's map
     */
    public static Pane createMapView(ObservableGameState gameState, ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandler,
                                     CardChooser cardChooser) {
        HashMap<Route, Node> routeNodes = new HashMap<>();
        for (Route route : ChMap.routes()) {
            Group routeGroup = createRouteGroup(route);
            gameState.getRouteOwner(route).addListener((p, o, n) -> {
                if (n != null) {
                    routeGroup.getStyleClass().add(n.name());
                }
            });
            routeGroup.disableProperty().bind(claimRouteHandler.isNull().or(gameState.canClaimRoute(route).not()));
            routeGroup.setOnMouseClicked(e -> {
                List<SortedBag<Card>> options = gameState.possibleClaimCards(route);
                if (options.size() == 1) {
                    claimRouteHandler.get().onClaimRoute(route, options.get(0));
                } else {
                    ActionHandlers.ChooseCardsHandler chooseCardsH = chosenCards -> claimRouteHandler.get().onClaimRoute(route, chosenCards);
                    cardChooser.chooseCards(options, chooseCardsH);
                }
            });
            routeNodes.put(route, routeGroup);
        }
        Pane pane = new Pane();
        pane.getStylesheets().addAll("map.css", "colors.css");
        ImageView map = new ImageView();
        pane.getChildren().add(map);
        pane.getChildren().addAll(routeNodes.values());
        return pane;
    }

    /**
     * creates the Route group, called as many times as there are routes on the map
     *
     * @param route the Route to create a group for
     * @return the group created for the given route
     */
    private static Group createRouteGroup(Route route) {
        Group group = new Group();
        group.setId(route.id());
        String color = route.color() == null ? "NEUTRAL" : route.color().name();
        group.getStyleClass().addAll("route", route.level().name(), color);
        for (int i = 1; i <= route.length(); ++i) {
            group.getChildren().add(createRouteBox(route.id() + "_" + i));
        }
        return group;
    }

    /**
     * creates a box for each unity of length of each route of the map, can be either filled with a case or a wagon
     *
     * @param id the route's portion ID
     * @return the group created
     */
    private static Group createRouteBox(String id) {
        Group group = new Group();
        group.setId(id);
        Rectangle r = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        r.getStyleClass().addAll("track", "filled");
        group.getChildren().add(r);
        group.getChildren().add(createRouteWagon());
        return group;
    }

    /**
     * creates a wagon for each box of each route of the game's map
     *
     * @return the group created
     */
    private static Group createRouteWagon() {
        Rectangle r = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        r.getStyleClass().add("filled");
        Circle c1 = new Circle(12, CIRCLE_SPACE, CIRCLE_RADIUS);
        Circle c2 = new Circle(24, CIRCLE_SPACE, CIRCLE_RADIUS);
        Group group = new Group(r, c1, c2);
        group.getStyleClass().add("car");
        return group;
    }

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options,
                         ActionHandlers.ChooseCardsHandler handler);
    }
}
