package cs6301.g27;

import cs6301.g00.ArrayIterator;
import cs6301.g00.Graph;

import java.util.Iterator;
import java.util.List;

public class EdmondGraph extends Graph
{
	private EdmondVertex[] vertices;
	private int newN, newM;

	public EdmondGraph( Graph g )
	{
		super( g );
		newN = g.size();
		newM = g.edgeSize();

		vertices = new EdmondVertex[ g.size() << 1 ];
		for( Vertex v : g )
		{
			vertices[ v.getName() ] = new EdmondVertex( v );
		}

		for( Vertex u : g )
		{
			for( Edge e : u )
			{
				Vertex v = e.otherEnd( u );
				EdmondVertex v1 = getVertex( u );
				EdmondVertex v2 = getVertex( v );

				EdmondEdge edge = new EdmondEdge( v1, v2, e.getWeight(), e.getName() );
				v1.adj.add( edge );
				v2.revAdj.add( edge );
			}
		}
	}

	public EdmondVertex getVertex( Vertex u )
	{
		return Vertex.getVertex( vertices, u );
	}

	/*
	* Helpers
	* */

	/**
	 * Disable the vertex with the name {@code i}
	 *
	 * @param i Name of the vertex to be disabled.
	 */
	public void disable( int i )
	{
		EdmondVertex u = ( EdmondVertex ) getVertex( i );
		u.disable();
	}

	/**
	 * Utility method to print the graph to console
	 */
	public void printGraph()
	{
		for( Vertex u : this )
		{
			for( Edge e : u )
			{
				System.out.println( e.stringWithSpaces() );
			}
		}
	}

	/**
	 * Create a new vertex and add it to the graph.
	 *
	 * @return Newly created vertex.
	 */
	public EdmondVertex createNewVertex()
	{
		if( newN >= vertices.length )
		{
			return null;
		}

		vertices[ newN ] = new EdmondVertex( newN );
		return vertices[ newN++ ];
	}

	/**
	 * Utility method to enable the non-zero edges of the graph
	 */
	public void enableNonZeroEdges()
	{
		EdmondEdge.enableNonZeroEdges();
	}

	/**
	 * Utility method to disable the non-zero edges of the graph
	 */
	public void disableNonZeroEdges()
	{
		EdmondEdge.disableNonZeroEdges();
	}

	/*
	* Overrides
	* */

	@Override
	public Edge addEdge( Vertex from, Vertex to, int weight )
	{
		EdmondEdge e = new EdmondEdge( from, to, weight, newM );
		( ( EdmondVertex ) from ).adj.add( e );
		( ( EdmondVertex ) to ).revAdj.add( e );

		newM++;
		return e;
	}

	public EdmondEdge addEdge( Vertex from, Vertex to, EdmondEdge sourceEdge )
	{
		EdmondEdge edge = (EdmondEdge) addEdge( from, to, sourceEdge.getWeight() );
		edge.setSourceEdge( sourceEdge );
		return edge;
	}

	@Override
	public int size()
	{
		return this.newN;
	}

	@Override
	public int edgeSize()
	{
		return this.newM;
	}

	@Override
	public Vertex getVertex( int n )
	{
		return vertices[ n - 1 ];
	}

	@Override
	public void reverseGraph()
	{
		for( Vertex v : this )
		{
			List<EdmondEdge> temp = ( ( EdmondVertex ) v ).adj;
			( ( EdmondVertex ) v ).adj = ( ( EdmondVertex ) v ).revAdj;
			( ( EdmondVertex ) v ).revAdj = temp;
		}
	}

	@Override
	public Vertex[] getVertexArray()
	{
		return this.vertices;
	}

	@Override
	public Iterator<Vertex> iterator()
	{
		return new EdmondGraphIterator( this );
	}

	/*
	* Classes
	* */

	public class EdmondGraphIterator implements Iterator<Vertex>
	{
		Iterator<EdmondVertex> it;
		EdmondVertex current;
		boolean ready;

		EdmondGraphIterator( EdmondGraph g )
		{
			this.it = new ArrayIterator<>( g.vertices, 0, g.newN - 1 );  // Iterate over existing elements only
			ready = false;
		}


		public boolean hasNext()
		{
			if( ready )
			{
				return true;
			}
			if( !it.hasNext() )
			{
				return false;
			}
			current = it.next();
			while( current.isDisabled() && it.hasNext() )
			{
				current = it.next();
			}
			ready = true;
			return !current.isDisabled();
		}

		public Vertex next()
		{
			if( !ready )
			{
				if( !hasNext() )
				{
					throw new java.util.NoSuchElementException();
				}
			}
			ready = false;
			return current;
		}

		public void remove()
		{
			throw new java.lang.UnsupportedOperationException();
		}
	}
}