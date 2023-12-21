package org.example.experiments;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.example.dijkstra.DijkstraAlgorithm;
import org.example.model.Graph;
import org.example.model.Vertex;
import org.example.util.TimeParser;

/**
 * NtDijkstra is the class that reads a n-triples file, parses the triples
 * into a graph and runs the Dijkstra's algorithm for finding the shortest
 * path between nodes in a graph.
 */
public class NtDijkstra {
	
	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}
	
	public static void main(String[] args) throws IOException{
		long startTimeCreatingGraph = System.currentTimeMillis();
		String filename = "./data/subset100000.nt";

		Graph graph = new Graph(filename);
		graph.degreeWeight();
		graph.degreeWeight2();
		graph.pageRankWeight(10);
		graph.pageRankWeight2();
		
		long endTimeCreatingGraph = System.currentTimeMillis();
		long durationCreatingGraph = (endTimeCreatingGraph - startTimeCreatingGraph);
		System.out.format("Milli = %s, ( S_Start : %s, S_End : %s ) \n", durationCreatingGraph, endTimeCreatingGraph, startTimeCreatingGraph );
		System.out.println("Human-Readable format : "+TimeParser.millisToShortDHMS( durationCreatingGraph ) );
		HashMap<Integer, Vertex> nodes = graph.getNodes();
		
		// Get the Java runtime
	    Runtime runtime = Runtime.getRuntime();
	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
	    long memory = runtime.totalMemory() - runtime.freeMemory();
	    System.out.println("Used memory is megabytes: "
	        + bytesToMegabytes(memory));

		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		
		Vertex start = nodes.get(1001);
		Vertex end = nodes.get(352);
		
		LinkedList<Vertex> path = dijkstra.executeBaseline(start, end).getPath();
        System.out.println("Path length: "+path.size());

        Vertex before = null;
        for (Vertex vertex : path) {
        	if(before != null)
        		System.out.println("edge "+before.getEdge(vertex, false).getId());
            System.out.println("node "+vertex.getId());
            before = vertex;
        }
		
        long endTime = System.currentTimeMillis();
		long duration = (endTime - endTimeCreatingGraph);
		System.out.format("Milli = %s, ( S_Start : %s, S_End : %s ) \n", duration, endTimeCreatingGraph, endTime );
		long durationTotal = (endTime - startTimeCreatingGraph);  
		System.out.format("Milli = %s, ( S_Start : %s, S_End : %s ) \n", durationTotal, startTimeCreatingGraph, endTime );
		System.out.println("Human-Readable format : "+TimeParser.millisToShortDHMS( durationTotal ) );
		

	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
	    long memory2 = runtime.totalMemory() - runtime.freeMemory() - memory;
//	    System.out.println("Used memory is bytes: " + memory2);
	    System.out.println("Used memory is megabytes: "
	        + bytesToMegabytes(memory2));
	}
}
