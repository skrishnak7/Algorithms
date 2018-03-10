/*
 * Group number: G27
 * Members:
 *  Gayathri Balakumar
 *  Susindaran Elangovan
 *  Vidya Gopalan
 *  Saikrishna Kanukuntla
 *
 * Long Project #2
 */

package cs6301.g27;

import cs6301.g00.Graph;
import cs6301.g00.Timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class LP2
{
	static int VERBOSE = 1;

	public static void main( String[] args ) throws FileNotFoundException
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
		int start = 1;
		if( args.length > 1 )
		{
			start = Integer.parseInt( args[ 1 ] );
		}
		if( args.length > 2 )
		{
			VERBOSE = Integer.parseInt( args[ 2 ] );
		}
		Graph g = Graph.readDirectedGraph( in );
		Graph.Vertex startVertex = g.getVertex( start );

		Timer timer = new Timer();
		Euler euler = new Euler( g, startVertex );
		euler.setVerbose( VERBOSE );

		boolean eulerian = euler.isEulerian();
		if( !eulerian )
		{
			return;
		}

		List<Graph.Edge> tour = euler.findEulerTour();
		timer.end();

		if( VERBOSE > 0 )
		{
			System.out.println( "Output:\n_________________________" );
			for( Graph.Edge e : tour )
			{
				System.out.print( e );
			}
			System.out.println();
			System.out.println( "_________________________" );
		}
		System.out.println( timer );
		System.out.println("Valid tour: " + validateTour( g, tour ));
	}

	public static boolean validateTour( Graph g, List<Graph.Edge> tour )
	{
		boolean[] seen = new boolean[g.edgeSize() + 1];
		Graph.Edge prev = null;

		for( Graph.Edge e : tour )
		{
			if( seen[e.getName()] ) return false;
			if( prev != null && !prev.toVertex().equals( e.fromVertex() ))
			{
				return false;
			}

			seen[ e.getName() ] = true;
			prev = e;
		}

		for( Graph.Vertex v : g )
		{
			for( Graph.Edge e : v )
			{
				if( !tour.contains( e ) )
				{
					System.out.println(e.stringWithSpaces());
				}
			}
		}

		System.out.println( Math.abs( tour.size() - g.edgeSize() ) );

		return prev != null && tour.get( 0 ).fromVertex().equals( prev.toVertex() ) && tour.size() == g.edgeSize();
	}
}

/*
9
13
1 2 1
2 3 1
3 4 1
3 1 1
4 5 1
4 7 1
5 6 1
5 7 1
6 3 1
7 8 1
7 9 1
8 4 1
9 5 1

1: (1,2)(2,3)(3,4)(4,5)(5,6)(6,3)(3,1)
4: (4,7)(7,8)(8,4)
5: (5,7)(7,9)(9,5)

(1,2)(2,3)(3,4)(4,7)(7,8)(8,4)(4,5)(5,7)(7,9)(9,5)(5,6)(6,3)(3,1)

/home/susindaran/Desktop/impltest/lp2-big.txt 3 0
* */