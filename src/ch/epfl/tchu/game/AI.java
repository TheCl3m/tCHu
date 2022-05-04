package ch.epfl.tchu.game;

import ch.epfl.tchu.gui.SoundID;

import java.util.Map;

public interface AI extends Player {


    @Override
    default void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        //does nothing
    }

    @Override
    default void receiveInfo(String info) {
        //does nothing
    }

    @Override
    default void playSound(SoundID sound) {
        //does nothing
    }

    @Override
    default void gameEnded(String message) {
        //does nothing
    }

    @Override
    default String sendChat() {
        return null;
    }

    @Override
    default void receiveChat(String message) {
        //do nothing
    }

}
