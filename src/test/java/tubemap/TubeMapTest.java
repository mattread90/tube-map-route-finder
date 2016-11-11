package tubemap;

import graph.Edge;
import graph.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

public class TubeMapTest {

    TubeMap map;

    @Before
    public void setUp() throws Exception {
        map = new TubeMap();

    }

    @Test
    public void addStation() throws Exception {
        map.addStation(new Station("name", "line"));
        Assert.assertEquals(1, map.getNodes().size());
    }

    @Test
    public void addStation_WhenStationAlreadyExists_DoesntAdd() throws Exception {
        map.addStation(new Station("name", "line"));
        map.addStation(new Station("name", "line"));
        Assert.assertEquals(1, map.getNodes().size());
    }

    @Test
    public void isSameStation_WhenStationsHaveSameName_ReturnsTrue() throws Exception {
        Station station1 = new Station("name", "line1");
        Station station2 = new Station("name", "line2");
        Assert.assertTrue(station1.isSameStation(station2));
    }

    @Test
    public void isSameStation_WhenStationsHaveDifferentName_ReturnsFalse() throws Exception {
        Station station1 = new Station("name1", "line1");
        Station station2 = new Station("name2", "line2");
        Assert.assertFalse(station1.isSameStation(station2));
    }

    @Test
    public void addConnection_AddsEdgeToStations() throws Exception {
        Station station1 = new Station("name1", "line1");
        Station station2 = new Station("name2", "line2");
        map.addStation(station1);
        map.addStation(station2);
        map.addConnection(station1, station2);

        Assert.assertEquals(1, station1.getEdges().count());
        Assert.assertEquals(1, station2.getEdges().count());
    }

    @Test
    public void addConnection_WhenDifferentStationNames_AddsEdgeWithWeight2ToStations() throws Exception {
        Station station1 = new Station("name1", "line");
        Station station2 = new Station("name2", "line");
        map.addStation(station1);
        map.addStation(station2);
        map.addConnection(station1, station2);

        Assert.assertEquals(2, station1.getEdges().findAny().get().getValue());
        Assert.assertEquals(2, station2.getEdges().findAny().get().getValue());
    }

    @Test
    public void addConnection_WhenSameStationNames_AddsEdgeWithWeight4ToStations() throws Exception {
        Station station1 = new Station("name", "line1");
        Station station2 = new Station("name", "line2");
        map.addStation(station1);
        map.addStation(station2);
        map.addConnection(station1, station2);

        Assert.assertEquals(4, station1.getEdges().findAny().get().getValue());
        Assert.assertEquals(4, station2.getEdges().findAny().get().getValue());
    }

    @Test
    public void getStationsByName_GetsCorrectStations() throws Exception {
        Station station1 = new Station("name", "line1");
        Station station2 = new Station("name", "line2");
        Station station3 = new Station("different", "line2");
        map.addStation(station1);
        map.addStation(station2);
        map.addStation(station3);

        Set<Station> stations = map.getStationsByName("name");
        Assert.assertEquals(2, stations.size());
        stations.forEach(station -> Assert.assertEquals("name", station.getName()));
    }

    @Test
    public void getStation_WhenStationExists_GetsCorrectStation() throws Exception {
        Station station1 = new Station("name", "line1");
        Station station2 = new Station("name", "line2");
        Station station3 = new Station("different", "line2");
        map.addStation(station1);
        map.addStation(station2);
        map.addStation(station3);

        Optional<Station> foundStation = map.getStation("name", "line1");
        Assert.assertTrue(foundStation.isPresent());
        Assert.assertEquals("name", foundStation.get().getName());
        Assert.assertEquals("line1", foundStation.get().getLine());
    }

    @Test
    public void getStation_WhenStationDoesntExist_GetsEmptyOptional() throws Exception {
        Station station1 = new Station("name", "line1");
        Station station2 = new Station("name", "line2");
        Station station3 = new Station("different", "line2");
        map.addStation(station1);
        map.addStation(station2);
        map.addStation(station3);

        Optional<Station> foundStation = map.getStation("different", "line1");
        Assert.assertFalse(foundStation.isPresent());
    }

    @Test
    public void reduceStation_WhenNoneToReduce_DoesNothing() throws Exception {
        Station station1 = new Station("name1", "line1");
        Station station2 = new Station("name2", "line2");
        map.addStation(station1);
        map.addStation(station2);
        map.addConnection(station1, station2);

        map.reduceStation("name1");
        Assert.assertEquals(2, map.getNodes().size());
    }

    @Test
    public void reduceStation_WhenThereIsAStationOnTheMultipleLines_ReducesToOneStation() throws Exception {
        /*
        Testing that

            station1
               *                           station<1 or 2>
               |              ----->             *
               *
            station2

         */

        Station station1 = new Station("name", "line1");
        Station station2 = new Station("name", "line2");
        map.addStation(station1);
        map.addStation(station2);
        map.addConnection(station1, station2);

        map.reduceStation("name");
        Assert.assertEquals(1, map.getNodes().size());

        Station stationLeft = map.getNodes().stream().findAny().get();
        Assert.assertTrue(stationLeft.equals(station1) || stationLeft.equals(station2));
        Assert.assertEquals(0, stationLeft.getEdges().count());
    }

    @Test
    public void reduceStation_WhenAStationIsRemoved_KeepsItsConnectionsToOtherStations() throws Exception {
        /*
        Testing that

            station1                 otherStation
               * ------------------------ *
               |
               *
            station2
                            |
                            |
                            V

            station<1 or 2>          otherStation
                   * ------------------- *
         */
        Station station1 = new Station("name", "line1");
        Station station2 = new Station("name", "line2");
        Station otherStation = new Station("other", "line2");
        map.addStation(station1);
        map.addStation(station2);
        map.addStation(otherStation);
        map.addConnection(station1, station2);
        map.addConnection(station2, otherStation);

        map.reduceStation("name");
        Assert.assertEquals(2, map.getNodes().size());

        Set<Station> stationsLeft = map.getStationsByName("name");
        Assert.assertEquals(1, map.getStationsByName("name").size());

        Station stationLeft = stationsLeft.stream().findAny().get();

        Assert.assertEquals(1, stationLeft.getEdges().count());

        Edge<Node> connection = stationLeft.getEdges().findAny().get();
        Assert.assertEquals(otherStation, connection.getOtherNode(stationLeft));
    }

    @Test
    public void reduceStation_WhenAStationIsRemoved_KeepsItsConnectionsToOtherStationsOnAllLines() throws Exception {
        /*
        Testing that

                                    station1            otherStation1
                                       * ------------------- *
              otherStation2            |
                 * ------------------- *
                                    station2

                                       |
                                       |
                                       V

             otherStation2       station<1 or 2>        otherStation1
                 * ------------------- * ------------------- *

         */
        Station station1 = new Station("name", "line1");
        Station station2 = new Station("name", "line2");
        Station otherStation1 = new Station("other1", "line1");
        Station otherStation2 = new Station("other2", "line2");
        map.addStation(station1);
        map.addStation(station2);
        map.addStation(otherStation1);
        map.addStation(otherStation2);
        map.addConnection(station1, station2);
        map.addConnection(station1, otherStation1);
        map.addConnection(station2, otherStation2);

        map.reduceStation("name");
        Assert.assertEquals(3, map.getNodes().size());

        Set<Station> stationsLeft = map.getStationsByName("name");
        Assert.assertEquals(1, map.getStationsByName("name").size());

        Station stationLeft = stationsLeft.stream().findAny().get();

        Assert.assertEquals(2, stationLeft.getEdges().count());

        stationLeft.getEdges().forEach(connection ->
            Assert.assertTrue(
                    otherStation1.equals(connection.getOtherNode(stationLeft)) ||
                    otherStation2.equals(connection.getOtherNode(stationLeft))
            )
        );

    }
}
