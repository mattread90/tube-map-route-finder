package tubemap;

import routefinder.Route;
import routefinder.RouteFinder;

import java.io.IOException;
import java.util.List;

public class TubeMapRouteFinder implements com.byhiras.recruitment.test9.RouteFinder {

    private TubeMapParser parser;

    public TubeMapRouteFinder(String tubeNetworkDescriptionFilePath) throws IOException {
        parser = new TubeMapParser(tubeNetworkDescriptionFilePath);
    }

    @Override
    public List<String> anyOptimalRoute(String originStation, String destinationStation) {
//        Parse input file contents here as the map/graph will be modified for the particular
//        start and end stations
        TubeMap map = parser.parseSource();

        if (map.getStationsByName(originStation).size() == 0) {
            throw new IllegalArgumentException("Origin station does not exist: " + originStation);
        }
        if (map.getStationsByName(destinationStation).size() == 0) {
            throw new IllegalArgumentException("Destination station does not exist: " + destinationStation);
        }

//        Reduce end points to one station if the station lies on multiple lines,
//        as we don't need to transfer at the end points
        Station startStation = map.reduceStation(originStation);
        Station endStation = map.reduceStation(destinationStation);

//        Treat tube map as any unordered graph with weighted edges and apply algorithm to get route
        TubeRoute route = new TubeRoute();
        RouteFinder.getAnyShortestRoute(map, startStation, endStation, route);

        return route.toListOfStationNames();
    }
}
