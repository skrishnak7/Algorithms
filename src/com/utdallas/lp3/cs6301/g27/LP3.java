package cs6301.g27;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

import cs6301.g00.Graph;
import cs6301.g00.Graph.Vertex;
import cs6301.g00.Graph.Edge;
import cs6301.g00.Timer;

public class LP3
{
	static int VERBOSE = 0;

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
		if( args.length > 1 )
		{
			VERBOSE = Integer.parseInt( args[ 1 ] );
		}

		int start = in.nextInt();  // root node of the MST
		Graph g = Graph.readDirectedGraph( in );
		Vertex startVertex = g.getVertex( start );
		List<Edge> dmst = new ArrayList<>();

		Timer timer = new Timer();
		int wmst = directedMST( g, startVertex, dmst );
		timer.end();

		System.out.println( wmst );
		if( VERBOSE > 0 )
		{
			System.out.println( "_________________________" );
			for( Edge e : dmst )
			{
				System.out.print( e );
			}
			System.out.println();
			System.out.println( "_________________________" );
		}
		System.out.println( timer );
	}

	/**
	 * TO DO: List dmst is an empty list. When your algorithm finishes,
	 * it should have the edges of the directed MST of g rooted at the
	 * start vertex.  Edges must be ordered based on the vertex into
	 * which it goes, e.g., {(7,1),(7,2),null,(2,4),(3,5),(5,6),(3,7)}.
	 * In this example, 3 is the start vertex and has no incoming edges.
	 * So, the list has a null corresponding to Vertex 3.
	 * The function should return the total weight of the MST it found.
	 */
	public static int directedMST( Graph g, Vertex start, List<Edge> dmst )
	{
		EdmondGraph graph = new EdmondGraph( g );

		DFS dfs = new DFS( g );
		List<Vertex> dfsList = dfs.dfs( Collections.singletonList( start ).iterator() );
		if( dfsList.size() < g.size() )
		{
			return -1;
		}

		ArrayDeque<Vertex> startReplacements = findMST( graph, start );
		expandVertices( graph, g, startReplacements );

		int weightMST = 0;
		for( Vertex u : graph )
		{
			EdmondVertex x = ( EdmondVertex ) u;
			dmst.add( x.getFinalInEdge() );

			if( x.getFinalInEdge() != null )
			{
				weightMST += x.getFinalInEdge().getActualWeight();
			}
		}

		return weightMST;
	}

	/**
	 * Find a minimum spanning tree rooted at the {@code start} vertex in a directed graph
	 * using the Tarjan's algorithm.
	 * <p>
	 * NOTE: This step does not perform the expansion of shrinked vertices.
	 *
	 * @param graph Directed graph
	 * @param start Root of the MST to be found
	 *
	 * @return A stack of vertices that depict the replacements of the MST's start vertex
	 * from top to bottom
	 */
	private static ArrayDeque<Vertex> findMST( EdmondGraph graph, Vertex start )
	{
		// A stack of vertices that will show the order of super nodes that hides
		// the start vertex of the MST at the bottom
		ArrayDeque<Vertex> startReplacement = new ArrayDeque<>();
		startReplacement.push( graph.getVertex( start ) );

		// The result of the final run of the DFS (before the loop terminates) will be used
		// to disable non-MST edges.
		DFS dfs;

		while( true )
		{
			// Reducing the weight of the graph
			reduceGraphWeight( graph );

			// Running DFS from the start vertex
			dfs = new DFS( graph );

			List<Vertex> vertexList = Collections.singletonList( graph.getVertex( startReplacement.peek() ) );
			dfs.dfs( vertexList.iterator() );

			// Checking if the DFS was able to reach all the nodes
			boolean foundMST = true;
			for( Vertex u : graph )
			{
				if( !dfs.getVertex( u ).seen )
				{
					foundMST = false;
					break;
				}
			}

			if( foundMST )
			{
				break;
			}

			start = shrinkNodes( graph, start );

			// If the current start vertex was replaced by a super node, push it to the stack
			if( !start.equals( startReplacement.peek() ) )
			{
				startReplacement.push( start );
			}
		}

		// Disable the non-MST edges
		for( Vertex u : graph )
		{
			disableNonDFSEdges( dfs, u.iterator() );
		}

		return startReplacement;
	}

	/**
	 * Identify the strongly connected components with more than one vertex of the Graph
	 * and shrink it by replacing it with a single new vertex and new edges connecting
	 * it to the rest of the graph.
	 *
	 * @param graph Directed graph
	 * @param start Start vertex of the MST
	 *
	 * @return A new super node if the {@code start} vertex is replaced by it, else the
	 * original start vertex.
	 */
	private static Vertex shrinkNodes( EdmondGraph graph, Vertex start )
	{
		DFS dfs = new DFS( graph );

		int numComponents = dfs.stronglyConnectedComponents();

		Map<Integer, List<EdmondVertex>> components = new HashMap<>();
		// Consolidate all the SCCs
		for( Vertex u : graph )
		{
			int componentNumber = dfs.getVertex( u ).componentNumber;
			List<EdmondVertex> vertices = components.get( componentNumber );
			if( vertices == null )
			{
				vertices = new ArrayList<>();
				components.put( componentNumber, vertices );
			}

			vertices.add( ( EdmondVertex ) u );
		}

		for( int cno = 1; cno <= numComponents; cno++ )
		{
			List<EdmondVertex> componentVertices = components.get( cno );

			// If a SCC has more than one vertex in it, then shrink it
			if( componentVertices.size() > 1 )
			{
				List<EdmondVertex> verticesToDisable = new ArrayList<>();
				List<EdmondEdge> edgesToDisable = new ArrayList<>();

				EdmondVertex superNode = graph.createNewVertex();
				SuperNodeInfo superNodeInfo = new SuperNodeInfo();

				superNode.getShrinkedVertices().addAll( componentVertices );
				verticesToDisable.addAll( superNode.getShrinkedVertices() );

				graph.enableNonZeroEdges();
				for( EdmondVertex u : superNode.getShrinkedVertices() )
				{
					// If the start vertex of the MST itself is replaced by a super node
					// then we need a reference to this super node to run the DFS in the
					// next iteration of the algorithm's shrinking step.
					if( u.equals( start ) )
					{
						start = superNode;
					}

					for( Edge e : u )
					{
						Vertex v = e.otherEnd( u );

						// Edge exists within the SCC
						if( belongsToComponent( dfs, v, cno ) )
						{
							if( e.getWeight() == 0 )
							{
								superNode.getShrinkedEdges().add( ( EdmondEdge ) e );
							}
						}
						// Edge goes from SCC to outside
						else
						{
							EdmondVertex edmondVertex = ( EdmondVertex ) v;
							EdmondEdge edge = edmondVertex.getSccToOutEdge();
							if( edge == null || edge.getWeight() > e.getWeight() )
							{
								edmondVertex.setSccToOutEdge( ( EdmondEdge ) e );
								if( edge == null )
								{
									superNodeInfo.sccToOutVertices.add( v );
								}
							}
						}

						edgesToDisable.add( ( EdmondEdge ) e );
					}

					for( Iterator<Edge> it = u.reverseIterator(); it.hasNext(); )
					{
						Edge e = it.next();
						Vertex v = e.otherEnd( u );

						// Edge comes from outside to SCC
						if( !belongsToComponent( dfs, v, cno ) )
						{
							EdmondVertex edmondVertex = ( EdmondVertex ) v;
							EdmondEdge edge = edmondVertex.getOutToSccEdge();
							if( edge == null || edge.getWeight() > e.getWeight() )
							{
								edmondVertex.setOutToSccEdge( ( EdmondEdge ) e );
								if( edge == null )
								{
									superNodeInfo.outToSCCVertices.add( v );
								}
							}

							edgesToDisable.add( ( EdmondEdge ) e );
						}
					}
				}
				graph.disableNonZeroEdges();

				// Disabling vertices and edges that were shrinked
				disableVertices( verticesToDisable );
				disableEdges( edgesToDisable );

				// Replacing edges going out from shrinked vertex with new ones
				for( Vertex u : superNodeInfo.sccToOutVertices )
				{
					EdmondVertex v = ( EdmondVertex ) u;
					EdmondEdge sccToOutEdge = v.getSccToOutEdge();
					graph.addEdge( superNode, u, sccToOutEdge );
					v.setSccToOutEdge( null );
				}

				// Replacing edges coming into the shrinked vertex with new ones
				for( Vertex u : superNodeInfo.outToSCCVertices )
				{
					EdmondVertex v = ( EdmondVertex ) u;
					EdmondEdge outToSccEdge = v.getOutToSccEdge();
					graph.addEdge( u, superNode, outToSccEdge );
					v.setOutToSccEdge( null );
				}
			}
		}

		return start;
	}

	/**
	 * Expand the super nodes (those that were born out of shrinking SCCs) in the graph.
	 *
	 * @param graph             Graph containing the super nodes
	 * @param g                 The original graph with the original vertices.
	 * @param startReplacements A stack of vertices that depict the replacements of the
	 *                          MST's start vertex from top to bottom
	 */
	private static void expandVertices( EdmondGraph graph, Graph g, ArrayDeque<Vertex> startReplacements )
	{
		// Since the most recent start vertex's replacement is on the top of the stack (startReplacement)
		// we won't be needing it, because, we only need to know the start-replacement-vertex/start vertex
		// that was replaced by a particular super node. Hence, we remove the top of the stack.
		// If the stack has only one entry (i.e., the original start vertex), then we can still pop it,
		// because, in that case we won't be needing it at all.
		startReplacements.pop();

		for( int i = graph.size(); i > g.size(); i-- )
		{
			Vertex superNode = graph.getVertex( i );
			EdmondVertex edmondSuperNode = ( EdmondVertex ) superNode;

			// It should have exactly one 0-edge coming into it
			Edge e = null;
			for( Iterator<Edge> it = superNode.reverseIterator(); it.hasNext(); )
			{
				e = it.next();
			}

			Vertex startVertex;
			if( e != null )
			{
				Vertex v = e.otherEnd( superNode );
				startVertex = ( ( EdmondEdge ) e ).getSourceEdge().otherEnd( v );
			}
			else
			{
				startVertex = startReplacements.pop();
			}

			// Enable the vertices and edges inside the super node
			enableEdges( edmondSuperNode.getShrinkedEdges() );
			enableVertices( edmondSuperNode.getShrinkedVertices() );

			// Run DFS from the startVertex of this super node's shrunken vertices.
			DFS dfs = new DFS( graph );
			dfs.dfs( Collections.singletonList( startVertex ).iterator() );

			// Disable the edge that is not part of the DFS tree.
			for( EdmondVertex u : edmondSuperNode.getShrinkedVertices() )
			{
				disableNonDFSEdges( dfs, u.iterator() );
			}

			// Disable the edges coming in to and going out of the super node while enabling their
			// source edges
			restoreOriginalEdges( edmondSuperNode.iterator() );
			restoreOriginalEdges( edmondSuperNode.reverseIterator() );

			// Disabling the super node
			edmondSuperNode.disable();
		}
	}

	/**
	 * Disable the edges and restore their original ones.
	 * <p>
	 * Also record each edge as the incoming edge of the 'toVertex' which can
	 * be conveniently used in the later part of the algorithm.
	 *
	 * @param it Iterator of edges
	 */
	private static void restoreOriginalEdges( Iterator<Edge> it )
	{
		while( it.hasNext() )
		{
			EdmondEdge edge = ( EdmondEdge ) it.next();
			edge.disable();
			edge.getSourceEdge().enable();
			edge.getSourceEdge().setWeight( edge.getWeight() );

			EdmondVertex x = ( EdmondVertex ) edge.getSourceEdge().toVertex();
			x.setFinalInEdge( edge.getSourceEdge() );
		}
	}

	/**
	 * Reduce the weight of the graph if possible, by reducing the incoming edges'
	 * weight by the min-weight of each vertex.
	 *
	 * @param graph Directed graph
	 */
	private static void reduceGraphWeight( EdmondGraph graph )
	{
		graph.enableNonZeroEdges();
		for( Vertex u : graph )
		{
			int minWeight = Integer.MAX_VALUE;

			// Finding the minimum weight of all the edges coming into this vertex
			for( Iterator<Edge> it = u.reverseIterator(); it.hasNext(); )
			{
				minWeight = Math.min( minWeight, it.next().getWeight() );
			}

			// Reducing the weight of all the incoming edges by the minimum weight
			for( Iterator<Edge> it = u.reverseIterator(); it.hasNext(); )
			{
				Edge e = it.next();
				e.setWeight( e.getWeight() - minWeight );
			}
		}
		graph.disableNonZeroEdges();
	}

	/**
	 * Disable the edges that are not part of the DFS tree while also marking
	 * each vertex's incoming edge if the edge belongs to the DFS tree.
	 *
	 * @param dfs          Result of the DFS
	 * @param edgeIterator Edges to be validated
	 */
	private static void disableNonDFSEdges( DFS dfs, Iterator<Edge> edgeIterator )
	{
		while( edgeIterator.hasNext() )
		{
			Edge edge = edgeIterator.next();
			Vertex fromVertex = edge.fromVertex(), toVertex = edge.toVertex();
			if( !fromVertex.equals( dfs.getVertex( toVertex ).parent ) )
			{
				( ( EdmondEdge ) edge ).disable();
			}
			else
			{
				( ( EdmondVertex ) toVertex ).setFinalInEdge( ( EdmondEdge ) edge );
			}
		}
	}

	/**
	 * Check if the vertex {@code v} has the component number {@code componentNumber}.
	 *
	 * @param dfs             Result of the Strongly Connected Components algorithm
	 * @param v               Vertex
	 * @param componentNumber Component ID
	 *
	 * @return True if the vertex's component ID is {@code componentNumber}, false otherwise.
	 */
	private static boolean belongsToComponent( DFS dfs, Vertex v, int componentNumber )
	{
		return ( v.getName() < dfs.node.length && dfs.getVertex( v ).componentNumber == componentNumber );
	}

	/**
	 * Enable all the edges.
	 *
	 * @param edges Edges to be enabled
	 */
	private static void enableEdges( Collection<EdmondEdge> edges )
	{
		for( EdmondEdge e : edges )
		{
			e.enable();
		}
	}

	/**
	 * Disable all the edges.
	 *
	 * @param edges Edges to be disabled
	 */
	private static void disableEdges( Collection<EdmondEdge> edges )
	{
		for( EdmondEdge e : edges )
		{
			e.disable();
		}
	}

	/**
	 * Enable all the vertices.
	 *
	 * @param vertices Vertices to be enabled.
	 */
	private static void enableVertices( Collection<EdmondVertex> vertices )
	{
		for( EdmondVertex u : vertices )
		{
			u.enable();
		}
	}

	/**
	 * Disable all the vertices.
	 *
	 * @param vertices Vertices to be disabled.
	 */
	private static void disableVertices( Collection<EdmondVertex> vertices )
	{
		for( EdmondVertex u : vertices )
		{
			u.disable();
		}
	}


	/**
	 * A holder class to temporarily hold the shrinking information while
	 * creating a super node during the shrinking step of Tarjan's algorithm.
	 */
	private static class SuperNodeInfo
	{
		List<Vertex> sccToOutVertices, outToSCCVertices;

		SuperNodeInfo()
		{
			this.sccToOutVertices = new ArrayList<>();
			this.outToSCCVertices = new ArrayList<>();
		}
	}
}

/*
1
5 7
1 5 8
1 4 7
1 3 6
4 3 3
3 5 6
5 3 2
5 2 1

1
8 13
1 2 5
2 3 3
3 4 12
1 5 11
5 2 6
6 2 2
2 7 13
3 7 9
8 3 4
4 8 1
6 5 10
7 6 7
7 8 8
* */