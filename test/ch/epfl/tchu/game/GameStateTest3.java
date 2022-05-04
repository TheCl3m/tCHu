package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static ch.epfl.tchu.game.Constants.ALL_CARDS;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.SortedBag.Builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameStateTest3 {
    public static final Random NON_RANDOM = new Random() {
        @Override
        public int nextInt(int i) {
            return i - 1;
        }
    };

    @Test
    void testingWithInitiallyChosenTickets() {
        PlayerId player = PlayerId.PLAYER_1;
        SortedBag<Ticket> tickets;
        SortedBag.Builder<Ticket> B = new SortedBag.Builder<>();
        List<Ticket> L = ChMap.tickets();

        for (Ticket l : L) {
            B.add(l);
        }
        tickets = B.build();

        GameState gameState = GameState.initial(tickets, new Random());
        assertEquals(0, gameState.playerState(player).tickets().size());

        B = new SortedBag.Builder<>();

        for (int i = 0; i < 5; i++) {
            B.add(tickets.get(i));
        }

        SortedBag<Ticket> chosenTickets;
        chosenTickets = B.build();

        gameState = gameState.withInitiallyChosenTickets(player, chosenTickets);
        assertEquals(5, gameState.playerState(player).tickets().size());
    }


    @Test
    void initialWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets, NON_RANDOM);
        assertEquals(ALL_CARDS.size() - 13, gameState.cardState().deckSize());
        assertEquals(PlayerId.ALL.get(1), gameState.currentPlayerId());

    }

    @Test
    void playerStateWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets, NON_RANDOM);
        SortedBag.Builder<Card> SB1 = new Builder();
        SortedBag.Builder<Card> SB2 = new Builder();
        for (int i = 0; i < 4; ++i) {
            SB1.add(ALL_CARDS.get(i));
            SB2.add(ALL_CARDS.get(i + 4));
        }
        SortedBag<Card> initialCards1 = SB1.build();
        SortedBag<Card> initialCards2 = SB2.build();

        assertEquals(initialCards1, gameState.playerState(PlayerId.PLAYER_1).cards());
        assertEquals(initialCards2, gameState.playerState(PlayerId.PLAYER_2).cards());
    }

    @Test
    void countTicketsWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets, NON_RANDOM);
        SortedBag.Builder<Ticket> SB = new SortedBag.Builder<>();
        SB.add(tickets.get(0));
        SB.add(tickets.get(1));
        SB.add(tickets.get(2));
        SortedBag<Ticket> chosenTickets = SB.build();

        assertEquals(chosenTickets, gameState.topTickets(3));
    }

    @Test
    void withoutTopTicketsWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(1));
        GameState gameState = GameState.initial(tickets, NON_RANDOM);
        GameState newGameState = gameState.withoutTopTickets(1);
        SortedBag<Ticket> tickets1 = SortedBag.of(1, ChMap.tickets().get(1));
        GameState expected = GameState.initial(tickets1, NON_RANDOM);

        assertEquals(newGameState.topTickets(1), expected.topTickets(1));


    }


    @Test
    void withInitiallyChosenTicketsWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets, NON_RANDOM);
        GameState newGameState = gameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(1)));
        assertEquals(SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(1)), newGameState.playerState(PlayerId.PLAYER_1).tickets());

    }

    @Test
    void withInitiallyChosenTicketsWorksThrowsIllegalArgumentException() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets, NON_RANDOM);
        GameState newGameState = gameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(1)));
        assertThrows(IllegalArgumentException.class, () -> {
            newGameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, SortedBag.of(ChMap.tickets().get(13)));
        });

    }

    @Test
    void withChosenAdditionalTicketsWorks() {
        Random ayre = new Random();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets, ayre);
        SortedBag<Ticket> drawnTickets = SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(1));
        SortedBag<Ticket> chosenTickets = SortedBag.of(1, ChMap.tickets().get(0));
        GameState newGameState = gameState.withChosenAdditionalTickets(drawnTickets, chosenTickets);
        System.out.println(newGameState.currentPlayerState().tickets());
        System.out.println(gameState.currentPlayerState().withAddedTickets(chosenTickets).tickets());
        System.out.println(newGameState.currentPlayerState().tickets());
        System.out.println(gameState.currentPlayerId());
        System.out.println(newGameState.currentPlayerId());
        assertEquals(newGameState.currentPlayerState().tickets(), gameState.currentPlayerState().withAddedTickets(chosenTickets).tickets());
    }

    @Test
    void withChosenAdditionalTicketsThrowsException() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets, NON_RANDOM);
        SortedBag<Ticket> drawnTickets = SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(1));
        SortedBag<Ticket> chosenTickets = SortedBag.of(1, ChMap.tickets().get(3));
        assertThrows(IllegalArgumentException.class, () -> {
            gameState.withChosenAdditionalTickets(drawnTickets, chosenTickets);
        });
    }

    @Test
    void withDrawnFaceUpCardWorks() {
        Random rng = new Random();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets, rng);
        GameState newGameState = gameState.withDrawnFaceUpCard(2);
        assertEquals(gameState.topCard(), newGameState.cardState().faceUpCard(2));
        System.out.println(ALL_CARDS);
        //assertEquals(gameState.cardState().faceUpCard(2), newGameState.currentPlayerState().cards().get(0));
        Map<Card, Integer> stephano = Objects.requireNonNull(newGameState.currentPlayerState().cards().toMap());
        Map<Card, Integer> stephano2 = Objects.requireNonNull(gameState.currentPlayerState().cards().toMap());

        int integerMappedToCard = 0;
        if (stephano2.get(gameState.cardState().faceUpCard(2)) != null) {
            integerMappedToCard = stephano2.get(gameState.cardState().faceUpCard(2));
        }
        assertEquals(integerMappedToCard + 1, stephano.get(gameState.cardState().faceUpCard(2)));
        assertEquals(gameState.currentPlayerState().cards().size() + 1, newGameState.currentPlayerState().cards().size());
    }


    @Test
    void withBlindlyDrawnCardWorks() {
        Random rng = new Random();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets, rng);
        GameState newGameState = gameState.withBlindlyDrawnCard();
        Map<Card, Integer> stephano = Objects.requireNonNull(newGameState.currentPlayerState().cards().toMap());
        Map<Card, Integer> stephano2 = Objects.requireNonNull(gameState.currentPlayerState().cards().toMap());

        int integerMappedToCard = 0;
        CardState botros = (CardState) gameState.cardState();
        if (stephano2.get(botros.topDeckCard()) != null) {
            integerMappedToCard = stephano2.get(botros.topDeckCard());
        }
        assertEquals(integerMappedToCard + 1, stephano.get(botros.topDeckCard()));
        assertEquals(gameState.currentPlayerState().cards().size() + 1, newGameState.currentPlayerState().cards().size());
    }

    @Test
    void withClaimedRouteWorks() {
        Random rng = new Random();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets, rng);
        GameState newGameState = gameState.withClaimedRoute(ChMap.routes().get(0), SortedBag.of(4, Card.ORANGE));
        assertEquals(true, newGameState.playerState(newGameState.currentPlayerId()).routes().contains(ChMap.routes().get(0)));
    }


}
