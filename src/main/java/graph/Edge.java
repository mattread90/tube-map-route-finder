package graph;

import java.util.Objects;

public class Edge<N extends Node> {
    private int value;
    private N node1;
    private N node2;

    public Edge(N node1, N node2, int value) {
        this.node1 = node1;
        this.node2 = node2;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public N getOtherNode(final N thisNode) throws IllegalArgumentException {
        if (thisNode.equals(node1)) {
            return node2;
        } else if (thisNode.equals(node2)) {
            return node1;
        } else {
            throw new IllegalArgumentException("Given Node must belong to this Edge");
        }
    }

    public void replaceNode(final N nodeToReplace, final N newNode) {
        if (nodeToReplace.equals(node1)) {
            node1 = newNode;
        } else if (nodeToReplace.equals(node2)) {
            node2 = newNode;
        } else {
            throw new IllegalArgumentException("Given Node must belong to this Edge");
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null || !(other instanceof Edge)) return false;
        final Edge otherEdge = (Edge) other;
        return (node1.equals(otherEdge.node1) && node2.equals(otherEdge.node2)) ||
               (node1.equals(otherEdge.node2) && node2.equals(otherEdge.node1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(node1, node2);
    }

}