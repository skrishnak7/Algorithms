/*
 * Group number: G27
 * Members:
 *  Gayathri Balakumar
 *  Susindaran Elangovan
 *  Vidya Gopalan
 *  Saikrishna Kanukuntla
 *
 * Long Project #2
 */

package cs6301.g27;

import cs6301.g00.Graph;
import cs6301.g00.GraphAlgorithm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Euler extends GraphAlgorithm<Euler.EulerVertex>
{
	private int VERBOSE;
	private List<Graph.Edge> tour;
	private Graph.Vertex startVertex;

	class EulerVertex
	{
		// This member variable is set to true whenever a new sub-tour is started
		// from this particular vertex. Used when stitching sub-tours.
		boolean toStitch;

		// Holding onto the actual base vertex of the Graph for convenience, like
		// to find the other end of an edge.
		Graph.Vertex baseVertex;

		// If toStitch is set to true, then this list will hold the edges of the
		// sub-tour starting from this vertex.
		List<Graph.Edge> tour;

		// Common Iterator to be used when finding tours of the graph.
		Iterator<Graph.Edge> edgeIterator;

		EulerVertex( Graph.Vertex vertex )
		{
			this.toStitch = false;
			this.tour = null;
			this.baseVertex = vertex;
			this.edgeIterator = vertex.iterator();
		}
	}

	Euler( Graph g, Graph.Vertex start )
	{
		super(g);

		this.VERBOSE = 1;
		this.tour = new LinkedList<>();
		this.startVertex = start;

		node = new EulerVertex[ g.size() ];
		for( Graph.Vertex u : g )
		{
			node[ u.getName() ] = new EulerVertex( u );
		}
	}

	/**
	 * Find a Euler tour in the Graph starting from the {@code startVertex} by finding
	 * sub-tours and stitching them together.
	 *
	 * @return List of Graph edges in the order of the Euler tour
	 */
	public List<Graph.Edge> findEulerTour()
	{
		findTours();

		if( VERBOSE > 9 )
		{
			printTours();
		}

		stitchTours();

		return this.tour;
	}

	/**
	 * Test if the graph is Eulerian.
	 * If the graph is not Eulerian, it prints the message:
	 * "Graph is not Eulerian" and one reason why, such as
	 * "inDegree = 5, outDegree = 3 at Vertex 37" or
	 * "Graph is not strongly connected"
	 *
	 * @return true if graph is eulerian, false otherwise
	 */
	boolean isEulerian()
	{
		boolean pass = true;
		String reason = null;
		if( isStronglyConnected() )
		{
			for( Graph.Vertex u : g )
			{
				if( u.adj.size() != u.revAdj.size() )
				{
					reason = "inDegree = " + u.revAdj.size() + ", outDegree = " + u.adj.size() + " at Vertex " + u;
					pass = false;
				}
			}
		}
		else
		{
			reason = "Graph is not strongly connected";
			pass = false;
		}

		if( !pass )
		{
			System.out.println( "Graph is not Eulerian" );
			System.out.println( "Reason: " + reason );
		}

		return pass;
	}

	/**
	 * Check if the graph is strongly connected by checking if the number of connected
	 * components in the graph is just 1.
	 *
	 * @return True if the graph is strongly connected, false otherwise.
	 */
	private boolean isStronglyConnected()
	{
		int SCCNum = StronglyConnectedComponents.stronglyConnectedComponents( g );
		return SCCNum == 1;
	}


	/**
	 * Find tours starting at vertices with unexplored edges (initially starting from the
	 * start vertex).
	 * <p>
	 * Each tour (represented as a linked list of edges) starting from a particular vertex will
	 * be present in that vertex's {@code tour} member variable.
	 */
	private void findTours()
	{
		EulerVertex vertex = getVertex( startVertex );
		do
		{
			// Instantiating a list for the tour starting from this vertex
			vertex.tour = new LinkedList<>();

			// Setting `toStitch` to true - to be used while stitching.
			vertex.toStitch = true;

			vertex = findSubTour( vertex, vertex.tour );

		} while( vertex != null && vertex.edgeIterator.hasNext() );
	}

	/**
	 * Starting from the vertex {@code u} of the graph, this method will find a
	 * sub-tour (potentially tour) in the graph and record it in the {@code subTour}
	 * parameter.
	 * <p>
	 * In the process, this method will also keep track of the vertices it
	 * visits and records the one with another unvisited outgoing edge. Please note,
	 * this is only a POTENTIAL candidate for the starting vertex of the next sub-tour
	 * to be found, if at all one exists. So the return value of this method should
	 * be carefully examined before using it to find another sub-tour.
	 *
	 * @param u       Start and end vertex of the sub-tour (potentially tour)
	 * @param subTour Holder for the tour edges
	 *
	 * @return A vertex with potentially more unvisited outgoing edges that was a
	 * part of the sub-tour that was found.
	 */
	private EulerVertex findSubTour( EulerVertex u, List<Graph.Edge> subTour )
	{
		EulerVertex next = null;
		while( u.edgeIterator.hasNext() )
		{
			Graph.Edge edge = u.edgeIterator.next();
			subTour.add( edge );

			if( u.edgeIterator.hasNext() )
			{
				next = u;
			}

			u = getVertex( edge.otherEnd( u.baseVertex ) );
		}

		return next;
	}

	/**
	 * Print all the sub-tours of the graph that have been found.
	 */
	private void printTours()
	{
		for( EulerVertex u : node )
		{
			if( u.tour != null )
			{
				System.out.print( u.baseVertex + ": " );
				for( Graph.Edge edge : u.tour )
				{
					System.out.print( edge );
				}
				System.out.println();
			}
		}
	}

	/**
	 * Stitch all the sub-tours into a single tour.
	 */
	private void stitchTours()
	{
		stitchTours( getVertex( startVertex ) );
	}

	/**
	 * Stitch the sub-tour starting at the vertex {@code start} to the Euler tour
	 * and also recursively stitching other sub-tour starting at some vertex
	 * that is a part of this sub-tour.
	 *
	 * @param start Starting point of the sub-tour to be stitched to Euler tour
	 */
	private void stitchTours( EulerVertex start )
	{
		EulerVertex u = start;

		// Setting toStitch to false the moment we are starting to stitch this particular
		// sub-tour to the final Euler tour because, it is possible to revisit the `start`
		// vertex in any of the sub-tours, at which point we shouldn't be re-stitch it again.
		start.toStitch = false;
		for( Graph.Edge edge : start.tour )
		{
			this.tour.add( edge );

			u = getVertex( edge.otherEnd( u.baseVertex ) );

			// If toStitch is set to true for a vertex, it means a sub-tour starts from that
			// vertex and also it hasn't been stitched to the final Euler tour yet.
			if( u.toStitch )
			{
				stitchTours( u );
			}
		}
	}

	/**
	 * Set the verbose level for the print statements.
	 *
	 * @param v Verbose level
	 */
	void setVerbose( int v )
	{
		VERBOSE = v;
	}
}