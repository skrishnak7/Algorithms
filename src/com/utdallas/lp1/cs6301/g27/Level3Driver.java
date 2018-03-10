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
import java.util.Scanner;

public class Level3Driver
{
	public static void main( String[] args ) throws FileNotFoundException, IllegalArgumentException
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

		Num[] variables = new Num[26];

		// State booleans to represent if a new line is seen or if we are
		// currently recording a post-fix expression.
		boolean newLine = true, recordingExpression = false;

		// Holder variable to keep track of the currently evaluated variable
		char currentVariable = 'a';

		// Holder variable to record the expression in a statement
		PostfixExpression expression = new PostfixExpression();

		while( in.hasNext() )
		{
			Token token = new Token( in.next() );

			if( newLine )
			{
				newLine = false;
				expression = new PostfixExpression();
				if( token.isEOL() )
				{
					// To handle empty input programs (with just a semi-colon)
					if( variables[ currentVariable - 'a' ] != null )
					{
						variables[ currentVariable - 'a' ].printList();
					}
					break;
				}

				// Just a redundant check
				if( token.isVar() )
				{
					// Since the grammar specifies variable names will be single small case alphabet
					currentVariable = token.value.charAt( 0 );
				}
				else
				{
					throw new IllegalArgumentException("Invalid input format");
				}
			}
			else if( recordingExpression )
			{
				if( token.isEOL() )
				{
					variables[ currentVariable - 'a' ] = expression.evaluate( variables );
					System.out.println( variables[ currentVariable - 'a' ] );

					recordingExpression = false;
					newLine = true;
				}
				else
				{
					expression.addToken( token );
				}
			}
			else
			{
				if( token.isEQ())
				{
					recordingExpression = true;
				}
				else if( token.isEOL() )
				{
					newLine = true;
					System.out.println( variables[ currentVariable - 'a' ] );
				}
				else
				{
					throw new IllegalArgumentException( "Invalid input format" );
				}
			}
		}
	}
}