/*
 * Group number: G27
 * Members:
 *  Gayathri Balakumar
 *  Susindaran Elangovan
 *  Vidya Gopalan
 *  Saikrishna Kanukuntla
 *
 * Short Project #6
 */

package cs6301.g27;

import cs6301.g00.Shuffle;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Implementation of Binary Heap using array (Priority Queue).
 * <p>
 * Elements are ordered using the comparator provided in the natural order.
 * i.e., lower the value, higher the priority.
 *
 * @param <T>
 */
public class BinaryHeap<T>
{
	/**
	 * Current size of the Priority Queue
	 */
	protected int size;

	/**
	 * Priority Queue
	 */
	private T[] pq;

	/**
	 * Comparator used by the Priority Queue
	 */
	private Comparator<T> comparator;

	/*
	* Constructor(s)
	* */

	/**
	 * Build a binary heap with a given array q, using q[0..n-1].
	 *
	 * @param q    Array of elements to be converted into a heap
	 * @param comp Comparator to be used when ordering the elements
	 * @param n    Current size of the heap (array)
	 */
	public BinaryHeap( T[] q, Comparator<T> comp, int n )
	{
		this.size = n;
		this.pq = q;
		this.comparator = comp;

		buildHeap();
	}

	/*
	* APIs
	* */

	/**
	 * Insert the element {@code x} to the heap.
	 *
	 * @param x Element to be inserted.
	 *
	 * @throws Exception If the heap is already full, or if x is null
	 */
	public void insert( T x ) throws Exception
	{
		add( x );
	}

	/**
	 * Adds the element {@code x} to the heap.
	 *
	 * @param x Element to be inserted.
	 *
	 * @throws Exception If the heap is already full, or if x is null
	 */
	public void add( T x ) throws Exception
	{
		if( x == null )
		{
			throw new Exception( "Cannot add null elements to heap" );
		}

		if( this.size == pq.length )
		{
			throw new Exception( "Heap is full" );
		}

		pq[ size ] = x;
		percolateUp( size );
		size++;
	}

	/**
	 * Removes the element with the Highest priority from the heap and returns it.
	 *
	 * @return Element with the highest priority.
	 *
	 * @throws Exception If the heap is empty
	 */
	public T deleteMin() throws Exception
	{
		return remove();
	}

	/**
	 * Removes the element with the Highest priority from the heap and returns it.
	 *
	 * @return Element with the highest priority.
	 *
	 * @throws Exception If the heap is empty
	 */
	public T remove() throws Exception
	{
		if( isEmpty() )
		{
			throw new Exception( "Heap is empty" );
		}

		T min = pq[ 0 ];
		move( 0, pq[ --size ] );
		percolateDown( 0 );

		return min;
	}

	/**
	 * Returns the elements with the highest priority from the heap, but does not
	 * delete it.
	 *
	 * @return Element with the highest priority
	 *
	 * @throws Exception If the heap is empty
	 */
	public T min() throws Exception
	{
		return peek();
	}

	/**
	 * Returns the elements with the highest priority from the heap, but does not
	 * delete it.
	 *
	 * @return Element with the highest priority
	 *
	 * @throws Exception If the heap is empty
	 */
	public T peek() throws Exception
	{
		if( isEmpty() )
		{
			throw new Exception( "Heap is empty" );
		}

		return pq[ 0 ];
	}

	/**
	 * The root of the heap is replaced by the element {@code x} if the root has
	 * a higher priority.
	 * <p>
	 * This operation is typically used in the Select K largest elements algorithm.
	 *
	 * @param x Element to replace the root of the heap with
	 *
	 * @throws Exception If the heap is empty
	 */
	public void replace( T x ) throws Exception
	{
		if( isEmpty() )
		{
			throw new Exception( "Heap is empty" );
		}

		move( 0, x );
		percolateDown( 0 );
	}

	/**
	 * Sort the array {@code A} using the Heap Sort Algorithm.
	 * <p>
	 * Sorted order depends on comparator used to build heap. </br>
	 * min heap ==> descending order</br>
	 * max heap ==> ascending order
	 *
	 * @param A    Array to be sorted
	 * @param comp Comparator to be used for sorting
	 * @param <T>  Type of elements
	 */
	public static <T> void heapSort( T[] A, Comparator<T> comp )
	{
		BinaryHeap<T> heap = new BinaryHeap<>( A, comp, A.length );
		heap.heapSort();
	}

	/**
	 * Check if the heap is empty.
	 *
	 * @return True if the heap is empty, false otherwise.
	 */
	public boolean isEmpty()
	{
		return this.size == 0;
	}

	/**
	 * Current size of the heap - number of elements.
	 *
	 * @return Current size of the heap
	 */
	public int size()
	{
		return this.size;
	}

	/**
	 * Returns the elements of the heap as an array.
	 *
	 * @return Elements of the heap
	 */
	public T[] getElements()
	{
		return pq;
	}

	/*
	* Helpers
	* */

	/**
	 * Reorder the heap elements if pq[i] violates heap's order property with
	 * its parent.
	 *
	 * @param i The index of the element that may violate the order property
	 */
	protected void percolateUp( int i )
	{
		T x = pq[ i ];

		while( i > 0 && comparator.compare( x, getParent( i ) ) < 0 )
		{
			move( i, getParent( i ) );
			i = getParentIndex( i );
		}

		move( i, x );
	}

	/**
	 * Reorder the heap elements if pq[i] violates heap's order property with
	 * its children.
	 *
	 * @param i The index of the element that may violate the order property
	 */
	protected void percolateDown( int i )
	{
		T x = pq[ i ];
		int childIndex = getLeftChildIndex( i );

		while( childIndex <= size - 1 )
		{
			// If there is a right child to i and it is smaller than left, then we take right
			if( childIndex < size - 1 && comparator.compare( pq[ childIndex ], pq[ childIndex + 1 ] ) > 0 )
			{
				childIndex++;
			}

			// If the smaller of the two children of i is no smaller than i itself, then
			// this is point where we have to insert x.
			if( comparator.compare( x, pq[ childIndex ] ) <= 0 )
			{
				break;
			}

			// Move the smaller child to i, and repeat the above steps for the child's index
			move( i, pq[ childIndex ] );
			i = childIndex;
			childIndex = getLeftChildIndex( i );
		}

		move( i, x );
	}

	/**
	 * Build a heap in-place out of the elements in the {@code pq} array using the
	 * bottom-up approach.
	 */
	protected void buildHeap()
	{
		for( int i = (size >>> 1) - 1; i >= 0; i-- )
		{
			percolateDown( i );
		}
	}

	/**
	 * Sort the elements present in the heap in-place by extracting the min
	 * and placing in the available spot from the end of the array.
	 */
	private void heapSort()
	{
		for( int i = size - 1; i >= 0; i-- )
		{
			try
			{
				pq[ i ] = remove();
			}
			catch( Exception e )
			{
				// Ideally, no exception should occur here.
				e.printStackTrace();
			}
		}
	}

	/**
	 * Helper method to move the element {@code x} to the position i
	 *
	 * @param i Position
	 * @param x Element to be moved
	 */
	protected void move( int i, T x )
	{
		this.pq[ i ] = x;
	}

	/**
	 * Get the parent element of the element present at index {@code i}.
	 * <p>
	 * NOTE: This method does not validate i
	 *
	 * @param i Index
	 *
	 * @return Parent of the element at i
	 */
	private T getParent( int i )
	{
		return pq[ getParentIndex( i ) ];
	}

	/**
	 * Index of the parent of the element present at index i.
	 * <p>
	 * NOTE: This method does not validate i
	 *
	 * @param i Index
	 *
	 * @return Index of the parent of i
	 */
	private int getParentIndex( int i )
	{
		return ( i - 1 ) >>> 1;
	}

	/**
	 * Get the left child of the element present at index {@code i}.
	 * <p>
	 * NOTE: This method does not validate i
	 *
	 * @param i Index
	 *
	 * @return Left child of the element at i
	 */
	private T getLeftChild( int i )
	{
		return pq[ getLeftChildIndex( i ) ];
	}

	/**
	 * Index of the left child of the element present at index i.
	 * <p>
	 * NOTE: This method does not validate i
	 *
	 * @param i Index
	 *
	 * @return Index of the left child of i
	 */
	private int getLeftChildIndex( int i )
	{
		return (i << 1) + 1;
	}

	public static void main( String[] args ) throws Exception
	{
		Scanner in;
		if( args.length > 0 )
		{
			File inputFile = new File( args[ 0 ] );
			in = new Scanner( inputFile );
		}
		else
		{
			in = new Scanner( System.in );
		}

		int n = in.nextInt();
		Integer[] arr1 = new Integer[ n ], arr2 = new Integer[ n ];

		for( int i = 0; i < n; i++ )
		{
			arr1[ i ] = i;
		}

		Shuffle.shuffle( arr1 );
		System.arraycopy( arr1, 0, arr2, 0, arr1.length );

		System.out.println( "Before sorting: " + Arrays.toString( arr1 ) );

		heapSort( arr1, Comparator.comparingInt( a -> a ) );
		System.out.println( "Decreasing order: " + Arrays.toString( arr1 ) );

		heapSort( arr2, Collections.reverseOrder() );
		System.out.println( "Increasing order: " + Arrays.toString( arr2 ) );
	}
}