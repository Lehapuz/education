import core.Line;
import core.Station;
import junit.framework.TestCase;


import java.util.*;


public class RouteCalculatorTest extends TestCase {
    /** * *
     Line 2 *
                         B1 *
                          | *
                          | *
     line 1       A1 -- A2/B2 -- A3 *
                          | *
                          | *
                         B3 *
                          | *
                          | *
     line 3       C1 -- C2/B4 -- C3 * */


    List<Station> route;
    StationIndex stationIndex;

    Line line1 = new Line(1, "Первая");
    Line line2 = new Line(2, "Вторая");
    Line line3 = new Line(3, "Третья");

    Station station = new Station("А1", line1);
    Station station1 = new Station("А2", line1);
    Station station2 = new Station("А3", line1);

    Station station3 = new Station("В1", line2);
    Station station4 = new Station("В2", line2);
    Station station5 = new Station("В3", line2);
    Station station6 = new Station("В4", line2);

    Station station7 = new Station("С1", line3);
    Station station8 = new Station("С2", line3);
    Station station9 = new Station("С3", line3);


    protected void setUp() throws Exception {
        route = new ArrayList<>();

        route.add(station);
        route.add(station4);
        route.add(station5);
        route.add(station8);
        route.add(station9);

    }

    public void testCalculateDuration(){
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 12;
        TestCase.assertEquals(expected, actual);
    }

    public void testGetShortestRouteOnTheLine(){

        stationIndex = new StationIndex();
        List<Station> expected;
        expected = new ArrayList<>();
        expected.add(station3);
        expected.add(station4);
        expected.add(station5);
        expected.add(station6);

        System.out.println(expected);

        line1.addStation(station);
        line1.addStation(station1);
        line1.addStation(station2);
        line2.addStation(station3);
        line2.addStation(station4);
        line2.addStation(station5);
        line2.addStation(station6);
        line3.addStation(station7);
        line3.addStation(station8);
        line3.addStation(station9);
        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);
        stationIndex.addStation(station);
        stationIndex.addStation(station1);
        stationIndex.addStation(station2);
        stationIndex.addStation(station3);
        stationIndex.addStation(station4);
        stationIndex.addStation(station5);
        stationIndex.addStation(station6);
        stationIndex.addStation(station7);
        stationIndex.addStation(station8);
        stationIndex.addStation(station8);

        List <Station> connection1 = new ArrayList<>();
        connection1.add(station1);
        connection1.add(station4);
        List <Station> connection2 = new ArrayList<>();
        connection2.add(station6);
        connection2.add(station8);
        stationIndex.addConnection(connection1);
        stationIndex.addConnection(connection2);

        RouteCalculator calculator = new RouteCalculator(stationIndex);

        List<Station> actual = calculator.getShortestRoute(station3, station6);
        System.out.println(actual);

        assertEquals(expected, actual);
    }

    public void testShortestRouteGetRouteWithOneConnection(){
        stationIndex = new StationIndex();
        List<Station> expected;
        expected = new ArrayList<>();
        expected.add(station3);
        expected.add(station4);
        expected.add(station1);
        expected.add(station2);

        System.out.println(expected);

        line1.addStation(station);
        line1.addStation(station1);
        line1.addStation(station2);
        line2.addStation(station3);
        line2.addStation(station4);
        line2.addStation(station5);
        line2.addStation(station6);
        line3.addStation(station7);
        line3.addStation(station8);
        line3.addStation(station9);
        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);
        stationIndex.addStation(station);
        stationIndex.addStation(station1);
        stationIndex.addStation(station2);
        stationIndex.addStation(station3);
        stationIndex.addStation(station4);
        stationIndex.addStation(station5);
        stationIndex.addStation(station6);
        stationIndex.addStation(station7);
        stationIndex.addStation(station8);
        stationIndex.addStation(station8);

        List <Station> connection1 = new ArrayList<>();
        connection1.add(station1);
        connection1.add(station4);
        List <Station> connection2 = new ArrayList<>();
        connection2.add(station6);
        connection2.add(station8);
        stationIndex.addConnection(connection1);
        stationIndex.addConnection(connection2);

        RouteCalculator calculator = new RouteCalculator(stationIndex);

        List<Station> actual = calculator.getShortestRoute(station3, station2);
        System.out.println(actual);

        assertEquals(expected, actual);
    }

    public void testShortestRouteGetRouteWithTwoConnection(){
        stationIndex = new StationIndex();
        List<Station> expected;
        expected = new ArrayList<>();
        expected.add(station);
        expected.add(station1);
        expected.add(station4);
        expected.add(station5);
        expected.add(station6);
        expected.add(station8);
        expected.add(station9);

        System.out.println(expected);

        line1.addStation(station);
        line1.addStation(station1);
        line1.addStation(station2);
        line2.addStation(station3);
        line2.addStation(station4);
        line2.addStation(station5);
        line2.addStation(station6);
        line3.addStation(station7);
        line3.addStation(station8);
        line3.addStation(station9);
        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);
        stationIndex.addStation(station);
        stationIndex.addStation(station1);
        stationIndex.addStation(station2);
        stationIndex.addStation(station3);
        stationIndex.addStation(station4);
        stationIndex.addStation(station5);
        stationIndex.addStation(station6);
        stationIndex.addStation(station7);
        stationIndex.addStation(station8);
        stationIndex.addStation(station8);

        List <Station> connection1 = new ArrayList<>();
        connection1.add(station1);
        connection1.add(station4);
        List <Station> connection2 = new ArrayList<>();
        connection2.add(station6);
        connection2.add(station8);
        stationIndex.addConnection(connection1);
        stationIndex.addConnection(connection2);

        RouteCalculator calculator = new RouteCalculator(stationIndex);

        List<Station> actual = calculator.getShortestRoute(station, station9);
        System.out.println(actual);

        assertEquals(expected, actual);
    }
}
