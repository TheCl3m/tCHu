package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * the PublicCardState is a representation of all the cards of the game that are not in the players' hands
 * it only contains the public part of this representation, that the players can access,
 * such as the deck's and discard's size, not what they contain, and the face up cards
 *
 * @author Mathieu Faure (328086)
 * @author Clement HUSLER (328105)
 */
public class PublicCardState {

    private final int deckSize;
    private final int discardsSize;
    private final List<Card> faceUpCards;

    /**
     * public constructor of the PublicCardState class
     *
     * @param faceUpCards  the cards that are face up in the game, known by the players
     * @param deckSize     the current size of the deck
     * @param discardsSize the current size of the discard
     * @throws IllegalArgumentException if the size of faceUpCards is not 5, or discard or deck is empty
     */
    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize) {
        Preconditions.checkArgument(faceUpCards.size() == Constants.FACE_UP_CARDS_COUNT && discardsSize >= 0 && deckSize >= 0);
        this.deckSize = deckSize;
        this.faceUpCards = List.copyOf(faceUpCards);
        this.discardsSize = discardsSize;
    }

    /**
     * @return the face up cards as a list
     */
    public List<Card> faceUpCards() {
        return List.copyOf(faceUpCards);
    }

    /**
     * @param slot the slot of the card we want to get
     * @return the face up card at the given slot
     * @throws IndexOutOfBoundsException if the given slot is not between 0 and 4 included
     */
    public Card faceUpCard(int slot) {
        return faceUpCards.get(Objects.checkIndex(slot, faceUpCards.size()));
    }

    /**
     * @return the current size of the deck
     */
    public int deckSize() {
        return deckSize;
    }

    /**
     * @return true if the deck is empty, false else
     */
    public boolean isDeckEmpty() {
        return deckSize == 0;
    }

    /**
     * @return the current size of the discard
     */
    public int discardsSize() {
        return discardsSize;
    }
}
