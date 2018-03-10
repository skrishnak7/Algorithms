/** @author rbk
 *  Singly linked list: for instructional purposes only
 *  Ver 1.0: 2017/08/08
 *  Ver 1.1: 2017/08/30: Fixed error: If last element of list is removed,
 *  "tail" is no longer a valid value.  Subsequently, if items are added
 *  to the list, code would do the wrong thing.
 */

package cs6301.g00;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SinglyLinkedList<T> implements Iterable<T> {

	/** Class Entry holds a single node of the list */
	public static class Entry<T> {
		T element;
		Entry<T> next;

		Entry(T x, Entry<T> nxt) {
			element = x;
			next = nxt;
		}
	}

	// Dummy header is used.  tail stores reference of tail element of list
	Entry<T> header, tail;
	protected int size;

	public SinglyLinkedList() {
		header = new Entry<>(null, null);
		tail = header;
		size = 0;
	}

	public Iterator<T> iterator() { return new SLLIterator<>(this); }

	private class SLLIterator<E> implements Iterator<E> {
		SinglyLinkedList<E> list;
		Entry<E> cursor, prev;
		boolean ready;  // is item ready to be removed?

		SLLIterator(SinglyLinkedList<E> list) {
			this.list = list;
			cursor = list.header;
			prev = null;
			ready = false;
		}

		public boolean hasNext() {
			return cursor.next != null;
		}

		public E next() {
			prev = cursor;
			cursor = cursor.next;
			ready = true;
			return cursor.element;
		}

		// Removes the current element (retrieved by the most recent next())
		// Remove can be called only if next has been called and the element has not been removed
		public void remove() {
			if(!ready) {
				throw new NoSuchElementException();
			}
			prev.next = cursor.next;
			// Handle case when tail of a list is deleted
			if(cursor == list.tail) {
				list.tail = prev;
			}
			cursor = prev;
			ready = false;  // Calling remove again without calling next will result in exception thrown
			size--;
		}
	}

	// Add new elements to the end of the list
	public void add(T x) {
		tail.next = new Entry<>(x, null);
		tail = tail.next;
		size++;
	}

	public void printList() {
	/* Code without using implicit iterator in for each loop:

        Entry<T> x = header.next;
        while(x != null) {
            System.out.print(x.element + " ");
            x = x.next;
        }
	*/

		System.out.print(this.size + ": ");
		for(T item: this) {
			System.out.print(item + " ");
		}

		System.out.println();
	}

	// Rearrange the elements of the list by linking the elements at even index
	// followed by the elements at odd index. Implemented by rearranging pointers
	// of existing elements without allocating any new elements.
	public void unzip() {
		if(size < 3) {  // Too few elements.  No change.
			return;
		}

		Entry<T> tail0 = header.next;
		Entry<T> head1 = tail0.next;
		Entry<T> tail1 = head1;
		Entry<T> c = tail1.next;
		int state = 0;

		// Invariant: tail0 is the tail of the chain of elements with even index.
		// tail1 is the tail of odd index chain.
		// c is current element to be processed.
		// state indicates the state of the finite state machine
		// state = i indicates that the current element is added after taili (i=0,1).
		while(c != null) {
			if(state == 0) {
				tail0.next = c;
				tail0 = c;
				c = c.next;
			} else {
				tail1.next = c;
				tail1 = c;
				c = c.next;
			}
			state = 1 - state;
		}
		tail0.next = head1;
		tail1.next = null;
	}

	public void multiUnzip(int k)
	{
		if( k <= 1 || size <= k )
		{
			return;
		}

		// Creating k dummy heads to hold k sub-lists
		List<Entry<T>> heads = IntStream.range(0, k).mapToObj( i -> new Entry<T>(null, null) ).collect( Collectors.toList() );

		// Creating k cursors for k sub-lists, starting from their respective
		// dummy headers.
		List<Entry<T>> tails = new ArrayList<>();
		for( int i = 0; i < k; i++ )
		{
			tails.add( heads.get( i ) );
		}

		// Cursor to travers the original list.
		Entry<T> cursor = header.next;

		// Finite state machine variable indicating the ith list to which the
		// next element should be added to.
		int i = 0;
		while( cursor != null )
		{
			tails.get( i ).next = cursor;
			tails.set( i, cursor );

			// Incrementing the state and looping back to 0 when exceeding k.
			i = (i + 1) % k;
			cursor = cursor.next;
		}

		// Linking the j'th tail to the j+1'th head to form a connected list
		for( int j = 0; j < k - 1; j++ )
		{
			tails.get( j ).next = heads.get( j + 1 ).next;
		}

		// Updating the original tail of the list
		tails.get( k - 1 ).next = null;
		this.tail = tails.get( k - 1 );
	}

	public void iterativeReverse()
	{
		Entry<T> prev, cursor = header.next, newHeader = new Entry<>( null, null );

		// Loop invariants:
		//  cursor - pointer that traverses the list from left to right
		//  prev - pointer to the previous position of the cursor
		//  newHeader - header to the reversed list whose next will point to the
		//              prev pointer at the end of each iteration.
		while( cursor != null )
		{
			prev = cursor;
			cursor = cursor.next;

			prev.next = newHeader.next;
			newHeader.next = prev;
		}

		// Since the list is reversed, we swap the header and tail.
		this.tail = this.header;
		this.header = newHeader;
	}

	public void recursiveReverse()
	{
		// Storing a pointer to the original list's tail to be used
		// as the reversed list's header.
		Entry<T> temp = this.tail;
		this.tail = recursiveReverse( this.header.next );
		this.header.next = temp;
	}

	/**
	 * Reverses the linked list starting from the node <code>entry</code>
	 * and returns the tail of the reversed list.
	 *
	 * @param entry Header to the linked list to be reversed
	 *
	 * @return  Pointer to the tail of the reversed list
	 */
	private Entry<T> recursiveReverse( Entry<T> entry )
	{
		if( entry == null ) return null;
		if( entry.next == null ) return entry;

		// Reverse the rest of the list leaving the head
		Entry<T> reversed = recursiveReverse( entry.next );

		// Attach the head to the end of the reversed sublist.
		reversed.next = entry;

		// Properly terminate the tail - mark its next as null
		entry.next = null;

		// Return the head itself - the new tail
		return entry;
	}

	public void printReverseRecursive()
	{
		printReverseRecursive( this.header.next );
		System.out.println();
	}

	private void printReverseRecursive( Entry<T> entry )
	{
		if( entry == null ) return;
		printReverseRecursive( entry.next );
		System.out.print( entry.element + " " );
	}

	public void printReverse()
	{
		StringBuilder sb = new StringBuilder();

		Entry<T> cursor = this.header.next;
		ArrayDeque<T> stack = new ArrayDeque<>();

		while( cursor != null )
		{
			stack.push( cursor.element );
			cursor = cursor.next;
		}

		while( !stack.isEmpty() )
		{
			sb.append( stack.pop() ).append( " " );
		}

		System.out.println( sb );
	}

	public static void main(String[] args) throws NoSuchElementException {
		int n = 10;
		if(args.length > 0) {
			n = Integer.parseInt(args[0]);
		}

		SinglyLinkedList<Integer> lst = new SinglyLinkedList<>();
		for(int i=1; i<=n; i++) {
			lst.add(new Integer(i));
		}
		lst.printList();

		Iterator<Integer> it = lst.iterator();
		Scanner in = new Scanner(System.in);
		whileloop:
		while(in.hasNext()) {
			int com = in.nextInt();
			switch(com) {
				case 1:  // Move to next element and print it
					if (it.hasNext()) {
						System.out.println(it.next());
					} else {
						break whileloop;
					}
					break;
				case 2:  // Remove element
					it.remove();
					lst.printList();
					break;
				default:  // Exit loop
					break whileloop;
			}
		}
		lst.printList();
		lst.multiUnzip(1);
		lst.printList();

		lst.printReverseRecursive();
		lst.printReverse();
		lst.iterativeReverse();
		lst.printList();
	}
}