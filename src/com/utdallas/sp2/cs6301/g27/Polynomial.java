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
import java.util.LinkedList;
import java.util.Scanner;

/**
 * A Polynomial equation represented by a Linked List of Polynomial Terms ordered by the exponent value.
 */
public class Polynomial implements Iterable<Polynomial.PolynomialTerm>
{
	/**
	 * A Polynomial term represented by its coefficient and exponent.
	 */
	public static class PolynomialTerm implements Comparable<PolynomialTerm>
	{
		int coefficient, exponent;

		public PolynomialTerm( int coefficient, int exponent )
		{
			this.coefficient = coefficient;
			this.exponent = exponent;
		}

		@Override
		public int compareTo( PolynomialTerm polynomialTerm )
		{
			return this.exponent - polynomialTerm.exponent;
		}

		@Override
		public String toString()
		{
			return coefficient + ( exponent > 0 ? "x^" + exponent : "" );
		}

		/**
		 * Add two polynomial terms by summing their coefficients.
		 *
		 * @param addend
		 * @param augend
		 *
		 * @return  New Polynomial term that is the sum of addend and augend.
		 */
		public static PolynomialTerm add( PolynomialTerm addend, PolynomialTerm augend )
		{
			return new PolynomialTerm( addend.coefficient + augend.coefficient, addend.exponent );
		}

		/**
		 * Multiply two polynomial terms by multiplying their coefficients and adding the exponents.
		 *
		 * @param multiplicand
		 * @param multiplier
		 *
		 * @return  New polynomial term that is the product of multiplicand and multiplier.
		 */
		public static PolynomialTerm multiply( PolynomialTerm multiplicand, PolynomialTerm multiplier )
		{
			return new PolynomialTerm( multiplicand.coefficient * multiplier.coefficient,
					multiplicand.exponent + multiplier.exponent );
		}
	}

	private LinkedList<PolynomialTerm> terms;

	public Polynomial()
	{
		terms = new LinkedList<>();
	}

	public Polynomial( Scanner in )
	{
		this();
		int n = in.nextInt();
		for( int i = 0; i < n; i++ )
		{
			int coefficient = in.nextInt(), exponent = in.nextInt();
			PolynomialTerm term = new PolynomialTerm( coefficient, exponent );
			terms.add( term );
		}
	}

	/**
	 * Add a term to the end of the polynomial.
	 * <p>
	 * This new term's exponent value should be greater than the
	 * polynomial's last term's exponent value.
	 *
	 * @param term Term to be added to the end of the polynomial
	 */
	public void addTerm( PolynomialTerm term )
	{
		terms.add( term );
	}

	@Override
	public Iterator<PolynomialTerm> iterator()
	{
		return terms.iterator();
	}

	@Override
	public String toString()
	{
		if( terms.size() == 0 )
		{
			return "";
		}
		StringBuilder sb = new StringBuilder( terms.get( 0 ).toString() );
		for( int i = 1; i < terms.size(); i++ )
		{
			sb.append( " + " ).append( terms.get( i ) );
		}
		return sb.toString();
	}
}