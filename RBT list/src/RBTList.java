import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Implementation of a RedBlack BST-based variation of a list, Created for the
 * submission of Assignment 3, EECS 2011 W 2021-2022.
 * 
 * @author Xu Nan Shen (218485904)
 * @author Xiaowei Gu (218419184) adapts code from:
 *         https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html
 */

public class RBTList<E> implements List<E> {
	/**
	 * Class representing a node in the list that holds a single element of generic
	 * type.
	 */
	private static final class Node<E> {
		E val;
		private Node<E> left, right;
		private boolean color;
		private int size;

		Node(E val, boolean color, int size) {
			this.val = val;
			this.color = color;
			this.size = size;
		}
	}

	/**
	 * Helper Constants, adapted from
	 * https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html.
	 */
	private static final boolean RED = true;
	private static final boolean BLACK = false;

	private Node<E> root;

	/**
	 * Constructor for creating an empty list.
	 */
	public RBTList() {
		root = null;
	}

	/**
	 * Implementation of the List interface public methods: 1. boolean add(E e) 2.
	 * void add(int index, E element) 3. E remove(int index) 4. E get(int index) 5.
	 * int size() 6. void clear() 7. String toString() See Java API:
	 * https://docs.oracle.com/javase/8/docs/api/java/util/List.html and
	 * https://docs.oracle.com/javase/7/docs/api/java/util/AbstractCollection.html#toString()
	 */
	@Override
	public boolean add(E e) {
		root = put(root, this.size(), e);
		root.color = BLACK;
		return true;
	}

	@Override
	public void add(int index, E element) {
		if (index > this.size() || index < 0)
			throw new IndexOutOfBoundsException();
		root = put(root, index, element);
		root.color = BLACK;
	}

	@Override
	public E remove(int index) {
		if (index > this.size() - 1 || index < 0)
			throw new IndexOutOfBoundsException();

		E val = get(index);

		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;

		root = delete(root, index);
		if (size() > 0)
			root.color = BLACK;

		return val;

	}

	@Override
	public E get(int index) {
		if (index > this.size() - 1 || index < 0)
			throw new IndexOutOfBoundsException();

		return get(index, root).val;
	}

	@Override
	public int size() {
		return size(root);
	}

	@Override
	public void clear() {
		root = null;
	}

	/**
	 * Adapts code from A1 written by the same authors.
	 */
	public String toString() {

		if (size() == 0)
			return "[]";
		String res = "[";
		for (int i = 0; i < size() - 1; i++) {
			res += String.valueOf(this.get(i));
			res += ", ";
		}
		res += String.valueOf(this.get(size() - 1));
		res += "]";

		return res;
	}

	/**
	 * Helper Functions: Uses code from
	 * https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html.
	 */
	private boolean isRed(Node<E> x) {
		if (x == null)
			return false;
		return x.color == RED;
	}

	private int size(Node<E> x) {
		if (x == null)
			return 0;
		return x.size;
	}

	private Node<E> rotateRight(Node<E> h) {
		Node<E> x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = h.color;
		h.color = RED;
		x.size = h.size;
		h.size = size(h.left) + size(h.right) + 1;
		return x;
	}

	private Node<E> rotateLeft(Node<E> h) {
		Node<E> x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = h.color;
		h.color = RED;
		x.size = h.size;
		h.size = size(h.left) + size(h.right) + 1;
		return x;
	}

	private void flipColors(Node<E> h) {
		h.color = !h.color;
		h.left.color = !h.left.color;
		h.right.color = !h.right.color;
	}

	private Node<E> moveRedLeft(Node<E> h) {
		flipColors(h);
		if (isRed(h.right.left)) {
			h.right = rotateRight(h.right);
			h = rotateLeft(h);
			flipColors(h);
		}
		return h;
	}

	private Node<E> moveRedRight(Node<E> h) {
		flipColors(h);
		if (isRed(h.left.left)) {
			h = rotateRight(h);
			flipColors(h);
		}
		return h;
	}

	private Node<E> balance(Node<E> h) {
		if (isRed(h.right) && !isRed(h.left))
			h = rotateLeft(h);
		if (isRed(h.left) && isRed(h.left.left))
			h = rotateRight(h);
		if (isRed(h.left) && isRed(h.right))
			flipColors(h);

		h.size = size(h.left) + size(h.right) + 1;
		return h;
	}

	private Node<E> put(Node<E> h, int index, E val) {
		if (h == null)
			return new Node<E>(val, RED, 1);

		if (index <= size(h.left)) {
			h.left = put(h.left, index, val);
		} else if (index > size(h.left)) {
			h.right = put(h.right, index - size(h.left) - 1, val);
		}

		if (isRed(h.right) && !isRed(h.left))
			h = rotateLeft(h);
		if (isRed(h.left) && isRed(h.left.left))
			h = rotateRight(h);
		if (isRed(h.left) && isRed(h.right))
			flipColors(h);
		h.size = size(h.left) + size(h.right) + 1;

		return h;
	}

	private Node<E> get(int index, Node<E> h) {
		while (h != null) {
			if (index == size(h.left))
				return h;
			else if (index < size(h.left))
				h = h.left;
			else if (index > size(h.left)) {
				index = index - size(h.left) - 1;
				h = h.right;
			}
		}
		return null;
	}

	private Node<E> delete(Node<E> h, int index) {

		if (index < size(h.left)) {
			if (!isRed(h.left) && !isRed(h.left.left))
				h = moveRedLeft(h);
			h.left = delete(h.left, index);
		} else {
			if (isRed(h.left))
				h = rotateRight(h);
			if (index == size(h.left) && (h.right == null))
				return null;
			if (!isRed(h.right) && !isRed(h.right.left))
				h = moveRedRight(h);
			if (index == size(h.left)) {
				Node<E> x = min(h.right);
				h.val = x.val;
				h.right = deleteMin(h.right);
			} else
				h.right = delete(h.right, index - size(h.left) - 1);
		}
		return balance(h);
	}

	private Node<E> min(Node<E> x) {
		if (x.left == null)
			return x;
		else
			return min(x.left);
	}

	private Node<E> deleteMin(Node<E> h) {
		if (h.left == null)
			return null;

		if (!isRed(h.left) && !isRed(h.left.left))
			h = moveRedLeft(h);

		h.left = deleteMin(h.left);
		return balance(h);
	}

	/**
	 * This class is a partial implementation of List, a number of operations are
	 * unsupported and throws the UnsupportedOperationException.
	 */
	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

}
