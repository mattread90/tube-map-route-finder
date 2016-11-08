package parser;

import tubemap.Station;
import tubemap.TubeMap;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class TubeMapParser {
    private static final String RECORD_SEPERATOR = "^";


    public static TubeMap parseFile(String filePath) throws IOException {
        TubeMap map = new TubeMap();

        Files.lines(FileSystems.getDefault().getPath(filePath))
                .forEach((line) -> parseLine(line, map));

        return map;
    }

    private static void parseLine(String line, TubeMap map) {
        final String[] lineSplit = line.split(RECORD_SEPERATOR);

        final String tubeLine = lineSplit[0];
        map.addLine(tubeLine);

        String stationName;
        Station station;
        for (int i = 1; i < lineSplit.length; i++) {
            stationName = lineSplit[i];
            station = new Station(stationName, tubeLine);

            if (i - 1 > 0) {
                station.setAdjacentStation(lineSplit[i - 1]);
            }
            if (i + 1 < lineSplit.length) {
                station.setAdjacentStation(lineSplit[i + 1]);
            }

            map.addStation(station);
        }
    }

}
