/*
 * Group number: G27
 * Members:
 *  Gayathri Balakumar
 *  Susindaran Elangovan
 *  Vidya Gopalan
 *  Saikrishna Kanukuntla
 *
 * Short Project #6
 */

package cs6301.g27;

import cs6301.g00.Graph;
import cs6301.g00.GraphAlgorithm;
import cs6301.g00.Timer;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class PrimMST extends GraphAlgorithm<PrimMST.PrimVertex>
{
	class PrimVertex implements Comparator<PrimVertex>, Index
	{
		boolean seen;
		Graph.Vertex baseVertex;

		// Weight of the shortest edge to one of the MST vertices
		int d;

		// Index of this vertex in the Indexed Heap
		int index;

		// Parent of this vertex in the MST
		PrimVertex parent;

		PrimVertex( Graph.Vertex u )
		{
			seen = false;
			parent = null;
			d = INFINITY;
			baseVertex = u;
			index = -1;
		}

		/**
		 * Reset all the properties of this vertex to their defaults
		 */
		public void reset()
		{
			seen = false;
			parent = null;
			d = INFINITY;
			index = -1;
		}

		@Override
		public void putIndex( int index )
		{
			this.index = index;
		}

		@Override
		public int getIndex()
		{
			return this.index;
		}

		@Override
		public int compare( PrimVertex u, PrimVertex v )
		{
			return u.d - v.d;
		}

		@Override
		public String toString()
		{
			return "( " + baseVertex + ", " + d + ", " + index + " )";
		}
	}

	/**
	 * Default value for the {@code d} attribute of the PrimVertices
	 */
	private static final int INFINITY = Integer.MAX_VALUE;
	private static final Comparator<Graph.Edge> EDGE_COMPARATOR = Comparator.comparingInt( Graph.Edge::getWeight );

	public PrimMST( Graph g )
	{
		super( g );
		node = new PrimVertex[ g.size() ];

		for( Graph.Vertex u : g )
		{
			node[ u.getName() ] = new PrimVertex( u );
		}
	}

	/**
	 * Constructs a minimum spanning tree (implicitly stored by parent pointers) starting
	 * from the vertex {@code s }, using a priority queue of edges.
	 *
	 * @param s Start vertex of the MST
	 *
	 * @return Weight of the MST
	 */
	public int prim1( Graph.Vertex s )
	{
		int weightMST = 0;

		PriorityQueue<Graph.Edge> pq = new PriorityQueue<>( EDGE_COMPARATOR );

		PrimVertex source = getVertex( s );
		source.seen = true;

		for( Graph.Edge e : s )
		{
			pq.offer( e );
		}

		while( !pq.isEmpty() )
		{
			Graph.Edge e = pq.poll();
			PrimVertex v;

			// Checking if the edge is connecting an MST vertex with a non-MST vertex
			// else discarding
			if( !getVertex( e.fromVertex() ).seen )
			{
				v = getVertex( e.fromVertex() );
			}
			else if( !getVertex( e.toVertex() ).seen )
			{
				v = getVertex( e.toVertex() );
			}
			else
			{
				continue;
			}

			v.seen = true;
			v.parent = getVertex( e.otherEnd( v.baseVertex ) );
			weightMST += e.getWeight();

			// Adding all the edges connecting to non-MST vertices to the priority queue
			for( Graph.Edge edge : v.baseVertex )
			{
				if( !getVertex( edge.otherEnd( v.baseVertex ) ).seen )
				{
					pq.offer( edge );
				}
			}
		}

		return weightMST;
	}

	/**
	 * Constructs a minimum spanning tree (implicitly stored by parent pointers) starting
	 * from the vertex {@code s }, using a indexed heap of vertices.
	 *
	 * @param s Start vertex of the MST
	 *
	 * @return Weight of the MST
	 *
	 * @throws Exception If the heap operations have went out of bounds.
	 */
	public int prim2( Graph.Vertex s ) throws Exception
	{
		int weightMST = 0;

		PrimVertex source = getVertex( s );
		source.d = 0;

		IndexedHeap<PrimVertex> heap = new IndexedHeap<>( Arrays.copyOf( node, node.length ),
				Comparator.comparingInt( vertex -> vertex.d ), node.length );

		while( !heap.isEmpty() )
		{
			PrimVertex u = heap.deleteMin();
			u.seen = true;
			weightMST += u.d;

			for( Graph.Edge edge : u.baseVertex )
			{
				PrimVertex v = getVertex( edge.otherEnd( u.baseVertex ) );
				if( !v.seen && edge.getWeight() < v.d )
				{
					v.d = edge.getWeight();
					v.parent = u;
					heap.decreaseKey( v );
				}
			}
		}

		return weightMST;
	}

	/**
	 * Resets the graph's vertices' properties back to their defaults.
	 */
	public void reset()
	{
		for( PrimVertex vertex : node )
		{
			vertex.reset();
		}
	}

	public static void main( String[] args ) throws Exception
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

		Graph g = Graph.readGraph( in );
		Graph.Vertex s = g.getVertex( 1 );

		Timer timer = new Timer();
		PrimMST mst = new PrimMST( g );

		System.out.println( "Normal Priority Queue" );
		int prim1Weight = mst.prim1( s );
		timer.end();
		System.out.println( "Weight of MST: " + prim1Weight );
		System.out.println( timer );

		mst.reset();

		System.out.println( "\nIndexed binary heap" );
		timer.start();
		int prim2Weight = mst.prim2( s );
		timer.end();
		System.out.println( "Weight of MST: " + prim2Weight );
		System.out.println( timer );

	}
}

/*
Sample input:
6 10
1 2 6
1 4 5
1 3 1
2 5 3
2 3 5
5 3 8
3 4 5
4 6 2
3 6 4
5 6 6
* */