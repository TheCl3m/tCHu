package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest2 {

    public static final Random rng = new Random() {
        @Override
        public int nextInt(int i) {
            return i-1;
        }
    };
    @Test
    void of() {
    }


    @Test
    void size() {
        Deck<Card> packet = Deck.<Card>of(SortedBag.of(1, Card.BLUE) , rng);
        assertEquals(1 , packet.size());
    }

    @Test
    void isEmpty() {
        SortedBag ewaa = SortedBag.of();
        Deck<Card> packet = Deck.<Card>of(ewaa , rng);
        assertEquals(true , packet.isEmpty());
    }

    @Test
    void topCard() {
        Deck<Card> packet = Deck.<Card>of(SortedBag.of(1, Card.BLUE) , rng);
        assertEquals(Card.BLUE , packet.topCard());
    }


    //Sometimes true checking if the shuffle is working
    @Test
    void withoutTopCard() {
        Deck<Card> packet = Deck.<Card>of(SortedBag.of(10, Card.BLUE , 10,  Card.GREEN ) , rng);
        assertEquals(Card.BLUE , packet.withoutTopCard().topCard());
    }


    //not all the time pass because of shuffle
    @Test
    void topCards2() {
        Deck<Card> packet = Deck.<Card>of(SortedBag.of(5, Card.BLUE , 2 , Card.GREEN  ) , rng);
        SortedBag dawg = packet.topCards(3);
        assertEquals(SortedBag.of(3, Card.BLUE) , dawg);

    }

    @Test
    void withoutTopCards2() {
        Deck<Card> packet = Deck.<Card>of(SortedBag.of(5, Card.BLUE , 2 , Card.GREEN  ) , rng);
        Deck<Card> packet2 = packet.withoutTopCards(3);
        assertEquals(4 , packet2.size());
    }

    @Test
    void withoutTopCardsCheckForCondition() {
        Deck<Card> packet = Deck.<Card>of(SortedBag.of(1, Card.BLUE) , rng);
        Deck<Card> packet2 = packet.withoutTopCard();
       assertThrows(IllegalArgumentException.class, () -> {
           packet2.withoutTopCard();
       });
    }
}