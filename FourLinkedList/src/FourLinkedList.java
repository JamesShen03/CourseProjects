import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Quadruply-linked list-based partial implementation of the List interface.
 * List nodes contain references four positions forwards and backwards in
 * addition to one position in each direction. Created for the submission of
 * Assignment 1, EECS 2011 W 2021-2022.
 * 
 * @author Xu Nan Shen (218485904)
 * @author Xiaowei Gu (218419184)
 * @see List
 */
public class FourLinkedList<E> implements List<E> {

	/**
	 * Class representing a node in the list that holds a single element of generic
	 * type.
	 */
	private static final class Node<E> {
		E element;
		Node<E> previous;
		Node<E> next;
		Node<E> back4;
		Node<E> forward4;

		Node(E element) {
			this.element = element;
			previous = next = back4 = forward4 = null;
		}

	}

	/**
	 * The first node in the list.
	 */
	private Node<E> header;
	/**
	 * The last node in the list.
	 */
	private Node<E> trailer;
	/**
	 * The current length of the list.
	 */
	private int size;

	/**
	 * Constructor for the class.
	 */
	public FourLinkedList() {
		size = 0;
		header = trailer = null;
	}

	/**
	 * Appends an element to the end of the list.
	 * 
	 * @param e - element to be added.
	 * @return true (method always succeeds).
	 */
	@Override
	public boolean add(E e) {
		Node<E> n = new Node<E>(e);
		if (size == 0) {
			header = trailer = n;
		} else {
			n.previous = trailer;
			trailer.next = n;
			if (size > 4) {
				n.back4 = trailer.back4.next;
				trailer.back4.next.forward4 = n;
			} else if (size == 4) {
				n.back4 = header;
				header.forward4 = n;
			}
		}
		trailer = n;
		size++;
		return true;
	}

	/**
	 * Inserts the specified element at the specified index in the list. Shifting
	 * the element in the current position and those that follows accordingly.
	 * 
	 * @param index   - position to insert element at.
	 * @param element - element to be inserted.
	 */
	@Override
	public void add(int index, E element) {
		checkBoundsInc(index);
		if (index == size) {
			add(element);
		} else {
			Node<E> curr = this.getNode(index);
			Node<E> e = new Node<E>(element);
			e.next = curr;
			e.previous = curr.previous;
			if (curr.previous == null)
				header = e;
			else
				curr.previous.next = e;
			curr.previous = e;

			if (curr.back4 != null) {
				e.back4 = curr.back4;
				e.back4.forward4 = e;
			}
			if (index >= 3) {
				e.previous.previous.previous.forward4 = curr;
				curr.back4 = e.previous.previous.previous;
			}
			if (index >= 2 && curr.next != null) {
				e.previous.previous.forward4 = curr.next;
				curr.next.back4 = e.previous.previous;
			}
			if (index >= 1 && (size - index) > 2) {
				e.previous.forward4 = curr.next.next;
				curr.next.next.back4 = e.previous;
			}
			if ((size - index) > 3) {
				e.forward4 = curr.next.next.next;
				curr.next.next.next.back4 = e;
			}
			size++;
		}

	}

	/**
	 * Removes the element at the specified position and shifts any elements that
	 * follow to the left. Returns the removed element.
	 * 
	 * @param index - position of the element to be removed.
	 * @return the element that was removed.
	 */
	@Override
	public E remove(int index) {
		checkBoundsExc(index);
		Node<E> e = this.getNode(index);
		size--;
		if (size == 0)
			header = trailer = null;
		else {
			if (e == header) {
				header = e.next;
				e.next.previous = null;
				e.forward4.back4 = null;
			} else if (e == trailer) {
				trailer = e.previous;
				e.previous.next = null;
				e.back4.forward4 = null;
			} else {
				e.next.previous = e.previous;
				e.previous.next = e.next;
				if (e.back4 != null) {
					e.back4.forward4 = e.next;
					e.next.back4 = e.back4;
				}
				if (index >= 3 && e.next.next != null) {
					e.previous.previous.previous.forward4 = e.next.next;
					e.next.next.back4 = e.previous.previous.previous;
				}
				if (index >= 2 && (size - index) > 2) {
					e.previous.previous.forward4 = e.next.next.next;
					e.next.next.next.back4 = e.previous.previous;
				}
				if (index >= 1 && (size - index) > 3) {
					e.previous.forward4 = e.forward4;
					e.forward4.back4 = e.previous;
				}

			}

		}
		return e.element;
	}

	/**
	 * Returns the element at the specified position.
	 * 
	 * @param index - position of the element to be returned.
	 * @return the element at the specified position.
	 */
	@Override
	public E get(int index) {
		checkBoundsExc(index);
		return this.getNode(index).element;
	}

	/**
	 * Returns the current length of the list.
	 * 
	 * @return current length.
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Removes all of the elements from this list.
	 */
	@Override
	public void clear() {
		if (size > 0) {
			header = null;
			trailer = null;
			size = 0;
		}
	}

	/**
	 * Returns a string representation of the list, consisting of each element in
	 * the list enclosed in square brackets and separated by comma and space.
	 * 
	 * @return string representation of the list.
	 */
	public String toString() {
		if (size == 0)
			return "[]";
		String res = "[";
		for (int i = 0; i < size - 1; i++) {
			res += String.valueOf(this.getNode(i).element);
			res += ", ";
		}
		res += String.valueOf(this.getNode(size - 1).element);
		res += "]";

		return res;
	}

	/**
	 * Helper function to check bounds for the index. Throws exception if the
	 * specified position is less than zero or exceeds the current length of the
	 * list.
	 * 
	 * @throws IndexOutOfBoundsException
	 */
	private void checkBoundsInc(int index) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException("Index out of Bounds");
	}

	/**
	 * Helper function to check bounds for the index. Throws exception if the
	 * specified position is less than zero or is equal to or more than the current
	 * length of the list.
	 * 
	 * @throws IndexOutOfBoundsException
	 */
	private void checkBoundsExc(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Index out of Bounds");
	}

	/**
	 * Helper function to return the Node object at a specified position in the
	 * list.
	 * 
	 * @param n - the specified index of the Node object to be returned.
	 * @return the Node at the specified index in the list.
	 */
	private Node<E> getNode(int n) {
		Node<E> e;
		if (n < size / 2) {

			e = header;
			while (n > 0) {
				if (n > 4) {
					e = e.forward4;
					n -= 4;
				} else {
					e = e.next;
					n--;
				}
			}
		} else {
			e = trailer;
			while (n < size - 1) {
				if (size - n > 4) {
					e = e.back4;
					n += 4;
				} else {
					e = e.previous;
					n++;
				}
			}

		}
		return e;
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
