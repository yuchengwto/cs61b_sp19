package bearmaps.proj2c;

import bearmaps.hw4.WeightedEdge;
import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.lab9.MyTrieSet;
import bearmaps.proj2ab.KdTree;
import bearmaps.proj2ab.Point;
import edu.princeton.cs.algs4.TrieSET;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private KdTree tree;
    private Map<Point, Long> mapPID;
    private MyTrieSet trie;
    private Map<String, List<Node>> mapSN;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        List<Node> nodes = this.getNodes();
        List<Point> points = new ArrayList<>();
        trie = new MyTrieSet();
        mapPID = new HashMap<>();
        mapSN = new HashMap<>();
        for (Node n: nodes) {
            if (!neighbors(n.id()).isEmpty()) {
                Point p = new Point(n.lon(), n.lat());
                points.add(p);
                mapPID.put(p, n.id());
            }

            if (n.name() == null || n.name().equals("")) {
                continue;
            }
            String cleanedName = cleanString(n.name());
            trie.add(cleanedName);
            if (!mapSN.containsKey(cleanedName)) {
                mapSN.put(cleanedName, new ArrayList<>());
            }
            mapSN.get(cleanedName).add(n);
        }
        tree = new KdTree(points);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {

        Point nearestPoint = tree.nearest(lon, lat);
        return mapPID.get(nearestPoint);
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String cleanedPrefix = cleanString(prefix);
        List<String> cleanedNames = trie.keysWithPrefix(cleanedPrefix);
        List<String> fullNames = new ArrayList<>();
        for (String s: cleanedNames) {
            List<Node> ns = mapSN.get(s);
            for (Node n: ns) {
                fullNames.add(n.name());
            }
        }
        return fullNames;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {

        String cleanedString = cleanString(locationName);
        List<Map<String, Object>> locations = new ArrayList<>();
        if (mapSN.containsKey(cleanedString)) {
            for (Node n: mapSN.get(cleanedString)) {
                Map<String, Object> m = new HashMap<>();
                m.put("lat", n.lat());
                m.put("lon", n.lon());
                m.put("name", n.name());
                m.put("id", n.id());
                locations.add(m);
            }
        }
        return locations;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
