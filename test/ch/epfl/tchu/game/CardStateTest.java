package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CardStateTest {

    public static final Random RANDOM = new Random() {
        @Override
        public int nextInt(int i) {
            return i-1;
        }
    };
    @Test
    void of() {
    }

    @Test
    void ofWrongDeckSize(){
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(Card.LOCOMOTIVE);
        Deck deck = Deck.of(bag.build(), RANDOM);
        assertThrows(IllegalArgumentException.class, () -> {CardState.of(deck);});
    }

    @Test
    void withDrawnFaceUpCardNonTrivialSlot(){
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(Card.LOCOMOTIVE);
        bag.add(Card.ORANGE);
        bag.add(Card.RED);
        bag.add(Card.GREEN);
        bag.add(Card.WHITE);
        bag.add(Card.BLACK);
        bag.add(Card.BLUE);
        Deck deck = Deck.of(bag.build(), RANDOM);
        CardState test = CardState.of(deck);
        List<Card> topCardsStart = test.faceUpCards();
        for(Card card : topCardsStart){
            System.out.println(card);
        }
        System.out.println("----------");
        Card originTopCard = test.topDeckCard();
        System.out.println("La carte en haut de la pile était :" + originTopCard);
        int slot = 1;
        CardState end = test.withDrawnFaceUpCard(slot);
        System.out.println("Remplaçage de la carte");
        List<Card> topCardsEnd = end.faceUpCards();
        for(Card card : topCardsEnd){
            System.out.println(card);
        }
        assertEquals(originTopCard, topCardsEnd.get(slot));
    }

    @Test
    void withDrawnFaceUpCardOutOfBoundsThrown(){
        SortedBag.Builder<Card> list = new SortedBag.Builder<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        Deck deck = Deck.of(list.build(), RANDOM);
        CardState test = CardState.of(deck);
        assertThrows(IndexOutOfBoundsException.class, ()-> {test.withDrawnFaceUpCard(8);});
    }

    @Test
    void withDeckRecreatedFromDiscardsThrowsException(){
        SortedBag.Builder<Card> bag = new SortedBag.Builder<>();
        bag.add(Card.LOCOMOTIVE);
        bag.add(Card.ORANGE);
        bag.add(Card.RED);
        bag.add(Card.GREEN);
        bag.add(Card.WHITE);
        bag.add(Card.BLACK);
        bag.add(Card.BLUE);
        Deck deck = Deck.of(bag.build(), RANDOM);
        CardState test = CardState.of(deck);
        test.withMoreDiscardedCards(bag.build());
        assertThrows(IllegalArgumentException.class, ()-> {test.withDeckRecreatedFromDiscards(RANDOM);});
    }

    @Test
    void withDeckRecreatedFromDiscardsTrivial(){
        SortedBag.Builder<Card> bag = new SortedBag.Builder<>();
        bag.add(Card.LOCOMOTIVE);
        bag.add(Card.ORANGE);
        bag.add(Card.RED);
        bag.add(Card.GREEN);
        bag.add(Card.WHITE);
        Deck deck = Deck.of(bag.build(), RANDOM);
        CardState test = CardState.of(deck);
        assertEquals(0, test.deckSize());
        CardState test2  = test.withMoreDiscardedCards(bag.build());
        assertEquals(5, test2.discardsSize());
        CardState test3 = test2.withDeckRecreatedFromDiscards(RANDOM);
        assertEquals(5, test3.deckSize());
    }

    @Test
    void topDeckCardTest(){
        SortedBag.Builder<Card> bag = new SortedBag.Builder<>();
        bag.add(Card.LOCOMOTIVE);
        bag.add(Card.ORANGE);
        bag.add(Card.RED);
        bag.add(Card.GREEN);
        bag.add(Card.WHITE);
        bag.add(Card.BLACK);
        bag.add(Card.BLUE);
        Deck deck = Deck.of(bag.build(), RANDOM);
        CardState test = CardState.of(deck);
        assertEquals(Card.WHITE, test.topDeckCard());

    }

    @Test
    void topDeckCardThrowsException(){
        SortedBag.Builder<Card> bag = new SortedBag.Builder<>();
        bag.add(Card.LOCOMOTIVE);
        bag.add(Card.ORANGE);
        bag.add(Card.RED);
        bag.add(Card.GREEN);
        bag.add(Card.WHITE);
        Deck deck = Deck.of(bag.build(), RANDOM);
        CardState test = CardState.of(deck);
        assertThrows(IllegalArgumentException.class, ()-> {test.topDeckCard();});
    }


    @Test
    void withoutTopDeckCardThrowsException(){
        SortedBag.Builder<Card> bag = new SortedBag.Builder<>();
        bag.add(Card.LOCOMOTIVE);
        bag.add(Card.ORANGE);
        bag.add(Card.RED);
        bag.add(Card.GREEN);
        bag.add(Card.WHITE);
        Deck deck = Deck.of(bag.build(), RANDOM);
        CardState test = CardState.of(deck);
        assertThrows(IllegalArgumentException.class, ()-> {test.withoutTopDeckCard();});
    }
}
