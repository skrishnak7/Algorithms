/*
 *
 * Group number: G27
 * Members:
 *  Gayathri Balakumar
 *  Susindaran Elangovan
 *  Vidya Gopalan
 *  Saikrishna Kanukuntla
 *
 * Long Project #4
 */
package cs6301.g27;

import cs6301.g00.Graph;
import cs6301.g00.Graph.Edge;
import cs6301.g00.Graph.Vertex;
import cs6301.g00.GraphAlgorithm;

import java.util.*;

public class LP4 extends GraphAlgorithm<LP4.XVertex>
{
	XVertex source;


	//common constructor for all parts of LP4: g is graph, s is source vertex
	public LP4( Graph g, Vertex source )
	{
		super( g );
		node = new XVertex[ g.size() ];
		for( Graph.Vertex u : g )
		{
			node[ u.getName() ] = new XVertex( u );
		}

		this.source = getVertex( source );
	}

	public class XVertex
	{
		Graph.Vertex baseVertex;
		long inDegree;
		boolean visited;

		long distance;
		int spUpdateCount;
		boolean seen, INF;
		LinkedList<XVertex> spParents;
		long[] kEdges;

		public XVertex( Graph.Vertex baseVertex )
		{
			this.baseVertex = baseVertex;
			inDegree = -1;
			visited = false;

			distance = -1;
			spUpdateCount = 0;
			seen = false;
			INF = true;

			spParents = new LinkedList<>();
		}

		@Override
		public String toString()
		{
			return this.baseVertex.toString();
		}
	}

	private void printVertices( int startIndex, XVertex[] vertices )
	{
		for( int i = startIndex; i < vertices.length; i++ )
		{
			System.out.print( vertices[i] + " " );
		}
		System.out.println();
	}

	/**
	 * @param topologicalResult vertices to store the possible Topological order in g
	 * @param enumerate         boolean flag to decide whether to print Topological order or not.
	 */
	private long TopologicalOrders( XVertex[] topologicalResult, int index, LinkedList<XVertex> candidates,
	                                boolean enumerate )
	{

		if( index == g.size() )
		{
			if( enumerate )
			{
				printVertices( 0, topologicalResult );
			}

			return 1;
		}

		int candidatesSize = candidates.size();

		long count = 0;
		for( int i = 0; i < candidatesSize; i++ )
		{
			XVertex du = candidates.removeFirst();
			Vertex u = du.baseVertex;

			// Just a redundant check
			if( du.inDegree == 0 && !du.visited )
			{
				for( Graph.Edge e : u )
				{
					Graph.Vertex v = e.otherEnd( u );
					XVertex dv = getVertex( v );
					dv.inDegree--;

					if( dv.inDegree == 0 && !dv.visited )
					{
						candidates.offer( dv );
					}
				}

				topologicalResult[ index ] = du;
				du.visited = true;

				//Recursive call to process other nodes.
				count += TopologicalOrders( topologicalResult, index + 1, candidates, enumerate );

				int extraSize = candidates.size() - ( candidatesSize - 1 );
				for( int j = 0; j < extraSize; j++ )
				{
					candidates.removeLast();
				}

				//Resetting all fields, so that the node can be processed for another possible ordering
				du.visited = false;
				candidates.offer( du );

				for( Graph.Edge e : u )
				{
					Graph.Vertex v = e.otherEnd( u );
					XVertex dv = getVertex( v );
					dv.inDegree++;
				}
			}
		}

		return count;
	}

	/**
	 * Helper function called from countTopologicalOrders() and enumerateTopologicalOrders
	 *
	 * @param enumerate boolean flag
	 *
	 * @return number of topological orders of g
	 */
	private long TopologicalOrders( boolean enumerate )
	{
		LinkedList<XVertex> candidates = new LinkedList<>();

		for( XVertex du : node )
		{
			du.inDegree = du.baseVertex.revAdj.size();
			du.visited = false;

			if( du.inDegree == 0 )
			{
				candidates.offer( du );
			}
		}

		XVertex[] topologicalResult = new XVertex[ g.size() ];

		return TopologicalOrders( topologicalResult, 0, candidates, enumerate );
	}

	// Part a. Return number of topological orders of g
	public long countTopologicalOrders()
	{
		return TopologicalOrders( false );
	}


	public long enumerateTopologicalOrders()
	{
		return TopologicalOrders( true );
	}

	private boolean bellmanFordShortestPath()
	{
		this.source.INF = false;
		this.source.distance = 0;
		this.source.seen = true;

		Queue<XVertex> q = new LinkedList<>();
		q.offer( this.source );

		while( !q.isEmpty() )
		{
			XVertex du = q.poll();
			du.seen = false;
			du.spUpdateCount++;

			if( du.spUpdateCount >= g.size() )
			{
				return false;
			}

			for( Edge e : du.baseVertex )
			{
				Vertex v = e.otherEnd( du.baseVertex );
				XVertex dv = getVertex( v );

				if( dv.INF || dv.distance > du.distance + e.getWeight() )
				{
					dv.distance = du.distance + e.getWeight();

					dv.spParents.clear();
					dv.spParents.offer( du );

					if( !dv.seen )
					{
						q.offer( dv );
						dv.seen = true;
					}

					dv.INF = false;
				}
				else if( !dv.INF && dv.distance == du.distance + e.getWeight() )
				{
					dv.spParents.offer( du );
				}
			}
		}

		return true;
	}

	private long enumerateShortestPaths( XVertex[] shortestPath, int index, XVertex vertex, boolean printEnumeration )
	{
		shortestPath[index] = vertex;

		if( vertex.baseVertex.getName() == source.baseVertex.getName() )
		{
			if( printEnumeration )
			{
				printVertices( index, shortestPath );
			}
			return 1;
		}

		long count = 0;
		for( XVertex u : vertex.spParents )
		{
			count += enumerateShortestPaths( shortestPath, index - 1, u, printEnumeration );
		}

		return count;
	}

	private long countShortestPaths( XVertex target, boolean printEnumeration )
	{
		if( bellmanFordShortestPath() )
		{
			XVertex[] shortestPath = new XVertex[ g.size() ];
			return enumerateShortestPaths( shortestPath, g.size() - 1, target, printEnumeration );
		}

		System.out.println( "Non-positive cycle in graph.  Unable to solve problem" );
		return -1;
	}

	// Part c. Return the number of shortest paths from s to t
	// 	Return -1 if the graph has a negative or zero cycle
	public long countShortestPaths( Vertex target )
	{
		return countShortestPaths( getVertex( target ), false );
	}


	// Part d. Print all shortest paths from s to t, one per line, and
	//	return number of shortest paths from s to t.
	//	Return -1 if the graph has a negative or zero cycle.
	public long enumerateShortestPaths( Vertex target )
	{
		return countShortestPaths( getVertex( target ), true );
	}


	/**
	 *
	 * @param t The target vertex provided
	 * @param K The value k for which we have to find shortest path in at most k edges.
	 * @return The shortest path distance of source s to vertex t with atmost k edges.
	 */
	public int constrainedShortestPath( Vertex t, int K )
	{
        for(Vertex u : g){
            XVertex du = getVertex(u);
            du.kEdges = new long[K+1];
            du.kEdges[0] = Integer.MAX_VALUE;
            du.INF = true;
        }

        source.kEdges[0]= 0;
        source.INF = false;
        boolean noChange ;

        for( int k =1 ; k < K+1; k++){
            noChange = true;
            for(Vertex u : g){
                XVertex du = getVertex(u);
                du.kEdges[k] = du.kEdges[k-1];
                for( Edge e: u.revAdj){
                    XVertex dp = getVertex(e.otherEnd(u));
                    if(du.kEdges[k] > dp.kEdges[k-1]+ e.getWeight()){
                        du.kEdges[k] = dp.kEdges[k-1] + e.getWeight();

                        noChange = false;
                    }
                }
            }
            if(noChange){
                for(Vertex u: g){
                    XVertex du = getVertex(u);
                    du.distance = du.kEdges[k];
                }
            }
        }
        for(Vertex u: g){
        XVertex du = getVertex(u);
        du.distance = du.kEdges[K];
        }


        XVertex r = getVertex(t);
	    return (int)r.distance;
	}


	// Part f. Reward collection problem
	// Reward for vertices is passed as a parameter in a hash map
	// tour is empty list passed as a parameter, for output tour
	// Return total reward for tour
	public int reward( HashMap<Vertex, Integer> vertexRewardMap, List<Vertex> tour )
	{
		// To do
		return 0;
	}

	// Do not modify this function
	static void printGraph( Graph g, HashMap<Vertex, Integer> map, Vertex s, Vertex t, int limit )
	{
		System.out.println( "Input graph:" );
		for( Vertex u : g )
		{
			if( map != null )
			{
				System.out.print( u + "($" + map.get( u ) + ")\t: " );
			}
			else
			{
				System.out.print( u + "\t: " );
			}
			for( Edge e : u )
			{
				System.out.print( e + "[" + e.getWeight() + "] " );
			}
			System.out.println();
		}
		if( s != null )
		{
			System.out.println( "Source: " + s );
		}
		if( t != null )
		{
			System.out.println( "Target: " + t );
		}
		if( limit > 0 )
		{
			System.out.println( "Limit: " + limit + " edges" );
		}
		System.out.println( "___________________________________" );
	}
}
