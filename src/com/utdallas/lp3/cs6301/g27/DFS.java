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

import java.util.*;


public class DFS extends GraphAlgorithm<DFS.DFSVertex>
{
	class DFSVertex
	{
		boolean seen;
		Graph.Vertex parent;
		int discovery;
		int finish;
		boolean onStack;
		int componentNumber;

		DFSVertex()
		{
			reset();
		}

		public void reset()
		{
			seen = false;
			parent = null;
			discovery = -1;
			finish = -1;
			onStack = false;
			componentNumber = -1;
		}
	}

	private LinkedList<Graph.Vertex> decFinList;
	private int componentNumber;

	public DFS( Graph g )
	{
		super( g );
		node = new DFSVertex[ g.size() ];

		for( Graph.Vertex u : g )
		{
			node[ u.getName() ] = new DFSVertex();
		}
	}

	public List<Graph.Vertex> dfs()
	{
		return dfs( g.iterator() );
	}

	public List<Graph.Vertex> dfs( Iterator<Graph.Vertex> it )
	{
		int time = 0;
		this.decFinList = new LinkedList<>();
		this.componentNumber = 0;

		while( it.hasNext() )
		{
			Graph.Vertex u = it.next();
			if( !seen( u ) )
			{
				++componentNumber;
				visit( null, u );
				time = DFSVisit( u, time );
			}
		}

		return decFinList;
	}

	public int stronglyConnectedComponents()
	{
		dfs();
		List<Graph.Vertex> topologicalOrder = decFinList;

		this.resetGraph();
		this.g.reverseGraph();

		dfs( topologicalOrder.iterator() );

		this.g.reverseGraph();
		return this.componentNumber;
	}

	public List<Graph.Vertex> topologicalOrder()
	{
		dfs();

		for( DFSVertex vertex : node )
		{
			if( vertex.onStack )
			{
				return null;
			}
		}

		return decFinList;
	}

	public void resetGraph()
	{
		for( DFSVertex v : node )
		{
			if( v != null )
			{
				v.reset();
			}
		}
	}

	private int DFSVisit( Graph.Vertex u, int time )
	{
		DFSVertex du = getVertex( u );
		du.discovery = ++time;
		du.componentNumber = this.componentNumber;
		du.onStack = true;

		boolean backEdgePresent = false;
		for( Graph.Edge e : u )
		{
			Graph.Vertex v = e.otherEnd( u );
			if( !seen( v ) )
			{
				visit( u, v );
				time = DFSVisit( v, time );
			}
			else if( getVertex( v ).onStack )
			{
				backEdgePresent = true;
			}
		}

		if( !backEdgePresent )
		{
			du.onStack = false;
		}
		du.finish = ++time;
		this.decFinList.addFirst( u );

		return time;
	}

	private void visit( Graph.Vertex parent, Graph.Vertex u )
	{
		DFSVertex vertex = getVertex( u );
		vertex.parent = parent;
		vertex.seen = true;
	}

	private boolean seen( Graph.Vertex u )
	{
		return getVertex( u ).seen;
	}

	public static void main( String[] args )
	{

		Scanner in = new Scanner( System.in );
		Graph g = Graph.readDirectedGraph( in );
		DFS run = new DFS( g );
		List<Graph.Vertex> topologicalOrder = run.topologicalOrder();
		if( topologicalOrder == null )
		{
			System.out.println( "Graph is not a DAG" );
		}
		else
		{
			System.out.println( Arrays.toString( topologicalOrder.toArray() ) );
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
[8, 9, 5, 3, 1, 7, 6, 2, 4]
 */
}