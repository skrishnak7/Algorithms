package cs6301.g27;

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
public class GenericSort
{

	/**
	 * Sort the elements of <code>sourceArray</code> using the Merge Sort Algorithm in the order
	 * specified by the Comparable interface.
	 *
	 * @param sourceArray Array to be sorted(in-place) using Merge Sort Algorithm
	 * @param tmpArray    Temporary holding array to be used in the Algorithm
	 */
	public static <T extends Comparable<? super T>> void mergeSort( T[] sourceArray, T[] tmpArray )
	{
		sort( sourceArray, tmpArray, 0, sourceArray.length - 1 );
	}


	/**
	 * Recursively sort the elements of the <code>sourceArray</code> array by splitting it in
	 * half and merging the sorted halves.
	 *
	 * @param sourceArray Array to be sorted(in-place) using Merge Sort Algorithm
	 * @param tmpArray    Temporary holding array to be used in the Algorithm
	 * @param startIndex  Starting index of the sub-array to be sorted
	 * @param endIndex    Ending index of the sub-array to be sorted
	 */
	private static <T extends Comparable<? super T>> void sort( T[] sourceArray, T[] tmpArray, int startIndex,
	                                                            int endIndex )
	{
		if( startIndex < endIndex )
		{
			// Finding the middle index this way instead of ( start + end ) / 2
			// to avoid Numeric overflow
			int midIndex = startIndex + ( endIndex - startIndex ) / 2;

			sort( sourceArray, tmpArray, startIndex, midIndex );
			sort( sourceArray, tmpArray, midIndex + 1, endIndex );

			merge( sourceArray, tmpArray, startIndex, midIndex, endIndex );
		}
	}

	/**
	 * Merge the two sorted sub-arrays contained in the <code>sourceArray</code> in-place.
	 * The left sub-array is from <code>startIndex</code> to <code>midIndex</code> and the
	 * right sub-array is from <code>midIndex + 1</code> to <code>endIndex</code>.
	 * <p>
	 * This method assumes that the elements of the array in the above mentioned ranges
	 * are already sorted separately.
	 *
	 * @param sourceArray Array containing the sorted sub-arrays which are to be merged
	 * @param tmpArray    Temporary holding array to be used while merging
	 * @param startIndex  Starting index of the sorted left sub-array
	 * @param midIndex    Middle index at which the array was split [startIndex..midIndex] [midIndex+1..endIndex]
	 * @param endIndex    Ending index of the sorted right sub-array
	 */
	private static <T extends Comparable<? super T>> void merge( T[] sourceArray, T[] tmpArray, int startIndex,
	                                                             int midIndex, int endIndex )
	{
		// Copying the elements within the given index range to the temporary array, so that it can be used
		// for comparison, while putting the elements in sorted order in the original array.
		System.arraycopy( sourceArray, startIndex, tmpArray, startIndex, endIndex + 1 - startIndex );

		int left = startIndex, right = midIndex + 1, cursor = startIndex;

		while( left <= midIndex && right <= endIndex )
		{
			if( tmpArray[ left ].compareTo( tmpArray[ right ] ) <= 0 )
			{
				sourceArray[ cursor ] = tmpArray[ left++ ];
			}
			else
			{
				sourceArray[ cursor ] = tmpArray[ right++ ];
			}

			cursor++;
		}

		// If there are any elements left out in the left sorted sub-array, copy them.
		while( left <= midIndex )
		{
			sourceArray[ cursor++ ] = tmpArray[ left++ ];
		}

		// There is no need to do the same for the right sorted sub-array since we are
		// sorting in-place. Meaning, the right sub-array elements are already in their
		// right place.
	}

	/**
	 * Sort the elements of <code>sourceArray</code> using the Insertion Sort Algorithm.
	 *
	 * @param sourceArray   Array to be sorted.
	 */
	public static <T extends Comparable<? super T>> void nSquareSort( T[] sourceArray )
	{
		for( int i = 0; i < sourceArray.length; i++ )
		{
			for( int j = i + 1; j < sourceArray.length; j++ )
			{
				if( sourceArray[ i ].compareTo( sourceArray[ j ] ) > 0 )
				{
					swap( sourceArray, i, j );
				}
			}
		}
	}

	/**
	 * Helper method to swap two elements in an array.
	 *
	 * @param arr       Array containing the elements to be swapped.
	 * @param index1    Index of the first element to be swapped
	 * @param index2    Index of the second elements to be swapped with the first
	 */
	private static <T extends Comparable<? super T>> void swap( T[] arr, int index1, int index2 )
	{
		T temp = arr[ index1 ];
		arr[ index1 ] = arr[ index2 ];
		arr[ index2 ] = temp;
	}
}