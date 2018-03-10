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

import cs6301.g00.Graph;

import java.util.List;
import java.util.Scanner;

public class DAG
{
	public static boolean isDAG( Graph graph )
	{
		DFS dfs = new DFS( graph );
		List<Graph.Vertex> topologicalOrder = dfs.topologicalOrder();
		return topologicalOrder != null;
	}

	public static void main( String[] args )
	{
		Scanner in = new Scanner( System.in );
		Graph graph = Graph.readDirectedGraph( in );
		System.out.println( isDAG( graph ) );
	}
}