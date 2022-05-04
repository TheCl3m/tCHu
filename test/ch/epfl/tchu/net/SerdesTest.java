package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.Serdes;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.Card.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerdesTest {

    @Test
    void PublicGameStateEnonce(){
        List<Card> fu = List.of(RED, WHITE, BLUE, BLACK, RED);
        PublicCardState cs = new PublicCardState(fu, 30, 31);
        List<Route> rs1 = ChMap.routes().subList(0, 2);
        Map<PlayerId, PublicPlayerState> ps = Map.of(
                PlayerId.PLAYER_1, new PublicPlayerState(10, 11, rs1),
                PlayerId.PLAYER_2, new PublicPlayerState(20, 21, List.of()));
        PublicGameState gs =
                new PublicGameState(40, cs, PlayerId.PLAYER_2, ps, null);
        assertEquals("40:6,7,2,0,6;30;31:1:10;11;0,1:20;21;:", Serdes.publicGameStateSerde.serialize(gs));
        PublicGameState gs2 = Serdes.publicGameStateSerde.deserialize("40:6,7,2,0,6;30;31:1:10;11;0,1:20;21;:");
    }

    @Test
    void intSerde(){
        assertEquals("2021", Serdes.intSerde.serialize(2021));
        assertEquals(2021, Serdes.intSerde.deserialize("2021"));
    }

    @Test
    void stringSerde(){
        assertEquals("Q2hhcmxlcw==", Serdes.stringSerde.serialize("Charles"));
        assertEquals("Charles", Serdes.stringSerde.deserialize("Q2hhcmxlcw=="));
    }

    @Test
    void stringSerdeNonAscii(){
        String ser = Serdes.stringSerde.serialize("Héllo");
        System.out.println(ser);
        assertEquals("Héllo", Serdes.stringSerde.deserialize(ser));
    }

    @Test
    void cardBagSerde(){
        SortedBag.Builder<Card> b = new SortedBag.Builder<>();
        b.add(5, BLACK);
        b.add(4, WHITE);
        SortedBag<Card> bag = b.build();
        String bagSerde = Serdes.cardBagSerde.serialize(bag);
        assertEquals(bag, Serdes.cardBagSerde.deserialize(bagSerde));
    }

    @Test
    void cardBagSerdeEmpty(){
        SortedBag.Builder<Card> b = new SortedBag.Builder<>();
        SortedBag<Card> bag = b.build();
        String bagSerde = Serdes.cardBagSerde.serialize(bag);
        assertEquals(bag, Serdes.cardBagSerde.deserialize(bagSerde));
    }

    @Test
    void routeListSerde(){
        String routes = Serdes.routeListSerde.serialize(ChMap.routes());
        assertEquals(ChMap.routes(), Serdes.routeListSerde.deserialize(routes));
    }
    @Test
    void routeListSerdeEmpty(){
        String routes = Serdes.routeListSerde.serialize(List.of());
        assertEquals(List.of(), Serdes.routeListSerde.deserialize(routes));
    }

    @Test
    void stringListSerde(){
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("Mathieu");
        list.add("tests");
        list.add("Serdes");
        list.add("classsssss");
        list.add("jhdzkucbzjhcbdkhzb===616118cez18c");
        String string = (Serdes.stringListSerde.serialize(list));
        assertEquals(list, Serdes.stringListSerde.deserialize(string));
    }

    @Test
    void stringListSerdeEmpty(){
        String string = (Serdes.stringListSerde.serialize(List.of()));
        assertEquals(List.of(), Serdes.stringListSerde.deserialize(string));
    }

    @Test
    void cardListSerde() {
        List<Card> list = new ArrayList<>();
        list.add(Card.LOCOMOTIVE);
        list.add(Card.BLUE);
        list.add(Card.ORANGE);
        list.add(Card.RED);
        String string = (Serdes.cardListSerde.serialize(list));
    }

    @Test
    void cardListSerdeEmpty(){
        List<Card> list = List.of();
        String string = Serdes.cardListSerde.serialize(list);
        assertEquals(list, Serdes.cardListSerde.deserialize(string));
    }

    @Test
    void ticketBagSerde(){
        SortedBag.Builder<Ticket> bag = new SortedBag.Builder();
        for(Ticket t : ChMap.tickets()){
            bag.add(t);
        }
        String string = Serdes.ticketBagSerde.serialize(bag.build());
        assertEquals(bag.build(), Serdes.ticketBagSerde.deserialize(string));
    }

    @Test
    void ticketBagSerdeEmpty(){
        SortedBag<Ticket> bag = SortedBag.of();
        String string = Serdes.ticketBagSerde.serialize(bag);
        assertEquals(bag, Serdes.ticketBagSerde.deserialize(string));
    }
    @Test
    void cardBagListSerde(){
        List<SortedBag<Card>> list = List.of(Constants.ALL_CARDS);
        String string = Serdes.cardBagListSerde.serialize(list);
        assertEquals(list, Serdes.cardBagListSerde.deserialize(string));
    }

    @Test
    void cardBagListSerdeEmpty(){
        List<SortedBag<Card>> list = List.of();
        String string = Serdes.cardBagListSerde.serialize(list);
        assertEquals(list, Serdes.cardBagListSerde.deserialize(string));
    }

    @Test
    void publicCardStateSerde(){
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        PublicCardState state = new PublicCardState(list, 10, 10);
        String string = Serdes.publicCardStateSerde.serialize(state);
        assertEquals(state.deckSize(), Serdes.publicCardStateSerde.deserialize(string).deckSize());
        assertEquals(state.discardsSize(), Serdes.publicCardStateSerde.deserialize(string).discardsSize());
        assertEquals(state.faceUpCards(), Serdes.publicCardStateSerde.deserialize(string).faceUpCards());
    }
    @Test
    void publicPlayerStateSerde(){
        PublicPlayerState state = new PublicPlayerState(3,4, ChMap.routes());
        String string = Serdes.publicPlayerStateSerde.serialize(state);
        assertEquals(state.ticketCount(), Serdes.publicPlayerStateSerde.deserialize(string).ticketCount());
        assertEquals(state.cardCount(), Serdes.publicPlayerStateSerde.deserialize(string).cardCount());
        assertEquals(state.carCount(), Serdes.publicPlayerStateSerde.deserialize(string).carCount());
        assertEquals(state.routes(), Serdes.publicPlayerStateSerde.deserialize(string).routes());
        assertEquals(state.claimPoints(), Serdes.publicPlayerStateSerde.deserialize(string).claimPoints());

    }

    @Test
    void publicGameStateSerde(){
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        PublicCardState publicCardState = new PublicCardState(list, 10, 10);
        PublicPlayerState publicPlayerState = new PublicPlayerState(3, 4, ChMap.routes());
        PublicGameState state = new PublicGameState(3, publicCardState, PlayerId.PLAYER_1, Map.of(PlayerId.PLAYER_1, publicPlayerState, PlayerId.PLAYER_2, publicPlayerState), PlayerId.PLAYER_2 );
        String string = Serdes.publicGameStateSerde.serialize(state);
        assertEquals(state.ticketsCount(), Serdes.publicGameStateSerde.deserialize(string).ticketsCount());
        assertEquals(state.lastPlayer(), Serdes.publicGameStateSerde.deserialize(string).lastPlayer());
        assertEquals(state.currentPlayerId(), Serdes.publicGameStateSerde.deserialize(string).currentPlayerId());
        assertEquals(state.claimedRoutes(), Serdes.publicGameStateSerde.deserialize(string).claimedRoutes());
        assertEquals(state.canDrawCards(), Serdes.publicGameStateSerde.deserialize(string).canDrawCards());
        assertEquals(state.canDrawTickets(), Serdes.publicGameStateSerde.deserialize(string).canDrawTickets());
    }

    @Test
    void playerStateSerde(){
        SortedBag.Builder<Ticket> bag = new SortedBag.Builder<>();
        for(Ticket t : ChMap.tickets()){
            bag.add(t);
        }
        PlayerState state = new PlayerState(bag.build(), SortedBag.of(6, Card.BLUE, 7 , Card.ORANGE), ChMap.routes());
        String string = Serdes.playerStateSerde.serialize(state);
        assertEquals(state.tickets(), Serdes.playerStateSerde.deserialize(string).tickets());
        assertEquals(state.cards(), Serdes.playerStateSerde.deserialize(string).cards());
        assertEquals(state.ticketPoints(), Serdes.playerStateSerde.deserialize(string).ticketPoints());

    }

    @Test
    void playerStateSerdeEmptyCases(){

        PlayerState state = new PlayerState(SortedBag.of(), SortedBag.of(), List.of());
        String string = Serdes.playerStateSerde.serialize(state);
        assertEquals(state.tickets(), Serdes.playerStateSerde.deserialize(string).tickets());
        assertEquals(state.cards(), Serdes.playerStateSerde.deserialize(string).cards());
        assertEquals(state.ticketPoints(), Serdes.playerStateSerde.deserialize(string).ticketPoints());
    }

    @Test
    void publicGameStateSerdeEmptyCases(){
        List<Card> list = new ArrayList<>();
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        list.add(Card.BLACK);
        PublicCardState publicCardState = new PublicCardState(list, 10, 10);
        PublicPlayerState publicPlayerState = new PublicPlayerState(3, 4, ChMap.routes());
        PublicGameState state = new PublicGameState(3, publicCardState, PlayerId.PLAYER_1, Map.of(PlayerId.PLAYER_1, publicPlayerState, PlayerId.PLAYER_2, publicPlayerState), PlayerId.PLAYER_2 );
        String string = Serdes.publicGameStateSerde.serialize(state);
        assertEquals(state.ticketsCount(), Serdes.publicGameStateSerde.deserialize(string).ticketsCount());
        assertEquals(state.lastPlayer(), Serdes.publicGameStateSerde.deserialize(string).lastPlayer());
        assertEquals(state.currentPlayerId(), Serdes.publicGameStateSerde.deserialize(string).currentPlayerId());
        assertEquals(state.claimedRoutes(), Serdes.publicGameStateSerde.deserialize(string).claimedRoutes());
        assertEquals(state.canDrawCards(), Serdes.publicGameStateSerde.deserialize(string).canDrawCards());
        assertEquals(state.canDrawTickets(), Serdes.publicGameStateSerde.deserialize(string).canDrawTickets());
    }

}