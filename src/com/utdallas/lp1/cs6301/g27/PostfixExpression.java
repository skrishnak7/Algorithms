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

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents an arithmetic expression in the post-fix form.
 */
public class PostfixExpression implements Iterable<Token>
{
	/**
	 * Tokens of the arithmetic expression in the post-fix order.
	 */
	private List<Token> tokens;

	public PostfixExpression()
	{
		this.tokens = new LinkedList<>();
	}

	public PostfixExpression( List<Token> tokens )
	{
		this.tokens = tokens;
	}

	/**
	 * Evaluate the post-fix expression using an array of large numbers each representing
	 * the value of a variable that may be used in the expression.
	 * <p>
	 * A variable name can be a single small-case english alphabet.
	 * <p>
	 * Hence, the value of each variable can be accessed from the array using the following
	 * indexing operation: <br/>
	 * <p>
	 * variables[ variableName - 'a' ]
	 *
	 * @param variables Values of variables
	 *
	 * @return Result of the expression after evaluation
	 */
	public Num evaluate( Num[] variables )
	{
		ArrayDeque<Num> stack = new ArrayDeque<>();

		for( Token token : this )
		{
			if( token.isVar() )
			{
				stack.push( variables[ token.value.charAt( 0 ) - 'a' ] );
			}
			else if( token.isNum() )
			{
				stack.push( new Num( token.value ) );
			}
			else if( token.isOperator() )
			{
				if( token.value.equals( "|" ) || token.value.equals( "!" ) )
				{
					Num a = stack.pop();
					stack.push( Num.performUnaryOperation( token.value, a ) );
				}
				else
				{
					Num b = stack.pop();
					Num a = stack.pop();
					stack.push( Num.performBinaryOperation( token.value, a, b ) );
				}
			}
		}

		return stack.pop();
	}

	/**
	 * Add a token to the end of the expression.
	 *
	 * @param token Token to be inserted at the end of the expression.
	 */
	public void addToken( Token token )
	{
		this.tokens.add( token );
	}

	/**
	 * @return The tokens of the expression
	 */
	public List<Token> getTokens()
	{
		return this.tokens;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for( Token token : tokens )
		{
			sb.append( token.value ).append( " " );
		}
		sb.deleteCharAt( sb.length() - 1 );
		return sb.toString();
	}

	@Override
	public Iterator<Token> iterator()
	{
		return this.tokens.iterator();
	}
}