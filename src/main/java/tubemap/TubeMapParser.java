package tubemap;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.stream.Stream;

public class TubeMapParser {
    private Stream<String> mapSource;
    private static final String RECORD_SEPARATOR = "\\^";

    public TubeMapParser(String tubeNetworkDescriptionFilePath) throws IOException {
        mapSource = Files.lines(FileSystems.getDefault()
                        .getPath(tubeNetworkDescriptionFilePath));
    }

    public TubeMap parseSource() {
        TubeMap map = new TubeMap();
        mapSource.forEach(line -> parseLine(line, map));
        return map;
    }

    private void parseLine(String line, TubeMap map) {
        final String[] lineSplit = line.split(RECORD_SEPARATOR);

        final String tubeLine = lineSplit[0];
        String stationName = lineSplit[1];

        Station currentStation = addStation(stationName, tubeLine, map);

        Station prevStation;
        for (int i = 2; i < lineSplit.length; i++) {
            stationName = lineSplit[i];
            prevStation = currentStation;
            currentStation = addStation(stationName, tubeLine, map);
            map.addConnection(prevStation, currentStation);
        }
    }

    private Station addStation(String stationName, String tubeLine, TubeMap map) {
        Station station = new Station(stationName, tubeLine);
        if (map.addStation(station)) {
            // Add connections to stations with same name to represent line transfer time
            map.getStationsByName(station.getName())
                    .forEach(sameStation -> map.addConnection(station, sameStation));
            return station;
        } else {
            // This station has already been added
            return map.getStation(stationName, tubeLine).get();
        }
    }

}
