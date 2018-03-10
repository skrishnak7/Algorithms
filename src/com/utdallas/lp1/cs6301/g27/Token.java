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

/**
 * Represents a token in a mathematical expression.
 * Each token contains a value (in string) and type (<code>TokenType</code>).
 * <p>
 * Example: In the expression 1 + 2 * 3, the tokens are 1, 2, 3, + and *.
 *
 * @see TokenType
 */
public class Token
{
	/**
	 * Type of a Token
	 * <p>
	 * Examples: <br/>
	 * VAR: a, var, idx <br/>
	 * NUM: 1, 982, 123 <br/>
	 * OP:  +, -, *, /, ^, !, %, | <br/>
	 * EQ:  = <br/>
	 * OPEN: ( <br/>
	 * CLOSE: ) <br/>
	 * EOL: ;
	 */
	public enum TokenType
	{
		VAR, NUM, OP, EQ, OPEN, CLOSE, EOL, NZ, ZR
	}

	public String value;
	public TokenType type;

	public Token( String value ) throws IllegalArgumentException
	{
		this.value = value;
		this.type = tokenize( value );
	}

	/**
	 * Given two Tokens that are of type <code>TokenType.OP</code>, compares
	 * their precedence.
	 * <p>
	 * NOTE: The  method assumes that both the tokens are operators.
	 *
	 * @param token1
	 * @param token2
	 *
	 * @return <0 if token1 is of lower precedence than token2<br/>
	 * >0 if token1 is of higher precedence than token2<br/>
	 * 0 otherwise
	 */
	public static int compareOperators( Token token1, Token token2 )
	{
		return precedence( token1 ) - precedence( token2 );
	}

	/**
	 * Returns the precedence of the Token in a integer value, if the
	 * Token is an operator.
	 *
	 * @param token Token representing the operator
	 *
	 * @return Integer value of the precedence of the operator<br/>
	 * -1 if the Token is not an operator
	 */
	private static int precedence( Token token )
	{
		if( token.type != TokenType.OP )
		{
			return -1;
		}
		switch( token.value.charAt( 0 ) )
		{
			case '+':
				return 2;
			case '-':
				return 2;
			case '*':
				return 3;
			case '/':
				return 3;
			case '^':
				return 4;
			case '!':
				return 5;
			default:
				return -1;
		}
	}

	/**
	 * Given a string representing a Token, returns the type of the token.
	 *
	 * @param s String representing the token
	 *
	 * @return  A TokenType enum
	 *
	 * @throws IllegalArgumentException If the string <code>s</code> contains an invalid or
	 * unrecognizable value
	 */
	public static TokenType tokenize( String s ) throws IllegalArgumentException
	{
		if( s.matches( "\\d+" ) )
		{
			return TokenType.NUM;
		}
		else if( s.matches( "[a-z]+" ) )
		{
			return TokenType.VAR;
		}
		else if( s.equals( "+" ) || s.equals( "-" ) || s.equals( "*" ) || s.equals( "/" ) || s.equals( "%" ) ||
				s.equals( "^" ) || s.equals( "|" ) || s.equals( "!" ) )
		{
			return TokenType.OP;
		}
		else if( s.equals( "=" ) )
		{
			return TokenType.EQ;
		}
		else if( s.equals( "(" ) )
		{
			return TokenType.OPEN;
		}
		else if( s.equals( ")" ) )
		{
			return TokenType.CLOSE;
		}
		else if( s.equals( ";" ) )
		{
			return TokenType.EOL;
		}
		else if( s.equals( "?" ))
		{
			return TokenType.NZ;
		}
		else if( s.equals( ":" ) )
		{
			return TokenType.ZR;
		}
		else
		{
			throw new IllegalArgumentException( "Unknown token: " + s );
		}
	}

	/**
	 * Set of utility methods to check for the type of the token.
	 * Entirely for the purpose of more clearer readability.
	 */
	public boolean isOperator()
	{
		return type == TokenType.OP;
	}

	public boolean isNum()
	{
		return type == TokenType.NUM;
	}

	public boolean isOpen()
	{
		return type == TokenType.OPEN;
	}

	public boolean isClose()
	{
		return type == TokenType.CLOSE;
	}

	public boolean isEOL()
	{
		return type == TokenType.EOL;
	}

	public boolean isVar()
	{
		return type == TokenType.VAR;
	}

	public boolean isEQ()
	{
		return type == TokenType.EQ;
	}

	public boolean isNZ()
	{
		return type == TokenType.NZ;
	}

	public boolean isZR()
	{
		return type == TokenType.ZR;
	}

	@Override
	public String toString()
	{
		return value;
	}
}