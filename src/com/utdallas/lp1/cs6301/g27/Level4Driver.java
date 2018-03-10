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
import java.util.*;

public class Level4Driver
{
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

		ArithmeticProgram program = new ArithmeticProgram();
		program.readStatements( in );
		program.executeProgram();
	}
}