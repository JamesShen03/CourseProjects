/**
 * Implementations of heapsort and Shellsort Created for the submission of
 * Assignment 2, EECS 2011 W 2021-2022.
 * 
 * @author Xu Nan Shen (218485904)
 * @author Xiaowei Gu (218419184) 
 * sorting functions adapts code from https://algs4.cs.princeton.edu/20sorting/
 */
public class A2 {
	/**
	 * Class not to be instantiated.
	 */
	private A2() {
	}

	/**
	 * Four sort functions that rearrange an array in ascending order.
	 * 
	 * @param a the array to be sorted
	 * @exception throws ClassCastException if the array contains elements that are
	 *                   not mutually comparable.
	 */
	public static void heapSort(int[] a) {
		int len = a.length;

		for (int i = len / 2; i >= 1; i--) {
			intDh(a, i, len);
		}
		int i = len;
		while (i > 1) {
			intHs(a, 1, i--);
			intDh(a, 1, i);
		}
	}

	public static void heapSort(Object[] a) {
		int len = a.length;

		for (int i = len / 2; i >= 1; i--) {
			downHeap(a, i, len);
		}
		int i = len;
		while (i > 1) {
			heapSwap(a, 1, i--);
			downHeap(a, 1, i);
		}

	}

	public static void shellSort(int[] a) {
		int len = a.length;
		int step = 1;
		while (step < len / 3)
			step = 3 * step + 1;

		while (step >= 1) {
			for (int i = step; i < len; i++) {
				for (int j = i; j >= step && a[j] < a[j - step]; j -= step) {
					int temp = a[j];
					a[j] = a[j - step];
					a[j - step] = temp;
				}
			}
			step /= 3;
		}
	}

	public static void shellSort(Object[] a) {
		int len = a.length;
		int step = 1;
		while (step < len / 3)
			step = 3 * step + 1;

		while (step >= 1) {
			for (int i = step; i < len; i++) {
				for (int j = i; j >= step && smaller(a[j], a[j - step]); j -= step) {
					swap(a, j, j - step);
				}
			}
			step /= 3;
		}
	}

	/**
	 * Helper functions for comparing and swapping elements of the array. Ones
	 * dedicated to heapswap supports 1-based indexing.
	 */

	/**
	 * @exception ClassCastException if the elements that are not mutually
	 *                               comparable.
	 */
	@SuppressWarnings("unchecked")
	private static boolean smaller(Object x, Object y) {
		Comparable<Object> a = (Comparable<Object>) x;
		Comparable<Object> b = (Comparable<Object>) y;
		return a.compareTo(b) < 0;
	}

	private static void swap(Object[] a, int i, int j) {
		Object temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	private static void heapSwap(Object[] a, int i, int j) {
		Object temp = a[i - 1];
		a[i - 1] = a[j - 1];
		a[j - 1] = temp;
	}

	private static void intHs(int[] a, int i, int j) {
		int temp = a[i - 1];
		a[i - 1] = a[j - 1];
		a[j - 1] = temp;
	}

	private static void downHeap(Object[] a, int i, int j) {
		while (2 * i <= j) {
			int k = 2 * i;
			if (k < j && smaller(a[k - 1], a[k]))
				k++;
			if (!smaller(a[i - 1], a[k - 1]))
				break;
			heapSwap(a, i, k);
			i = k;
		}
	}

	private static void intDh(int[] a, int i, int j) {
		while (2 * i <= j) {
			int k = 2 * i;
			if (k < j && a[k - 1] < a[k])
				k++;
			if (!(a[i - 1] < a[k - 1]))
				break;
			intHs(a, i, k);
			i = k;
		}
	}

}
