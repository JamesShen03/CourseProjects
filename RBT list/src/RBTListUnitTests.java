import static org.junit.Assert.*;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RBTListUnitTests {

	@Test
	public void constructorInterfaceTester() {
		Field[] fields = RBTList.class.getDeclaredFields();
		for (Field f: fields){
			assertTrue("List class contains a public field", 
					!Modifier.isPublic(f.getModifiers()));
		}

		assertTrue ("Number of constructors != 1", 
				RBTList.class.getDeclaredConstructors().length == 1);

		assertTrue ("List interface not implemented or other interfaces are", 
				RBTList.class.getInterfaces().length == 1 
				&& RBTList.class.getInterfaces()[0].getName().equals("java.util.List"));
	}

	@Test
	public void addTester1() {
		RBTList<String> list = new RBTList<>();
		//check for exceptions
		try{
			list.add(1, "0");
			fail("Exception was to be thrown");
		}catch (IndexOutOfBoundsException e){
			//OK
		}catch (Exception ex) {
			fail("Wrong type of exception");
		}

		list.add("0");
		assertTrue(list.get(0).equals("0"));
	}

	@Test
	public void addTester2() {
		RBTList<String> list = new RBTList<>();
		list.add(0, "0");
		list.add(0, "1");
		list.add(1, "2");
		assertTrue(list.toString().equals("[1, 2, 0]") 
				|| list.get(0).equals("1") && list.get(1).equals("2") && list.get(2).equals("0"));
	}

	@Test
	public void removeTester1() {
		RBTList<String> list = new RBTList<>();
		//check for exception
		try{
			list.remove(0);
			fail("Exception was to be thrown");
		}catch (IndexOutOfBoundsException e){
			//OK
		}catch (Exception ex) {
			//fail("Wrong type of exception");
		}
	}

	@Test
	public void removeTester2() {
		RBTList<String> list = new RBTList<>();
		list.add("1"); list.add("2"); list.add("3");
		assertTrue(list.size() == 3);
		list.remove(1);
		assertTrue(list.size() == 2);
		assertTrue(list.toString().equals("[1, 3]") 
				|| list.get(0).equals("1") && list.get(1).equals("3"));
	}

	@Test
	public void randomAddRemove(){
		Random rnd = new Random();
		List <Integer> list1 = new ArrayList<>();
		List <Integer> list2 = new RBTList<>();
		for (int i = 0; i < 30000; i++){
			if (rnd.nextDouble() < 0.5){
				int position = rnd.nextInt(list1.size()+1);
				//int value = rnd.nextInt(1000);
				list1.add(position, i);
				list2.add(position, i);
			}
			else{
				if (list1.size() > 0){
					int position = rnd.nextInt(list1.size());
					list1.remove(position);
					//System.out.println("L1: "+list1);
					list2.remove(position);
					//System.out.println("L2: "+list2);
				}
			}
		}

		//assertTrue("randomAddRemove: toString" , list1.toString().equals(list2.toString()));
		for (int i = 0; i < list1.size(); i++){
			assertTrue("randomAddRemove: " + list2.size() + ": " + i + "\n"+ list1 + "\n" + list2, 
						list1.get(i).equals(list2.get(i)));
		}
	}

	@Test
	public void getTester1() {
		RBTList<String> list = new RBTList<>();
		//check for exceptions
		try{
			list.get(0);
			fail("Exception was to be thrown");
		}catch (IndexOutOfBoundsException e){
			//OK
		}catch (Exception ex) {
			//fail("Wrong type of exception");
		}
	}

	@Test
	public void getTester2() {
		RBTList<String> list = new RBTList<>();
		list.add("0");list.add("1");
		assertTrue(list.get(0).equals("0"));
		assertTrue(list.get(1).equals("1"));
	}

	@Test
	public void clearTester() {
		RBTList<String> list = new RBTList<>();
		assertTrue(list.size() == 0);

		list.add("1"); list.add("2"); list.add("3");
		//assertTrue(!list.isEmpty());
		assertTrue(list.size() == 3);

		list.clear();
		assertTrue(list.size() == 0);

		try{
			list.get(0);
			fail("Exception was to be thrown");
		}catch (IndexOutOfBoundsException e){
			//OK
		}catch (Exception ex) {
			//fail("Wrong type of exception");
		}
	}

	@Test
	public void sizeTester() {
		RBTList<String> list = new RBTList<>();
		assertTrue(list.size() == 0);
		list.add("1");
		assertTrue(list.size() == 1);
		list.add("9");
		assertTrue(list.size() == 2);
	}

	@Test
	public void toStringTester1() {
		RBTList<String> list = new RBTList<>();
		assertTrue(list.toString().equals("[]"));
	}

	@Test
	public void toStringTester2() {
		RBTList<String> list = new RBTList<>();
		list.add("1"); list.add("2"); list.add("3");
		assertTrue(list.toString().equals("[1, 2, 3]"));
	}
	
	@Test (timeout = 2000)
	public void addConstPerformance_FAIL_MINUS(){
		final int SIZE = 65_536;
		final int CONST_FACTOR = 10;
		List <Integer> list1 = new ArrayList<>();
		List <Integer> list2 = new RBTList<>();

		//"Warm-up"
		for (int i = 0; i < SIZE; i++) list1.add(0, i);
		list1.clear();

		//LinkedList add at the end (benchmark)
		long startTime = System.nanoTime();
		for (int i = 0; i < SIZE; i++){
				list1.add(i);
		}
		long endTime = System.nanoTime();
		long elapsedAL = endTime - startTime;

		//"Warm-up"
		for (int i = 0; i < SIZE; i++) list2.add(0, i);

		//RBTList add at the beginning
		startTime = System.nanoTime();
		for (int i = SIZE; i < SIZE*2; i++){
			list2.add(0, i);
		}
		endTime = System.nanoTime();
		long elapsedRBTL0 = endTime - startTime;
		
		list2.clear();
		for (int i = 0; i < SIZE; i++) list2.add(0, i);

		//RBTList add at the end
		startTime = System.nanoTime();
		for (int i = SIZE; i < SIZE*2; i++){
			list2.add(i, i);
		}
		endTime = System.nanoTime();
		long elapsedRBTL = endTime - startTime;
		
		System.out.println("Benchmark LL add time (ms): " + elapsedAL/1e6 + "\nRBTL (ms): " + elapsedRBTL0/1e6
				+ " : " + elapsedRBTL/1e6);
		
		assertTrue("Possibly non-constant add() performance for 0/end" , 
				elapsedRBTL0 < elapsedAL * CONST_FACTOR 
				&& elapsedRBTL < elapsedAL * CONST_FACTOR);
	}
	
	
	
	@Test (timeout = 2000)
	public void Performance_Exponentiala(){
		final int SIZE = 1;
		List <Integer> list10 = new RBTList<>();
		List <Integer> list100 = new RBTList<>();
		List <Integer> list1000 = new RBTList<>();
		List <Integer> list10000 = new RBTList<>();
		List <Integer> list100000 = new RBTList<>();
		List <Integer> list1000000 = new RBTList<>();

		long t1,t2,t3,t4, t5,t6,startTime,endTime;
		
		//
		for (int i = 0; i < 10; i++){
				list10.add(i);
		}
		startTime = System.nanoTime();
		for (int i = 0; i < 1000; i++){
			int x = ThreadLocalRandom.current().nextInt(0, 10);
			list10.get(x);
		}
		endTime = System.nanoTime();
		t1 = endTime - startTime;

		//
		startTime = System.nanoTime();
		for (int i = 0; i < 100; i++){
				list100.add(i);
		}
		startTime = System.nanoTime();
		for (int i = 0; i < 1000; i++){
			int x = ThreadLocalRandom.current().nextInt(0, 100);
			list100.get(x);
		}
		endTime = System.nanoTime();
		t2 = endTime - startTime;

		//
		startTime = System.nanoTime();
		for (int i = 0; i < 1000; i++){
				list1000.add(i);
		}
		startTime = System.nanoTime();
		for (int i = 0; i < 1000; i++){
			int x = ThreadLocalRandom.current().nextInt(0, 1000);
			list1000.get(x);
		}
		endTime = System.nanoTime();
		t3 = endTime - startTime;

		//
		startTime = System.nanoTime();
		for (int i = 0; i < 10000; i++){
				list10000.add(i);
		}
		startTime = System.nanoTime();
		for (int i = 0; i < 1000; i++){
			int x = ThreadLocalRandom.current().nextInt(0, 10000);
			list10000.get(x);
		}
		endTime = System.nanoTime();
		t4 = endTime - startTime;

		//
		startTime = System.nanoTime();
		for (int i = 0; i < 100000; i++){
				list100000.add(i);
		}
		startTime = System.nanoTime();
		for (int i = 0; i < 1000; i++){
			int x = ThreadLocalRandom.current().nextInt(0, 100000);
			list100000.get(x);
		}
		endTime = System.nanoTime();
		t5 = endTime - startTime;

		//
		startTime = System.nanoTime();
		for (int i = 0; i < 1000000; i++){
				list1000000.add(i);
		}
		startTime = System.nanoTime();
		for (int i = 0; i < 1000; i++){
			int x = ThreadLocalRandom.current().nextInt(0, 1000000);
			list1000000.get(x);
		}
		endTime = System.nanoTime();
		t6 = endTime - startTime;
		
		System.out.println(" times 10 (ms): " + t1/1e6 + "\n times 100 (ms): " + t2/1e6 + "\n times 1000 (ms): " + t3/1e6
				+ "\n times 10000 (ms): " + t4/1e6
				+ "\n times 100000 (ms): " + t5/1e6
				+ "\n times 1000000 (ms): " + t6/1e6);
		
		
	}
	
	
	
	@Test (timeout = 2000)
	public void getLongJumps_FAIL_MINUS(){
//		final int SIZE = 65_536;
		final int SIZE = 65536;
		final int CONST_FACTOR = 2;
		List <Integer> list1 = new LinkedList<>();
		List <Integer> list2 = new RBTList<>();

		//fill with data
		for (int i = 0; i < SIZE; i++) list1.add(i);
		for (int i = 0; i < SIZE; i++) list2.add(i);

		//LinkedList get (benchmark)
		long startTime = System.nanoTime();
		for (int i = 0; i < SIZE ; i++){
			int x = ThreadLocalRandom.current().nextInt(0, SIZE);
			list1.get(x);
		}
		long endTime = System.nanoTime();
		long elapsedLL = endTime - startTime;

		//fill

		//RBTList get
		startTime = System.nanoTime();
		for (int i = SIZE-1; i > -1 ; i--){
			int x = ThreadLocalRandom.current().nextInt(0, SIZE);
			list2.get(x);
		}
		endTime = System.nanoTime();
		long elapsedRBTL = endTime - startTime;
		
		System.out.println("LinkedList get time (ms): " + elapsedLL/1e6 + "\nRBTList (ms): " + elapsedRBTL/1e6);
		
		assertTrue("get in RBTList must be faster than in LinkedList" , 
				elapsedRBTL * CONST_FACTOR <= elapsedLL ); 
	}


	@Test (timeout = 2000)
	public void removePerformance_FAIL_MINUS(){
	final int SIZE = 262144;
		//final int SIZE = 4096;
		final int CONST_FACTOR = 2;
		List <Integer> list1 = new ArrayList<>();
		List <Integer> list2 = new RBTList<>();

		//fill with data
		for (int i = 0; i < SIZE; i++) list1.add(i);
		for (int i = 0; i < SIZE; i++) list2.add(i);

		//ArrayList remove (benchmark)
		long startTime = System.nanoTime();
		for (int i = SIZE-1; i > -1 ; i--){
			int x = ThreadLocalRandom.current().nextInt(0, i+1);
			list1.remove(x);
		}
		long endTime = System.nanoTime();
		long elapsedAL = endTime - startTime;

		//fill

		//RBTList remove
		startTime = System.nanoTime();
		for (int i = SIZE-1; i > -1 ; i--){
			int x = ThreadLocalRandom.current().nextInt(0, i+1);
			list2.remove(x);
		}
		endTime = System.nanoTime();
		long elapsedRBTL = endTime - startTime;
		
		System.out.println("ArrayList remove time (ms): " + elapsedAL/1e6 + "\nRBTList (ms): " + elapsedRBTL/1e6);
		
		assertTrue("remove in RBTList must be faster than in ArrayList" , 
				elapsedRBTL * CONST_FACTOR <= elapsedAL ); 
	}

	
	
	
}