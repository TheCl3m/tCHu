package ch.epfl.tchu.gui;

import java.util.List;

public enum SoundID {

    DRAW_TICKET("draw-card.mp3"),
    DRAW_CARD("draw-card.mp3"),
    ROUTE_SUCCESS("route-success.mp3"),
    ROUTE_FAIL("route-fail.mp3"),
    YOUR_TURN("your-turn.mp3"),
    GAME_WON("win.mp3"),
    GAME_LOST("loss.mp3"),
    MAIN_MENU("main.mp3"),
    NEW_CHAT("blop.mp3");

    private final String filename;

    SoundID(String filename) {
        this.filename = "/sounds/" + filename;
    }

    public String filename() {
        return filename;
    }

    /**
     * The list of all card kinds
     */
    public static final List<SoundID> ALL = List.of(SoundID.values());

}
