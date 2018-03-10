package cs6301.g27;

import cs6301.g00.Graph;
import cs6301.g00.GraphAlgorithm;
import cs6301.g00.Timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class KruskalMST extends GraphAlgorithm<KruskalMST.KruskalVertex>
{
	class KruskalVertex implements DisjointSetElement<KruskalVertex>
	{
		KruskalVertex parent;
		int rank;

		@Override
		public KruskalVertex getParent()
		{
			return this.parent;
		}

		@Override
		public void setParent( KruskalVertex parent )
		{
			this.parent = parent;
		}

		@Override
		public int getRank()
		{
			return this.rank;
		}

		@Override
		public void setRank( int rank )
		{
			this.rank = rank;
		}
	}

	private static final Comparator<Graph.Edge> EDGE_COMPARATOR = Comparator.comparingInt( Graph.Edge::getWeight );

	private DisjointSet<KruskalVertex> disjointSet;

	public KruskalMST( Graph g )
	{
		super( g );
		node = new KruskalVertex[ g.size() ];

		disjointSet = new DisjointSet<>();

		for( Graph.Vertex u : g )
		{
			node[ u.getName() ] = new KruskalVertex();
			disjointSet.makeSet( node[ u.getName() ] );
		}
	}

	public int findMST( List<Graph.Edge> mstEdges )
	{
		List<Graph.Edge> sortedEdges = new ArrayList<>();

		for( Graph.Vertex v : g )
		{
			for( Graph.Edge e : v )
			{
				sortedEdges.add( e );
			}
		}

		sortedEdges.sort( EDGE_COMPARATOR );

		int mstWeight = 0;
		for( Graph.Edge e : sortedEdges )
		{
			KruskalVertex ru = disjointSet.find( getVertex( e.fromVertex() ) );
			KruskalVertex rv = disjointSet.find( getVertex( e.toVertex() ) );

			if( ru != rv )
			{
				mstWeight += e.getWeight();
				mstEdges.add( e );
				disjointSet.union( ru, rv );
			}
		}

		return mstWeight;
	}

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

		Graph g = Graph.readGraph( in );
		Graph.Vertex s = g.getVertex( 1 );
		List<Graph.Edge> mstEdges = new ArrayList<>();

		Timer timer = new Timer();
		KruskalMST mst = new KruskalMST( g );
		int weightMST = mst.findMST( mstEdges );
		timer.end();
		System.out.println( weightMST );
		System.out.println( Arrays.toString( mstEdges.toArray() ) );

		System.out.println( timer );
	}
}

/*
9 14
1 2 4
2 3 8
3 4 7
4 5 9
5 6 10
6 7 2
7 8 1
8 1 8
2 8 11
8 9 7
3 9 2
9 7 6
3 6 4
4 6 14
* */