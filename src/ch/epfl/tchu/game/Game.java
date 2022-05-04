package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;
import ch.epfl.tchu.gui.SoundID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * public class ruling the whole game and its different steps
 *
 * @author Clement HUSLER (328105)
 * @author Mathieu FAURE (328086)
 */
public final class Game {
    /**
     * private constructor to avoid the class of being instantiated
     */


    private Game() {
    }


    /**
     * @param players     the map associating a player with his Id
     * @param playerNames the map associating the player Ids with the players' names
     * @param tickets     the bag containing all the tickets of the game
     * @param rng         a Random used to shuffle the tickets and cards and choose the player who starts via the GameState class
     *                    and it's "initial" method
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng) {
        Preconditions.checkArgument(players.size() == PlayerId.COUNT && playerNames.size() == PlayerId.COUNT); //checks that there are effectively two players in the game


        //--------------------------- Initialisation of the game ---------------------------

        players.forEach((id, player) -> player.initPlayers(id, playerNames));
        Map<Player, Info> playersInfo = new HashMap<>();
        playerNames.forEach((id, name) -> playersInfo.put(players.get(id), new Info(name)));
        GameState gameState = GameState.initial(tickets, rng);
        updateStateBothPlayers(gameState, players);
        receiveInfoBothPlayers(playersInfo.get(players.get(gameState.currentPlayerId())).willPlayFirst(), players);

        //ticket distribution to the players
        for (PlayerId pId : PlayerId.ALL) {
            players.get(pId).setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        }
        for (PlayerId pId : PlayerId.ALL) {
            SortedBag<Ticket> bag = players.get(pId).chooseInitialTickets();
            players.get(pId).playSound(SoundID.DRAW_TICKET);
            gameState = gameState.withInitiallyChosenTickets(pId, bag);
        }
        updateStateBothPlayers(gameState, players);
        for (PlayerId pId : PlayerId.ALL) {
            receiveInfoBothPlayers(playersInfo.get(players.get(pId)).keptTickets(gameState.playerState(pId).ticketCount()), players);
        }

        //--------------------------- Start of the game ---------------------------
        boolean isFinished = false;
        boolean lastTurnBegun = false;
        boolean claimedRouteNoCondition;
        boolean didNotClaimRouteWithCondition;

        while (!isFinished) { // condition used to stop the game once the last turn is over
            Player player = players.get(gameState.currentPlayerId());
            player.playSound(SoundID.YOUR_TURN);
            if (lastTurnBegun && players.get(gameState.lastPlayer()) == player) {
                isFinished = true;
            }
            Info info = playersInfo.get(player);

            updateStateBothPlayers(gameState, players);
            receiveInfoBothPlayers(info.canPlay(), players);
            Player.TurnKind turnKind = player.nextTurn();
            switch (turnKind) {
                //case where the current player chose to draw tickets
                case DRAW_TICKETS:
                    receiveInfoBothPlayers(info.drewTickets(Constants.IN_GAME_TICKETS_COUNT), players);
                    SortedBag<Ticket> drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                    SortedBag<Ticket> chosenTickets = player.chooseTickets(drawnTickets);
                    player.playSound(SoundID.DRAW_TICKET);
                    gameState = gameState.withChosenAdditionalTickets(drawnTickets, chosenTickets);
                    receiveInfoBothPlayers(info.keptTickets(chosenTickets.size()), players);
                    break;
                //case where the player chose to draw two cards, either that are face up or the first one of the pile
                case DRAW_CARDS:
                    int firstDraw = player.drawSlot();
                    player.playSound(SoundID.DRAW_CARD);
                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                    gameState = playerDrawsCard(gameState, info, players, firstDraw);
                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                    updateStateBothPlayers(gameState, players);
                    int secondDraw = player.drawSlot();
                    player.playSound(SoundID.DRAW_CARD);
                    gameState = playerDrawsCard(gameState, info, players, secondDraw);
                    break;
                //case where the player chose to claim a route with the cards he owns
                case CLAIM_ROUTE:
                    Route routeToClaim = player.claimedRoute();
                    SortedBag<Card> initialClaimCards = player.initialClaimCards();
                    SortedBag.Builder<Card> drawnCardsBuilder = new SortedBag.Builder<>();
                    claimedRouteNoCondition = true;
                    if (routeToClaim.level() == Route.Level.UNDERGROUND) {
                        receiveInfoBothPlayers(info.attemptsTunnelClaim(routeToClaim, initialClaimCards), players);
                        for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; ++i) {
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                            drawnCardsBuilder.add(gameState.topCard());
                            gameState = gameState.withoutTopCard();
                        }
                        SortedBag<Card> drawnCards = drawnCardsBuilder.build();
                        gameState = gameState.withMoreDiscardedCards(drawnCards);
                        int additionalClaimCardsCount = routeToClaim.additionalClaimCardsCount(initialClaimCards, drawnCards);
                        receiveInfoBothPlayers(info.drewAdditionalCards(drawnCards, additionalClaimCardsCount), players);
                        if (additionalClaimCardsCount > 0) { //case where there is an additional cost to claim the route
                            claimedRouteNoCondition = false;
                            didNotClaimRouteWithCondition = true;
                            List<SortedBag<Card>> possibleAdditionalCards = gameState.currentPlayerState().possibleAdditionalCards(additionalClaimCardsCount, initialClaimCards);

                            if (possibleAdditionalCards.size() > 0) { //case where the player has a combination of cards that can be used to claim the route
                                SortedBag<Card> chosenCards = player.chooseAdditionalCards(possibleAdditionalCards);

                                if (!chosenCards.isEmpty()) { //case where the player chose to give an additional combination of cards to claim the route
                                    SortedBag<Card> allClaimCards = chosenCards.union(initialClaimCards);
                                    player.playSound(SoundID.ROUTE_SUCCESS);
                                    gameState = gameState.withClaimedRoute(routeToClaim, allClaimCards);
                                    didNotClaimRouteWithCondition = false;
                                    receiveInfoBothPlayers(info.claimedRoute(routeToClaim, allClaimCards), players);
                                }
                            }
                            if (didNotClaimRouteWithCondition) {
                                player.playSound(SoundID.ROUTE_FAIL);
                                receiveInfoBothPlayers(info.didNotClaimRoute(routeToClaim), players);
                            }
                        }
                    }
                    if (claimedRouteNoCondition) { //case where the route is either overground or there are no additional cards required to claim it
                        player.playSound(SoundID.ROUTE_SUCCESS);
                        gameState = gameState.withClaimedRoute(routeToClaim, initialClaimCards);
                        receiveInfoBothPlayers(info.claimedRoute(routeToClaim, initialClaimCards), players);
                    }
                    break;
            }
            if (gameState.lastTurnBegins()) { //case where the last turn has to begin
                lastTurnBegun = true;
                receiveInfoBothPlayers(info.lastTurnBegins(gameState.currentPlayerState().carCount()), players);
            }
            String reaction = player.sendChat();
            if ((reaction != null) && !reaction.equals("NO_REACTION")) {
                receiveReactionBothPlayers(reaction, players);
                playSoundBothPlayers(SoundID.NEW_CHAT, players);
            }
            gameState = gameState.forNextTurn();
        }

        //--------------------------- End of the game ---------------------------

        updateStateBothPlayers(gameState, players);
        Trail longestTrailPlayer1 = Trail.longest(gameState.playerState(PlayerId.PLAYER_1).routes());
        Trail longestTrailPlayer2 = Trail.longest(gameState.playerState(PlayerId.PLAYER_2).routes());
        int player1Points = gameState.playerState(PlayerId.PLAYER_1).finalPoints();
        int player2Points = gameState.playerState(PlayerId.PLAYER_2).finalPoints();

        String longestTrail = "";
        //checks who deserves the longest trail bonus
        if (longestTrailPlayer1.length() > longestTrailPlayer2.length()) {
            longestTrail = playersInfo.get(players.get(PlayerId.PLAYER_1)).getsLongestTrailBonus(longestTrailPlayer1);
            receiveInfoBothPlayers(longestTrail, players);
            player1Points += Constants.LONGEST_TRAIL_BONUS_POINTS;
        } else if (longestTrailPlayer1.length() < longestTrailPlayer2.length()) {
            longestTrail = playersInfo.get(players.get(PlayerId.PLAYER_2)).getsLongestTrailBonus(longestTrailPlayer2);
            receiveInfoBothPlayers(longestTrail, players);
            player2Points += Constants.LONGEST_TRAIL_BONUS_POINTS;
        } else {
            longestTrail = playersInfo.get(players.get(PlayerId.PLAYER_1)).getsLongestTrailBonus(longestTrailPlayer1) + playersInfo.get(players.get(PlayerId.PLAYER_2)).getsLongestTrailBonus(longestTrailPlayer2);
            receiveInfoBothPlayers(playersInfo.get(players.get(PlayerId.PLAYER_1)).getsLongestTrailBonus(longestTrailPlayer1), players);
            player1Points += Constants.LONGEST_TRAIL_BONUS_POINTS;
            receiveInfoBothPlayers(playersInfo.get(players.get(PlayerId.PLAYER_2)).getsLongestTrailBonus(longestTrailPlayer2), players);
            player2Points += Constants.LONGEST_TRAIL_BONUS_POINTS;
        }

        //checks who is the winner if there is one and sends the appropriate message to both the players
        String messageP1 = "";
        String messageP2 = "";
        Player p1 = players.get(PlayerId.PLAYER_1);
        Player p2 = players.get(PlayerId.PLAYER_2);
        if (player1Points > player2Points) {
            players.get(PlayerId.PLAYER_1).playSound(SoundID.GAME_WON);
            players.get(PlayerId.PLAYER_2).playSound(SoundID.GAME_LOST);
            //receiveInfoBothPlayers(playersInfo.get(players.get(PlayerId.PLAYER_1)).won(player1Points, player2Points), players);
            messageP1 = playersInfo.get(p1).won(player1Points, player2Points);
            messageP2 = playersInfo.get(p2).lose(player2Points, player1Points);
        } else if (player1Points < player2Points) {
            players.get(PlayerId.PLAYER_2).playSound(SoundID.GAME_WON);
            players.get(PlayerId.PLAYER_1).playSound(SoundID.GAME_LOST);
            //receiveInfoBothPlayers(playersInfo.get(players.get(PlayerId.PLAYER_2)).won(player2Points, player1Points), players);
            messageP1 = playersInfo.get(p1).lose(player1Points, player2Points);
            messageP2 = playersInfo.get(p2).won(player2Points, player1Points);
        } else {
            playSoundBothPlayers(SoundID.GAME_WON, players);
            //List<String> playerNamesString = new ArrayList<>();
            //playerNames.forEach((id, name) -> playerNamesString.add(name));
            //receiveInfoBothPlayers(Info.draw(playerNamesString, player1Points), players);
            messageP1 = playersInfo.get(p1).draw(player1Points);
            messageP2 = messageP1;
        }
        p1.gameEnded(messageP1 + longestTrail);
        p2.gameEnded(messageP2 + longestTrail);

    }

    /**
     * sends an info to both the players
     *
     * @param info    the info to send to the players
     * @param players the map of all the players in the game, linked to their ID
     */
    private static void receiveInfoBothPlayers(String info, Map<PlayerId, Player> players) {
        players.forEach((PlayerId playerId, Player player) -> player.receiveInfo(info));
    }

    /**
     * regularly called to update the state of the players during the game
     *
     * @param gameState the current gameState
     * @param players   thr map containing the players, linked to their ID
     */
    private static void updateStateBothPlayers(GameState gameState, Map<PlayerId, Player> players) {
        players.forEach((PlayerId playerId, Player player) -> player.updateState(gameState, gameState.playerState(playerId)));
    }

    private static void playSoundBothPlayers(SoundID sound, Map<PlayerId, Player> players) {
        players.forEach((playerId, player) -> player.playSound(sound));
    }

    private static void receiveReactionBothPlayers(String message, Map<PlayerId, Player> players) {
        players.forEach((playerId, player) -> player.receiveChat(message));
    }

    /**
     * Allows a player to draw a card
     *
     * @param currentGameState the current game state
     * @param playersInfo      the player's Info instance
     * @param players          the map containing both players
     * @param slot             the slot chosen by the player
     * @return the gameState after the player has drawn the card
     */
    private static GameState playerDrawsCard(GameState currentGameState, Info playersInfo, Map<PlayerId, Player> players, int slot) {
        if (slot == Constants.DECK_SLOT) {
            receiveInfoBothPlayers(playersInfo.drewBlindCard(), players);
            return currentGameState.withBlindlyDrawnCard();
        } else {
            Card drawnCard = currentGameState.cardState().faceUpCard(slot);
            receiveInfoBothPlayers(playersInfo.drewVisibleCard(drawnCard), players);
            return currentGameState.withDrawnFaceUpCard(slot);
        }
    }
}
