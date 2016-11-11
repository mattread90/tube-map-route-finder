package graph;

import java.util.HashSet;
import java.util.Set;

public class Graph<N extends Node> {
    private Set<N> nodes;

    public Graph() {
        this.nodes = new HashSet<>();
    }

    public boolean addNode(N node) {
        return this.nodes.add(node);
    }

    public void addEdge(N node1, N node2, int value) {
        Edge edge = new Edge(node1, node2, value);
        node1.addEdge(edge);
        node2.addEdge(edge);
    }

    public Set<N> getNodes() {
        return nodes;
    }

    public void removeNode(N node) {
        nodes.remove(node);
    }

}
