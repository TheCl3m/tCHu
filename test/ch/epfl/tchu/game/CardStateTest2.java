package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ranges.Range;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class CardStateTest2 {

    Random rng = new Random();

    @Test
    void ofFailsWithDeckContainingLessThan5Cards() {
        Deck<Card> packet = Deck.<Card>of(SortedBag.of(2, Card.BLUE , 2,  Card.GREEN ) , rng);

        assertThrows(IllegalArgumentException.class, () -> {
            CardState.of(packet);
        });
    }

}
