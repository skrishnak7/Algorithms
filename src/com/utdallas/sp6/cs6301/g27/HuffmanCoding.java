/*
 * Group number: G27
 * Members:
 *  Gayathri Balakumar
 *  Susindaran Elangovan
 *  Vidya Gopalan
 *  Saikrishna Kanukuntla
 *
 * Short Project #6
 */

package cs6301.g27;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Construction of huffman codes using the Huffman Coding Algorithm
 * and representation using binary tree.
 */
public class HuffmanCoding
{
	/**
	 * Each node in Huffman coding tree will have a frequency attached to it.
	 * Since it is a binary tree, only the left and right child pointers are
	 * available at each node.
	 * <p>
	 * Leaf nodes have a symbol attached to them apart from the frequency.
	 */
	class CodingTreeNode
	{
		Character symbol;
		float frequency;

		CodingTreeNode left, right;

		public CodingTreeNode( float frequency, CodingTreeNode left, CodingTreeNode right )
		{
			this.frequency = frequency;
			this.left = left;
			this.right = right;
			this.symbol = null;
		}

		public CodingTreeNode( float frequency, Character symbol )
		{
			this.frequency = frequency;
			this.symbol = symbol;
			this.left = this.right = null;
		}

		@Override
		public String toString()
		{
			return "[ " + this.symbol + ", " + this.frequency + " ]";
		}
	}

	/**
	 * Once the Huffman coding tree is constructed for the given symbols and frequencies,
	 * this variable holds the pointer to the root of the resulting coding tree.
	 */
	private CodingTreeNode root;

	/**
	 * Construct a Huffman coding tree from the given symbols and frequencies.
	 * <p>
	 * The size of the {@code symbols} and {@code frequencies} array must be the same
	 * and at least two.
	 * <p>
	 * It is assumed that frequencies[i] relates to the symbol at symbols[i]
	 *
	 * @param symbols     Symbols that will constitute the leaf nodes of the coding tree
	 * @param frequencies Frequencies of the symbols
	 */
	public HuffmanCoding( char[] symbols, float[] frequencies )
	{
		if( symbols.length != frequencies.length || symbols.length < 2 )
		{
			throw new IllegalArgumentException( "Size of symbols and frequencies must be the same and at least 2" );
		}

		// A priority queue of CodingTrees, ordered by their frequency.
		PriorityQueue<CodingTreeNode> priorityQueue = new PriorityQueue<>( symbols.length,
				Comparator.comparingDouble( node -> node.frequency ) );

		for( int i = 0; i < symbols.length; i++ )
		{
			priorityQueue.offer( new CodingTreeNode( frequencies[ i ], symbols[ i ] ) );
		}

		// As long as there are at least two CodingTree nodes in the queue, remove two nodes with the least
		// frequencies and merge them and add the merged node back to the queue.
		while( priorityQueue.size() >= 2 )
		{
			CodingTreeNode left = priorityQueue.poll(), right = priorityQueue.poll();
			CodingTreeNode parent = new CodingTreeNode( left.frequency + right.frequency, left, right );

			priorityQueue.offer( parent );
		}

		this.root = priorityQueue.poll();
	}

	/**
	 * Traverse the CodingTree in the In-order traversal fashion and print the
	 * codes of the leaf nodes.
	 * <p>
	 * Left sub-tree adds a '0' to the code and right sub-tree adds a '1'
	 */
	public void printCodes()
	{
		StringBuilder stringBuilder = new StringBuilder();
		inOrderTraversal( this.root, stringBuilder );
	}

	/**
	 * Recursive in-order traversal of the sub-tree rooted at {@code root}.
	 * The StringBuilder is used to keep track of the path taken to reach the root.
	 * <p>
	 * '0' in the path means a left child was traversed. '1' means right child's path
	 * was traversed.
	 *
	 * @param root          Root of the sub-tree to be traversed
	 * @param stringBuilder String representing the path so far
	 */
	private static void inOrderTraversal( CodingTreeNode root, StringBuilder stringBuilder )
	{
		if( root == null )
		{
			return;
		}

		if( root.symbol != null )
		{
			System.out.println( root + " : " + stringBuilder.toString() );
		}
		else
		{
			stringBuilder.append( "0" );
			inOrderTraversal( root.left, stringBuilder );
			stringBuilder.deleteCharAt( stringBuilder.length() - 1 );

			stringBuilder.append( "1" );
			inOrderTraversal( root.right, stringBuilder );
			stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
		}
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

		System.out.print("n: ");
		int n = Integer.parseInt( in.next() );
		char[] symbols = new char[ n ];
		float[] frequencies = new float[ n ];

		System.out.print("Enter the symbols: ");
		for( int i = 0; i < n; i++ )
		{
			symbols[ i ] = in.next().charAt( 0 );
		}

		System.out.print("Enter their frequencies: ");
		for( int i = 0; i < n; i++ )
		{
			frequencies[ i ] = Float.parseFloat( in.next() );
		}

		HuffmanCoding huffmanCoding = new HuffmanCoding( symbols, frequencies );
		huffmanCoding.printCodes();
	}
}

/*
* Sample inputs:
16
e n o u a t m i x p h s r l f .
4 2 1 1 4 2 2 2 1 1 2 2 1 1 3 7

6
a b c d e f
5 9 12 13 16 45
* */