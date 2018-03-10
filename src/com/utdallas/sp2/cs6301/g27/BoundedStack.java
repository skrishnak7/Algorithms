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
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * An Array-based, bounded-sized stack.
 */
public class BoundedStack
{
	private static final int DEFAULT_SIZE = 1 << 4;

	private int[] array;
	private int size;
	private int top;

	/**
	 * Creates a stack with the default size of 16
	 */
	public BoundedStack()
	{
		this( DEFAULT_SIZE );
	}

	/**
	 * Create a stack with the given size.
	 *
	 * @param size Size of the stack
	 */
	public BoundedStack( int size )
	{
		this.size = size;
		array = new int[ this.size ];
		this.top = -1;
	}

	/**
	 * Push an element to the top of the stack.
	 *
	 * @param item Item to be pushed
	 *
	 * @throws IllegalStateException If the stack is already full
	 */
	public void push( int item ) throws IllegalStateException
	{
		if( isFull() )
		{
			throw new IllegalStateException( "Stack is full" );
		}

		array[ ++top ] = item;
	}

	/**
	 * Remove an element from the top of the stack.
	 *
	 * @return Element removed from the top of the stack
	 *
	 * @throws NoSuchElementException If the stack is empty
	 */
	public int pop() throws NoSuchElementException
	{
		if( isEmpty() )
		{
			throw new NoSuchElementException( "Stack is empty" );
		}

		return array[ top-- ];
	}

	/**
	 * Returns the element in the top of stack, but doesn't remove it.
	 *
	 * @return Element in the top of the stack.
	 *
	 * @throws NoSuchElementException If the stack is empty
	 */
	public int peek() throws NoSuchElementException
	{
		if( isEmpty() )
		{
			throw new NoSuchElementException( "Stack is empty" );
		}

		return array[ top ];
	}

	/**
	 * @return Current size of the stack, i.e., number of elements
	 * in the stack at the given moment.
	 */
	public int size()
	{
		return top + 1;
	}

	public boolean isEmpty()
	{
		return top < 0;
	}

	public boolean isFull()
	{
		return top == size - 1;
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

		BoundedStack stack = new BoundedStack();

		System.out.println( "1. Create stack of size n" );
		System.out.println( "2. Push element to stack" );
		System.out.println( "3. Pop element from stack" );
		System.out.println( "4. Peek" );
		System.out.println( "5. Print size of stack" );

		int choice;
		boolean terminate = false;
		do
		{
			choice = in.nextInt();
			switch( choice )
			{
				case 1:
					stack = new BoundedStack( in.nextInt() );
					break;
				case 2:
					try
					{
						stack.push( in.nextInt() );
					}
					catch( IllegalStateException e )
					{
						System.out.println( e.getMessage() );
					}
					break;
				case 3:
					try
					{
						System.out.println( stack.pop() );
					}
					catch( NoSuchElementException e )
					{
						System.out.println( e.getMessage() );
					}
					break;
				case 4:
					try
					{
						System.out.println( stack.peek() );
					}
					catch( NoSuchElementException e )
					{
						System.out.println( e.getMessage() );
					}
					break;
				case 5:
					System.out.println( stack.size() );
					break;
				default:
					terminate = true;
			}
		} while( !terminate );
	}
}