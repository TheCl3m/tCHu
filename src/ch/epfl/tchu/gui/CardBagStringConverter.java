package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import javafx.util.StringConverter;

/**
 * This class allows to convert a SortedBag of Cards to a String representation
 */
public final class CardBagStringConverter extends StringConverter<SortedBag<Card>> {
    /**
     * Converts a SortedBag of Cards to its representation as a String
     *
     * @param bag the SortedBag to convert
     * @return the String that has been created
     */
    @Override
    public String toString(SortedBag<Card> bag) {
        return Info.cardsToString(bag);
    }

    /**
     * This method is never called
     *
     * @param string the string the sortedBag could be regenerated from
     * @return an UnsupportedOperationException
     * @throws UnsupportedOperationException when called
     */
    @Override
    public SortedBag<Card> fromString(String string) {
        throw new UnsupportedOperationException();
    }
}
