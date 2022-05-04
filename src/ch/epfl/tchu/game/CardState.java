package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * the CardState is a representation of all the cards of the game that are not in the players' hands
 * and can't be known by the player
 * it extends PublicCardState, the information about these cards that the players can have
 *
 * @author Mathieu Faure (328086)
 * @author Clement HUSLER (328105)
 */
public final class CardState extends PublicCardState {

    private final Deck<Card> deck;
    private final SortedBag<Card> discards;

    /**
     * private constructor for the CardState class used in the "of" method
     * uses the super constructor
     *
     * @param faceUpCards the five cards that are currently face up
     * @param deck        the current Deck
     * @param discards    the current discards
     */
    private CardState(List<Card> faceUpCards, Deck<Card> deck, SortedBag<Card> discards) {
        super(faceUpCards, deck.size(), discards.size());
        this.deck = deck;
        this.discards = discards;
    }

    /**
     * public method used to create an instance of CardState by taking a deck in parameter and using the public constructor
     *
     * @param deck the deck used to create the CardState
     * @return a new CardState
     */
    public static CardState of(Deck<Card> deck) {
        Preconditions.checkArgument(deck.size() >= Constants.FACE_UP_CARDS_COUNT);
        return new CardState(deck.topCards(Constants.FACE_UP_CARDS_COUNT).toList(), deck.withoutTopCards(Constants.FACE_UP_CARDS_COUNT), SortedBag.of());
    }

    /**
     * switches the face up card of index slot with the top card of the deck
     *
     * @param slot the slot of the face up card that will be switched
     * @return a new CardState with this change in the faceUpCards
     * @throws IndexOutOfBoundsException if the slot is not between 0 and 4
     */
    public CardState withDrawnFaceUpCard(int slot) {
        Objects.checkIndex(slot, Constants.FACE_UP_CARDS_COUNT);
        List<Card> temp = new ArrayList<Card>();
        List<Card> current = this.faceUpCards();
        for (int index : Constants.FACE_UP_CARD_SLOTS) {
            if (index == slot) {
                temp.add(deck.topCard());
            } else {
                temp.add(current.get(index));
            }
        }
        return new CardState(temp, deck.withoutTopCard(), discards);
    }

    /**
     * @return the top card of the current deck as a Card
     * @throws IllegalArgumentException if the deckSize is negative
     */
    public Card topDeckCard() {
        Preconditions.checkArgument(!isDeckEmpty());
        return deck.topCard();

    }

    /**
     * @return a new CardState with a new deck without its top card
     * @throws IllegalArgumentException if the deckSize is negative
     */
    public CardState withoutTopDeckCard() {
        Preconditions.checkArgument(!isDeckEmpty());
        return new CardState(faceUpCards(), deck.withoutTopCard(), discards);

    }

    /**
     * Creates a new CardState when the deck has no more cards, using the discard and shuffling it to create a new deck
     *
     * @param rng used to shuffle the discard
     * @return the new CardState
     * @throws IllegalArgumentException if the deck is not empty
     */
    public CardState withDeckRecreatedFromDiscards(Random rng) {
        Preconditions.checkArgument(isDeckEmpty());
        Deck<Card> newDeck = Deck.of(discards, rng);
        return new CardState(faceUpCards(), newDeck, SortedBag.of());
    }

    /**
     * create a new CardState identical to "this", but adding the cards in parameter to the discard
     *
     * @param additionalDiscards the cards to add to the discard stocked in a SortedBag
     * @return the new CardState as previously described
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards) {
        SortedBag<Card> union = discards.union(additionalDiscards);
        return new CardState(faceUpCards(), deck, union);
    }
}
