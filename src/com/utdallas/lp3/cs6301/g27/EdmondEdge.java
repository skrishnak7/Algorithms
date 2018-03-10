package cs6301.g27;

import cs6301.g00.Graph;

/**
 * Edges connecting the vertices of the Edmond Graph
 */
public class EdmondEdge extends Graph.Edge
{
	/**
	 * Utility variable to enable/disable non-zero edges in a graph.
	 * Used by the {@code isDisabled()} method to return the status of the edge.
	 */
	private static boolean nonZeroEdgesDisabled = true;

	/**
	 * Current disabled status of the edge
	 */
	private boolean disabled;

	/**
	 * If this edge was created to replace another edge, then this variable
	 * records the original edge.
	 */
	private EdmondEdge sourceEdge;

	/**
	 * Since the weight of the edges will be changed during the Tarjan's algorithm,
	 * this variable records the original weight of the edge.
	 */
	private int actualWeight;

	public EdmondEdge( Graph.Vertex from, Graph.Vertex to, int w, int name )
	{
		super( from, to, w, name );
		disabled = false;
		sourceEdge = null;
		actualWeight = w;
	}

	public boolean isDisabled()
	{
		EdmondVertex from = ( EdmondVertex ) this.fromVertex();
		EdmondVertex to = ( EdmondVertex ) this.toVertex();
		return disabled || from.isDisabled() || to.isDisabled() || ( nonZeroEdgesDisabled && getWeight() != 0 );
	}

	/*
	* Accessors
	* */

	public void disable()
	{
		this.disabled = true;
	}

	public void enable()
	{
		this.disabled = false;
	}

	public EdmondEdge getSourceEdge()
	{
		return sourceEdge;
	}

	public void setSourceEdge( EdmondEdge sourceEdge )
	{
		this.sourceEdge = sourceEdge;
	}

	public int getActualWeight()
	{
		return actualWeight;
	}

	public void setActualWeight( int actualWeight )
	{
		this.actualWeight = actualWeight;
	}

	/*
	* Static utilities
	* */

	public static void disableNonZeroEdges()
	{
		nonZeroEdgesDisabled = true;
	}

	public static void enableNonZeroEdges()
	{
		nonZeroEdgesDisabled = false;
	}
}