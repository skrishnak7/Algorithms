/*
 * Group number: G27
 * Members:
 *  Gayathri Balakumar
 *  Susindaran Elangovan
 *  Vidya Gopalan
 *  Saikrishna Kanukuntla
 *
 * Short Project #2
 */

package cs6301.g27;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SetOperations
{
	/**
	 * Given two sets(<code>list1</code> and <code>list2</code>) of sorted elements, the intersection
	 * of the two sets in populated in the <code>outList</code> parameter list.
	 *
	 * @param list1   First list of sorted elements
	 * @param list2   Second list of sorted elements
	 * @param outList Output list that contains the intersection of list1 and list2
	 */
	public static <T extends Comparable<? super T>> void intersect( List<T> list1, List<T> list2, List<T> outList )
	{
		if( list1.size() == 0 || list2.size() == 0 )
		{
			return;
		}

		Iterator<T> l1Iterator = list1.iterator(), l2Iterator = list2.iterator();
		T element1 = getNext( l1Iterator ), element2 = getNext( l2Iterator );

		while( element1 != null && element2 != null )
		{
			int comparison = element1.compareTo( element2 );
			if( comparison < 0 )
			{
				element1 = getNext( l1Iterator );
			}
			else if( comparison > 0 )
			{
				element2 = getNext( l2Iterator );
			}
			else
			{
				outList.add( element1 );
				element1 = getNext( l1Iterator );
				element2 = getNext( l2Iterator );
			}
		}
	}

	/**
	 * Given two sets(<code>list1</code> and <code>list2</code>) of sorted elements, this method
	 * finds the union of both the sets.
	 *
	 * @param list1   First list of sorted elements
	 * @param list2   Second list of sorted elements
	 * @param outList Output list that contains the union of list1 and list2
	 */
	// Return the union of l1 and l2, in sorted order.
	// Output is a set, so it should have no duplicates.
	public static <T extends Comparable<? super T>> void union( List<T> list1, List<T> list2, List<T> outList )
	{
		Iterator<T> l1Iterator = list1.iterator(), l2Iterator = list2.iterator();
		T element1 = getNext( l1Iterator );
		T element2 = getNext( l2Iterator );

		while( element1 != null && element2 != null )
		{
			int comparison = element1.compareTo( element2 );
			T elementToAdd;
			if( comparison < 0 )
			{
				elementToAdd = element1;
				element1 = getNext( l1Iterator );
			}
			else if( comparison > 0 )
			{
				elementToAdd = element2;
				element2 = getNext( l2Iterator );
			}
			else
			{
				elementToAdd = element1;
				element1 = getNext( l1Iterator );
				element2 = getNext( l2Iterator );
			}

			addUniqueToList( outList, elementToAdd );
		}

		while( element1 != null )
		{
			addUniqueToList( outList, element1 );
			element1 = getNext( l1Iterator );
		}

		while( element2 != null )
		{
			addUniqueToList( outList, element2 );
			element2 = getNext( l2Iterator );
		}
	}

	/**
	 * Given two sets(<code>list1</code> and <code>list2</code>) of sorted elements, this method
	 * returns list1 - list2 (i.e., items in list1 that are not in list2), in sorted order.
	 *
	 * @param list1   First list of sorted elements
	 * @param list2   Second list of sorted elements
	 * @param outList Output list that should contain the results of list1 - list2
	 */
	public static <T extends Comparable<? super T>> void difference( List<T> list1, List<T> list2, List<T> outList )
	{
		if( list1.size() == 0 )
		{
			return;
		}
		else if( list2.size() == 0 )
		{
			outList.addAll( list1 );
			return;
		}

		Iterator<T> l1Iterator = list1.iterator(), l2Iterator = list2.iterator();
		T element1 = l1Iterator.next(), element2 = l2Iterator.next();

		while( element1 != null && element2 != null )
		{
			int comparison = element1.compareTo( element2 );
			if( comparison < 0 )
			{
				outList.add( element1 );
				element1 = getNext( l1Iterator );
			}
			else if( comparison > 0 )
			{
				element2 = getNext( l2Iterator );
			}
			else
			{
				element1 = getNext( l1Iterator );
				element2 = getNext( l2Iterator );
			}
		}

		while( element1 != null )
		{
			outList.add( element1 );
			element1 = getNext( l1Iterator );
		}
	}

	/**
	 * Utility method to add an element to the list if it is not already present in the list.
	 * <p>
	 * Note: The list is assumed to be already sorted, thus this method only checks the last
	 * element in the list for duplication.
	 *
	 * @param list         The list to which the new element should be added to
	 * @param elementToAdd New element to be added to the list
	 */
	private static <T extends Comparable<? super T>> void addUniqueToList( List<T> list, T elementToAdd )
	{
		if( list.size() == 0 || list.get( list.size() - 1 ).compareTo( elementToAdd ) < 0 )
		{
			list.add( elementToAdd );
		}
	}

	/**
	 * Utility method to get the next element in the iterator, if present, else null.
	 * @param iterator  Iterator to be used
	 *
	 * @return  Next element in the iterator, if present, otherwise null.
	 */
	private static <T> T getNext( Iterator<T> iterator )
	{
		return iterator.hasNext() ? iterator.next() : null;
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

		int n1 = in.nextInt(), n2 = in.nextInt();
		List<Integer> list1 = new LinkedList<>(), list2 = new LinkedList<>();
		for( int i = 0; i < n1; i++ )
		{
			list1.add( in.nextInt() );
		}

		for( int i = 0; i < n2; i++ )
		{
			list2.add( in.nextInt() );
		}

		List<Integer> outList = new LinkedList<>();

		intersect( list1, list2, outList );
		System.out.println( "Intersection: " + Arrays.toString( outList.toArray() ) );

		outList.clear();
		union( list1, list2, outList );
		System.out.println( "Union: " + Arrays.toString( outList.toArray() ) );

		outList.clear();
		difference( list1, list2, outList );
		System.out.println( "L1 - L2: " + Arrays.toString( outList.toArray() ) );
	}
}