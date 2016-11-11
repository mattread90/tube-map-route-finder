package tubemap;

import graph.Node;

import java.util.Objects;

public class Station extends Node {
    private final String name;
    private final String line;

    public Station(String name, String line) {
        super();
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public String getLine() {
        return line;
    }

    public boolean isSameStation(final Station other) {
        return this.name.equals(other.name);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Station)) return false;
        final Station otherStation = (Station) other;
        return getName().equals(otherStation.getName()) &&
               getLine().equals(otherStation.getLine());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, line);
    }

    @Override
    public String toString() {
        return getName() + " (" + getLine() + ")";
    }
}
