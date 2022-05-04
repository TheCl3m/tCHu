package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.SoundID;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private static final class TestPlayer implements Player {
        private static final int TURN_LIMIT = 1000;

        private int nbInfosReceived = 0;
        private int nbUpdateState = 0;
        private boolean initCalled = false;
        private int discardedTickets = 0;

        private final Random rng;
        // Toutes les routes de la carte
        private final List<Route> allRoutes;

        private int turnCounter;
        private PlayerState ownState;
        private PublicGameState gameState;
        private PlayerId ownId;
        private Map<PlayerId, String> playerNames;

        //For the initial ticket's choice
        private SortedBag<Ticket> initialTicketChoice;

        // Lorsque nextTurn retourne CLAIM_ROUTE
        private Route routeToClaim;
        private SortedBag<Card> initialClaimCards;

        public static PlayerState playerOwnState(TestPlayer player){
            return player.ownState;
        }

        public TestPlayer(long randomSeed, List<Route> allRoutes) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
        }


        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
            this.ownId = ownId;
            this.playerNames = playerNames;
            initCalled = true;
        }

        @Override
        public void receiveInfo(String info) {
            System.out.println(info);
            ++ nbInfosReceived;
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            this.gameState = newState;
            this.ownState = ownState;
            ++nbUpdateState;
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            initialTicketChoice = tickets;
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            int numberOfTicketsToChoose = rng.nextInt(3) + 3;
            SortedBag.Builder<Ticket> chosenTickets = new SortedBag.Builder<>();
            for (int i = 0; i < numberOfTicketsToChoose; i++) {
                Ticket chosen = initialTicketChoice.get(rng.nextInt(initialTicketChoice.size()));
                chosenTickets.add(chosen);
                initialTicketChoice = initialTicketChoice.difference(SortedBag.of(chosen));
            }
            discardedTickets += initialTicketChoice.size();
            return chosenTickets.build();
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            int numberOfTicketsToChoose = rng.nextInt(3) + 1;
            SortedBag.Builder<Ticket> chosenTickets = new SortedBag.Builder<>();
            for (int i = 0; i < numberOfTicketsToChoose; i++) {
                Ticket chosen = options.get(rng.nextInt(options.size()));
                chosenTickets.add(chosen);
                options = options.difference(SortedBag.of(chosen));
            }
            discardedTickets += options.size();
            return chosenTickets.build();
        }

        @Override
        public TurnKind nextTurn() {
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");

            // Détermine les routes dont ce joueur peut s'emparer
            List<Route> claimableRoutes = new ArrayList<>();
            for (Route r: allRoutes) {
                if (ownState.canClaimRoute(r) && !gameState.claimedRoutes().contains(r))
                    claimableRoutes.add(r);
            }

            if (claimableRoutes.isEmpty()) {
//               if(gameState.canDrawTickets())
//                return TurnKind.ALL.get(rng.nextInt(2));
//                else
                if(gameState.cardState().deckSize() + gameState.cardState().discardsSize() >= 6)
                   return TurnKind.DRAW_CARDS;
                else return TurnKind.DRAW_TICKETS;
            } else {
                int routeIndex = rng.nextInt(claimableRoutes.size());
                Route route = claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                initialClaimCards = cards.get(0);
                return TurnKind.CLAIM_ROUTE;
             //   return TurnKind.ALL.get(rng.nextInt(3));
                }


        }



        @Override
        public int drawSlot() {
            return rng.nextInt(6) - 1;
        }

        @Override
        public Route claimedRoute() {
            return routeToClaim;
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            return initialClaimCards;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            int chosen = rng.nextInt(options.size());
            return options.get(chosen);
        }

        @Override
        public void playSound(SoundID sound) {

        }

        @Override
        public void gameEnded(String message) {

        }

        @Override
        public void receiveChat(String message) {

        }

        @Override
        public String sendChat() {
            return null;
        }
    }
    public static final Random NON_RANDOM = new Random() {
        @Override
        public int nextInt(int i) {
            return i-1;
        }
    };

    public static Random RANDOM = new Random(100000);


    @Test
    public void testPlay(){
        TestPlayer Peter = new TestPlayer(50, ChMap.routes());
        TestPlayer Ivo = new TestPlayer(50, ChMap.routes());

        Map<PlayerId, Player> players = new EnumMap<PlayerId, Player>(PlayerId.class);
        players.put(PlayerId.PLAYER_1, Peter);
        players.put(PlayerId.PLAYER_2, Ivo);


        Map<PlayerId, String> playerNames = new EnumMap<PlayerId, String>(PlayerId.class);
        playerNames.put(PlayerId.PLAYER_1, "Peter");
        playerNames.put(PlayerId.PLAYER_2, "Ivo");

        Game.play(players,playerNames,SortedBag.of(ChMap.tickets()),RANDOM);
        assertTrue(Peter.nbInfosReceived > 5 + 2* Peter.turnCounter);
        assertTrue(Ivo.nbInfosReceived > 5 + 2* Ivo.turnCounter);

        assertTrue(Peter.nbUpdateState > 2 + Peter.turnCounter);
        assertTrue(Ivo.nbUpdateState > 2 + Ivo.turnCounter);

        /*System.out.println(Peter.ownState.cards().size());
        System.out.println(Ivo.ownState.cards().size());
        System.out.println(Ivo.gameState.cardState().discardsSize());
        System.out.println(Peter.gameState.cardState().deckSize());
        System.out.println(Peter.gameState.cardState().faceUpCards().size());*/




        int tickets = Peter.ownState.tickets().size() + Ivo.ownState.tickets().size() + Peter.gameState.ticketsCount()
                + Peter.discardedTickets + Ivo.discardedTickets;
        assertEquals(ChMap.tickets().size(), tickets);

        assertTrue(Peter.initCalled);
        assertTrue(Ivo.initCalled);

        assertEquals(Peter.gameState.cardState().deckSize(),Ivo.gameState.cardState().deckSize());
        assertEquals(Peter.gameState.cardState().faceUpCards(),Ivo.gameState.cardState().faceUpCards());
        assertEquals(Peter.gameState.cardState().discardsSize(),Ivo.gameState.cardState().discardsSize());
        assertEquals(Peter.gameState.ticketsCount(),Ivo.gameState.ticketsCount());


    }


   /* @Test
    public void testPlayThrows(){
        assertThrows(IllegalArgumentException.class, () -> {
            //Game.play();
        });
    }*/

  
}
