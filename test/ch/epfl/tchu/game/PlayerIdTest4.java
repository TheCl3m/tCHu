package ch.epfl.tchu.game;


import org.junit.jupiter.api.Test;
import java.util.List;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerIdTest4 {

    @Test
    void testingNext() {
        PlayerId player1 = PLAYER_1;
        PlayerId player2 = PLAYER_2;

        assertEquals(PLAYER_2, player1.next());
        assertEquals(PLAYER_1, player2.next());
    }

    @Test
    void testingALL(){
        List<PlayerId> ayre = List.of(PLAYER_1,PLAYER_2);

        assertEquals(ayre,PlayerId.ALL);
    }

    @Test
    void testingCount(){
        assertEquals(2,PlayerId.COUNT);
    }
}