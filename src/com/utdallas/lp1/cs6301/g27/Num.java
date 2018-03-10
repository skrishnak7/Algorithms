/*
 * Group number: G27
 * Members:
 *  Gayathri Balakumar
 *  Susindaran Elangovan
 *  Vidya Gopalan
 *  Saikrishna Kanukuntla
 *
 * Long Project #1
 */

package cs6301.g27;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Store and perform arithmetic operations on arbitrarily large numbers.
 */
public class Num implements Comparable<Num>, Iterable<Long>
{
	// Although having a power of 2 base speeds up the operations
	// a power of 10 base does a faster base 10 conversion.
	// Thus a power of 10 base is chosen as the default base so that
	// the end user experience is better when printing out the numbers.
	private static final long DEFAULT_BASE = 100000000;
	private static final long INPUT_OUTPUT_BASE = 10;

	// Base in which the number is stored
	private static long BASE = DEFAULT_BASE;

	// Constants to be used in various operations
	private static final Num ZERO = new Num( 0 );
	private static final Num ONE = new Num( 1 );
	private static final Num TWO = new Num ( 2 );

	// Digits of the number in base `BASE` stored in the LSD to MSD order.
	private LinkedList<Long> digits = new LinkedList<>();

	// Denotes if the number is a negative number
	private boolean negative;

	/*
	* Constructors
	* */

	Num()
	{}

	Num( String s )
	{
		int startIndex = 0;
		if( s.charAt( 0 ) == '-' )
		{
			this.negative = true;
			startIndex = 1;
		}

		// Skip leading zeroes
		for( ; startIndex < s.length() && s.charAt( startIndex ) == '0'; startIndex++ );

		this.digits = convertFrom( s.substring( startIndex ), INPUT_OUTPUT_BASE ).digits;
	}

	Num( long x )
	{
		this.digits = convertTo( x, BASE );
	}

	Num( List<Long> digits )
	{
		this.digits = new LinkedList<>( digits );
	}

	/*
	* Arithmetic Operations
	* */

	/**
	 * Add two arbitrarily large numbers and return the sum as Num.
	 *
	 * @param a Addend
	 * @param b Augend
	 *
	 * @return  Sum of Addend and Augend.
	 */
	public static Num add( Num a, Num b )
	{
		return add( a, b, BASE );
	}

	/**
	 * Subtract two arbitrarily large numbers and return the difference
	 * as num.
	 *
	 * @param a Minuend
	 * @param b Subtrahend
	 *
	 * @return Minuend - Subtrahend
	 */
	public static Num subtract( Num a, Num b )
	{
		return subtract( a, b, BASE );
	}

	/**
	 * Product of two arbitrarily large numbers.
	 *
	 * @param a Multiplicand
	 * @param b Multiplier
	 *
	 * @return  Multiplicand * Multiplier
	 */
	public static Num product( Num a, Num b )
	{
		return product( a, b, BASE );
	}

	/**
	 * Raise {@code a} to the power of {@code n}.
	 *
	 * @param a Number whose power of n should be found.
	 * @param n Power factor
	 *
	 * @return a ^ n
	 */
	public static Num power( Num a, long n )
	{
		return power( a, n, BASE );
	}

	/**
	 * Calculate {@code a} to the power of {@code power} using
	 * a divide and conquer algorithm.
	 * <p>
	 * Algorithm is based on the following property
	 * <p>
	 * let power = c0 + (c1 * B) + (c2 * B^2)<br/>
	 * c0 + (c1 * B) + (c2 * B^2) = [c1 + (c2*B)]*B + c0
	 * <p>
	 * let partial = c1 + (c2*B) and lsd = c0<br/>
	 * then, power = partial * B + lsd
	 * <p>
	 * a^power = ((a^partial)^B) * a^lsd
	 *
	 * @param a     Number whose power of {@code power} should be found.
	 * @param power Power factor
	 *
	 * @return a ^ power
	 */
	public static Num power( Num a, Num power )
	{
		if( power.compareTo( ZERO ) < 0 )
		{
			throw new UnsupportedOperationException( "Negative power" );
		}

		Num n = power.copy();
		if( n.digits.size() == 1 )
		{
			return power( a, n.digits.remove() );
		}

		Long lsd = n.digits.removeFirst();
		Num partialPower = power( a, n );
		Num result = product( power( partialPower, BASE ), power( a, lsd ) );

		if( a.negative )
		{
			a.negative = mod( a, TWO ).compareTo( ONE ) == 0;
		}

		return result;
	}

	/**
	 * Divide {@code a} by {@code b}.
	 *
	 * @param a Dividend
	 * @param b Divisor
	 *
	 * @return a / b
	 */
	public static Num divide( Num a, Num b )
	{
		if( b.isZero() )
		{
			throw new IllegalArgumentException( "Divide by zero" );
		}

		if( b.isOne() )
		{
			return a.copy();
		}

		Num result = null;

		// Setting the signs of the numbers to positive, which will be restored
		// later
		boolean aNegative = a.negative, bNegative = b.negative;
		a.negative = b.negative = false;

		int comparison = a.compareTo( b );

		// If a < b return 0
		if( comparison < 0 )
		{
			return ZERO.copy();
		}

		// If a == b return 1
		if( comparison == 0 )
		{
			result = ONE.copy();
		}
		else
		{
			Num low = ONE.copy(), high = a.copy();
			boolean found = false;

			// Using binary search to find the quotient
			while( add( low, ONE ).compareTo( high ) < 0 )
			{
				Num mid = halfNum( add( low, high ) );
				Num product = product( mid, b );
				comparison = product.compareTo( a );
				if( comparison < 0 )
				{
					low = mid;
				}
				else if( comparison > 0 )
				{
					high = mid;
				}
				else
				{
					result = mid;
					found = true;
					break;
				}
			}

			// If perfect quotient is not found, then approximate
			if( !found )
			{
				result = low;
			}
		}

		// Restore the signs of the input numbers
		a.negative = aNegative; b.negative = bNegative;

		// Setting the sign of the result to be negative if the inputs
		// had different signs
		result.negative = a.negative ^ b.negative;

		return result;
	}

	/**
	 * Perform a / b and return the remainder of the division.
	 *
	 * @param a Dividend
	 * @param b Divisor
	 *
	 * @return a % b
	 */
	public static Num mod( Num a, Num b )
	{
		if( a.negative || b.negative )
		{
			throw new IllegalArgumentException( "Negative number" );
		}

		int comparison = a.compareTo( b );

		// If a = 0 or a = b, then a % b = 0
		if( a.isZero() || comparison == 0 )
		{
			return ZERO.copy();
		}

		// a % b = a if a < b
		if( comparison < 0 )
		{
			return a.copy();
		}

		// a%b = a-((a/b) * b)
		return subtract( a, product( divide( a, b ), b ) );
	}

	/**
	 * Find the square root of {@code a}
	 *
	 * @param a Large number
	 *
	 * @return  Square root of a
	 */
	public static Num squareRoot( Num a )
	{
		if( a.compareTo( ZERO ) <= 0 )
		{
			throw new IllegalArgumentException( "Only positive number" );
		}

		Num low = ONE.copy(), high = a.copy();

		// Using binary search to approximate to the square root of `a`
		while( add( low, ONE ).compareTo( high ) < 0 )
		{
			Num mid = halfNum( add( low, high ) );
			Num square = product( mid, mid );
			int comparison = square.compareTo( a );
			if( comparison < 0 )
			{
				low = mid;
			}
			else if( comparison > 0 )
			{
				high = mid;
			}
			else
			{
				return mid;
			}
		}
		return low;
	}

	/**
	 * Find the factorial of an arbitrarily large number
	 *
	 * @param a Large number
	 *
	 * @return a!
	 */
	public static Num factorial( Num a )
	{
		if( a.compareTo( ZERO ) < 0 )
		{
			throw new IllegalArgumentException( "Only non-negative number" );
		}

		Num result = ONE.copy(), counter = ONE.copy();

		while( counter.compareTo( a ) <= 0 )
		{
			result = product( result, counter );
			counter = add( counter, ONE );
		}

		return result;
	}

	/*
	* Arithmetic Operation's helpers
	* */

	/**
	 * Perform addition of the numbers a and b represented in base {@code base}
	 * and return the result in the same base.
	 *
	 * @param a    Addend
	 * @param b    Augend
	 * @param base Base in which the numbers are represented
	 *
	 * @return a + b in base {@code base}.
	 */
	private static Num add( Num a, Num b, long base )
	{
		// If the signs of the input numbers are same, perform normal addition
		if( a.negative == b.negative )
		{
			Iterator<Long> aIterator = a.digits.iterator(), bIterator = b.digits.iterator();
			long aDigit = getNext( aIterator ), bDigit = getNext( bIterator ), carry = 0;

			Num sum = new Num();

			while( ( aDigit > 0 || aIterator.hasNext() ) || ( bDigit > 0 || bIterator.hasNext() ) || carry > 0 )
			{
				long total = aDigit + bDigit + carry;
				sum.digits.add( total % base );
				carry = total / base;
				aDigit = getNext( aIterator );
				bDigit = getNext( bIterator );
			}

			sum.negative = a.negative && b.negative;

			return sum;
		}

		// If the signs are different, then record their original signs and perform
		// subtraction of smallerNum from biggerNum and then restore the signs of the inputs
		// and assign appropriate sign for the result.

		int comparison = absoluteComparison( a, b );
		if( comparison == 0 )
		{
			return ZERO.copy();
		}
		Num bigger = comparison < 0 ? b : a;
		Num smaller = comparison < 0 ? a : b;

		boolean biggerSign = bigger.negative, smallerSign = smaller.negative;

		bigger.negative = false;
		smaller.negative = false;
		Num difference = subtract( bigger, smaller, base );
		bigger.negative = biggerSign;
		smaller.negative = smallerSign;

		difference.negative = bigger.negative;
		return difference;
	}

	/**
	 * Subtract two arbitrarily large numbers represented in base {@code base}
	 * and return the difference represented in the same base.
	 *
	 * @param a Minuend
	 * @param b Subtrahend
	 * @param base Base in which the numbers are represented
	 *
	 * @return a - b
	 */
	private static Num subtract( Num a, Num b, long base )
	{
		Num difference;

		// If either of the numbers is negative, then return the other
		// with appropriate sign.
		if( a.isZero() )
		{
			difference = b.copy();
			difference.negative = !b.negative;
			return difference;
		}

		if( b.isZero() )
		{
			difference = a.copy();
			difference.negative = a.negative;
			return difference;
		}

		// If the inputs have opposite signs, then negate the sign of b and
		// perform addition.
		if( a.negative ^ b.negative )
		{
			b.negative = !b.negative;
			difference = add( a, b, base );
			b.negative = !b.negative;
			return difference;
		}

		int comparison = absoluteComparison( a, b );
		if( comparison == 0 )
		{
			return ZERO;
		}

		Num bigger = comparison > 0 ? a.copy() : b.copy();
		Num smaller = comparison < 0 ? a.copy() : b.copy();

		difference = new Num();

		Iterator<Long> bigIterator = bigger.digits.iterator(), smallIterator = smaller.digits.iterator();
		long bigDigit = getNext( bigIterator ), smallDigit = getNext( smallIterator ), balance = 0;

		while( ( bigDigit > 0 || bigIterator.hasNext() ) )
		{
			long diff = bigDigit - smallDigit - balance;
			if( diff < 0 )
			{
				balance = 1;
				diff += base;
			}
			else
			{
				balance = 0;
			}
			difference.digits.add( diff );

			bigDigit = getNext( bigIterator );
			smallDigit = getNext( smallIterator );
		}

		// The following equation of based on these four possibilities
		// -2-(-3) = +1             +2-(+3) = -1
		// -3-(-2) = -1             +3-(+2) = +1
		difference.negative = ( ( a.negative ? -1 : 1 ) * comparison ) < 1;
		difference.removeLeadingZeroes();

		return difference;
	}

	/**
	 * Multiply the numbers represented in the base {@code base} using the long
	 * multiplication method.
	 *
	 * @param a    Multiplicand
	 * @param b    Multiplier
	 * @param base Base in which the numbers are represented
	 *
	 * @return a * b
	 */
	private static Num longMultiplication( Num a, Num b, long base )
	{
		int comparison = a.compareTo( b );
		Num biggerNum = comparison >= 0 ? a : b;
		Num smallerNum = comparison >= 0 ? b : a;

		if( smallerNum.isZero() )
		{
			return ZERO.copy();
		}

		if( smallerNum.isOne() )
		{
			return biggerNum.copy();
		}

		Num[] levels = new Num[ smallerNum.digits.size() ];
		for( int i = 0; i < levels.length; i++ )
		{
			long carry = 0;
			levels[ i ] = new Num();
			for( int j = 0; j < i; j++ )
			{
				levels[ i ].digits.add( 0L );
			}

			for( long biggerNumDigit : biggerNum.digits )
			{
				long product = biggerNumDigit * smallerNum.digits.get( i ) + carry;
				levels[ i ].digits.add( product % base );
				carry = product / base;
			}

			if( carry > 0 )
			{
				levels[ i ].digits.add( carry );
			}
		}

		Num result = ZERO.copy();
		for( Num level : levels )
		{
			result = add( result, level, base );
		}

		return result;
	}

	/**
	 * Multiply two numbers a and b represented in the base {@code base} using
	 * karatsuba's algorithm.
	 *
	 * @param a    Multiplicand
	 * @param b    Multiplier
	 * @param base Base in which the numbers are represented
	 *
	 * @return a * b
	 */
	private static Num product( Num a, Num b, long base )
	{
		Num result = karatsuba( a.copy(), b.copy(), base );
		result.negative = a.negative ^ b.negative;
		return result;
	}

	/**
	 * Implementation of karatsuba's algorithm to multiply two large numbers
	 * represented in base {@code base}.
	 *
	 * @param a    Multiplicand
	 * @param b    Multiplier
	 * @param base Base in which the numbers are represented
	 *
	 * @return a * b
	 */
	private static Num karatsuba( Num a, Num b, long base )
	{
		if( a.isZero() || b.isZero() )
		{
			return ZERO.copy();
		}

		// Optimization: Remove the trailing zeroes in the inputs and add them
		//              after the multiplication is completed.
		int aTrailingZeroes = a.removeTrailingZeroes();
		int bTrailingZeroes = b.removeTrailingZeroes();

		Num result;

		if( Math.min( a.digits.size(), b.digits.size() ) < 4 )
		{
			result = longMultiplication( a, b, base );
		}
		else
		{
			int halfPosition = Math.min( a.digits.size(), b.digits.size() ) / 2;

			Num XL = new Num( a.digits.subList( 0, halfPosition ) );
			Num XR = new Num( a.digits.subList( halfPosition, a.digits.size() ) );

			Num YL = new Num( b.digits.subList( 0, halfPosition ) );
			Num YR = new Num( b.digits.subList( halfPosition, b.digits.size() ) );

			Num p1 = product( XL, YL, base ), p2 = product( XR, YR, base ),
					p3 = product( add( XL, XR, base ), add( YL, YR, base ), base );
			Num difference = subtract( subtract( p3, p2, base ), p1, base );

			// p2*Base^(2*mid)
			leftShiftByK( p2, 2 * halfPosition );

			// difference*Base^(mid)
			leftShiftByK( difference, halfPosition );

			result = add( add( p2, difference, base ), p1, base );
		}

		// Adding the trailing zeroes removed in the beginning.
		leftShiftByK( result, aTrailingZeroes + bTrailingZeroes );

		return result;
	}

	/**
	 * Calculate a to the power of n, where a is a large number represented
	 * in base {@code base}.
	 * <p>
	 * The algorithm to compute the power is as follows: <br/>
	 * if n is even <br/>
	 * a^n = (a^n/2)*(a^n/2)<br/>
	 * if n is odd <br/>
	 * a^n = a*(a^n/2)*(a^n/2)
	 *
	 * @param a    Large number
	 * @param n    Power factor
	 * @param base Base in which the number is represented
	 *
	 * @return a^n
	 */
	private static Num power( Num a, long n, long base )
	{
		if( n < 0 )
		{
			throw new UnsupportedOperationException( "Negative power" );
		}

		if( n == 0 )
		{
			return ONE.copy();
		}

		if( n == 1 )
		{
			return a.copy();
		}

		Num half = power( a, n / 2, base );

		Num result = product( half, half, base );

		if( n % 2 == 1 )
		{
			result = product( a, result, base );
			result.negative = a.negative;
		}

		return result;
	}

	/**
	 * Divide {@code num} by 2.
	 *
	 * @param num Number to be divided by 2.
	 *
	 * @return num / 2
	 */
	private static Num halfNum( Num num )
	{
		// If base is even, half of the number is (num*base/2) and then right shift once.
		if( BASE % 2 == 0 )
		{
			Num result = product( num, new Num( BASE / 2 ) );
			result.digits.removeFirst();
			return result;
		}
		// For odd base, usual long division is performed.
		else
		{
			Num result = new Num(), dividend = num.copy();
			long carry = 0;

			while( !dividend.digits.isEmpty() )
			{
				long currentDigit = carry * BASE + dividend.digits.removeLast();
				long quotient = currentDigit / 2;
				carry = currentDigit % 2;
				result.digits.addFirst( quotient );
			}

			result.removeLeadingZeroes();

			return result;
		}
	}

	/*
	* Private utilities
	* */

	/**
	 * Convert the input number {@code x} to base {@code toBase}
	 *
	 * @param x      Number to be converted to different base
	 * @param toBase Base to which {@code x} should be converted to
	 *
	 * @return x in base toBase
	 */
	private static LinkedList<Long> convertTo( long x, long toBase )
	{
		LinkedList<Long> digits = new LinkedList<>();
		while( x > 0 )
		{
			digits.add( x % toBase );
			x /= toBase;
		}
		return digits;
	}

	/**
	 * Converted a number in the form of a string represented in the base {@code fromBase}
	 * to a {@code Num} object.
	 *
	 * @param input     Number to be converted
	 * @param fromBase  Base of the input number
	 *
	 * @return  {@code input} as a {@code Num} object.
	 */
	private static Num convertFrom( String input, long fromBase )
	{
		Num result = ZERO.copy(), ioBase = new Num( fromBase );
		for( char c : input.toCharArray() )
		{
			long val = c - '0';
			result = add( product( result, ioBase ), new Num( val ) );
		}
		return result;
	}

	/**
	 * Remove trailing zeroes in the number by removing zeroes in the start
	 * of the linked list representing the digits.
	 *
	 * @return Number after the trailing zeroes are removed
	 */
	private int removeTrailingZeroes()
	{
		if( this.isZero() )
		{
			return 0;
		}

		int trailingZeroes = 0;
		while( !this.digits.isEmpty() )
		{
			if( this.digits.getFirst() == 0 )
			{
				this.digits.removeFirst();
				trailingZeroes++;
			}
			else
			{
				break;
			}
		}

		return trailingZeroes;
	}

	/**
	 * Remove leading zeroes in the number by removing zeroes in the end
	 * of the linked list representing the digits.
	 *
	 * @return Number after the leading zeroes are removed
	 */
	private int removeLeadingZeroes()
	{
		if( this.isZero() )
		{
			return 0;
		}

		int leadingZeroes = 0;
		while( !this.digits.isEmpty() )
		{
			if( this.digits.getLast() == 0 )
			{
				this.digits.removeLast();
				leadingZeroes++;
			}
			else
			{
				break;
			}
		}

		return leadingZeroes;
	}

	/**
	 * Utility method to get the next element of the iterator, if at all
	 * there exists another element in the list, else return 0.
	 *
	 * @param iterator Iterator from which the next element should be retrieved
	 *
	 * @return  Next element in the iterator if it exists, else 0.
	 */
	private static long getNext( Iterator<Long> iterator )
	{
		return iterator.hasNext() ? iterator.next() : 0;
	}

	/*
	* Public utilities
	* */

	/**
	 * Make of the copy of this number.
	 *
	 * @return  Copy of this number.
	 */
	public Num copy()
	{
		Num num = new Num( this.digits );
		num.negative = this.negative;
		return num;
	}

	/**
	 * Check if this number is zero.
	 *
	 * @return  True if the number is equal to zero
	 */
	public boolean isZero()
	{
		return this.digits == null || this.digits.size() == 0 ||
				( this.digits.size() == 1 && this.digits.getFirst() == 0 );
	}

	/**
	 * Check if this number is one.
	 *
	 * @return  True if the number is equal to one
	 */
	public boolean isOne()
	{
		return this.digits.size() == 1 && this.digits.getFirst() == 1;
	}

	/**
	 * Print the base of this number followed by the digits of this number in
	 * the order of lease significant digit to most significant digit.
	 */
	public void printList()
	{
		StringBuilder stringBuilder = new StringBuilder( BASE + ":" );

		if( this.isZero() )
		{
			stringBuilder.append( " 0" );
		}
		else if( this.negative )
		{
			stringBuilder.append( " -" );
		}

		for( Long digit : digits )
		{
			stringBuilder.append( " " ).append( digit );
		}
		System.out.println( stringBuilder );
	}

	/**
	 * Compare the magnitudes of two arbitrarily large numbers.
	 *
	 * @param a Large number
	 * @param b Large number
	 *
	 * @return  Negative number if |a| < |b|, positive number is |a| > |b|, else 0.
	 */
	public static int absoluteComparison( Num a, Num b )
	{
		int sizeDiff = ( a.digits.size() - b.digits.size() );
		if( sizeDiff != 0 )
		{
			return sizeDiff;
		}

		Iterator<Long> aIterator = a.digits.descendingIterator(), bIterator = b.digits.descendingIterator();
		long aDigit = getNext( aIterator ), bDigit = getNext( bIterator );

		while( aDigit == bDigit && aIterator.hasNext() )
		{
			aDigit = getNext( aIterator );
			bDigit = getNext( bIterator );
		}

		return ( int ) ( aDigit - bDigit );
	}

	/**
	 * Left-shift a large number k times by adding k zeroes to the start
	 * of the list representing its digits.
	 *
	 * @param a Number to be left-shifted
	 * @param k left-shift times
	 */
	public static void leftShiftByK( Num a, int k )
	{
		for( int i = 0; i < k; i++ )
		{
			a.digits.addFirst( 0L );
		}
	}

	/**
	 * @return Base used by the Num class.
	 */
	public static long getBase()
	{
		return BASE;
	}

	/**
	 * Change the base of the Num class.
	 *
	 * @param base New base
	 */
	public static void changeBaseTo( long base )
	{
		if( base >= 2 && base <= Integer.MAX_VALUE )
		{
			Num.BASE = base;
		}
		else
		{
			throw new IllegalArgumentException( "Allowed base values: 2 to 2147483647" );
		}
	}

	/**
	 * Base on the input {@code operator} perform the appropriate binary arithmetic operation.
	 *
	 * @param operator Binary arithmetic operator
	 * @param a        Input number
	 * @param b        Input number
	 *
	 * @return Result of the binary arithmetic operation
	 */
	public static Num performBinaryOperation( String operator, Num a, Num b )
	{
		switch( operator )
		{
			case "+":
				return add( a, b );
			case "-":
				return subtract( a, b );
			case "*":
				return product( a, b );
			case "/":
				return divide( a, b );
			case "%":
				return mod( a, b );
			case "^":
				return power( a, b );
			default:
				throw new UnsupportedOperationException( "Unrecognized operator " + operator );
		}
	}

	/**
	 * Base on the input {@code operator} perform the appropriate unary arithmetic operation.
	 *
	 * @param operator Unary arithmetic operator
	 * @param a        Input number
	 *
	 * @return Result of the unary arithmetic operation
	 */
	public static Num performUnaryOperation( String operator, Num a )
	{
		switch( operator )
		{
			case "|":
				return squareRoot( a );
			case "!":
				return factorial( a );
			default:
				throw new UnsupportedOperationException( "Unrecognized operator" + operator );
		}
	}

	/*
	* Overrides
	* */

	@Override
	public int compareTo( Num other )
	{
		if( this.negative != other.negative )
		{
			return this.negative ? -1 : 1;
		}
		int sign = this.negative ? -1 : 1;
		return absoluteComparison( this, other ) * sign;
	}

	@Override
	public Iterator<Long> iterator()
	{
		return this.digits.iterator();
	}

	@Override
	public String toString()
	{
		if( isZero() ) return "0";
		Num actualBase = new Num( convertTo( BASE, INPUT_OUTPUT_BASE ) );

		Num result = ZERO.copy();

		Iterator<Long> sourceIterator = this.digits.descendingIterator();
		while( sourceIterator.hasNext() )
		{
			Num term = new Num( convertTo( sourceIterator.next(), INPUT_OUTPUT_BASE ) );
			result = product( result, actualBase, INPUT_OUTPUT_BASE );
			result = add( result, term, INPUT_OUTPUT_BASE );
		}

		StringBuilder sb = new StringBuilder( this.negative ? "-" : "" );

		Iterator<Long> iterator = result.digits.descendingIterator();
		while( iterator.hasNext() )
		{
			sb.append( iterator.next() );
		}

		return sb.toString();
	}

	public static void main( String[] args ) throws FileNotFoundException
	{
		Scanner in;
		if( args.length == 2 )
		{
			in = new Scanner( new File( args[ 0 ] ) );
			Num.changeBaseTo( Long.parseLong( args[1] ) );
		}
		else if( args.length == 1 )
		{
			if( args[0].matches( "\\d+" ) )
			{
				in = new Scanner( System.in );
				Num.changeBaseTo( Long.parseLong( args[0] ) );
			}
			else
			{
				in = new Scanner( new File( args[ 0 ] ) );
			}
		}
		else
		{
			in = new Scanner( System.in );
		}

		Num a = null, b = null, result = null;

		System.out.println("1. Num + Num");
		System.out.println("2. Num - Num");
		System.out.println("3. Num * Num");
		System.out.println("4. Num ^ long");
		System.out.println("5. Num ^ Num");
		System.out.println("6. Num / Num");
		System.out.println("7. Num % Num");
		System.out.println("8. Num | (Sqrt)");
		System.out.println("9. Num ! (Fact)");

		int choice = Integer.parseInt( in.next() );
		while( ( choice >= 1 && choice <= 9 ) )
		{
			try
			{
				switch( choice )
				{
					case 1:
						a = new Num( in.next() ); b = new Num( in.next() );
						result = add( a, b );
						result.printList();
						System.out.println( result );
						break;
					case 2:
						a = new Num( in.next() ); b = new Num( in.next() );
						result = subtract( a, b );
						result.printList();
						System.out.println( result );
						break;
					case 3:
						a = new Num( in.next() ); b = new Num( in.next() );
						result = product( a, b );
						result.printList();
						System.out.println( result );
						break;
					case 4:
						a = new Num( in.next() );
						result = power( a, Long.parseLong( in.next() ) );
						result.printList();
						System.out.println( result );
						break;
					case 5:
						a = new Num( in.next() ); b = new Num( in.next() );
						result = power( a, b );
						result.printList();
						System.out.println( result );
						break;
					case 6:
						a = new Num( in.next() ); b = new Num( in.next() );
						result = divide( a, b );
						result.printList();
						System.out.println( result );
						break;
					case 7:
						a = new Num( in.next() ); b = new Num( in.next() );
						result = mod( a, b );
						result.printList();
						System.out.println( result );
						break;
					case 8:
						a = new Num( in.next() );
						result = squareRoot( a );
						result.printList();
						System.out.println( result );
						break;
					case 9:
						a = new Num( in.next() );
						result = factorial( a );
						result.printList();
						System.out.println( result );
						break;
				}
			}
			catch( Exception e )
			{
				System.out.println( "Exception: " + e.getMessage() );
			}
			if( in.hasNext() )
			{
				choice = Integer.parseInt( in.next() );
			}
			else
			{
				break;
			}
		}
	}
}