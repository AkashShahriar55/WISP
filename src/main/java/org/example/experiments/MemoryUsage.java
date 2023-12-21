package org.example.experiments;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.example.dijkstra.DijkstraAlgorithm;
import org.example.model.Graph;
import org.example.model.Vertex;

/**
 * MemoryUsage is the class to size the used memory 
 * for finding the shortest path between nodes in a graph.
 */
public class MemoryUsage {
	
	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}
	
	public static void main(String[] args) throws IOException{
		Option inO = new Option("i", "input data file");
		inO.setArgs(1);
		inO.setRequired(true);
		Option sn = new Option("sn", "start node for path search");
		sn.setArgs(1);
		sn.setRequired(true);
		Option en = new Option("en", "end node for path search");
		en.setArgs(1);
		en.setRequired(true);
		
		Options options = new Options();
		options.addOption(inO);
		options.addOption(sn);
		options.addOption(en);
		
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("***ERROR: " + e.getClass() + ": " + e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("parameters:", options );
			return;
		}

		// print help options and return
		if (cmd.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("parameters:", options );
			return;
		}

		String filename = cmd.getOptionValue(inO.getOpt());
		int startNode = Integer.parseInt(cmd.getOptionValue(sn.getOpt()));
		int endNode = Integer.parseInt(cmd.getOptionValue(en.getOpt()));
		
		Graph graph = new Graph(filename);
		
		
		// Get the Java runtime
	    Runtime runtime = Runtime.getRuntime();
	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
	    long memory_graph = runtime.totalMemory() - runtime.freeMemory();
	    System.out.println("Graph:");
	    System.out.println("Used memory is bytes: " + memory_graph);
	    System.out.println("Used memory is megabytes: "
	        + bytesToMegabytes(memory_graph));
	    

		
		HashMap<Integer, Vertex> nodes = graph.getNodes();		
		
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		Vertex start = nodes.get(startNode);
		Vertex end = nodes.get(endNode);
		@SuppressWarnings("unused")
		LinkedList<Vertex> path = dijkstra.executeBaseline(start, end).getPath();

	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
	    long memory_search = runtime.totalMemory() - runtime.freeMemory() - memory_graph;
	    System.out.println("Path:");
	    System.out.println("Used memory is bytes: " + memory_search);
	    System.out.println("Used memory is megabytes: "
	        + bytesToMegabytes(memory_search));
	
	}
}
