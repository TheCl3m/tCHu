package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * a deck is a pile of cards, used to draw cards in the game
 * it has a size and is constituted of a list of cards
 *
 * @author Mathieu Faure (328086)
 * @author Clément Husler (328105)
 */
public final class Deck<C extends Comparable<C>> {

    private final int size;
    private final List<C> cardList;

    /**
     * private constructor of an instance of deck used in the method "of"
     *
     * @param list, the shuffled list of the cards sent to the method "of"
     */
    private Deck(List<C> list) {
        size = list.size();
        cardList = list;
    }

    /**
     * method used to create a Deck
     *
     * @param cards, a SortedBag containing the cards that will constitute the deck
     * @param rng,   used to shuffle the cards
     * @return the Deck that has been created
     */
    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng) {
        List<C> list = cards.toList();
        Collections.shuffle(list, rng);
        return new Deck<>(list);
    }

    /**
     * @return the size of the deck
     */
    public int size() {
        return this.size;
    }

    /**
     * @return true if the deck is empty, false else
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return the top card of the deck
     * @throws IllegalArgumentException if the size of the deck is 0
     */
    public C topCard() {
        Preconditions.checkArgument(size != 0);
        return cardList.get(0);
    }

    /**
     * @return a copy of the deck without its top card
     * @throws IllegalArgumentException if the size of the deck is 0
     */
    public Deck<C> withoutTopCard() {
        Preconditions.checkArgument(size != 0);
        return new Deck<>(cardList.subList(1, size));
    }

    /**
     * @return a SortedBag containing the count top cards of the deck
     * @throws IllegalArgumentException if count is negative or superior to the deck's size
     */
    public SortedBag<C> topCards(int count) {
        Preconditions.checkArgument(count >= 0 && count <= size);
        SortedBag.Builder<C> temp = new SortedBag.Builder<C>();
        for (int i = 0; i < count; ++i) {
            temp.add(cardList.get(i));
        }
        return temp.build();
    }

    /**
     * @return a new Deck, copy of the first one but without the count top cards
     * @throws IllegalArgumentException if count is negative or superior to the deck's size
     */
    public Deck<C> withoutTopCards(int count) {
        Preconditions.checkArgument(count >= 0 && count <= size);
        return new Deck<>(cardList.subList(count, size));
    }


}
