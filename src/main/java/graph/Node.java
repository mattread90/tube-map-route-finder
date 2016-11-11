package graph;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Node {
    private final Set<Edge<Node>> edges;

    public Node() {
        this.edges = new HashSet<>();
    }

    public void addEdge(Edge<Node> edge) {
        this.edges.add(edge);
    }

    public void removeEdge(Edge<Node> edge) {
        this.edges.remove(edge);
    }

    public Stream<Edge<Node>> getEdges() {
        return edges.stream();
    }
}
