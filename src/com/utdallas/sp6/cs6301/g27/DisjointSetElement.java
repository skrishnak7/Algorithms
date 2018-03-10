package cs6301.g27;

public interface DisjointSetElement<T>
{
	T getParent();
	void setParent( T parent );

	int getRank();
	void setRank( int rank );
}