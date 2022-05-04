package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

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
    void sizeTrivial() {
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        Deck deck = Deck.of(bag.build(), RANDOM);
        assertEquals(0, deck.size());
    }

    @Test
    void sizeNonTrivial() {
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        int expected = 3;
        for (int i = 0; i < expected; i++) {
            bag.add(Card.BLUE);
        }
        Deck deck = Deck.of(bag.build(), RANDOM);
        assertEquals(expected, deck.size());
    }

    @Test
    void isEmptyEmptyCase() {
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        Deck deck = Deck.of(bag.build(), RANDOM);
        assertTrue(deck.isEmpty());
    }

    @Test
    void isEmptyNotEmptyCase() {
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(Card.BLUE);
        bag.add(Card.BLUE);
        Deck deck = Deck.of(bag.build(), RANDOM);
        assertFalse(deck.isEmpty());
    }

    @Test
    void TopCardEmptyDeck(){
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        Deck deck = Deck.of(bag.build(), RANDOM);
        assertThrows(IllegalArgumentException.class, ()-> {deck.topCard();});
    }

    @Test
    void topCardOneCardCase() {
        Card expected = Card.BLUE;
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(expected);
        Deck deck = Deck.of(bag.build(), RANDOM);
        assertEquals(expected, deck.topCard());
    }

    @Test
    void withoutTopCardSize() {
        Card expected = Card.BLUE;
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(expected);
        Deck deck = Deck.of(bag.build(), RANDOM);
        assertEquals(0, deck.withoutTopCard().size());
    }

    @Test
    void withoutTopCard() {
        boolean b = true;
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(Card.BLUE);
        bag.add(Card.LOCOMOTIVE);
        bag.add(Card.ORANGE);
        bag.add(Card.RED);
        bag.add(Card.GREEN);
        bag.add(Card.BLACK);
        bag.add(Card.VIOLET);
        bag.add(Card.WHITE);
        Deck<Card> deck = Deck.of(bag.build(), RANDOM);
        SortedBag<Card> copy = deck.topCards(deck.size()); //the topCards method returns the cards in the order of the enumeration
        for(Card c : copy) {
            System.out.println(c);
        }
        Card lookingFor = deck.topCard();
        Deck deck1 = deck.withoutTopCard();
        System.out.println("--------");
        SortedBag<Card> copy2 = deck1.topCards(deck1.size());
        for(Card c : copy2){
            if (c == lookingFor){
                b = false;
            }
        }
        assertTrue(b);
    }

    @Test
    void topCardsTrivial() {
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(Card.BLUE);
        bag.add(Card.LOCOMOTIVE);
        bag.add(Card.ORANGE);
        bag.add(Card.RED);
        bag.add(Card.GREEN);
        bag.add(Card.BLACK);
        bag.add(Card.VIOLET);
        bag.add(Card.WHITE);
        Deck<Card> deck = Deck.of(bag.build(), new Random());
        SortedBag<Card> topCards = deck.topCards(1);
        Card topCard = deck.topCard();
        boolean b = true;
        if (topCards.get(0) != topCard){
            b = false;
        }
        assertTrue(b);
    }

    @Test
    void topCardsNonTrivial() {
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(Card.BLUE);
        bag.add(Card.LOCOMOTIVE);
        bag.add(Card.ORANGE);
        bag.add(Card.RED);
        bag.add(Card.GREEN);
        bag.add(Card.BLACK);
        bag.add(Card.VIOLET);
        bag.add(Card.WHITE);
        Deck<Card> deck = Deck.of(bag.build(), RANDOM);
        SortedBag<Card> topCards = deck.topCards(3);
        for(Card c : topCards){
            System.out.println(c);
        }
    }

    @Test
    void topCardsIllegal() {
        SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
        bag.add(Card.BLUE);
        bag.add(Card.LOCOMOTIVE);
        bag.add(Card.ORANGE);
        bag.add(Card.RED);
        bag.add(Card.GREEN);
        bag.add(Card.BLACK);
        bag.add(Card.VIOLET);
        bag.add(Card.WHITE);
        Deck<Card> deck = Deck.of(bag.build(), RANDOM);
        assertThrows(IllegalArgumentException.class, () -> {
            deck.topCards(-5);
        });
    }

        @Test
        void withoutTopCards() {
        boolean b = true;
            int cardsToRemove = 3;
            int remainingCards = 8 - cardsToRemove;
            SortedBag.Builder<Card> bag = new SortedBag.Builder<Card>();
            bag.add(Card.BLUE);
            bag.add(Card.LOCOMOTIVE);
            bag.add(Card.ORANGE);
            bag.add(Card.RED);
            bag.add(Card.GREEN);
            bag.add(Card.BLACK);
            bag.add(Card.VIOLET);
            bag.add(Card.WHITE);
            Deck<Card> deck = Deck.of(bag.build(), RANDOM);
            SortedBag<Card> topCards = deck.topCards(cardsToRemove);
            for (Card card :topCards) {
                System.out.println(card);
            }
            System.out.println("---------------------");
            Deck<Card> newDeck = deck.withoutTopCards(cardsToRemove);
            SortedBag<Card> remaining = newDeck.topCards(remainingCards);
            for (Card card :remaining) {
                System.out.println(card);
            }
            /*for (int i = 0; i < remainingCards; ++i) {
                for (int j = 0; i < cardsToRemove; ++i) {
                    if (topCards.get(j) == remaining.get(i)) {
                        b =false;
                    }
                }
            }
            assertTrue(b);*/
        }
}