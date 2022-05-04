package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicPlayerStateTest {

@Test
void constructorThrowsExceptionNegativeTicket(){
    assertThrows(IllegalArgumentException.class, ()-> {    PublicPlayerState test = new PublicPlayerState(-4, 3, List.of());
    });
}

    @Test
    void constructorThrowsExceptionNegativeCard(){
        assertThrows(IllegalArgumentException.class, ()-> {    PublicPlayerState test = new PublicPlayerState(4, -3, List.of());
        });
    }


    @Test
    void computeCarCountTest(){
        Station station1 = new Station(1, "Bern");
        Station station2 = new Station(2, "Lausanne");
        Route route  = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route1  = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route3 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route4 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route5 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route6 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        PublicPlayerState test  = new PublicPlayerState(1, 2 , List.of(route, route1, route3, route4, route5, route6));
        assertEquals(4, test.carCount());
    }

    @Test
    void computePointsTest(){
        Station station1 = new Station(1, "Bern");
        Station station2 = new Station(2, "Lausanne");
        Route route  = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route1  = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route3 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route4 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route5 = new Route("Route", station1, station2, 6, Route.Level.OVERGROUND, Color.ORANGE);
        Route route6 = new Route("Route", station1, station2, 5, Route.Level.OVERGROUND, Color.ORANGE);
        PublicPlayerState test  = new PublicPlayerState(1, 2 , List.of(route, route1, route3, route4, route5, route6));
        assertEquals(5*15+10, test.claimPoints());
    }
}