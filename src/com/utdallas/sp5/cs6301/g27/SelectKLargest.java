/*
 * Group number: G27
 * Members:
 *  Gayathri Balakumar
 *  Susindaran Elangovan
 *  Vidya Gopalan
 *  Saikrishna Kanukuntla
 *
 * Short Project #5
 */

package cs6301.g27;

import cs6301.g00.*;
import cs6301.g00.Timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Compilation of different algorithms to retrieve k largest elements from
 * an integer array.
 */
public class SelectKLargest
{


	/**
	 * Builds a Max Heap with the given array and returns k largest elements by deleting root k times
	 *
	 * @param array Input array
	 * @param k     Number of largest elements to be retrieved
	 */

	public static int[] selectWithMaxHeap( int array[], int k )
	{
		if( k <= 0 )
		{
			return new int[ 0 ];
		}

		if( k > array.length )
		{
			return array;
		}


		PriorityQueue<Integer> maxHeap = new PriorityQueue<>( array.length, Collections.reverseOrder() );
		for( int element : array )
		{
			maxHeap.offer( element );
		}

		int[] result = new int[ k ];
		for( int i = 0; i < k; i++ )
		{
			result[ i ] = maxHeap.poll();
		}

		return result;
	}

	/**
	 * Use a min Heap of size k to keep track of the k largest elements seen so far,
	 * as the array is iterated over.
	 *
	 * @param array Input array
	 * @param k     Number of largest elements to be retrieved
	 */
	public static int[] selectWithMinHeap( int array[], int k )
	{
		if( k <= 0 )
		{
			return new int[ 0 ];
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

		int[] result = new int[ k ];
		for( int i = 0; i < k; i++ )
		{
			result[ i ] = minHeap.poll();
		}

		return result;
	}

	/**
	 * Does quick pivot partitioning on the array and compares k with the pivot position
	 * and accordingly returns k largest elements.
	 *
	 * @param array Input array
	 * @param k     Number of largest elements to be retrieved
	 */
	public static int[] selectWithPartition( int array[], int k )
	{
		int n = array.length;

		if( k <= 0 )
		{
			return new int[ 0 ];
		}

		if( k > n )
		{
			return array;
		}

		selectKthLargest( array, 0, n, k );

		return Arrays.copyOfRange( array, n - k, n );
	}

	/**
	 * Select the Kth largest from the sub-array containing {@code n} elements of
	 * the given array starting from index {@code p}.
	 *
	 * At the end of this method, all the elements to the right of the index n-k
	 * will be greater than or equal to the Kth largest element.
	 * @param array Input array
	 * @param p     Start index of the sub-array
	 * @param n     Size of the sub-array
	 * @param k     Kth largest elements
	 *
	 * @return  Kth largest element from the sub-array
	 */
	public static int selectKthLargest( int array[], int p, int n, int k )
	{
		int r = p + n - 1;
		if( n < 7 )
		{
			DPQuickSort.insertionSort( array, p, r );
			return array[ p + n - k ];
		}
		else
		{
			int q = QuickSort.partition( array, p, r );
			int left = q - p;
			int right = r - q;
			if( right >= k )
			{
				return selectKthLargest( array, q + 1, right, k );
			}
			else if( right + 1 == k )
			{
				return array[ q ];
			}
			else
			{
				return selectKthLargest( array, p, left, k - ( right + 1 ) );
			}
		}
	}

	public static void main( String args[] ) throws FileNotFoundException
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

		Random generator = new Random();
		System.out.print( "Enter n: " );
		int inputSize = in.nextInt();

		System.out.print( "Enter k: " );
		int k = in.nextInt();

		int[] array = new int[ inputSize ];

		System.out.print( "Generate duplicate values (0/1)?  " );
		boolean duplicate = in.nextInt() != 0;

		if( duplicate )
		{
			for( int i = 0; i < inputSize; i++ )
			{
				array[ i ] = generator.nextInt( inputSize / 2 );
			}
		}
		else
		{
			for( int i = 0; i < inputSize; i++ )
			{
				array[ i ] = i;
			}
			Shuffle.shuffle( array );
		}

		int[] array1 = new int[ inputSize ], array2 = new int[ inputSize ];
		System.arraycopy( array, 0, array1, 0, inputSize );
		System.arraycopy( array, 0, array2, 0, inputSize );

		System.out.println( "\nSelect with Max Heap - \n" );
		run( array, k, 0 );

		System.out.println();

		System.out.println( "\nSelect with Min Heap - \n" );
		run( array1, k, 1 );

		System.out.println();

		System.out.println( "\nSelect with Partition - \n" );
		run( array2, k, 2 );
	}

	public static void run( int[] array, int k, int type )
	{
		if( array.length <= 100 )
		{
			System.out.println( "Elements: " + Arrays.toString( array ) );
		}

		int[] result;

		Timer timer = new Timer();
		if( type == 0 )
		{
			result = selectWithMaxHeap( array, k );
		}
		else if( type == 1 )
		{
			result = selectWithMinHeap( array, k );
		}
		else
		{
			result = selectWithPartition( array, k );
		}
		timer.end();

		if( array.length <= 100 )
		{
			System.out.println( "K Largest:  " + Arrays.toString( result ) );
		}

		System.out.println( timer );
	}
}
