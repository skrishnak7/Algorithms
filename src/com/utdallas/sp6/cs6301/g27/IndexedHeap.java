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

import java.util.Comparator;

public class IndexedHeap<T extends Index> extends BinaryHeap<T>
{
	/**
	 * Build a binary heap with a given array q, using q[0..n-1].
	 *
	 * @param q    Array of elements to be converted into a heap
	 * @param comp Comparator to be used when ordering the elements
	 * @param n    Current size of the heap (array)
	 */
	public IndexedHeap( T[] q, Comparator<T> comp, int n )
	{
		super( q, comp, n );
	}

	/**
	 * Restore heap's order property after the priority of x has increased
	 *
	 * @param x Element with the increased priority
	 */
	public void decreaseKey( T x )
	{
		percolateUp( x.getIndex() );
	}

	@Override
	protected void move( int i, T x )
	{
		super.move( i, x );
		x.putIndex( i );
	}

	/**
	 * Build the heap using the bottom up approach.
	 *
	 * Even though leaf nodes have no need to be percolated down, we still start
	 * the loop from size - 1 index because the {@code move()} method could do something
	 * that we might miss out on for the leaf nodes if we don't percolate them
	 * just for the sake of it.
	 */
	@Override
	protected void buildHeap()
	{
		for( int i = size - 1; i >= 0; i-- )
		{
			percolateDown( i );
		}
	}
}