package tubemap;

import graph.Graph;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TubeMap extends Graph<Station> {
    private static final int CHANGE_TIME = 4;
    private static final int TRAVEL_TIME = 2;

    public TubeMap() {
        super();
    }

    public Optional<Station> getStation(String stationName, String lineName) {
        return getStationsByName(stationName).stream()
                .filter(station -> station.getLine().equals(lineName))
                .findAny();
    }

    public boolean addStation(Station station) {
        return addNode(station);
    }

    public void addConnection(Station station1, Station station2) {
        if (station1.equals(station2)) return;
        final int travelTime;
        if (station1.isSameStation(station2)) {
            travelTime = CHANGE_TIME;
        } else {
            travelTime = TRAVEL_TIME;
        }
        addEdge(station1, station2, travelTime);
    }

    public Set<Station> getStationsByName(String stationName) {
        return getNodes().stream()
                .filter(station -> station.getName().equals(stationName))
                .collect(Collectors.toSet());
    }

    // Remove stations with same name but different lines to one station,
    // keeping all connections to other stations. Returns the kept station
    public Station reduceStation(String stationName) {
        Set<Station> sameStations = getStationsByName(stationName);
        Iterator<Station> stationsLeft = sameStations.iterator();

        Station stationToKeep = stationsLeft.next();
        while (stationsLeft.hasNext()) {
            final Station stationToRemove = stationsLeft.next();
            stationToRemove.getEdges().forEach(connection -> {
                Station otherStation = (Station) connection.getOtherNode(stationToRemove);
                if (otherStation.isSameStation(stationToRemove)) {
                    otherStation.removeEdge(connection);
                } else {
                    connection.replaceNode(stationToRemove, stationToKeep);
                    stationToKeep.addEdge(connection);
                }
            });
            removeNode(stationToRemove);
        }
        return stationToKeep;
    }
}
