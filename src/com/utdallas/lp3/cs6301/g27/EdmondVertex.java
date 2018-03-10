package cs6301.g27;

import cs6301.g00.Graph;
import cs6301.g00.Graph.Edge;

import java.util.*;

/**
 * Vertices connected by the edges of Edmond Graph
 */
public class EdmondVertex extends Graph.Vertex
{
	private boolean disabled;
	public List<EdmondEdge> adj, revAdj;

	/**
	 * If this vertex is a super node, then the vertices replaced by this
	 * super node is recorded in this variable.
	 */
	private List<EdmondVertex> shrinkedVertices;

	/**
	 * If this vertex is a super node, then the 0-weight edges connecting
	 * the vertices replaced by this super node is recorded in this variable.
	 */
	private List<EdmondEdge> shrinkedEdges;

	/**
	 * During the expansion step of the Tarjan's algorithm, the final incoming
	 * edge of this node in the MST is recorded in this variable.
	 */
	private EdmondEdge finalInEdge;

	/**
	 * Variables to record the minimum edges going into a super node from this node
	 * or coming from a super node into this node during the shrinking step. This
	 * is just a temporary holder, purely to avoid hashing.
	 */
	private EdmondEdge sccToOutEdge, outToSccEdge;

	EdmondVertex( Graph.Vertex u )
	{
		super( u );
		disabled = false;
		adj = new LinkedList<>();
		revAdj = new LinkedList<>();
		shrinkedVertices = null;
		shrinkedEdges = null;
		finalInEdge = null;
	}

	EdmondVertex( int name )
	{

		super( name );
		disabled = false;
		adj = new LinkedList<>();
		revAdj = new LinkedList<>();
		shrinkedVertices = new LinkedList<>();
		shrinkedEdges = new LinkedList<>();
		finalInEdge = null;
	}

	/*
	* Accessors
	* */

	public boolean isDisabled()
	{
		return disabled;
	}

	public void disable()
	{
		disabled = true;
	}

	public void enable()
	{
		disabled = false;
	}

	public List<EdmondVertex> getShrinkedVertices()
	{
		return shrinkedVertices;
	}

	public void setShrinkedVertices( List<EdmondVertex> shrinkedVertices )
	{
		this.shrinkedVertices = shrinkedVertices;
	}

	public List<EdmondEdge> getShrinkedEdges()
	{
		return shrinkedEdges;
	}

	public void setShrinkedEdges( List<EdmondEdge> shrinkedEdges )
	{
		this.shrinkedEdges = shrinkedEdges;
	}

	public EdmondEdge getFinalInEdge()
	{
		return finalInEdge;
	}

	public void setFinalInEdge( EdmondEdge finalInEdge )
	{
		this.finalInEdge = finalInEdge;
	}

	public EdmondEdge getSccToOutEdge()
	{
		return sccToOutEdge;
	}

	public void setSccToOutEdge( EdmondEdge sccToOutEdge )
	{
		this.sccToOutEdge = sccToOutEdge;
	}

	public EdmondEdge getOutToSccEdge()
	{
		return outToSccEdge;
	}

	public void setOutToSccEdge( EdmondEdge outToSccEdge )
	{
		this.outToSccEdge = outToSccEdge;
	}

	/*
	* Overrides
	* */

	@Override
	public Iterator<Edge> iterator()
	{
		return new EdmondVertexIterator( this.adj );
	}

	@Override
	public Iterator<Edge> reverseIterator()
	{
		return new EdmondVertexIterator( this.revAdj );
	}

	/*
	* Classes
	* */

	class EdmondVertexIterator implements Iterator<Edge>
	{
		EdmondEdge current;
		Iterator<EdmondEdge> it;
		boolean ready;

		EdmondVertexIterator( List<EdmondEdge> edges )
		{
			this.it = edges.iterator();
			this.ready = false;
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

		public Edge next()
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