/*
 * Group number: G27
 * Members:
 *  Gayathri Balakumar
 *  Susindaran Elangovan
 *  Vidya Gopalan
 *  Saikrishna Kanukuntla
 *
 * Long Project #5
 */

package cs6301.g27;

import java.lang.reflect.Array;
import java.util.*;

public class SkipList<T extends Comparable<? super T>> implements Iterable<T>
{
	/* *********************
	* Classes
	** *********************/

	/**
	 * Representation of each element in the skip list
	 */
	public class SkipListElement
	{
		/**
		 * Value of the skip list element
		 */
		public T element;

		/**
		 * Pointers to the next element at each level
		 */
		public SkipListElement[] next;


		/**
		 * Span of the next pointer at each level.
		 * Example: span value of 2 means the next pointer is pointing to the
		 * 2nd element from this element.
		 */
		public int[] span;

		public SkipListElement( T element, int nextSize )
		{
			this.element = element;
			this.span = new int[ nextSize ];
			this.next = ( SkipListElement[] ) Array.newInstance( SkipListElement.class, nextSize );
		}

		@Override
		public String toString()
		{
			return element == null ? "null" : element.toString();
		}
	}

	/**
	 * Representation of the state of the {@code find} method at each level.
	 */
	public class FindStackElement
	{
		/**
		 * The last element in a particular level whose value is less than the target
		 * find element.
		 */
		public SkipListElement skipListElement;

		/**
		 * The span of the {@code skipListElement} from the FindStackElement of the
		 * previous level.
		 */
		public int cumulativeSpan;

		public FindStackElement( SkipListElement skipListElement, int cumulativeSpan )
		{
			this.skipListElement = skipListElement;
			this.cumulativeSpan = cumulativeSpan;
		}

		@Override
		public String toString()
		{
			return "( " + skipListElement.toString() + ", " + cumulativeSpan + " )";
		}
	}

	/**
	 * Iterator for the SkipList
	 */
	public class SkipListIterator implements Iterator<T>
	{
		private SkipListElement p;

		public SkipListIterator( SkipList<T> skipList )
		{
			this.p = skipList.head.next[ 0 ];
		}

		@Override
		public boolean hasNext()
		{
			return p.element != null;
		}

		@Override
		public T next()
		{
			if( hasNext() )
			{
				T element = p.element;
				p = p.next[ 0 ];
				return element;
			}

			throw new NoSuchElementException();
		}
	}

	/* *********************
	* Member variables
	** *********************/

	/**
	 * Maximum number of levels an element is allowed to have in the skip list.
	 */
	private static final int MAX_ALLOWED_LEVELS = 31;
	private static final Random random = new Random();

	/**
	 * Pointers to HEAD and TAIL sentinels of the skip list.
	 */
	private SkipListElement head, tail;

	/**
	 * Pointer to the very last element in the skip list (the element before the
	 * TAIL sentinel of the skip list).
	 */
	private SkipListElement lastElement;

	/**
	 * The level of the element in the skip list with the max level.
	 */
	private int maxLevels;

	/**
	 * Number of elements in the skip list at any given instant.
	 */
	private int size;

	/* *********************
	* Constructors
	** *********************/

	public SkipList()
	{
		this.head = new SkipListElement( null, MAX_ALLOWED_LEVELS + 1 );
		this.tail = new SkipListElement( null, MAX_ALLOWED_LEVELS + 1 );

		this.maxLevels = 0;
		this.size = 0;

		initializeSentinels();
	}

	/* *********************
	* Public APIs
	** *********************/

	/**
	 * Add x to list. If x already exists, replace it.
	 *
	 * @param x Element to be added
	 *
	 * @return true if new node is added to list, false otherwise(replaced).
	 */
	public boolean add( T x )
	{
		if( x == null )
		{
			return false;
		}

		FindStackElement[] prev = find( x );
		if( prev[ 0 ].skipListElement.next[ 0 ].element != null && prev[ 0 ].skipListElement.next[ 0 ].element
				.compareTo( x ) == 0 )
		{
			prev[ 0 ].skipListElement.next[ 0 ].element = x;
			return false;
		}

		int level = Math.min( chooseLevel(), MAX_ALLOWED_LEVELS );
		maxLevels = Math.max( maxLevels, level );

		SkipListElement newNode = addElementWithLevel( x, level, prev );

		// Update the SkipList's last element if the newly added element is in the last position
		if( newNode.next[ 0 ].element == null )
		{
			this.lastElement = newNode;
		}

		size++;
		return true;
	}


	/**
	 * Remove x from the skip list.
	 *
	 * @param x Element to be removed
	 *
	 * @return Removed element if the element is in the list, null otherwise
	 */
	public T remove( T x )
	{
		if( x == null )
		{
			return null;
		}
		FindStackElement[] prev = find( x );
		SkipListElement n = prev[ 0 ].skipListElement.next[ 0 ];
		if( n.element == null || n.element.compareTo( x ) != 0 )
		{
			return null;
		}

		// Update the SkipList's last element pointer if the element deleted was the
		// previous last element of the list.
		if( n.next[ 0 ].element == null )
		{
			this.lastElement = prev[ 0 ].skipListElement;
		}

		for( int i = 0; i <= MAX_ALLOWED_LEVELS; i++ )
		{
			SkipListElement prevNode = i < prev.length ? prev[ i ].skipListElement : head;
			if( prevNode.next[ i ].equals( n ) )
			{
				prevNode.span[ i ] += n.span[ i ] - 1;
				prevNode.next[ i ] = n.next[ i ];
			}
			else
			{
				prevNode.span[ i ]--;
			}
		}

		size--;
		return n.element;
	}

	/**
	 * Return element at index n of list.  First element is at index 0.
	 *
	 * @param n Index of the element to be retrieved
	 *
	 * @return Element at index n
	 */
	public T get( int n )
	{
		if( n < 0 || n >= size() )
		{
			throw new ArrayIndexOutOfBoundsException();
		}

		int distance = 0;
		SkipListElement temp = head;
		for( int i = maxLevels; i >= 0; i-- )
		{
			while( distance + temp.span[ i ] <= n )
			{
				distance += temp.span[ i ];
				temp = temp.next[ i ];
			}
		}

		return temp.next[ 0 ].element;
	}

	/**
	 * Check if the list contains an element
	 *
	 * @param x Element to be found in the list
	 *
	 * @return True if the element is found in the list, false otherwise
	 */
	public boolean contains( T x )
	{
		FindStackElement[] prev = find( x );
		return prev[ 0 ].skipListElement.next[ 0 ].element != null && prev[ 0 ].skipListElement.next[ 0 ].element
				.compareTo( x ) == 0;
	}

	/**
	 * Find smallest element that is greater or equal to x
	 *
	 * @param x Element for which ceiling is to be found
	 *
	 * @return Ceiling of the element if there exists one, null otherwise
	 */
	public T ceiling( T x )
	{
		FindStackElement[] prev = find( x );
		return prev[ 0 ].skipListElement.next[ 0 ].element;
	}

	/**
	 * Find largest element that is less than or equal to x
	 *
	 * @param x Element for which floor is to be found
	 *
	 * @return Floor of the element if there exists one, null otherwise
	 */
	public T floor( T x )
	{
		FindStackElement[] prev = find( x );
		SkipListElement lower = prev[ 0 ].skipListElement;
		return lower.next[ 0 ].element != null && lower.next[ 0 ].element.compareTo( x ) == 0 ? x : lower.element;
	}

	/**
	 * Get the first element in the list
	 *
	 * @return First element in the list if the list is non-empty, null otherwise
	 */
	public T first()
	{
		return head.next[ 0 ].element;
	}

	/**
	 * Get the last element of list
	 *
	 * @return Last element in the list if the list is non-empty, null otherwise
	 */
	public T last()
	{
		return this.lastElement.element;
	}

	/**
	 * Reorganize the elements of the list into a perfect skip list
	 */
	public void rebuild()
	{
		SkipListElement[] list = ( SkipListElement[] ) Array.newInstance( SkipListElement.class, this.size() );

		int i = 0;
		SkipListElement temp = this.head.next[ 0 ];
		while( temp.element != null )
		{
			list[ i++ ] = temp;
			temp = temp.next[ 0 ];
		}

		this.maxLevels = getPerfectMaxLevels() - 1;

		initializeSentinels();
		assignPerfectLevels( list, 0, ( 1 << maxLevels + 1 ) - 1, this.maxLevels );
	}

	/**
	 * Check if the list empty
	 *
	 * @return True if the list is empty, false otherwise
	 */
	public boolean isEmpty()
	{
		return size == 0;
	}

	/**
	 * Get the number of elements in the list
	 *
	 * @return Size of the list
	 */
	public int size()
	{
		return size;
	}

	/* *********************
	* Private helper methods
	** *********************/

	/**
	 * Clear the pointer from the HEAD sentinel and initialize them to point to TAIL sentinel
	 * with a span of 1.
	 */
	private void initializeSentinels()
	{
		for( int i = 0; i <= MAX_ALLOWED_LEVELS; i++ )
		{
			this.head.next[ i ] = tail;
			this.head.span[ i ] = 1;
			this.tail.next[ i ] = null;
		}
	}

	/**
	 * Look for an element in the skip list.
	 *
	 * @param x The element to be found in the skip list.
	 *
	 * @return A stack trace of the find operation with each element in the trace is the state
	 * of the find operation parameters in a particular level.
	 */
	private FindStackElement[] find( T x )
	{
		FindStackElement[] prev = ( FindStackElement[] ) Array.newInstance( FindStackElement.class,
				Math.min( maxLevels + 2, MAX_ALLOWED_LEVELS ) );
		SkipListElement p = head;
		for( int i = Math.min( maxLevels + 1, MAX_ALLOWED_LEVELS ); i >= 0; i-- )
		{
			int hops = 0;
			while( p.next[ i ].element != null && p.next[ i ].element.compareTo( x ) < 0 )
			{
				hops += p.span[ i ];
				p = p.next[ i ];
			}

			prev[ i ] = new FindStackElement( p, hops );
		}

		return prev;
	}

	/**
	 * Choose a level for a new element to be inserted into the skip list.
	 *
	 * @return New level
	 */
	private int chooseLevel()
	{
		int mask = ( 1 << maxLevels ) - 1;
		int level = Integer.numberOfTrailingZeros( random.nextInt() & mask );
		return Math.min( level, maxLevels + 1 );
	}

	/**
	 * Get the max level to be used by a perfect skip list for a given size.
	 * Used by the rebuild operation.
	 *
	 * @return Perfect skip list's max level value
	 */
	private int getPerfectMaxLevels()
	{
		// Rounding up to the next power of 2
		int perfectSize = 1, i = 0;
		while( perfectSize - 1 < this.size() )
		{
			perfectSize = perfectSize << 1;
			i++;
		}

		return i;
	}

	/**
	 * Given an array of elements and a level value, assign levels to each element so that
	 * they form a (near) perfect skip list.
	 *
	 * @param list  List of elements
	 * @param start Start index of the elements
	 * @param end   End index of the elements
	 * @param level Level to be assigned for the mid element between the start and end indices.
	 */
	private void assignPerfectLevels( SkipListElement[] list, int start, int end, int level )
	{
		if( start <= end )
		{
			int mid = start + ( end - start ) / 2;
			if( mid < list.length )
			{
				this.addElementWithLevel( list[ mid ].element, level );
			}

			assignPerfectLevels( list, start, mid - 1, level - 1 );

			// Search in the right only if the mid is in range of the list size
			if( mid < list.length )
			{
				assignPerfectLevels( list, mid + 1, end, level - 1 );
			}
		}
	}

	/**
	 * Add an element to the skip list with the given level value.
	 *
	 * @param x     Element to be added to the skip list
	 * @param level Level of the skip list element.
	 */
	private void addElementWithLevel( T x, int level )
	{
		addElementWithLevel( x, level, find( x ) );
	}

	/**
	 * Add an element to the skip list with the given level value and the stack trace of
	 * the find operation.
	 *
	 * @param x     Element to be added
	 * @param level Level of the skip list element
	 * @param prev  Find operation's trace
	 *
	 * @return New Skip list element that was added
	 */
	private SkipListElement addElementWithLevel( T x, int level, FindStackElement[] prev )
	{
		SkipListElement newNode = new SkipListElement( x, level + 1 );

		// This variable records the cumulative distance between the new node and the
		// prev node at each level.
		int totalSpanSoFar = 0;

		for( int i = 0; i <= MAX_ALLOWED_LEVELS; i++ )
		{
			SkipListElement prevNode = i < prev.length ? prev[ i ].skipListElement : head;
			if( i <= level )
			{
				newNode.next[ i ] = prevNode.next[ i ];
				prevNode.next[ i ] = newNode;

				// The distance covered by the new node at this level 'i' is the distance
				// covered by the previous node subtracted by the distance from this prevNode
				// to the point of insertion of the new node.
				newNode.span[ i ] = Math.max( prevNode.span[ i ] - totalSpanSoFar, 1 );

				// Now the new distance covered by the prev node at this level 'i' is the
				// distance between the prev node and the new node
				prevNode.span[ i ] = totalSpanSoFar + 1;

				totalSpanSoFar += prev[ i ].cumulativeSpan;
			}
			else
			{
				// Counting the newly added element to the levels above the new node's height
				prevNode.span[ i ]++;
			}
		}

		return newNode;
	}

	/* *********************
	* Overrides
	** *********************/

	@Override
	public Iterator<T> iterator()
	{
		return new SkipListIterator( this );
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for( int i = maxLevels; i >= 0; i-- )
		{
			sb.append( "\nHEAD" );
			SkipListElement temp = this.head;
			do
			{
				for( int j = 0; j < temp.span[ i ]; j++ )
				{
					sb.append( "\t" );
				}
				sb.append( temp.next[ i ] );
				temp = temp.next[ i ];
			} while( temp.next[ i ] != null );
		}

		return sb.toString();
	}

	public static void main( String[] args )
	{
		Scanner in = new Scanner( System.in );

		SkipList<Integer> sl = new SkipList<>();
		TreeMap<Integer, Integer> map = new TreeMap<>();
		int inputSize = 20;

		Random random = new Random();
		int[] elements = new int[ inputSize ];
		for( int i = 0; i < inputSize; i++ )
		{
			int anInt = random.nextInt( 200 );
			sl.add( anInt );
			map.put( anInt, anInt );
			elements[ i ] = anInt;
		}

		Arrays.sort( elements );
		System.out.println( Arrays.toString( elements ) );
		System.out.println( sl );

		for( int i = 0; i < 5; i++ )
		{
			int idx = random.nextInt( 2 * inputSize );
			int ele;
			if( idx < inputSize )
			{
				ele = elements[ idx ];
			}
			else
			{
				ele = random.nextInt();
			}

			System.out.println();
			System.out.println( "Element: " + ele );
			System.out.println( "Contains :\t" + sl.contains( ele ) + "\t-\t" + map.containsKey( ele ) );
			System.out.println( "Ceiling: \t" + sl.ceiling( ele ) + "\t-\t" + map.ceilingKey( ele ) );
			System.out.println( "Floor: \t\t" + sl.floor( ele ) + "\t-\t" + map.floorKey( ele ) );
			System.out.println( "Remove:  \t" + sl.remove( ele ) + "\t-\t" + map.remove( ele ) );
			System.out.println( "First:   \t" + sl.first() );
			System.out.println( "Last:    \t" + sl.last() );

			int getIdx = random.nextInt( inputSize );
			try
			{
				System.out.println( "Get: " + getIdx + "\t\t" + sl.get( getIdx ) + "\t" );
			}
			catch( ArrayIndexOutOfBoundsException e )
			{
				System.out.println( "Get: " + getIdx + "\t\t: " + "Out of bounds" );
			}

		}


		System.out.println( sl );
		sl.rebuild();
		System.out.println( sl );


		int choice = 0;
		do
		{
			System.out.println( "1.  Add" );
			System.out.println( "2.  Remove" );
			System.out.println( "3.  Ceil" );
			System.out.println( "4.  Floor" );
			System.out.println( "5.  Contains" );
			System.out.println( "6.  First" );
			System.out.println( "7.  Last" );
			System.out.println( "8.  Print" );
			System.out.println( "9.  Size" );
			System.out.println( "10. Get(index)" );
			System.out.println( "11. Exit" );

			choice = in.nextInt();
			switch( choice )
			{
				case 1:
					System.out.println( sl.add( in.nextInt() ) );
					break;
				case 2:
					System.out.println( sl.remove( in.nextInt() ) );
					break;
				case 3:
					System.out.println( sl.ceiling( in.nextInt() ) );
					break;
				case 4:
					System.out.println( sl.floor( in.nextInt() ) );
					break;
				case 5:
					System.out.println( sl.contains( in.nextInt() ) );
					break;
				case 6:
					System.out.println( sl.first() );
					break;
				case 7:
					System.out.println( sl.last() );
					break;
				case 8:
					System.out.println( sl );
					break;
				case 9:
					System.out.println( sl.size() );
					break;
				case 10:
					System.out.println( sl.get( in.nextInt() ) );
					break;
			}

		} while( choice <= 10 );
	}
}
