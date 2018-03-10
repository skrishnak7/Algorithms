package test;

import java.util.Scanner;

public class Driver
{
	public static class NewVertex extends BaseVertex<NewEdge>
	{
		boolean[] seen;

		public NewVertex(int n)
		{
			super(n);
			seen = new boolean[n];
		}
	}

	public static class NewEdge extends BaseEdge<NewVertex>
	{
		public NewEdge( NewVertex u, NewVertex v, int w )
		{
			super(u, v, w);
		}
	}

	public static void main( String[] args )
	{
		Scanner in = new Scanner( System.in );
		NewVertex[] vertices = new NewVertex[10];
		Graph<NewVertex, NewEdge> graph = new Graph<>( vertices );
		graph.getVertex( 1 ).seen[0] = true;
	}
}