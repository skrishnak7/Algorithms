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
import cs6301.g00.Timer;

import java.io.File;
import java.util.*;

public class SP6Q8
{
	/**
	 * This way of creating comparators (using lambda) is WAY slower than the traditional way
	 * (new Comparator<Integer>{}). However, since this way is a lot cleaner and shorter, it is
	 * mostly preferred, and it is declared as a static final member to avoid considering this
	 * computation time into the algorithm's time.
	 */
	private static final Comparator<Integer> INTEGER_COMPARATOR = Comparator.comparingInt( a -> a );

	/**
	 * Use a min Heap of size k to keep track of the k largest elements seen so far,
	 * as the array is iterated over.
	 *
	 * @param array Input array
	 * @param k     Number of largest elements to be retrieved
	 *
	 * @return K largest elements of the input array
	 */
	public static Integer[] selectWithMinHeap( Integer array[], int k )
	{
		if( k <= 0 )
		{
			return new Integer[ 0 ];
		}

		if( k > array.length )
		{
			return array;
		}

		PriorityQueue<Integer> minHeap = new PriorityQueue<>( k );
		for( int i = 0; i < k; i++ )
		{
			minHeap.offer( array[ i ] );
		}

		for( int i = k; i < array.length; i++ )
		{
			if( array[ i ] > minHeap.peek() )
			{
				minHeap.poll();
				minHeap.offer( array[ i ] );
			}
		}

		Integer[] result = new Integer[ k ];
		for( int i = 0; i < k; i++ )
		{
			result[ i ] = minHeap.poll();
		}

		return result;
	}

	/**
	 * Use a min Heap of size k to keep track of the k largest elements seen so far,
	 * as the array is iterated over. Instead of the traditional delete and add operations,
	 * the replace operation is used to replace the elements in the heap.
	 *
	 * @param array Input array
	 * @param k     Number of largest elements to be retrieved.
	 *
	 * @return K largest elements of the input array
	 *
	 * @throws Exception thrown by the replace operation if the heap is empty
	 */
	public static Integer[] selectUsingHeapReplace( Integer array[], int k ) throws Exception
	{
		if( k <= 0 )
		{
			return new Integer[ 0 ];
		}

		if( k > array.length )
		{
			return array;
		}

		BinaryHeap<Integer> heap = new BinaryHeap<>( Arrays.copyOfRange( array, 0, k ), INTEGER_COMPARATOR, k );

		for( int i = k; i < array.length; i++ )
		{
			if( array[ i ] > heap.peek() )
			{
				heap.replace( array[ i ] );
			}
		}

		return heap.getElements();
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

		System.out.print("n: "); int n = in.nextInt();
		System.out.print("k: "); int k = in.nextInt();
		System.out.print("Shuffle (0/1)? "); boolean shuffle = in.nextInt() != 0;
		Integer[] arr1 = new Integer[ n ], arr2 = new Integer[ n ];

		for( int i = 0; i < n; i++ )
		{
			arr1[ i ] = i;
		}

		if( shuffle )
		{
			Shuffle.shuffle( arr1 );
		}

		System.arraycopy( arr1, 0, arr2, 0, arr1.length );

		if( n <= 100 )
		{
			System.out.println( "Elements: " + Arrays.toString( arr1 ) );
		}

		Timer timer = new Timer();
		Integer[] kLargest = selectWithMinHeap( arr1, k );
		timer.end();
		if( n <= 100 )
		{
			System.out.println( "Java PQ: " + Arrays.toString( kLargest ) );
		}
		System.out.println( timer );

		System.out.println();

		timer.start();
		kLargest = selectUsingHeapReplace( arr2, k );
		timer.end();
		if( n <= 100 )
		{
			System.out.println( "Java PQ: " + Arrays.toString( kLargest ) );
		}
		System.out.println( timer );
	}
}