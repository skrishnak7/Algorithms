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
 * Statement representing a line of an arithmetic program, containing all the necessary
 * information to execute that line of the program.
 */
public class ProgramStatement
{
	Integer lineNumber;
	String targetVariable;

	PostfixExpression expression;

	/**
	 * To denote if this statement is a zero-check-jump-to condition.
	 */
	boolean isCondition;

	/**
	 * If it's a conditional statement, then these two variables will hold
	 * the jump-to line numbers.
	 */
	Integer ifTrueJumpTo, elseJumpTo;

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder( targetVariable );

		if( lineNumber != null )
		{
			sb.insert( 0, lineNumber + " " );
		}

		if( isCondition )
		{
			sb.append( " ? " ).append( ifTrueJumpTo );
			if( elseJumpTo != null )
			{
				sb.append( " : " ).append( elseJumpTo );
			}
		}
		else if( expression != null )
		{
			sb.append( " = " ).append( expression );
		}

		sb.append( " ;" );
		return sb.toString();
	}
}