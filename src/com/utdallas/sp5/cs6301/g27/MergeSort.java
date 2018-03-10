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

import cs6301.g00.Shuffle;
import cs6301.g00.Timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MergeSort
{
	/**
	 * Sort the input array, in-place, using the Merge Sort Algorithm.
	 *
	 * @param array Input array to be sorted
	 */
	public static void mergeSort( int[] array )
	{
		int[] copy = Arrays.copyOf( array, array.length );
		mergeSort( array, copy, 0, array.length - 1 );
	}

	/**
	 * Helper method for the recursive implementation of the Merge Sort
	 * Algorithm.
	 * <p>
	 * Sort the elements between the indices p to r and the sorted elements
	 * ending up in the {@code A} array between the same indices.
	 *
	 * @param A Input array to be sorted
	 * @param B Copy of the input array to help with merging
	 * @param p Start index of the range to be sorted
	 * @param r End index of the range to be sorted
	 */
	private static void mergeSort( int[] A, int[] B, int p, int r )
	{
		if( r - p < 7 )
		{
			insertionSort( A, p, r );
		}
		else
		{
			// Finding the middle index this way to avoid integer overflow
			int q = p + ( r - p ) / 2;
			mergeSort( B, A, p, q );
			mergeSort( B, A, q + 1, r );
			merge( B, A, p, q, r );
		}
	}

	/**
	 * Merge the sorted elements in the ranges {@code p} to {@code q} and {@code q+1} to {@code r}
	 * from array {@code A} to array {@code B}.
	 * <p>
	 * Prerequisite - Elements in the array {@code A} between the ranges {@code p} to {@code q} and
	 * {@code q+1} to {@code r} should be already sorted.
	 *
	 * @param A Array with sorted elements
	 * @param B Array with the resulting merged elements
	 * @param p Start index of the first range with sorted elements in array A
	 * @param q End index of the first range
	 * @param r End index of the second range
	 */
	private static void merge( int[] A, int[] B, int p, int q, int r )
	{
		int left = p, right = q + 1, cursor = p;

		while( left <= q && right <= r )
		{
			if( A[ left ] <= A[ right ] )
			{
				B[ cursor ] = A[ left++ ];
			}
			else
			{
				B[ cursor ] = A[ right++ ];
			}

			cursor++;
		}

		while( left <= q )
		{
			B[ cursor++ ] = A[ left++ ];
		}

		while( right <= r )
		{
			B[ cursor++ ] = A[ right++ ];
		}
	}

	/**
	 * Sort the elements of the input array between the indices p to r
	 * using the Insertion Sort Algorithm.
	 *
	 * @param array Input array to be sorted
	 * @param p     Start index of the range to be sorted
	 * @param r     End index of the range to be sorted
	 */
	private static void insertionSort( int[] array, int p, int r )
	{
		for( int i = p; i <= r; i++ )
		{
			for( int j = i + 1; j <= r; j++ )
			{
				if( array[ i ] > array[ j ] )
				{
					swap( array, i, j );
				}
			}
		}
	}

	/**
	 * Swap the elements present in the indices i & j of the array.
	 *
	 * @param array Array containing the elements to be swapped
	 * @param i     Index of the element to be swapped with j
	 * @param j     Index of the element to be swapped with i
	 */
	private static void swap( int[] array, int i, int j )
	{
		int temp = array[ i ];
		array[ i ] = array[ j ];
		array[ j ] = temp;
	}

	public static void main( String[] args ) throws FileNotFoundException
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
		System.out.print("Enter n: ");
		int inputSize = in.nextInt();
		int[] arrayWithDuplicates = new int[ inputSize ], uniqueArray = new int[ inputSize ];

		for( int i = 0; i < inputSize; i++ )
		{
			arrayWithDuplicates[ i ] = generator.nextInt( inputSize / 2 );
			uniqueArray[ i ] = i;
		}

		Shuffle.shuffle( uniqueArray );

		System.out.println("Array with unique elements");
		run( uniqueArray );

		System.out.println();

		System.out.println("Array with duplicate elements");
		run( arrayWithDuplicates );
	}

	public static void run( int[] array )
	{
		if( array.length <= 100 )
		{
			System.out.println( "Before sorting: " + Arrays.toString( array ) );
		}

		Timer timer = new Timer();
		mergeSort( array );
		timer.end();

		if( array.length <= 100 )
		{
			for( int i = 1; i < array.length; i++ )
			{
				if( array[ i ] < array[ i - 1 ] )
				{
					System.out.println("fail at " + i + ": " + array[ i - 1 ] + " > " + array[ i ]);
					break;
				}
			}

			System.out.println( "After sorting:  " + Arrays.toString( array ) );
		}

		System.out.println( timer );
	}
}