package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

/**
 * The Info class allows to build the strings that will inform a player of the state of the game
 *
 * @author Clement HUSLER (328105)
 * @author Mathieu Faure (328086)
 */
public final class Info {

    private final String playerName;

    /**
     * Instantiates a new Info.
     *
     * @param playerName the player's name
     */
    public Info(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Returns the card french name relative to it's count
     *
     * @param card  the card the french name will be given from
     * @param count the count of the card
     * @return the french name of the card (plural if count>1)
     */
    public static String cardName(Card card, int count) {
        String name = "";
        switch (card) {
            case BLACK:
                name = StringsFr.BLACK_CARD;
                break;
            case VIOLET:
                name = StringsFr.VIOLET_CARD;
                break;
            case BLUE:
                name = StringsFr.BLUE_CARD;
                break;
            case GREEN:
                name = StringsFr.GREEN_CARD;
                break;
            case YELLOW:
                name = StringsFr.YELLOW_CARD;
                break;
            case ORANGE:
                name = StringsFr.ORANGE_CARD;
                break;
            case RED:
                name = StringsFr.RED_CARD;
                break;
            case WHITE:
                name = StringsFr.WHITE_CARD;
                break;
            case LOCOMOTIVE:
                name = StringsFr.LOCOMOTIVE_CARD;
        }
        return name + StringsFr.plural(count);
    }

    /**
     * Draw string.
     *
     * @param points the players' points
     * @return the string containing the players' names and their respective points counts
     */
    public String draw(int points) {
        return String.format(StringsFr.DRAW, points);
    }

    /**
     * @return the string that tells which player will play first
     */
    public String willPlayFirst() {
        return String.format(StringsFr.WILL_PLAY_FIRST, playerName);
    }

    /**
     * @param count the amount of tickets the player kept
     * @return the string containing the name of the players and the amount of tickets he decided to keep
     */
    public String keptTickets(int count) {
        return String.format(StringsFr.KEPT_N_TICKETS, playerName, count, StringsFr.plural(count));
    }

    /**
     * @return the string telling who's turn it is to play
     */
    public String canPlay() {
        return String.format(StringsFr.CAN_PLAY, playerName);
    }

    /**
     * @param count the count of tickets that were drawn
     * @return the string indicating the number of tickets a player drew
     */
    public String drewTickets(int count) {
        return String.format(StringsFr.DREW_TICKETS, playerName, count, StringsFr.plural(count));

    }

    /**
     * @return the string telling who decided to draw a card from the deck
     */
    public String drewBlindCard() {
        return String.format(StringsFr.DREW_BLIND_CARD, playerName);
    }

    /**
     * @param card the card that was drawn
     * @return the string indicating that the player drew the visible card
     */
    public String drewVisibleCard(Card card) {
        return String.format(StringsFr.DREW_VISIBLE_CARD, playerName, cardName(card, 1));
    }

    /**
     * @param route the route that was claimed
     * @param cards the cards used to claim the route
     * @return the string informing that the player claimed a route with the given cards
     */
    public String claimedRoute(Route route, SortedBag<Card> cards) {
        return String.format(StringsFr.CLAIMED_ROUTE, playerName, routeToString(route), cardsToString(cards));
    }

    /**
     * @param route        the route the player is willing to claim
     * @param initialCards the initial cards he uses to claim it
     * @return the string telling that the player tries to take the given underground route using the given initial cards
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {
        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, playerName, routeToString(route), cardsToString(initialCards));
    }

    /**
     * @param drawnCards     the drawn cards
     * @param additionalCost the additional cost that the drawn cards imply
     * @return the string informing what the drawn cards are and if the player has to play more cards
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {
        String additionalCostOrNot;
        if (additionalCost != 0) {
            additionalCostOrNot = String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost));
        } else {
            additionalCostOrNot = StringsFr.NO_ADDITIONAL_COST;
        }
        return String.format(StringsFr.ADDITIONAL_CARDS_ARE, cardsToString(drawnCards)) + additionalCostOrNot;
    }

    /**
     * @param route the route the player refused to or could not claim
     * @return the string telling that the player did not claim the underground route
     */
    public String didNotClaimRoute(Route route) {
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, playerName, routeToString(route));
    }

    /**
     * @param carCount the car count of the player reaching a carCount<=2
     * @return the string informing that the player has only carCount remaining, and that the last turn begins
     */
    public String lastTurnBegins(int carCount) {
        return String.format(StringsFr.LAST_TURN_BEGINS, playerName, carCount, StringsFr.plural(carCount));
    }

    /**
     * @param longestTrail the longest trail
     * @return the string telling that the player gets the 10 points bonus for the longest trail and the name of this longest trail
     */
    public String getsLongestTrailBonus(Trail longestTrail) {
        return String.format(StringsFr.GETS_BONUS, playerName, longestTrail.station1() + StringsFr.EN_DASH_SEPARATOR + longestTrail.station2());
    }

    /**
     * @param points      the amount of points of the winner
     * @param loserPoints the loser points
     * @return the string informing which player won and showing both players total points
     */
    public String won(int points, int loserPoints) {
        return String.format(StringsFr.WINS, points, StringsFr.plural(points), loserPoints, StringsFr.plural(loserPoints));
    }

    public String lose(int points, int winnerPoints) {
        return String.format(StringsFr.LOSES, points, StringsFr.plural(points), winnerPoints, StringsFr.plural(winnerPoints));
    }

    /**
     * @param route the route to convert to a string
     * @return the string that contains the two stations of the given route separated by a dash separator
     */
    private static String routeToString(Route route) {
        return route.station1() + StringsFr.EN_DASH_SEPARATOR + route.station2();
    }

    /**
     * @param cards the bag of cards
     * @return the string representation of the cards bag
     */
    public static String cardsToString(SortedBag<Card> cards) {
        StringBuilder out = new StringBuilder();
        int count = 0;
        for (Card card : cards.toSet()) {
            ++count;
            int n = cards.countOf(card);
            out.append(n);
            out.append(" ");
            out.append(Info.cardName(card, n));
            if (count == cards.toSet().size() - 1) {
                out.append(StringsFr.AND_SEPARATOR);
            } else if (count != cards.toSet().size()) {
                out.append(", ");
            }
        }
        return out.toString();
    }
}
