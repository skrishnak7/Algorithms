/*
 * Group number: G27
 * Members:
 *  Gayathri Balakumar
 *  Susindaran Elangovan
 *  Vidya Gopalan
 *  Saikrishna Kanukuntla
 *
 * Short Project #3
 */

package cs6301.g27;

import cs6301.g00.Graph;
import cs6301.g00.GraphAlgorithm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class TopologicalOrdering extends GraphAlgorithm<TopologicalOrdering.TVertex>
{
	class TVertex
	{
		int inDegree;
		int topological;

		TVertex()
		{
			inDegree = -1;
			topological = -1;
		}
	}

	public TopologicalOrdering( Graph g )
	{
		super( g );
		node = new TVertex[ g.size() ];
		for( Graph.Vertex u : g )
		{
			node[ u.getName() ] = new TVertex();
		}
	}

	public List<Graph.Vertex> getTopologicalOrder( Graph g )
	{
		int topNum = 0;
		Queue<Graph.Vertex> q = new LinkedList<>();
		LinkedList<Graph.Vertex> topList = new LinkedList<>();

		for( Graph.Vertex u : g )
		{
			TVertex du = getVertex( u );
			du.inDegree = u.revAdj.size();
			if( du.inDegree == 0 )
			{
				q.add( u );
			}
		}

		while( !q.isEmpty() )
		{
			Graph.Vertex u = q.remove();
			getVertex( u ).topological = ++topNum;
			topList.add( u );
			for( Graph.Edge e : u )
			{
				Graph.Vertex v = e.otherEnd( u );
				TVertex dv = getVertex( v );
				dv.inDegree--;
				if( dv.inDegree == 0 )
				{
					q.add( v );
				}
			}
		}

		if( topNum != g.size() )
		{
			return null;
		}

		return topList;
	}

	public static void main( String[] args )
	{

		Scanner in = new Scanner( System.in );
		Graph g = Graph.readDirectedGraph( in );

		TopologicalOrdering run = new TopologicalOrdering( g );
		List<Graph.Vertex> topologicalOrder = run.getTopologicalOrder( g );

		if( topologicalOrder == null )
		{
			System.out.println("Graph is not a DAG");
		}
		else
		{
			System.out.println( Arrays.toString( topologicalOrder.toArray() ) );
		}
	}
}

/*
 *
Input:
9
13
8 1 0
8 3 0
8 9 0
1 2 0
1 7 0
3 2 0
3 7 0
9 3 0
9 5 0
2 4 0
7 4 0
7 6 0
5 6 0

Output:
[8, 1, 9, 3, 5, 2, 7, 4, 6]
*/
