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
import java.util.*;

public class ShuntingYard
{
	/**
	 * Given an infix expression with each token separated by space, returns the
	 * postfix expression by using the Shunting Yard Algorithm.
	 *
	 * @param infix The infix expression to be converted to postfix
	 *
	 * @return List of Tokens in the postfix order
	 *
	 * @throws IllegalArgumentException Thrown when the input infix expression contains an
	 *                   invalid token.
	 */
	public static List<Token> toPostfix( String infix ) throws IllegalArgumentException
	{
		// Cleanup the input expression by trimming the trailing and leading extra spaces
		// and removing more then one spaces between tokens.
		// Then splitting it by spaces to get an array of tokens.
		String[] tokens = infix.trim().replaceAll( "( )+", "$1" ).split( "\\s" );

		List<Token> infixTokens = new ArrayList<>();
		for( String token : tokens )
		{
			infixTokens.add( new Token( token ) );
		}

		return toPostfix( infixTokens );
	}

	public static List<Token> toPostfix( List<Token> infixTokens ) throws IllegalArgumentException
	{
		List<Token> output = new ArrayList<>();
		Deque<Token> stack = new ArrayDeque<>();

		for( Token token : infixTokens )
		{
			// If the token is a number or var or if it's the ! operator we add it directly
			// to the output.
			if( token.isNum() || token.isVar() || token.value.equals( "!" ) || token.value.equals( "|" ) )
			{
				output.add( token );
			}
			else if( token.isOpen() )
			{
				stack.push( token );
			}
			else if( token.isClose() )
			{
				// Pop the elements from the stacks and onto the output until we see
				// the open parenthesis
				while( !stack.isEmpty() && !stack.peek().isOpen() )
				{
					output.add( stack.pop() );
				}
				stack.pop();
			}
			else
			{
				// Whenever we see an operator we check the top of the stack to see if we have any operator
				// that has a precedence lesser than or equal to the current operator at hand.
				// Except ^ operator, because it is a right associative operator so we only need to pop it out
				// of the stack only if we see a operator of strictly higher precedence, like '!'.
				// Since we push the '!' operator directly to output whenever we see it, '^' are never gonna get
				// popped out of the stack unless they are inside parenthesis.
				while( !stack.isEmpty() && stack.peek().isOperator() &&
						Token.compareOperators( token, stack.peek() ) <= 0 && !( token.value.equals( "^" ) ) )
				{
					output.add( stack.pop() );
				}
				stack.push( token );
			}
		}

		// Move the remaining operators in the stack to the output
		while( !stack.isEmpty() )
		{
			output.add( stack.pop() );
		}

		return output;
	}

	public static void main( String args[] ) throws Exception
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

		String equation = in.nextLine();
		System.out.println( Arrays.toString( toPostfix( equation ).toArray() ) );
	}

	/*
	* Sample input: 3 + 4 * 2 ! /  ( 1 - 5 ) ^ 2 ^ 3 + 275 ( 123 - 10 ! ) ! / 2
	* */
}
