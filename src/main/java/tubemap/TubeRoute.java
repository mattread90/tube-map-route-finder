package tubemap;

import routefinder.Route;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TubeRoute extends Route<Station> {

   public TubeRoute() {
       super();
   }

   public List<String> toListOfStationNames() {
       LinkedList<String> stationNames = new LinkedList<>();
       Station current;
       String prevStationName = "";
       Iterator<Station> itr = getNodes().listIterator();
       while (itr.hasNext()) {
           current = itr.next();
           // Don't print the same station name more than once, in event of line changes
           if (!current.getName().equals(prevStationName)) {
               stationNames.addLast(current.getName());
           }
           prevStationName = current.getName();
       }
       return stationNames;
   }
}
