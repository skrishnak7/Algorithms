package cs6301.g27;

import cs6301.g00.Graph;
import cs6301.g00.Graph.Vertex;
import cs6301.g00.GraphAlgorithm;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;




public class ShortestPath extends GraphAlgorithm<ShortestPath.ShortestPathVertex> {
	
	private static final int INFINITY = Integer.MAX_VALUE;
	public class ShortestPathVertex implements Comparator<ShortestPathVertex>, Index {
		
		
		
		boolean seen;
		int distance;
		Vertex parent;
		Vertex vertex;
		int index;
 
		
		ShortestPathVertex(Vertex u){
			seen = false;
			parent = null;
			distance = 0;
			vertex = u;
			index = -1;
		}
		
		@Override
		public int compare( ShortestPathVertex u, ShortestPathVertex v )
		{
			return u.distance - v.distance;
		}


		@Override
		public void putIndex( int index )
		{
			this.index = index;
		}

		@Override
		public int getIndex()
		{
			return this.index;
		}

	}
	
	Vertex source;
	
	ShortestPath(Graph g, Vertex s){
		super(g);
		this.source = s;
		node = new ShortestPathVertex[g.size()];
		
		for(Graph.Vertex u : g) {
			node[u.getName()] = new ShortestPathVertex(u);
		}
		
		
	}
	
	public void initialize()
	{
		for(Graph.Vertex u : g) {
			
			getVertex(u).seen = false;
			getVertex(u).distance  = INFINITY;
			getVertex(u).parent = null; 
			
		}
		
		getVertex(source).seen = true;
		getVertex(source).distance = 0;
		getVertex(source).parent = source;
		
		
	}
	
	public boolean relax(Vertex  u , Vertex v , int weight){
		boolean changed = false;
		ShortestPathVertex spu = getVertex(u);
		ShortestPathVertex spv = getVertex(v);	
		if(spv.distance> spu.distance +weight)
		{
			changed = true;
			spv.distance = spu.distance + weight;
			spv.parent = u;

		}
		return changed;
	}
	
	
	public void printShortestPath(boolean parent)
	{ 
		if(parent)
		{
			System.out.println("Vertex \t Distance \t Parent");
			for(Graph.Vertex u : g){
				ShortestPathVertex spu = getVertex(u);
				System.out.println(spu.vertex + "\t\t" + spu.distance + "\t\t"  +spu.parent);
			}
		}
		else{
			System.out.println("Vertex \t Distance");
			for(Graph.Vertex u : g){
				ShortestPathVertex spu = getVertex(u);
				System.out.println(spu.vertex + "\t\t" + spu.distance  );
			}
		}
			
	}

	
	void bfs() {
		Queue<Graph.Vertex> q = new ArrayDeque<Graph.Vertex>();
		initialize();
		q.add(source);
		
		while(!q.isEmpty()) {
			Vertex u = q.poll();
			for( Graph.Edge e : u) {
				Graph.Vertex v = e.otherEnd(u);
				ShortestPathVertex spv = getVertex(v);
				ShortestPathVertex spu = getVertex(u);
				if(!spv.seen) {
					spv.seen = true;
					spv.distance = spu.distance + 1;	
					spv.parent = u;
					q.offer(v);
				}
			}
		}
		
		printShortestPath(true);
		
	}
	

	public void dagShortestPaths() { 
		DFS run = new DFS(g);
		List<Graph.Vertex> topologicalOrder = run.topologicalOrder();
		initialize();
		for(Graph.Vertex u : topologicalOrder)
		{
			for(Graph.Edge e : u)
			{
				Graph.Vertex  v = e.otherEnd(u);
				relax( u, v, e.getWeight());	
			}
		}
		printShortestPath(true);
	} 
	
	public void dijkstra() throws Exception { 
		
		IndexedHeap<ShortestPathVertex> pq = new IndexedHeap<>( Arrays.copyOf( node, node.length ), Comparator.comparingInt( vertex -> vertex.distance ), node.length );
		initialize();
	
		while(!pq.isEmpty())
		{
			ShortestPathVertex spu = pq.deleteMin();
			spu.seen = true;
			Vertex u = spu.vertex;
			for(Graph.Edge e :  u){
				Graph.Vertex v = e.otherEnd(u);
				boolean changed = relax( u, v, e.getWeight());
				if(changed)
					pq.decreaseKey(getVertex(v));
				
			}
			
		}
		printShortestPath(false);
	}

	

 
    
}
