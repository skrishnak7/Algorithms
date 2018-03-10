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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SCC extends GraphAlgorithm<SCC.SCCVertex>
{
	class SCCVertex
	{
		boolean seen;
		Graph.Vertex parent;
		int discovery;
		int finish;
		int componentNumber;

		SCCVertex()
		{
			seen = false;
			parent = null;
			discovery = -1;
			finish = -1;
			componentNumber = -1;
		}
	}

	private boolean reverseEdges;

	public SCC( Graph g, boolean reverseEdges )
	{
		super( g );
		node = new SCCVertex[ g.size() ];
		this.reverseEdges = reverseEdges;

		for( Graph.Vertex u : g )
		{
			node[ u.getName() ] = new SCCVertex();
		}
	}

	public LinkedList<Graph.Vertex> dfs( Iterator<Graph.Vertex> it )
	{
		int time = 0;
		LinkedList<Graph.Vertex> decFinList = new LinkedList<>();

		int componentNumber = 0;
		while( it.hasNext() )
		{
			Graph.Vertex u = it.next();
			if( !seen( u ) )
			{
				visit( null, u );
				time = DFSVisit( u, time, ++componentNumber, decFinList );
			}
		}

		return decFinList;
	}

	private int DFSVisit( Graph.Vertex u, int time, int componentNumber, LinkedList<Graph.Vertex> decFinList )
	{
		SCCVertex du = getVertex( u );
		du.discovery = ++time;
		du.componentNumber = componentNumber;

		List<Graph.Edge> edges = this.reverseEdges ? u.revAdj : u.adj;
		for( Graph.Edge e : edges )
		{
			Graph.Vertex v = e.otherEnd( u );
			if( !seen( v ) )
			{
				visit( u, v );
				time = DFSVisit( v, time, componentNumber, decFinList );
			}
		}

		du.finish = ++time;
		decFinList.addFirst( u );

		return time;
	}

	private void visit( Graph.Vertex parent, Graph.Vertex u )
	{
		SCCVertex vertex = getVertex( u );
		vertex.parent = parent;
		vertex.seen = true;
	}

	private boolean seen( Graph.Vertex u )
	{
		return getVertex( u ).seen;
	}
	
	public static int stronglyConnectedComponents(Graph g)
	{
		SCC firstRun = new SCC( g, false );
		LinkedList<Graph.Vertex> topologicalOrder = firstRun.dfs( g.iterator() );

		SCC secondRun = new SCC( g, true );
		secondRun.dfs( topologicalOrder.iterator() );

		int sccNumber = 0;
		for( SCC.SCCVertex vertex : secondRun.node )
		{
			sccNumber = Math.max( sccNumber, vertex.componentNumber );
		}

		return sccNumber;
	}

	public static void main( String[] args )
	{
		Scanner in = new Scanner( System.in );
		Graph g = Graph.readDirectedGraph( in );
		System.out.println( "Number of strongly connected components: " + stronglyConnectedComponents( g ) );
	}
}

/*
8
14
1 2 0
5 1 0
2 5 0
2 6 0
5 6 0
2 3 0
6 7 0
7 6 0
3 7 0
3 4 0
4 3 0
4 8 0
8 4 0
8 7 0
* */