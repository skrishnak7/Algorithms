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

/**
 * Implementation of Quick sort with dual-pivot
 */
public class DPQuickSort
{
	/**
	 * Sort the elements of the input array using the Quick Sort Algorithm with dual-pivot
	 * partition.
	 *
	 * @param A Input array to be sorted
	 */
	public static void quickSort( int[] A )
	{
		quickSort( A, 0, A.length - 1 );
	}

	/**
	 * Sort the elements of the input array {@code A} between the indices
	 * p to r using the Quick Sort Algorithm with dual-pivot partition.
	 *
	 * @param A Input array to be sorted
	 * @param p Start index of the sub-array to be sorted
	 * @param r End index of the sub-array to be sorted
	 */
	private static void quickSort( int[] A, int p, int r )
	{
		if( r - p < 7 )
		{
			insertionSort( A, p, r );
			return;
		}

		int[] locations = dualPivotPartition( A, p, r );
		int x1 = locations[ 0 ], x2 = locations[ 1 ];

		if( p < x1 - 1 ) quickSort( A, p, x1 - 1 );
		if( x2 + 1 < r ) quickSort( A, x2 + 1, r );
		if( A[ x1 ] != A[ x2 ] && x1 + 1 < x2 - 1 )
		{
			quickSort( A, x1 + 1, x2 - 1 );
		}
	}

	/**
	 * Partition the sub-array of the array {@code A} between the indices p to r
	 * by select two pivots at random from the sub-array and splitting the sub-array
	 * into three parts with respect to the two selected pivots.
	 *
	 * <pre>
	 *      <p1, p1<=x<=p2 and >p2
	 * </pre>
	 *
	 * @param A Input array
	 * @param p Start index
	 * @param r End index
	 *
	 * @return
	 */
	public static int[] dualPivotPartition( int[] A, int p, int r )
	{
		Random generator = new Random();

		int firstIndex = p + generator.nextInt( r - p + 1 );
		swap( A, p, firstIndex );

		int secondIndex = p + 1 + generator.nextInt( r - p );
		swap( A, r, secondIndex );

		if( A[p] > A[r] )
		{
			swap( A, p, r );
		}

		int x1 = A[ p ], x2 = A[ r ];
		int k, i, j;

		k = i = p + 1;
		j = r - 1;

		while( i <= j )
		{
			if( x1 <= A[ i ] && A[ i ] <= x2 )
			{
				i++;
			}
			else if( A[ i ] < x1 )
			{
				swap( A, k++, i++ );
			}
			else if( A[ j ] > x2 )
			{
				j--;
			}
			else if( A[ i ] > x2 )
			{
				if( x1 <= A[ j ] && A[ j ] <= x2 )
				{
					swap( A, i++, j-- );
				}
				else if( A[ j ] < x1 )
				{
					swap( A, j, k );
					if( k < i )
					{
						swap( A, i, j );
					}
					k++;
					i++;
					j--;
				}
			}
		}

		swap( A, p, k - 1 );
		swap( A, j + 1, r );

		return new int[]{ k - 1, j + 1 };
	}

	/**
	 * Sort the elements of the input array between the indices p to r
	 * using the Insertion Sort Algorithm.
	 *
	 * @param array Input array to be sorted
	 * @param p     Start index of the range to be sorted
	 * @param r     End index of the range to be sorted
	 */
	public static void insertionSort( int[] array, int p, int r )
	{
		if( r <= p  ) return;

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
	 * @param A Array containing the elements to be swapped
	 * @param i     Index of the element to be swapped with j
	 * @param j     Index of the element to be swapped with i
	 */
	private static void swap( int[] A, int i, int j )
	{
		if( i != j && A[i] != A[j] )
		{
			int temp = A[ i ];
			A[ i ] = A[ j ];
			A[ j ] = temp;
		}
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
		quickSort( array );
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