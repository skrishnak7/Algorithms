package cs6301.g27;

import cs6301.g00.Timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * Group number: G27
 * Members:
 *      Gayathri Balakumar
 *      Susindaran Elangovan
 *      Vidya Gopalan
 *      Saikrishna Kanukuntla
 * <p>
 * Short Project #1
 */
public class MergeSortComparison
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
		int inputSize = in.nextInt();
		Integer[] genericSortArray = new Integer[ inputSize ];
		int[] integerSortArray = new int[ inputSize ];

		for( int i = 0; i < genericSortArray.length; i++ )
		{
			integerSortArray[ i ] = genericSortArray[ i ] = generator.nextInt();
		}

		System.out.println( "\nGeneric Merge sort" );
		Timer timer = new Timer();
		timer.start();
		GenericSort.mergeSort( genericSortArray, new Integer[ genericSortArray.length ] );
		timer.end();
		System.out.println( timer );

		System.out.println( "\nInteger Merge sort" );
		timer.start();
		IntegerSort.mergeSort( integerSortArray, new int[ integerSortArray.length ] );
		timer.end();
		System.out.println( timer );
	}
}