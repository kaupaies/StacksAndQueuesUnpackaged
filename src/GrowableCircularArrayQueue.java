

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * A queue that grows as needed. The internal capacity doubles when it is full,
 * to give amortized O(1) performance.
 * 
 * @author boutell. Created Nov 30, 2013.
 * @param <T>
 */
public class GrowableCircularArrayQueue<T> implements SimpleQueue<T> {
	// TODO: Declare this class to implement SimpleQueue<T>, then add the
	// missing methods (Eclipse will help).
	// TODO: The javadoc for overridden methods is in the SimpleQueue interface.
	// Read it!
	private static final int INITIAL_CAPACITY = 5;

	// FIXME: Remove the fields before giving to students.
	// a,b,c
	// H, , ,T
	//
	// f,-,c,d,e
	// -,T,H
	private T[] array;
	private int head;
	private int size;
	private Class<T> type;

	/**
	 * Creates an empty queue with an initial capacity of 5
	 * 
	 * @param type
	 *            So that an array of this type can be constructed.
	 */
	public GrowableCircularArrayQueue(Class<T> type) {
		this.type = type;
		clear();
	}

	/**
	 * Removes all the items from this queue and sets its capacity back to the
	 * initial capacity.
	 */
	@Override
	public void clear() {

		this.array = (T[]) Array.newInstance(type, INITIAL_CAPACITY);
		this.head = 0;
		this.size = 0;
	}

	// Assumes this array has at least one item.
	private void resize() {
		int oldSize = this.array.length;
		int newSize = oldSize * 2;
		T[] original = this.array;

		this.array = (T[]) Array.newInstance(type, newSize);
		for (int i = 0; i < size(); i++) {
			this.array[i] = original[(this.head + i) % original.length];
		}
		this.head = 0;
	}

	/**
	 * @return The location where we will enqueue next.
	 */
	private int tail() {
		return (this.head + this.size) % this.array.length;
	}

	/**
	 * Adds the given item to the tail of the queue, resizing the internal array
	 * if needed.
	 * 
	 * @param item
	 */
	@Override
	public void enqueue(T item) {
		if (this.size >= this.array.length) {
			resize();
		}
		this.array[tail()] = item;
		this.size++;
	}

	/**
	 * Removes and returns the item at the head of the queue.
	 * 
	 * @return The item at the head of the queue.
	 * @throws NoSuchElementException
	 *             If the queue is empty.
	 */
	@Override
	public T dequeue() {
		if (isEmpty()) {
			throw new NoSuchElementException(
					"Cannot remove from an empty queue");
		}
		// Get the item first. Then advance the pointer, wrapping around if
		// needed.
		T item = this.array[this.head];
		this.head++;
		this.size--;
		this.head %= this.array.length;
		return item;
	}

	/**
	 * Returns the item at the head of the queue without mutating the queue.
	 * 
	 * @return The item at the head of the queue.
	 * @throws NoSuchElementException
	 *             If the queue is empty.
	 */
	@Override
	public T peek() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException(
					"Cannot remove from an empty queue");
		}
		return this.array[this.head];
	}

	/**
	 * @return True if and only if the queue contains no items.
	 */
	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * @return The number of items in this queue.
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * Searches for the given item in this queue.
	 * 
	 * @param item
	 * @return True if the item is contained within the queue.
	 */
	@Override
	public boolean contains(T item) {
		// TODO: implement
		QueueIterator iter = iterator();
		while (iter.hasNext()) {
			if (iter.next().equals(item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Displays the contents of the queue in insertion order, with the
	 * most-recently inserted one last. Each adjacent pair will be separated by
	 * a comma and a space, and the whole contents will be bounded by square
	 * brackets. See the unit tests for examples.
	 */
	@Override
	public String toString() {
		// TODO: fix this!
		if (isEmpty()) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder();
		QueueIterator iter = iterator();
		while (iter.hasNext()) {
			sb.append(iter.next().toString() + ", ");
		}
		String s = sb.toString();
		return "[" + s.substring(0, s.length() - 2) + "]";
	}

	@Override
	public String debugString() {
		return Arrays.toString(this.array);
	}

	/**
	 * This iterator is used by the contains and toString methods.
	 */
	private class QueueIterator {
		// CONSIDER: If they modify the queue in the middle of this operation,
		// it will break.
		// I should throw an exception!
		private int soFar = 0;

		public boolean hasNext() {
			return this.soFar < GrowableCircularArrayQueue.this.size();
		}

		public T next() throws NoSuchElementException {
			if (!hasNext())
				throw new NoSuchElementException(
						"No more items in the queue iterator.");
			// advance and return
			int nextPos = (GrowableCircularArrayQueue.this.head + this.soFar)
					% GrowableCircularArrayQueue.this.array.length;
			T temp = GrowableCircularArrayQueue.this.array[nextPos];
			this.soFar++;
			return temp;
		}
	}

	/**
	 * @return a new iterator over this queue.
	 */
	private QueueIterator iterator() {
		return new QueueIterator();
	}

}
