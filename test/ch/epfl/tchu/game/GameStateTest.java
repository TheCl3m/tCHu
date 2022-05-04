package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ch.epfl.tchu.game.ChMap.tickets;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    @Test
    void topTicketsThrowExceptionNegativeCount(){
        GameState test = GameState.initial(SortedBag.of(), new Random());
        assertThrows(IllegalArgumentException.class, ()->{test.topTickets(-2);});
    }

    @Test
    void topTicketsThrowExceptionTooBigCount(){
        GameState test = GameState.initial(SortedBag.of(), new Random());
        assertThrows(IllegalArgumentException.class, ()->{test.topTickets(6);});
    }

    @Test
    void withoutTopTicketsThrowExceptionNegativeCount(){
        GameState test = GameState.initial(SortedBag.of(), new Random());
        assertThrows(IllegalArgumentException.class, ()->{test.topTickets(-2);});
    }

    @Test
    void withoutTopTicketsThrowExceptionTooBigCount(){
        GameState test = GameState.initial(SortedBag.of(), new Random());
        assertThrows(IllegalArgumentException.class, ()->{test.topTickets(6);});
    }
    @Test
    void topCardThrowsExceptionEmptyDeck(){
        GameState test = GameState.initial(SortedBag.of(), new Random());
        for (int i = 0; i < 97; ++i) {
            test = test.withoutTopCard();
        }
        assertThrows(IllegalArgumentException.class, test::topCard);
    }

    @Test
    void withDrawnFaceUpCardsThrowsExceptionEmptyDeck(){
    GameState test = GameState.initial(SortedBag.of(), new Random());
        for (int i = 0; i < 96; ++i) {
            test = test.withoutTopCard();
        }
        final GameState test2 = test;
        assertThrows(IllegalArgumentException.class, ()-> {test2.withDrawnFaceUpCard(1);});
    }

    @Test
    void withBlindlyDrawnCardThrowsExceptionEmptyDeck(){
        GameState test = GameState.initial(SortedBag.of(), new Random());
        for (int i = 0; i < 96; ++i) {
            test = test.withoutTopCard();
        }
        assertThrows(IllegalArgumentException.class, test::withBlindlyDrawnCard);
    }

    @Test
    void initialGoodCardsAndTicketRepartition() {
        GameState gameState = GameState.initial(SortedBag.of(ChMap.tickets()), new Random());
       // assertEquals(Constants.ALL_CARDS.size() - 8, gameState.cardState().totalSize());
        assertEquals(4, gameState.playerState(PlayerId.PLAYER_1).cardCount());
        assertEquals(4, gameState.playerState(PlayerId.PLAYER_2).cardCount());
        assertEquals(ChMap.tickets().size(), gameState.ticketsCount());
    }

    @Test
    void initialPlayerIsRandom() {
        List<PlayerId> list = new ArrayList<>();
        GameState state;
        boolean firstPlayerWasSelected = false;
        boolean secondPlayerWasSelected = false;
        int count = 0;
        while ((!firstPlayerWasSelected || !secondPlayerWasSelected) && count < 40 ){
            ++count;
            state = GameState.initial(SortedBag.of(tickets()), new Random());
            if (state.currentPlayerId() == PlayerId.PLAYER_1){
                firstPlayerWasSelected = true;
            }
            else if (state.currentPlayerId() == PlayerId.PLAYER_2){
                secondPlayerWasSelected = true;
            }
        }
        assertTrue(firstPlayerWasSelected && secondPlayerWasSelected);
    }

    @Test
    void emptyDeckTestAndRecreation() {
        GameState gameState = GameState.initial(SortedBag.of(ChMap.tickets()), new Random());
        for(int i = 0; i < 97; ++i){
            gameState = gameState.withoutTopCard();
        }
        GameState finalStateNonEmptyDiscards = gameState.withMoreDiscardedCards(SortedBag.of(10, Card.BLACK));
        final GameState finalStateNonEmptyDeck = finalStateNonEmptyDiscards.withCardsDeckRecreatedIfNeeded(new Random());
        final GameState finalStateEmptyDeck = gameState;
        assertThrows(IllegalArgumentException.class, () -> {
            finalStateEmptyDeck.withoutTopCard();
        });
        assertNotEquals(gameState, finalStateNonEmptyDiscards);
        try{
            finalStateNonEmptyDeck.withoutTopCard();
        }
        catch(Exception e){
            fail();
        }

    }


}
