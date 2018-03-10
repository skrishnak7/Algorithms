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

import java.util.*;

/**
 * A program represented by a set of arithmetic expressions (both in infix and post-fix form)
 * and zero-check conditional branching statements, that could be executed in a top-down fashion.
 */
public class ArithmeticProgram
{
	/**
	 * An hash-map to map the line numbers to their actual line-position
	 * in the program
	 */
	private Map<Integer, Integer> lineNumberIndexMap;

	/**
	 * List of statements of the program
	 * */
	private List<ProgramStatement> statements;

	/**
	 * Holder to store the values of variables after evaluation of expressions.
	 * Using an array here instead of a map, because the input grammar ensures that variables will
	 * be of one character length.
	 * */
	private Num[] variables;

	public ArithmeticProgram()
	{
		this.lineNumberIndexMap = new HashMap<>();
		this.statements = new ArrayList<>();
		this.variables = new Num[ 26 ];
	}

	/**
	 * Read the program from the input scanner {@code in} using a state machine.
	 *
	 * The state machine can be visualized using the following matrix
	 *
	 * <pre>
	 *                OP/()    Num     Var      =       ;       ?       :
	 *      0           -       2       3       -       T       -       -
	 *      2           -       -       3       -       -       -       -
	 *      3           -       -       -       5       0       6       -
	 *      4           0       0       0       0       0       0       0
	 *      5           55      55      55      -       0       -       -
	 *      55          55      55      55      -       4       -       -
	 *      6           -       7       -       -       -       -       -
	 *      7           -       -       -       -       -       0       8
	 *      8           -       -       9       -       -       -       -
	 *      9           -       -       -       -       0       -       -
	 *
	 * Legend:
	 *
	 * Start State  -> 0
	 * T            -> Terminate
	 * OP/()        -> + - / * ^ % | ! ( )
	 * Num          -> 0|[1-9][0-9]*
	 * Var          -> [a-z]
	 * </pre>
	 *
	 * @param in Input scanner
	 */
	public void readStatements( Scanner in )
	{
		int state = 0;

		Token token = null;
		while( true )
		{
			state = executeState( state, token );
			token = new Token( in.next() );
			state = changeState( state, token );
			if( state == -1 )
			{
				break;
			}
		}

		// Removing the last statement added because the grammar proactively creates
		// a new statement when it sees a semi-colon
		this.statements.remove( this.statements.size() - 1 );
	}

	/**
	 * The statements of the program are executed in a top-down fashion using a Program Counter, which is
	 * used to jump to previous or future statements when executing conditional statements.
	 */
	public void executeProgram()
	{
		int programCounter = 0;
		Num currentVariableNum = null;
		while( programCounter < statements.size() )
		{
			ProgramStatement currentStatement = statements.get( programCounter );
			currentVariableNum = getVariableValue( currentStatement );
			if( currentStatement.isCondition )
			{
				if( !currentVariableNum.isZero() )
				{
					programCounter = lineNumberIndexMap.get( currentStatement.ifTrueJumpTo );
				}
				else
				{
					// If the variable is zero, then check if the statement has a else clause.
					// If there is one use that line number to jump to, else go to the next statement.
					programCounter = currentStatement.elseJumpTo != null ?
							lineNumberIndexMap.get( currentStatement.elseJumpTo ) : programCounter + 1;
				}
			}
			else
			{
				// If the statement has an expression, evaluate it and store it in memory(Num array)
				// and also update the current variable holder
				if( currentStatement.expression != null )
				{
					currentVariableNum
							= variables[ getVariableIndex( currentStatement.targetVariable ) ]
							= currentStatement.expression.evaluate( variables );
				}

				if( currentStatement.lineNumber == null )
				{
					System.out.println( currentVariableNum );
				}
				programCounter++;
			}
		}

		if( currentVariableNum != null )
		{
			currentVariableNum.printList();
		}
	}

	/**
	 * Perform the necessary action to record the input based on the current {@code state}
	 * <p>
	 * After performing the action, some states might require to perform additional tasks without
	 * needing to read a token from the input, in such cases this method will return that intermediate
	 * state's number.
	 * <p>
	 * For example, once an entire infix expression has been recorded, we need to spend a state to convert
	 * the expression to postfix before starting a new line.
	 *
	 * @param state Current state of the machine
	 * @param token Input token
	 *
	 * @return The current state of the machine, or an intermediate state if required.
	 */
	private int executeState( int state, Token token )
	{
		ProgramStatement statement = statements.size() > 0 ? statements.get( statements.size() - 1 ) : new ProgramStatement();
		switch( state )
		{
			case 0:
				statements.add( new ProgramStatement() );
				break;
			case 2:
				int lineNumber = Integer.parseInt( token.value );
				lineNumberIndexMap.put( lineNumber, statements.size() - 1 );
				statement.lineNumber = lineNumber;
				break;
			case 3:
				statement.targetVariable = token.value;
				break;
			case 4:
				if( statement.lineNumber != null )
				{
					List<Token> tokens = statement.expression.getTokens();
					statement.expression = new PostfixExpression( ShuntingYard.toPostfix( tokens ) );
				}
				return executeState( 0, token );
			case 5:
				break;
			case 55:
				if( statement.expression == null )
				{
					statement.expression = new PostfixExpression();
				}
				statement.expression.addToken( token );
				break;
			case 6:
				statement.isCondition = true;
				break;
			case 7:
				statement.ifTrueJumpTo = Integer.parseInt( token.value );
				statement.elseJumpTo = null;
				break;
			case 8:
				break;
			case 9:
				statement.elseJumpTo = Integer.parseInt( token.value );
				break;
		}

		return state;
	}

	/**
	 * Determine the next state the machine should move to depending on the current state
	 * and the next input token
	 *
	 * @param currentState Current state of the machine
	 * @param token        Next input token
	 *
	 * @return Next state of the machine
	 */
	private int changeState( int currentState, Token token )
	{
		switch( currentState )
		{
			case 0:
				if( token.isNum() )
				{
					return 2;
				}
				if( token.isVar() )
				{
					return 3;
				}
				if( token.isEOL() )
				{
					return -1;
				}
				break;
			case 2:
				if( token.isVar() )
				{
					return 3;
				}
				break;
			case 3:
				if( token.isEOL() )
				{
					return 0;
				}
				if( token.isEQ() )
				{
					return 5;
				}
				if( token.isNZ() )
				{
					return 6;
				}
				break;
			case 4:
				// This case should never occur. When executing state 4, it should itself change to 0
				return 0;
			case 5:
				if( token.isEOL() )
				{
					return 0;
				}
				return 55;
			case 55:
				if( token.isEOL() )
				{
					return 4;
				}
				return 55;
			case 6:
				if( token.isNum() )
				{
					return 7;
				}
				break;
			case 7:
				if( token.isZR() )
				{
					return 8;
				}
				else if( token.isEOL() )
				{
					return 0;
				}
				break;
			case 8:
				if( token.isNum() )
				{
					return 9;
				}
				break;
			case 9:
				if( token.isEOL() )
				{
					return 0;
				}
				break;
		}

		throw new IllegalArgumentException( "Invalid input format" );
	}

	private Num getVariableValue( ProgramStatement statement )
	{
		return variables[ getVariableIndex( statement.targetVariable ) ];
	}

	private int getVariableIndex( String variableName )
	{
		return variableName.charAt( 0 ) - 'a';
	}
}