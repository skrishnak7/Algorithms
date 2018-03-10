package cs6301.g27;

import cs6301.g00.Graph;
import cs6301.g00.Timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Group number: G27
 * Members:
 *      Gayathri Balakumar
 *      Susindaran Elangovan
 *      Vidya Gopalan
 *      Saikrishna Kanukuntla
 * <p>
 * Short Project #1
 */
public class DiameterOfTree
{
	/**
	 * Given a Tree (in the form of an undirected graph), this method will return the path
	 * of the diameter of the tree as a LinkedList.
	 * <p>
	 * Note: Although the tree could contain multiple diameter paths, this method will only
	 * return one of them.
	 *
	 * @param graph The undirected graph for which the diameter path should be determined
	 *
	 * @return Linked list of graph vertices forming the diameter of the tree
	 */
	public static LinkedList<Graph.Vertex> diameter( Graph graph )
	{
		Queue<Graph.Vertex> queue = new ArrayDeque<>();

		// Choosing the first vertex as the starting vertex for BFS
		queue.offer( graph.getVertex( 1 ) );

		// This variable is used to hold the last element seen in each level of BFS.
		// Thus at the end of the upcoming while loop this variable will hold a vertex
		// that is one of the vertices which are the farthest from the starting vertex.
		Graph.Vertex prev = null;

		// To maintain the seen status of the vertices during BFS
		boolean[] seen = new boolean[ graph.size() ];

		while( !queue.isEmpty() )
		{
			// Size of the current level
			int queueSize = queue.size();

			// For each node in the current level traverse all the unseen edges
			for( int i = 0; i < queueSize; i++ )
			{
				prev = queue.poll();
				seen[ prev.getName() ] = true;
				for( Graph.Edge edge : prev )
				{
					Graph.Vertex otherEnd = edge.otherEnd( prev );
					if( !seen[ otherEnd.getName() ] )
					{
						queue.offer( otherEnd );
					}
				}
			}
		}

		// Resetting the seen status for the second BFS to get the diameter path
		seen = new boolean[ graph.size() ];

		return getDiameterPath( prev, seen );
	}

	/**
	 * Given a leaf vertex, this method will return the diameter path of the tree using BFS.
	 *
	 * @param vertex The leaf vertex of a tree.
	 * @param seen   Boolean array to maintain the visited status for the vertices.
	 *
	 * @return Linked list of graph vertices forming the diameter of the tree
	 */
	private static LinkedList<Graph.Vertex> getDiameterPath( Graph.Vertex vertex, boolean[] seen )
	{
		LinkedList<Graph.Vertex> longestSubList = new LinkedList<>();
		if( vertex == null )
		{
			return longestSubList;
		}

		seen[ vertex.getName() ] = true;

		// For each unvisited vertex from this current vertex we choose the one yielding the maximum length
		// to prepend the current vertex to. Thus forming the diameter path from the current vertex.
		for( Graph.Edge edge : vertex )
		{
			Graph.Vertex otherEnd = edge.otherEnd( vertex );
			if( !seen[ otherEnd.getName() ] )
			{
				LinkedList<Graph.Vertex> sublist = getDiameterPath( otherEnd, seen );
				if( longestSubList.size() < sublist.size() )
				{
					longestSubList = sublist;
				}
			}
		}

		longestSubList.addFirst( vertex );
		return longestSubList;
	}

	/**
	 * Utility method to joined all the elements of a list using a separator.
	 * The string representation (printList()) of the elements will be used in
	 * the resulting string.
	 *
	 * @param separator Separator used to join the elements of the list
	 * @param list      List to be joined
	 *
	 * @return String of all the elements of the list joined using the separator
	 */
	private static String join( String separator, List<Graph.Vertex> list )
	{
		if( list.size() == 0 ) return "";

		StringBuilder stringBuilder = new StringBuilder( list.get( 0 ).toString() );
		for( int i = 1; i < list.size(); i++ )
		{
			stringBuilder.append( separator ).append( list.get( i ) );
		}
		return stringBuilder.toString();
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
		Graph graph = Graph.readGraph( in, false );

		Timer timer = new Timer();
		LinkedList<Graph.Vertex> linkedList = diameter( graph );
		timer.end();

		System.out.println( join( " -> ", linkedList ) );
		System.out.println( "\n" + timer );
	}
}

/*
* Sample input
8 7
1 2 1
1 3 1
2 4 1
4 5 1
3 6 1
3 7 1
7 8 1

				1
	2                   3
		4           6       7
			5                   8
* */