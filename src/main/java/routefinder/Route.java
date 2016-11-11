package routefinder;

import graph.Node;

import java.util.Iterator;
import java.util.LinkedList;

public class Route<N extends Node> {

    private LinkedList<N> nodes;

    public Route() {
        this.nodes = new LinkedList<>();
    }

    public Route clone() {
        Route clone = new Route();
        Iterator<N> itr = nodes.listIterator();
        while (itr.hasNext()) {
            clone.nodes.addLast(itr.next());
        }
        return clone;
    }

    public LinkedList<N> getNodes() {
        return nodes;
    }

    public void addToFront(N node) {
        nodes.addFirst(node);
    }

    public N getFront() {
        return nodes.getFirst();
    }

}
