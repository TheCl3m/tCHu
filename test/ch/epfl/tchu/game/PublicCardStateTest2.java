package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicCardStateTest2 {

    @Test
    void totalSizeNormalCase() {
        PublicCardState dawg = new PublicCardState((SortedBag.of(2, Card.BLACK, 3, Card.GREEN).toList()), 2, 2);
       // assertEquals(9, dawg.totalSize());
    }

    @Test
    void totalSizeAbnormalCase() {
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicCardState((SortedBag.of(2, Card.BLACK, 4, Card.GREEN).toList()), 2, 2);
        });
    }

    @Test
    void faceUpCards() {
        PublicCardState dawg = new PublicCardState((SortedBag.of(2, Card.BLACK, 3, Card.GREEN).toList()), 2, 2);
        assertEquals(List.of(Card.BLACK, Card.BLACK, Card.GREEN, Card.GREEN, Card.GREEN), dawg.faceUpCards());
    }

    @Test
    void faceUpCardNormalCase() {
        PublicCardState dawg = new PublicCardState((SortedBag.of(2, Card.BLACK, 3, Card.GREEN).toList()), 2, 2);
        assertEquals(Card.BLACK, dawg.faceUpCard(1));
    }

    @Test
    void faceUpCardAbnormalCase() {
        PublicCardState dawg = new PublicCardState((SortedBag.of(2, Card.BLACK, 3, Card.GREEN).toList()), 2, 2);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            dawg.faceUpCard(6);
        });
    }

    @Test
    void deckSize() {
        PublicCardState dawg = new PublicCardState((SortedBag.of(2, Card.BLACK, 3, Card.GREEN).toList()), 2, 2);
        assertEquals(2, dawg.deckSize());
    }

    @Test
    void isDeckEmpty() {
        PublicCardState dawg = new PublicCardState((SortedBag.of(2, Card.BLACK, 3, Card.GREEN).toList()), 0, 2);
        assertTrue(dawg.isDeckEmpty());
    }

    @Test
    void discardsSize() {
        PublicCardState dawg = new PublicCardState((SortedBag.of(2, Card.BLACK, 3, Card.GREEN).toList()), 0, 2);
        assertEquals(2 , dawg.discardsSize());
    }
}