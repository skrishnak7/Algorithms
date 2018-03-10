package test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BaseVertex<E> implements Iterable<E>
{
	private int name; // name of the vertex
	List<E> adj, revAdj; // adjacency list; use LinkedList or ArrayList

	/**
	 * Constructor for the vertex
	 *
	 * @param n : int - name of the vertex
	 */
	BaseVertex( int n )
	{
		name = n;
		adj = new LinkedList<>();
		revAdj = new LinkedList<>();   /* only for directed graphs */
	}

	public int getName()
	{
		return this.name;
	}

	public List<E> getAdj()
	{
		return this.adj;
	}

	public List<E> getRevAdj()
	{
		return this.revAdj;
	}

	public Iterator<E> iterator()
	{
		return adj.iterator();
	}

	/**
	 * Method to get vertex number.  +1 is needed because [0] is vertex 1.
	 */
	public String toString()
	{
		return Integer.toString( name + 1 );
	}
}