package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PublicCardStateTest {

    @Test
    void constructorThrowsExceptionWrongList(){
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        int deckSize = 3;
        int discardSize = 3;
        assertThrows(IllegalArgumentException.class, () -> {new PublicCardState(list, deckSize, discardSize);});
    }

    @Test
    void constructorThrowsExceptionWrongDeckSize(){
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        int deckSize = 0;
        int discardSize = 3;
        assertThrows(IllegalArgumentException.class, () -> {new PublicCardState(list, deckSize, discardSize);});
    }

    @Test
    void constructorThrowsExceptionWrongDiscardSize(){
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        int deckSize = 3;
        int discardSize = 0;
        assertThrows(IllegalArgumentException.class, () -> {new PublicCardState(list, deckSize, discardSize);});
    }

    @Test
    void totalSize() {
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        int deckSize = 3;
        int discardSize = 3;
        PublicCardState test = new PublicCardState(list, deckSize, discardSize);
       // assertEquals(deckSize + discardSize + list.size(), test.totalSize());
    }

    @Test
    void faceUpCards() {
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.ORANGE);
        list.add(Card.BLUE);
        list.add(Card.RED);
        list.add(Card.GREEN);
        int deckSize = 3;
        int discardSize = 3;
        PublicCardState test = new PublicCardState(list, deckSize, discardSize);
        assertEquals(list, test.faceUpCards());
    }

    @Test
    void faceUpCard() {
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK); //0
        list.add(Card.ORANGE); //1
        list.add(Card.BLUE); //2
        list.add(Card.RED); //3
        list.add(Card.GREEN); //4
        int deckSize = 3;
        int discardSize = 3;
        PublicCardState test = new PublicCardState(list, deckSize, discardSize);
        assertEquals(Card.BLUE, test.faceUpCard(2));
    }

@Test
void faceUpCardThrowsException(){
    List<Card> list = new ArrayList<>();
    list.add(Card.BLACK);
    list.add(Card.BLACK);
    list.add(Card.BLACK);
    list.add(Card.BLACK);
    list.add(Card.BLACK);
    int deckSize = 3;
    int discardSize = 3;
    PublicCardState test = new PublicCardState(list, deckSize, discardSize);
    assertThrows(IndexOutOfBoundsException.class, ()-> {test.faceUpCard(8);});
}



    @Test
    void deckSizeNonTrivial() {
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        int deckSize = 3;
        PublicCardState test = new PublicCardState(list, deckSize, 2);
        assertEquals(deckSize, test.deckSize());
    }

    @Test
    void deckSizeTrivial() {
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        int deckSize = 0;
        PublicCardState test = new PublicCardState(list, deckSize, 2);
        assertEquals(deckSize, test.deckSize());
    }

    @Test
    void isDeckEmptyEmptyDeck() {
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        int deckSize = 0;
        PublicCardState test = new PublicCardState(list, deckSize, 2);
        assertTrue(test.isDeckEmpty());
    }

    @Test
    void isDeckEmptyNonEmptyDeck(){
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        int deckSize = 3;
        PublicCardState test = new PublicCardState(list, deckSize, 2);
        assertFalse(test.isDeckEmpty());
    }

    @Test
    void discardsSizeTrivial() {
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        int discardSize = 0;
        PublicCardState test = new PublicCardState(list, 2, discardSize);
        assertEquals(discardSize, test.discardsSize());
    }

    @Test
    void discardsSizeNonTrivial() {
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        int discardSize = 3;
        PublicCardState test = new PublicCardState(list, 2, discardSize);
        assertEquals(discardSize, test.discardsSize());
    }
}