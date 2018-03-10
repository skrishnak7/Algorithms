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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class SP2Q4Driver
{
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
		int[] array = new int[ inputSize ];
		System.out.print("Generate duplicate values (0/1)?  ");
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

		int[] array1 = new int[inputSize];
		System.arraycopy( array, 0, array1, 0, inputSize );

		System.out.println("\nMerge Sort: \n");
		MergeSort.run( array );

		System.out.println("\nDual Pivot Quick Sort: \n");
		DPQuickSort.run( array1 );
	}
}