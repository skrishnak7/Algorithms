/*
 * Group number: G27
 * Members:
 *  Gayathri Balakumar
 *  Susindaran Elangovan
 *  Vidya Gopalan
 *  Saikrishna Kanukuntla
 *
 * Short Project #3
 */

package cs6301.g27;

import java.util.Scanner;

import cs6301.g00.Graph;

public class Eulerian
{
	private static boolean isStronglyConnected( Graph g )
	{
		int SCCNum = SCC.stronglyConnectedComponents( g );
		return SCCNum == 1;
	}

	private static boolean inDegreeEqualsOutDegree( Graph g )
	{
		boolean equals = true;
		for( Graph.Vertex u : g )
		{
			if( u.adj.size() != u.revAdj.size() )
			{
				equals = false;
			}
		}
		return equals;
	}

	public static boolean testEulerian( Graph g )
	{
		return isStronglyConnected( g ) && inDegreeEqualsOutDegree( g );
	}

	public static void main( String[] args )
	{
		Scanner in = new Scanner( System.in );
		Graph g = Graph.readDirectedGraph( in );
		if( testEulerian( g ) )
		{
			System.out.println("Given graph is Eulerian");
		}
		else
		{
			System.out.println("Given graph is not Eulerian");
		}
	}
}
/*
Input:
3
3
1 2 0
2 3 0
3 1 0
* */