package test;

public class BaseEdge<V>
{
	V from; // head vertex
	V to; // tail vertex
	int weight;// weight of edge

	/**
	 * Constructor for BaseEdge
	 *
	 * @param u : BaseVertex - BaseVertex from which edge starts
	 * @param v : BaseVertex - BaseVertex on which edge lands
	 * @param w : int - Weight of edge
	 */
	BaseEdge( V u, V v, int w )
	{
		from = u;
		to = v;
		weight = w;
	}

	/**
	 * Method to find the other end end of an edge, given a vertex reference
	 * This method is used for undirected graphs
	 *
	 * @param u : BaseVertex
	 *
	 * @return : BaseVertex - other end of edge
	 */
	public V otherEnd( V u )
	{
		assert from == u || to == u;
		// if the vertex u is the head of the arc, then return the tail else return the head
		if( from == u )
		{
			return to;
		}
		else
		{
			return from;
		}
	}

	/**
	 * Return the string "(x,y)", where edge goes from x to y
	 */
	public String toString()
	{
		return "(" + from + "," + to + ")";
	}

	public String stringWithSpaces()
	{
		return from + " " + to + " " + weight;
	}
}