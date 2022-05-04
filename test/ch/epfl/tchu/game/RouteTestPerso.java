package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RouteTestPerso {



    @Test
    void idTrivialCheckNullPointerThrown() {
        assertThrows(NullPointerException.class, () -> {new Route(null, new Station(1, "Bern"), new Station(2, "Lausanne"), 3, Route.Level.OVERGROUND, Color.BLUE);
        });
    }

    @Test
    void station1Test() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne, 3, Route.Level.OVERGROUND, Color.BLUE);
        assertEquals(bern, test.station1());
    }

    @Test
    void station2Test() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 3, Route.Level.OVERGROUND, Color.BLUE);
        assertEquals(lausanne, test.station2());
    }

    @Test
    void station1NullThrowsException(){
        assertThrows(NullPointerException.class, () -> {new Route("BER_LAU_1", null, new Station(2, "Lausanne"), 3, Route.Level.OVERGROUND, Color.BLUE);
        });
    }

    @Test
    void station2NullThrowsException(){
        assertThrows(NullPointerException.class, () -> {new Route("BER_LAU_1", new Station(2, "Lausanne"), null, 3, Route.Level.OVERGROUND, Color.BLUE);
        });
    }

    @Test
    void lengthNonTrivial() {
        int length = 3;
        Route test = new Route("BER_INT_1", new Station(1, "Bern"), new Station(2, "Lausanne"), length, Route.Level.OVERGROUND, Color.BLUE);
        assertEquals(length, test.length());

    }

    @Test
    void lengthTrivial() {
        int length = 1;
        Route test = new Route("BER_INT_1", new Station(1, "Bern"), new Station(2, "Lausanne"), length, Route.Level.OVERGROUND, Color.BLUE);
        assertEquals(length, test.length());

    }

    @Test
    void lengthMax() {
        int length = Constants.MAX_ROUTE_LENGTH;
        Route test = new Route("BER_INT_1", new Station(1, "Bern"), new Station(2, "Lausanne"), length, Route.Level.OVERGROUND, Color.BLUE);
        assertEquals(length, test.length());

    }

    @Test
    void lengthNegativeThrowsIllegalArgumentException() {
        int length = -3;
        assertThrows(IllegalArgumentException.class, () -> {new Route("BER_INT_1", new Station(1, "Bern"), new Station(2, "Lausanne"), length, Route.Level.OVERGROUND, Color.BLUE);
        });

    }



    @Test
    void levelTest() { //maybe problematic
        Route.Level level = Route.Level.OVERGROUND;
        Route test = new Route("BER_INT_1", new Station(1, "Bern"), new Station(2, "Lausanne"), 3, level, Color.BLUE);
        assertEquals(level, test.level());
    }

    @Test
    void color() {
        Color color = Color.BLUE;
        Route test = new Route("BER_INT_1", new Station(1, "Bern"), new Station(2, "Lausanne"), 3, Route.Level.OVERGROUND, color );
        assertEquals(color, test.color());
    }

    @Test
    void stations() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 3, Route.Level.OVERGROUND, Color.BLUE);
        assertEquals(List.of(bern, lausanne), test.stations());
    }

    @Test
    void stationOppositeStation1() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 3, Route.Level.OVERGROUND, Color.BLUE);
        assertEquals(lausanne , test.stationOpposite(bern));
    }

    @Test
    void stationOppositeStation2() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 3, Route.Level.OVERGROUND, Color.BLUE);
        assertEquals(bern, test.stationOpposite(lausanne));
    }

    @Test
    void possibleClaimCardslenght1() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 1, Route.Level.UNDERGROUND, null);
        List<SortedBag<Card>> bag =  test.possibleClaimCards();
        for(SortedBag<Card> bag1 : bag){
            System.out.println(bag1);
        }
    }
    @Test
    void possibleClaimCardsTrivial() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 2, Route.Level.UNDERGROUND, null);
        List<SortedBag<Card>> bag =  test.possibleClaimCards();
        for(SortedBag<Card> bag1 : bag){
            System.out.println(bag1);
        }
    }

    @Test
    void possibleClaimCardsNonTrivial() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 6, Route.Level.UNDERGROUND, null);
        List<SortedBag<Card>> bag =  test.possibleClaimCards();
        for(SortedBag<Card> bag1 : bag){
            System.out.println(bag1);
        }
    }
    @Test
    void possibleClaimCardslenght1Color() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 1, Route.Level.UNDERGROUND, Color.ORANGE);
        List<SortedBag<Card>> bag =  test.possibleClaimCards();
        for(SortedBag<Card> bag1 : bag){
            System.out.println(bag1);
        }
    }
    @Test
    void possibleClaimCardsTrivialColor() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 2, Route.Level.OVERGROUND, Color.BLUE);
        List<SortedBag<Card>> bag =  test.possibleClaimCards();
        for(SortedBag<Card> bag1 : bag){
            System.out.println(bag1);
        }
    }

    @Test
    void possibleClaimCardsNonTrivialColor() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 6, Route.Level.UNDERGROUND, Color.GREEN);
        List<SortedBag<Card>> bag =  test.possibleClaimCards();
        for(SortedBag<Card> bag1 : bag){
            System.out.println(bag1);
        }
    }

    @Test
    void additionalClaimCardsCountTestColorTrivial() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 1, Route.Level.UNDERGROUND, Color.ORANGE);
        SortedBag.Builder<Card> builder = new SortedBag.Builder<Card>();
        builder.add(Card.GREEN);
        builder.add(Card.ORANGE);
        builder.add(Card.LOCOMOTIVE);
        assertEquals(2,test.additionalClaimCardsCount(SortedBag.of(Card.ORANGE), builder.build()));
    }
    @Test
    void additionalClaimCardsCountTestColorNonTrivial() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 5, Route.Level.UNDERGROUND, Color.ORANGE);
        SortedBag.Builder<Card> builder = new SortedBag.Builder<Card>();
        builder.add(Card.GREEN);
        builder.add(Card.ORANGE);
        builder.add(Card.LOCOMOTIVE);
        SortedBag.Builder<Card> builder1 = new SortedBag.Builder<Card>();
        builder1.add(Card.GREEN);
        builder1.add(Card.GREEN);
        builder1.add(Card.LOCOMOTIVE);
        builder1.add(Card.LOCOMOTIVE);
        builder1.add(Card.LOCOMOTIVE);
        assertEquals(2,test.additionalClaimCardsCount(builder1.build(), builder.build()));
    }

    @Test
    void additionalClaimCardsCountTestNoColor0() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 1, Route.Level.UNDERGROUND, null);
        SortedBag.Builder<Card> builder = new SortedBag.Builder<Card>();
        builder.add(Card.GREEN);
        builder.add(Card.ORANGE);
        builder.add(Card.LOCOMOTIVE);
        assertEquals(1,test.additionalClaimCardsCount(SortedBag.of(Card.BLACK), builder.build()));
    }
    @Test
    void additionalClaimCardsCountTestNoColor1() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 1, Route.Level.UNDERGROUND, null);
        SortedBag.Builder<Card> builder = new SortedBag.Builder<Card>();
        builder.add(Card.GREEN);
        builder.add(Card.ORANGE);
        builder.add(Card.LOCOMOTIVE);
        assertEquals(1,test.additionalClaimCardsCount(SortedBag.of(Card.LOCOMOTIVE), builder.build()));
    }
    @Test
    void additionalClaimCardsCountTestNoColor2() {
        Station bern = new Station(1, "Bern");
        Station lausanne = new Station(2, "Lausanne");
        Route test = new Route("BER_LAU_1", bern, lausanne , 1, Route.Level.UNDERGROUND, null);
        SortedBag.Builder<Card> builder = new SortedBag.Builder<Card>();
        builder.add(Card.GREEN);
        builder.add(Card.ORANGE);
        builder.add(Card.ORANGE);
        assertEquals(0,test.additionalClaimCardsCount(SortedBag.of(Card.LOCOMOTIVE), builder.build()));
    }

    @Test
    void claimPoints() {
       int length = 3; //si modifié, modifier le nombre de points attendus également
        Route test = new Route("BER_LAU_1", new Station(1, "Bern"), new Station(2, "Lausanne"), length, Route.Level.OVERGROUND, Color.BLUE);
        assertEquals(4, test.claimPoints());
    }
}