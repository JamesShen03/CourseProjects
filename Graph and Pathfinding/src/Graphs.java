import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Implementation of graph algorithms shortest path and nearby.
 * 
 * @author Xu Nan Shen (218485904)
 * @author Xiaowei Gu (218419184) adapts code from:
 *         https://algs4.cs.princeton.edu/44sp/DijkstraSP.java.html
 *         Starter coded provided by:
 * @author Andriy
 */

public class Graphs {
	private static Map<String, Double> distTo;
	private static Map<String, Edge> edgeTo;
	private static PriorityQueue<String> verticesPQ;

	/**
	 * Implements a shortest path algorithm based on Dijkstra's stops as soon as the
	 * path to the destination is computed (does NOT compute the complete shortest
	 * path tree)
	 * 
	 * @param graph input graph
	 * @param from  starting vertex
	 * @param to    destination
	 * @return List of cities visited, in order, from source to the destination
	 *         (both are included), followed by distance in km
	 */

	public static List<String> shortestPath(Graph graph, String from, String to) {
		List<String> path = new ArrayList<>();
		distTo = new HashMap<>();
		edgeTo = new HashMap<>();
		verticesPQ = new PriorityQueue<>(new Comparator<String>() {
			@Override
			public int compare(String v1, String v2) {
				return distTo.get(v1) > distTo.get(v2) ? 1 : -1;
			}
		});

		for (String str : graph.vertices()) {
			distTo.put(str, Double.POSITIVE_INFINITY);
		}

		// check if the specified starting city or the destination is on the list
		validateVertex(from);
		validateVertex(to);

		distTo.put(from, 0.0);

		// relax vertices in order of distance from starting vertex
		verticesPQ.add(from);

		while (!verticesPQ.isEmpty()) {
			String s = verticesPQ.poll();
			for (Edge e : graph.adj.get(s)) {
				relax(e);
			}
		}

		// path: a List of the cities visited, ordered from source to the destination
		// (both are included)
		// followed by the total distance between the cities in km
		path.add(from);

		List<String> middlePath = new ArrayList<>();
		for (Edge e : pathTo(to)) {
			String s = e.either().name;
			if (!path.contains(s)) {
				middlePath.add(s);
			}
		}

		for (int i = middlePath.size() - 1; i >= 0; i--) {
			path.add(middlePath.get(i));
		}

		path.add(to);

		// the last item is the total distance, stored as a String
		String totalD = String.format("%.1f", distTo.get(to));
		path.add(totalD);
		return path;
	}

	/**
	 * Utilizes the shortest path algorithm to output a list of cities that are
	 * within a specified distance from the origin
	 * 
	 * @param graph  input graph
	 * @param origin the origin city
	 * @return List of cities within the specified distance from the origin, ordered
	 *         from the closest to the farthest, followed by a distance in km each
	 *         (quotes just illustrate that those are String objects) e.g.,
	 *         ["Etobicoke 13", " 27.5"]
	 */
	public static List<String> nearby(Graph graph, String origin, double distance) {
		validateVertex(origin);
		distTo = new HashMap<>();

		List<String> result = new ArrayList<>();
		verticesPQ = new PriorityQueue<>(new Comparator<String>() {
			@Override
			public int compare(String v1, String v2) {
				return distTo.get(v1) > distTo.get(v2) ? 1 : -1;
			}
		});
		for (String str : graph.vertices()) {
			distTo.put(str, Double.POSITIVE_INFINITY);
		}
		distTo.put(origin, 0.0);
		verticesPQ.add(origin);

		while (!verticesPQ.isEmpty()) {
			String s = verticesPQ.poll();
			for (Edge e : graph.adj.get(s)) {
				relax(e);
			}
		}

		for (Map.Entry<String, Double> entry : distTo.entrySet()) {
			if (entry.getValue() <= distance)
				verticesPQ.add(entry.getKey());
		}

		while (!verticesPQ.isEmpty()) {
			String c = verticesPQ.poll();
			String v = String.format("%.1f", distTo.get(c));
			result.add(c + " " + v);
		}

		return result;
	}
	
	/**
	 * Helper methods for Dijkstra's.
	 */

	// relax edge e and update verticesPQ if changed
	private static void relax(Edge e) {
		Vertex from = e.either(), to = e.other(from);
		if (distTo.get(to.name) > distTo.get(from.name) + e.weight()) {
			distTo.put(to.name, distTo.get(from.name) + e.weight());
			if (edgeTo != null)
				edgeTo.put(to.name, e);
			if (!verticesPQ.contains(to.name)) {
				verticesPQ.add(to.name);
			}
		}
	}

	// return a shortest path from the starting vertex to the destination
	private static Iterable<Edge> pathTo(String to) {
		Stack<Edge> cities = new Stack<Edge>();
		for (Edge e = edgeTo.get(to); e != null; e = edgeTo.get(e.either().name)) {
			cities.push(e);
		}
		return cities;
	}

	// throw an IllegalArgumentException if the starting/destination vertex is
	// invalid
	private static void validateVertex(String s) {
		if (!distTo.containsKey(s)) {
			throw new IllegalArgumentException("The starting/destination city is invalid.");
		}
	}

}
