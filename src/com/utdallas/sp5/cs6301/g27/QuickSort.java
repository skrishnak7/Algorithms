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
 * Implementations of Quick sort with single pivot
 */
public class QuickSort
{
	public static void quickSort( int[] A )
	{
		quickSort( A, 0, A.length - 1 );
	}

	private static void quickSort( int[] A, int p, int r )
	{
		if( p < r )
		{
			int q = partition( A, p, r );
			quickSort( A, p, q - 1 );
			quickSort( A, q + 1, r );
		}
	}

	public static int partition( int[] A, int p, int r )
	{

		Random rand = new Random();
		int pivot = p + rand.nextInt( r - p + 1 );
		swap( A, pivot, r );

		//pivot element
		int x = A[ r ];
		int i = p - 1;

		for( int j = p; j <= r - 1; j++ )
		{
			if( A[ j ] <= x )
			{
				i++;
				swap( A, i, j );
			}
		}

		//Bring pivot to middle
		swap( A, i + 1, r );

		return i + 1;
	}

	public static void quickSort2( int[] A )
	{
		quickSort2( A, 0, A.length - 1 );
	}

	private static void quickSort2( int[] A, int p, int r )
	{
		if( p < r )
		{
			int q = partition2( A, p, r );
			quickSort2( A, p, q );
			quickSort2( A, q + 1, r );
		}
	}

	public static int partition2( int[] A, int p, int r )
	{

		Random rand = new Random();
		//Choosing  a random index for pivot
		int pivot = p + rand.nextInt( r - p + 1 );
		int x = A[ pivot ];
		int i = p - 1, j = r + 1;

		while( true )
		{
			do
			{
				i++;
			} while( A[ i ] < x );

			do
			{
				j--;
			} while( A[ j ] > x );

			if( i >= j )
			{
				return j;
			}

			swap( A, i, j );
		}
	}

	private static void swap( int[] A, int i, int j )
	{
		int temp = A[ i ];
		A[ i ] = A[ j ];
		A[ j ] = temp;
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
		int[] arrayWithDuplicates2 = new int[ inputSize ], uniqueArray2 = new int[ inputSize ];

		for( int i = 0; i < inputSize; i++ )
		{
			arrayWithDuplicates[ i ] = generator.nextInt( inputSize / 2 );
			uniqueArray[ i ] = i;
		}

		Shuffle.shuffle( uniqueArray );

		System.arraycopy( arrayWithDuplicates, 0, arrayWithDuplicates2, 0, inputSize );
		System.arraycopy( uniqueArray, 0, uniqueArray2, 0, inputSize );

		System.out.println("Quick Sort partition type 1 - \n");
		System.out.println("Array with unique elements");
		run( uniqueArray, true );

		System.out.println();

		System.out.println("Array with duplicate elements");
		run( arrayWithDuplicates, true );



		System.out.println("\n\nQuick Sort partition type 2 - \n");
		System.out.println("Array with unique elements");
		run( uniqueArray2, false );

		System.out.println();

		System.out.println("Array with duplicate elements");
		run( arrayWithDuplicates2, false );
	}

	public static void run( int[] array, boolean type1 )
	{
		if( array.length <= 100 )
		{
			System.out.println( "Before sorting: " + Arrays.toString( array ) );
		}

		Timer timer = new Timer();
		if( type1 ) quickSort( array );
		else quickSort2( array );
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
