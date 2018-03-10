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

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

import cs6301.g27.Polynomial.PolynomialTerm;

public class SparsePolynomialOperations
{
	public static Polynomial add( Polynomial p1, Polynomial p2 )
	{
		Polynomial sum = new Polynomial();

		Iterator<PolynomialTerm> p1Iterator = p1.iterator(), p2Iterator = p2.iterator();
		PolynomialTerm t1 = getNext( p1Iterator ), t2 = getNext( p2Iterator );

		while( t1 != null || t2 != null )
		{
			if( t1 == null )
			{
				sum.addTerm( t2 );
				t2 = getNext( p2Iterator );
				continue;
			}

			if( t2 == null )
			{
				sum.addTerm( t1 );
				t1 = getNext( p1Iterator );
				continue;
			}

			int compareValue = t1.compareTo( t2 );
			if( compareValue < 0 )
			{
				sum.addTerm( t1 );
				t1 = getNext( p1Iterator );
			}
			else if( compareValue > 0 )
			{
				sum.addTerm( t2 );
				t2 = getNext( p2Iterator );
			}
			else
			{
				sum.addTerm( PolynomialTerm.add( t1, t2 ) );
				t1 = getNext( p1Iterator );
				t2 = getNext( p2Iterator );
			}
		}

		return sum;
	}

	public static Polynomial multiply( Polynomial p1, Polynomial p2 )
	{
		// Min Heap to hold all the multiplication terms. Since the polynomial terms
		// are comparable by their exponents, the heap will give the natural ordering
		// of the terms when polled.
		PriorityQueue<PolynomialTerm> queue = new PriorityQueue<>();

		for( PolynomialTerm multiplicand : p1 )
		{
			for( PolynomialTerm multiplier : p2 )
			{
				queue.offer( PolynomialTerm.multiply( multiplicand, multiplier ) );
			}
		}

		Polynomial product = new Polynomial();

		// Holder variable to keep track of the last polynomial added to the resulting
		// product polynomial.
		PolynomialTerm prevTerm = null;

		while( !queue.isEmpty() )
		{
			PolynomialTerm term = queue.poll();

			// If the new term polled from the heap has the same exponent value as the
			// previous term we simply add their coefficients.
			if( prevTerm != null && prevTerm.compareTo( term ) == 0 )
			{
				prevTerm.coefficient += term.coefficient;
			}
			else
			{
				product.addTerm( term );
				prevTerm = term;
			}
		}

		return product;
	}

	public static long evaluate( Polynomial polynomial, int xValue )
	{
		long value = 0;
		for( PolynomialTerm term : polynomial )
		{
			if( term.exponent == 0 )
			{
				value += term.coefficient;
			}
			else
			{
				value += ( term.coefficient * Math.pow( xValue, term.exponent ) );
			}
		}

		return value;
	}

	private static PolynomialTerm getNext( Iterator<PolynomialTerm> iterator )
	{
		return iterator.hasNext() ? iterator.next() : null;
	}

	public static void main( String[] args )
	{
		Scanner in = new Scanner( System.in );
		Polynomial p1 = new Polynomial( in ), p2 = new Polynomial( in );

		System.out.println( "P1: " + p1 );
		System.out.println( "P2: " + p2 );

		Polynomial sum = add( p1, p2 );
		System.out.println( "Addition: " + sum );

		Polynomial product = multiply( p1, p2 );
		System.out.println( "Multiplication: " + product );

		int xValue = in.nextInt();
		System.out.println( "P1(" + xValue + "): " + evaluate( p1, xValue ));
	}
}