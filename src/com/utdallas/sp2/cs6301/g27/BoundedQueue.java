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
import java.util.Scanner;

/**
 * An Array-based, bounded-sized Queue
 */
class BoundedQueue
{
	private static final Integer MINIMUM_SIZE = 16;

	private Integer[] elements;
	private int size, currentSize;
	private int front, rear;

	/**
	 * Creates a new queue with the given size.
	 * <p>
	 * The size of the queue will be max(16, given size)
	 *
	 * @param size Size of the new queue to be created
	 */
	public BoundedQueue( int size )
	{
		this.size = Math.max( MINIMUM_SIZE, size );
		elements = new Integer[ this.size ];
		this.currentSize = 0;
		this.front = 0;
		this.rear = -1;
	}

	public boolean isEmpty()
	{
		return this.currentSize == 0;
	}

	public boolean isFull()
	{
		return this.currentSize == size;
	}

	/**
	 * Inserts the specified element into this queue if it is possible to do so immediately without violating capacity restrictions.
	 * <p>
	 * Element will be inserted to the end of the queue.
	 *
	 * @param element Element to be inserted
	 *
	 * @return True if the element is added to the queue, else false.
	 */
	public boolean offer( Integer element )
	{
		if( isFull() )
		{
			return false;
		}

		this.currentSize++;
		rear = rear + 1 % size;
		elements[ rear ] = element;
		return true;
	}

	/**
	 * Retrieves and removes the head of this queue, or returns null if this queue is empty.
	 *
	 * @return the head of this queue, or null if this queue is empty
	 */
	public Integer poll()
	{
		if( isEmpty() )
		{
			return null;
		}

		Integer element = elements[ front ];
		front = front + 1 % size;
		currentSize--;
		return element;
	}

	/**
	 * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
	 *
	 * @return the head of this queue, or null if this queue is empty
	 */
	public Integer peek()
	{
		if( isEmpty() )
		{
			return null;
		}

		return elements[ front ];
	}

	public int size()
	{
		return currentSize;
	}

	/**
	 * Checks if the current size of the queue is filled more than 90% or less than 25%.
	 * <p>
	 * Accordingly doubles or halves the size of the queue.
	 *
	 * @return the size of the queue. Maybe smaller or bigger than the previous size if
	 * the resizing was performed.
	 */
	public int resize()
	{
		Integer newSize = this.size;
		if( this.currentSize > 0.9 * this.size )
		{
			newSize = 2 * this.size;
		}
		if( this.currentSize < 0.25 * this.size )
		{
			newSize = ( this.size / 2 ) < MINIMUM_SIZE ? MINIMUM_SIZE : this.size / 2;
		}

		if( newSize != this.size )
		{
			Integer[] newArray = new Integer[ newSize ];

			for( int k = 0; k < currentSize; k++ )
			{
				newArray[ k ] = elements[ front ];
				front = ( front + 1 ) % size;
			}

			elements = newArray;
			front = 0;
			rear = currentSize - 1;
			size = newSize;
		}
		return size;
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

		BoundedQueue queue = new BoundedQueue( MINIMUM_SIZE );

		System.out.println( "1. New Queue of size n" );
		System.out.println( "2. Offer new element" );
		System.out.println( "3. Poll an element from queue" );
		System.out.println( "4. Peek" );
		System.out.println( "5. Current size of the queue" );
		System.out.println( "6. Resize the queue" );

		int choice;
		boolean terminate = false;
		do
		{
			choice = in.nextInt();
			switch( choice )
			{
				case 1:
					queue = new BoundedQueue( in.nextInt() );
					break;
				case 2:
					System.out.println( queue.offer( in.nextInt() ) );
					break;
				case 3:
					System.out.println( queue.poll() );
					break;
				case 4:
					System.out.println( queue.peek() );
					break;
				case 5:
					System.out.println( queue.size() );
					break;
				case 6:
					System.out.println( queue.resize() );
					break;
				default:
					terminate = true;
			}
		} while( !terminate );
	}
}