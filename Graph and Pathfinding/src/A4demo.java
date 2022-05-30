import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class A4demo {

	public static void main(String[] args) {
		
		List mylist = new ArrayList<>();
		mylist.add(5);
		
		System.out.println(mylist);
		System.out.println(mylist.get(0).getClass());

		Set <Object> set = new HashSet<>();
		Vertex e = new Vertex("OP");
		set.add(e);
		Graph g = new DistanceGraph("Edges1.csv");
		//System.out.println(g);

		String from = "4";
		String to = "6";
		System.out.println(Graphs.shortestPath(g, from, to));

		List<String> cities = new ArrayList<>();
		cities.add("Ajax"); cities.add("Oshawa"); cities.add("Markham"); cities.add("Toronto");
		//start in Ajax, end in Toronto; 
		//visit Oshawa and Markham along the way, in the order so that the total path is the shortest
		System.out.println(Graphs.nearby(g, "1", 11.0));

	}

}
