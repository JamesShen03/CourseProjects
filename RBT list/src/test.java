
public class test {
	public static void main(String args[]) {
		
		
		RBTList<String> l1 = new RBTList<>();
		l1.add("A");
		l1.add("B");
		l1.add("C");
		l1.add("D");
		l1.add("E");
		l1.add(5,"F");
		l1.add("G");
		l1.add(7,"H");
		l1.add("I");

		
		l1.add(0,"1");
		l1.add(2,"2");
		l1.add(4,"3");
		l1.add(6,"4");
		l1.remove(0);
		l1.clear();
		l1.add(null);

		System.out.println(l1.toString());
		
	}
	
}
