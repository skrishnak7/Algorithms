package cs6301.g27;

public class DisjointSet<T extends DisjointSetElement<T>>
{
	public void makeSet(T u)
	{
		u.setParent( u );
		u.setRank( 0 );
	}

	public T find(T u)
	{
		if( u != u.getParent() )
		{
			u.setParent( find( u.getParent() ) );
		}

		return u.getParent();
	}

	public void union( T x, T y )
	{
		if( x.getRank() > y.getRank() )
		{
			y.setParent( x );
		}
		else if( y.getRank() > x.getRank() )
		{
			x.setParent( y );
		}
		else
		{
			x.setRank( x.getRank() + 1 );
			y.setParent( x );
		}
	}
}