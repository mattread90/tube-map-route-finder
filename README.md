# tube-map-route-finder

Given a path to a text file describing a tube map, as well as the names of an origin station and a destination station,
returns a list of station names that make up an optimal route from the origin to the destination, given the following
assumptions:
    * it takes 2 minutes to travel between adjacent stations;
    * it takes 4 minutes to interchange at a station to a different tube line;
    * it takes zero time to change between branches of the same tube line.

The solution involves modeling the tube map as a graph of nodes and weighted edges and applying Dijkstra's algorithm to
it. Edges between different stations on the same line are weighted with a value of 2. Stations that lie on multiple
lines each have their own node, with edges between them of weight 4. Before the algorithm can be applied, it is
necessary to ensure that both the origin and destination stations are modelled by only one node, even if they lie on
multiple lines, otherwise the algorithm might decide that a traveller would have to change lines at the start or end
of their journey. This is done by removing all but one of the nodes representing the origin/destination, and attaching
any disconnected edges to the remaining node.

For example, if the origin station was "Liverpool Street" this tube map

       Liverpool Street (Central)               Bank
                * ------------------------------- *
                |                2
                | 4
                |                2
                * ------------------------------- *
       Liverpool Street (Bakerloo)             Moorgate

would be translated to

      Moorgate            Liverpool Street            Bank
         * --------------------- * --------------------- *
                     2                       2

before applying the algorithm.
