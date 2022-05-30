
/**
 * Part 2 of Assignment 2, EECS 2011 W 2021-2022.
 * Produces a table which visualizes the running time complexity of 
 * A2's Shellsort and heapsort in comparison with that of quicksort
 * and merge sort.
 * @author Xu Nan Shen (218485904)
 * @author Xiaowei Gu (218419184) 
 *
 */
import java.util.Random;
import java.util.Arrays;

public class Part2 {
	public static void main(String args[]) {
		// create an array for input size n;
		int[] nums = new int[6];
		nums[0] = 10;
		for (int i = 1; i < nums.length; i++) {
			nums[i] = nums[i - 1] * 10;
		}
		int numOfN = nums.length;
		String[] methods = new String[] { "heapSort(int)", "heapSort(obj)", "shellSort(int)", "shellSort(obj)",
				"sort(int)", "sort(obj)" };
		int numOfMethods = methods.length;

		// calculate the running time and store in arrays
		long[] sortInt = new long[numOfN];
		long[] sortObj = new long[numOfN];
		long[] heapSortInt = new long[numOfN];
		long[] heapSortObj = new long[numOfN];
		long[] shellSortInt = new long[numOfN];
		long[] shellSortObj = new long[numOfN];

		for (int i = 0; i < numOfN; i++) {
			int[] intArr1 = getIntArray(nums[i]);
			Integer[] objArr1 = intToIntegerArray(intArr1.clone());
			int[] intArr2 = intArr1.clone();
			Integer[] objArr2 = intToIntegerArray(intArr1.clone());
			int[] intArr3 = intArr1.clone();
			Integer[] objArr3 = intToIntegerArray(intArr1.clone());

			long startTime = System.currentTimeMillis();
			Arrays.sort(intArr1);
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			sortInt[i] = duration;

			startTime = System.currentTimeMillis();
			Arrays.sort(objArr1);
			endTime = System.currentTimeMillis();
			duration = endTime - startTime;
			sortObj[i] = duration;

			startTime = System.currentTimeMillis();
			A2.heapSort(intArr2);
			endTime = System.currentTimeMillis();
			duration = endTime - startTime;
			heapSortInt[i] = duration;

			startTime = System.currentTimeMillis();
			A2.heapSort(objArr2);
			endTime = System.currentTimeMillis();
			duration = endTime - startTime;
			heapSortObj[i] = duration;

			startTime = System.currentTimeMillis();
			A2.shellSort(intArr3);
			endTime = System.currentTimeMillis();
			duration = endTime - startTime;
			shellSortInt[i] = duration;

			startTime = System.currentTimeMillis();
			A2.shellSort(objArr3);
			endTime = System.currentTimeMillis();
			duration = endTime - startTime;
			shellSortObj[i] = duration;
		}

		// create a 2d array to store running time of the sorting methods
		long[][] runningTime = new long[numOfN][numOfMethods];
		runningTime[0] = heapSortInt;
		runningTime[1] = heapSortObj;
		runningTime[2] = shellSortInt;
		runningTime[3] = shellSortObj;
		runningTime[4] = sortInt;
		runningTime[5] = sortObj;

		// print the table
		System.out.print("\t");
		for (int i = 0; i < methods.length; i++) {
			System.out.print(methods[i] + "\t");
		}
		System.out.println();
		for (int i = 0; i < numOfN; i++) {
			String[] numbers = new String[] { "10", "100", "1K", "10K", "100K", "1M" };
			System.out.print("n=" + numbers[i]);
			for (int j = 0; j < numOfMethods; j++) {
				System.out.print("\t" + runningTime[j][i] + " ms\t");
			}
			System.out.println();
		}
	}

	// helper method to create an int array in size n with random values
	private static int[] getIntArray(int n) {
		int[] a = new int[n];
		Random rd = new Random();
		for (int i = 0; i < n; i++) {
			a[i] = rd.nextInt(n);
		}
		return a;
	}

	// helper method to convert an array from int to Integer
	private static Integer[] intToIntegerArray(int[] array) {
		Integer[] ans = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			Integer a = Integer.valueOf(array[i]);
			ans[i] = a;
		}
		return ans;
	}
}
