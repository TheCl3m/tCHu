package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class GraphicalPlayerTest extends Application {


    private void setState(GraphicalPlayer player) {
        SortedBag.Builder<Card> playersCards = new SortedBag.Builder();
        playersCards.add(5, Card.ORANGE);
        playersCards.add(6, Card.BLUE);
        playersCards.add(10, Card.LOCOMOTIVE);
        playersCards.add(30, Card.RED);
        playersCards.add(6, Card.WHITE);
        playersCards.add(10, Card.GREEN);
        playersCards.add(30, Card.YELLOW);
        playersCards.add(30, Card.VIOLET);
        PlayerState p1State =
                new PlayerState(SortedBag.of(ChMap.tickets().subList(0, 3)),
                        playersCards.build(),
                        ChMap.routes().subList(0, 3));

        PublicPlayerState p2State =
                new PublicPlayerState(0, 0, ChMap.routes().subList(3, 6));

        Map<PlayerId, PublicPlayerState> pubPlayerStates =
                Map.of(PLAYER_1, p1State, PLAYER_2, p2State);
        PublicCardState cardState =
                new PublicCardState(Card.ALL.subList(0, 5), 110 - 2 * 4 - 5, 0);
        PublicGameState publicGameState =
                new PublicGameState(36, cardState, PLAYER_1, pubPlayerStates, null);
        player.setState(publicGameState, p1State);
    }

    @Override
    public void start(Stage primaryStage) {
        Map<PlayerId, String> playerNames =
                Map.of(PLAYER_1, "Ada", PLAYER_2, "Charles");
        GraphicalPlayer p = new GraphicalPlayer(PLAYER_1, playerNames);
        setState(p);

        ActionHandlers.ChooseTicketsHandler chooseTicketsHandler =
                (bag) -> p.receiveInfo("Utilisation de :" + bag.toString());

        ActionHandlers.DrawTicketsHandler drawTicketsH =
                () -> {
                    p.receiveInfo("Je tire des billets !");
                    p.chooseTickets(SortedBag.of(1, ChMap.tickets().get(0), 2, ChMap.tickets().get(6)), chooseTicketsHandler);
                };
        ActionHandlers.DrawCardHandler drawCardH =
                s -> p.receiveInfo(String.format("Je tire une carte de %s !", s));
        ActionHandlers.ClaimRouteHandler claimRouteH =
                (r, cs) -> {
                    String rn = r.station1() + " - " + r.station2();
                    p.receiveInfo(String.format("Je m'empare de %s avec %s", rn, Info.cardsToString(cs)));
                };

        p.startTurn(drawCardH, drawTicketsH, claimRouteH);
    }
}