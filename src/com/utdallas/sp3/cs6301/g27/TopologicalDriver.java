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

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TopologicalDriver
{
    public static void main( String args[] )
    {

        Scanner in = new Scanner( System.in );
        System.out.println( "Enter the Graph:" );
        Graph g = Graph.readDirectedGraph( in );

        //Running the algorithm which finds topological ordering using algorithm1
        TopologicalOrdering run = new TopologicalOrdering( g );
        List<Graph.Vertex> topologicalOrder = run.getTopologicalOrder( g );
        if( topologicalOrder == null )
        {
            System.out.println("Graph is not a DAG");
        }
        else
        {
            System.out.println( "Topological Order after running Algorithm 1:" );
            System.out.println( Arrays.toString( topologicalOrder.toArray() ) );
        }

        //Running the DFS algorithm which finds outputs the topological order.
        DFS run2 = new DFS( g );
        topologicalOrder = run2.topologicalOrder();
        if( topologicalOrder == null )
        {
            System.out.println("Graph is not a DAG");
        }
        else
        {
            System.out.println( "Topological Order after running Algorithm 2 using DFS in descending order:" );
            System.out.println( Arrays.toString( topologicalOrder.toArray() ) );
        }
    }
}

/*
Enter the Graph:
9
13
8 1 0
8 3 0
8 9 0
1 2 0
1 7 0
3 2 0
3 7 0
9 3 0
9 5 0
2 4 0
7 4 0
7 6 0
5 6 0
Enter the source node:
8
Topological Order after running Algorithm 1:
[8, 1, 9, 3, 5, 2, 7, 4, 6]
Topological Order after running Algorithm 2 using DFS:
[8, 9, 5, 3, 1, 7, 6, 2, 4]
*/