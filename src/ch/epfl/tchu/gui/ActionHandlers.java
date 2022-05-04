package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/**
 * The interface Action handlers, five functional interfaces representing different action handlers
 */
public interface ActionHandlers {

    /**
     * The handler used to draw tickets
     */
    @FunctionalInterface
    interface DrawTicketsHandler {
        /**
         * On draw tickets.
         */
        void onDrawTickets();
    }

    /**
     * The handler used to draw the card from a given slot
     */
    @FunctionalInterface
    interface DrawCardHandler {
        /**
         * On draw card.
         *
         * @param slot the slot
         */
        void onDrawCard(int slot);
    }

    /**
     * The handler used to claim a route
     */
    @FunctionalInterface
    interface ClaimRouteHandler {
        /**
         * On claim route.
         *
         * @param route             the route
         * @param initialClaimCards the initial claim cards
         */
        void onClaimRoute(Route route, SortedBag<Card> initialClaimCards);
    }

    /**
     * The handler used to choose tickets
     */
    @FunctionalInterface
    interface ChooseTicketsHandler {
        /**
         * On choose tickets.
         *
         * @param chosenTickets the chosen tickets
         */
        void onChooseTickets(SortedBag<Ticket> chosenTickets);
    }

    /**
     * The handler used to choose the initial or additional cards to claim a route
     */
    @FunctionalInterface
    interface ChooseCardsHandler {
        /**
         * On choose cards.
         *
         * @param cards the chosen cards, empty if the player renounces to claim the route
         */
        void onChooseCards(SortedBag<Card> cards);
    }

    @FunctionalInterface
    interface chatHandler {
        void onReaction(String reaction);
    }
}
