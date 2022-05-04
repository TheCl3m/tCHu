package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerIdTest {

    @Test
    void nextPlayer1() {
        PlayerId playerId = PlayerId.PLAYER_1;
        assertEquals(PlayerId.PLAYER_2, playerId.next());
    }

    @Test
    void nextPlayer2() {
        PlayerId playerId = PlayerId.PLAYER_2;
        assertEquals(PlayerId.PLAYER_1, playerId.next());
    }

    @Test
    void ALL() {
        List<PlayerId> list = List.of(PlayerId.PLAYER_1, PlayerId.PLAYER_2);
        assertEquals(list,  PlayerId.ALL);
    }

    @Test
    void count() {
        assertEquals(2, PlayerId.COUNT);
    }
}