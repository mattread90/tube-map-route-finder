package routefinder;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.*;

public class RouteFinder {

//    Dijkstra's algorithm
    public static <N extends Node, R extends Route> R getAnyShortestRoute(Graph<N> graph, N start, N end, R routeToFill) {
        NodeInfoMap<N> nodeInfo = new NodeInfoMap<>();

        // Create a priority heap which orders nodes based on the shortest path length from
        // the start node.
        PriorityQueue<N> nodes = new PriorityQueue<>(
                graph.getNodes().size(),
                getShortestPathComparator(nodeInfo)
        );

        // Initialise all nodes as unvisited and with a maximum shortest path length from the start node,
        // apart from the start node which obviously has a shortest path of 0.
        nodeInfo.newNode(start, false, 0);
        nodes.add(start);
        graph.getNodes().stream()
                .filter(node -> !node.equals(start))
                .forEach(node -> {
                    nodeInfo.newNode(node, false, Double.POSITIVE_INFINITY);
                    nodes.add(node);
                });

        while (!nodes.isEmpty()) {
            // Get the node with the shortest known path from the start node
            N current = nodes.peek();
            if (current.equals(end)) break; // We can finish if we reach the destination node

            nodeInfo.visit(current); // Mark it visited
            // For all unvisited neighbours to this node, see if we can reach that node from here
            // using a shorter total path length that currently recorded
            current.getEdges()
                    .filter(edge -> !nodeInfo.wasVisited(edge.getOtherNode(current)))
                    .forEach(edge -> nodeInfo.updateShortestPathToNeighbour(current, edge));
            // Remove the node from the heap
            nodes.remove();
        }

        N current = end;
        do {
            routeToFill.addToFront(current);
        } while ((current = nodeInfo.getPrevNodeInShortestPath(current)) != null);

        if (!routeToFill.getFront().equals(start)) {
            throw new IllegalArgumentException("End not reachable from start");
        }

        return routeToFill;
    }

    // Class to store metainfo about each node to be used in algorithm
    // Nodes themselves must be immutable to ensure they are stored in the HashSet correctly
    private static class NodeInfoMap<N extends Node> {
        private HashMap<Node, NodeInfo<N>> info;

        NodeInfoMap() {
            info = new HashMap<>();
        }

        void newNode(final N node, boolean visited, double shortestPath) {
            info.put(node, new NodeInfo<>(visited, shortestPath));
        }

        void visit(final Node node) {
            info.get(node).visited = true;
        }

        double getShortestPath(final Node node) {
            return info.get(node).shortestPathToHere;
        }

        void updateShortestPathToNeighbour(final Node node, final Edge<Node> edgeToNeighbour) {
            final double potentialPath = getShortestPath(node) + edgeToNeighbour.getValue();
            Node neighbour = edgeToNeighbour.getOtherNode(node);
            NodeInfo neighbourInfo = info.get(neighbour);
            final double currentShortestPath = neighbourInfo.shortestPathToHere;
            if (potentialPath < currentShortestPath) {
                neighbourInfo.shortestPathToHere = potentialPath;
                neighbourInfo.prevNodeInShortestPath = node;
            }
        }

        boolean wasVisited(final Node node) {
            return info.get(node).visited;
        }

        N getPrevNodeInShortestPath(final N node) {
            return info.get(node).prevNodeInShortestPath;
        }

        private static class NodeInfo<N> {
            boolean visited;
            double shortestPathToHere;
            N prevNodeInShortestPath;

            NodeInfo(boolean visited, double shortestPathToHere) {
                this.visited = visited;
                this.shortestPathToHere = shortestPathToHere;
            }
        }
    }

    private static Comparator<Node> getShortestPathComparator(NodeInfoMap nodeInfo) {
        return (o1, o2) -> Double.compare(nodeInfo.getShortestPath(o1), nodeInfo.getShortestPath(o2));
    }

}
