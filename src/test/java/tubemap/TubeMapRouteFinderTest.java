package tubemap;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Exchanger;

public class TubeMapRouteFinderTest {
    TubeMapRouteFinder routeFinder;

    @Test
    public void anyOptimalRoute_SolvesExample() throws Exception {
        performTest(
                "/challenge-sample-network.txt",
                "Oval", "Southwark",
                "Oval -> Kennington -> Waterloo -> Southwark"
        );
    }

    @Test
    public void anyOptimalRoute_ThrowsExceptionIfStartOrEndDontExist() throws Exception {
        try {
            performTest(
                    "/challenge-sample-network.txt",
                    "Timbuktoo", "Southwark",
                    "Oval -> Kennington -> Waterloo -> Southwark"
            );
            Assert.assertTrue("Shouldn't reach here", false);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Origin station does not exist: Timbuktoo");
        }

        try {
            performTest(
                    "/challenge-sample-network.txt",
                    "Oval", "Timbuktoo",
                    "Oval -> Kennington -> Waterloo -> Southwark"
            );
            Assert.assertTrue("Shouldn't reach here", false);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Destination station does not exist: Timbuktoo");
        } catch (Exception e) {
            Assert.assertTrue("Shouldn't reach here", false);
        }
    }

    @Test
    public void anyOptimalRoute_OrignAndDestinationTheSame() throws Exception {
        performTest(
                "/challenge-sample-network.txt",
                "Oval", "Oval",
                "Oval"
        );
    }

    @Test
    public void anyOptimalRoute_DestinationUnreachable() throws Exception {
        try {
            performTest(
                    "/disconnected.txt",
                    "Liverpool Street", "Grand Central",
                    "Oval"
            );
            Assert.assertTrue("Shouldn't reach here", false);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "End not reachable from start");
        } catch (Exception e) {
            Assert.assertTrue("Shouldn't reach here", false);
        }
    }

    @Test
    public void anyOptimalRoute_StartStationIsOnMultipleLines() throws Exception {
        performTest(
                "/challenge-sample-network.txt",
                "Embankment", "London Bridge",
                "Embankment -> Waterloo -> Southwark -> London Bridge"
        );
    }

    @Test
    public void anyOptimalRoute_EndStationIsOnMultipleLines() throws Exception {
        performTest(
                "/challenge-sample-network.txt",
                "Borough", "Waterloo",
                "Borough -> Elephant & Castle -> Kennington -> Waterloo"
        );
    }

    @Test
    public void anyOptimalRoute_WorksWithCircularLine() throws Exception {
        performTest(
                "/circle-line.txt",
                "Edgware Road", "King's Cross St. Pancras",
                "Edgware Road -> King's Cross St. Pancras"
        );

        performTest(
                "/circle-line.txt",
                "Edgware Road", "Liverpool Street",
                "Edgware Road -> King's Cross St. Pancras -> Liverpool Street",
                "Edgware Road -> Westminster -> Liverpool Street"
        );
    }

    private void performTest(String testFilePath, String start, String end, String... expectedRoutes) throws IOException {
        routeFinder = new TubeMapRouteFinder(
                this.getClass().getResource(testFilePath).getFile()
        );
        List<String> route = routeFinder.anyOptimalRoute(start, end);

        boolean foundOneOfTheRoutes = false;
        for (String validRoute : expectedRoutes) {
            if (printRoute(route).equals(validRoute)) {
                foundOneOfTheRoutes = true;
                break;
            }
        }
        Assert.assertTrue(
                printFailureMessage(printRoute(route), expectedRoutes),
                foundOneOfTheRoutes
        );
    }

    private String printRoute(List<String> route) {
        StringBuilder s  = new StringBuilder();
        Iterator<String> itr = route.iterator();
        String station;
        while (itr.hasNext()) {
            station = itr.next();
            s.append(station);
            if (itr.hasNext()) s.append(" -> ");
        }
        return s.toString();
    }

    private String printFailureMessage(String found, String... expectedRoutes) {
        StringBuilder s = new StringBuilder();
        s.append("Expected");
        if (expectedRoutes.length > 0) s.append(" one of");
        s.append(": \n");
        for (String route : expectedRoutes) s.append("\t" + route + "\n");
        s.append("but found: \n");
        s.append("\t" + found);
        return s.toString();
    }
}
