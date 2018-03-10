package test;

import cs6301.g00.ArrayIterator;

import java.util.Iterator;

public class Graph<V extends BaseVertex, E extends BaseEdge> implements Iterable<V>
{
	V[] vertices;
	int n;
	boolean directed;

	@Override
	public Iterator<V> iterator()
	{
		return new ArrayIterator<>( vertices );
	}

	public Graph( V[] vertices )
	{
		this.vertices = vertices;
		this.n = vertices.length;
		this.directed = false;
	}

	/**
	 * Find vertex no. n
	 *
	 * @param n : int
	 */
	public V getVertex( int n )
	{
		return vertices[ n - 1 ];
	}

	/**
	 * Method to add an edge to the graph
	 *
	 * @param from   : int - one end of edge
	 * @param to     : int - other end of edge
	 * @param weight : int - the weight of the edge
	 */
	void addEdge( V from, V to, E e )
	{
		if( this.directed )
		{
			from.adj.add( e );
			to.revAdj.add( e );
		}
		else
		{
			from.adj.add( e );
			to.adj.add( e );
		}
	}

	public int size()
	{
		return n;
	}
}