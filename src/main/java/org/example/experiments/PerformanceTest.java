package org.example.experiments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.example.dijkstra.DijkstraAlgorithm;
import org.example.model.Edge;
import org.example.model.Graph;
import org.example.model.Vertex;
import org.example.util.SearchResult;

/**
 * PerformanceTest is the class to measure the searches made 
 * for different graphs weights.
 */
public class PerformanceTest {

	public static void main(String[] args) throws IOException {

		Option inO = new Option("i", "input data file");
		inO.setArgs(1);
		inO.setRequired(true);
		
		Option inN = new Option("n", "input nodes file");
		inN.setArgs(1);
		inN.setRequired(false);
		
		Option outO = new Option("o", "output file");
		outO.setArgs(1);
		outO.setRequired(true);
		
		Option maxV = new Option("m", "max node value");
		maxV.setArgs(1);
		maxV.setRequired(true);

		Options options = new Options();
		options.addOption(inO);
		options.addOption(inN);
		options.addOption(outO);
		options.addOption(maxV);

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
		String n = cmd.getOptionValue(inN.getOpt());
		String out = cmd.getOptionValue(outO.getOpt());
		String maxValue = cmd.getOptionValue(maxV.getOpt());

		Graph graph = new Graph(filename);
		
		graph.degreeWeight();
		graph.degreeWeight2();
		graph.pageRankWeight(10);
		
		/*	TO SAVE PAGERANKS
		 * 				edgeWeight = false;
				System.out.println("Saving PageRanks");
		        try {
		            //create a temporary file
		            File logFile = new File(out);
		            logFile = new File(logFile.getParent(), "pageRanks.txt");
		            
		            HashMap<Integer, Vertex> nodesPR = graph.getNodes();

		            writer = new BufferedWriter(new FileWriter(logFile, true));
		            for (Map.Entry<Integer, Vertex> entry : nodesPR.entrySet()) {
		                Integer key = entry.getKey();
		                Vertex value = entry.getValue();
		                writer.write(key+" "+value.getWeight()+"\n");
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		            try {
		                // Close the writer regardless of what happens...
		                writer.close();
		            } catch (Exception e) {
		            }
		        }
		 * 
		 */
		
		
		graph.pageRankWeight2();
		
		HashMap<Integer, Vertex> nodes = graph.getNodes();
		
		double resMax[][] = new double[9][];
		double resMin[][] = new double[9][];
		double resMean[][] = new double[9][];
		double res1Q[][] = new double[9][];
		double res2Q[][] = new double[9][];
		double res3Q[][] = new double[9][];
		
		int notFoundPath = 0;
		
		String[] variations = {"Baseline", "Degree", "Degree and edge weight", "Degree normalized", "Degree normalized and edge weight", "PageRank", "PageRank and edge weight", "PageRank normalized", "PageRank normalized and edge weight"};
		
		
		int[][] nodeSF;
		if(n == null) {
			nodeSF = new int[300][];
			for(int i=0; i<300; i++) {
				Vertex start = null;
				Vertex end = null;
				int s = 0;
				int e = 0;
				while(start == null) {
					s = (int)(Math.random() * Integer.parseInt(maxValue));
					start = nodes.get(s);
				}
				while(end == null) {
					e = (int)(Math.random() * Integer.parseInt(maxValue));
					end = nodes.get(e);
				}
				nodeSF[i] = new int[]{s, e};	
			}
		} else {
			nodeSF = new int[100][];
			try(BufferedReader br = new BufferedReader(new FileReader(n))) {
			    String line = br.readLine();
			    
			    int i = 0;
			    while (line != null) {
			        String[] splited = line.split("\\s+");
			        nodeSF[i] = new int[]{Integer.parseInt(splited[0]), Integer.parseInt(splited[1])};
			        line = br.readLine();
			        i++;
			    }
			}
		}
		
		BufferedWriter writer = null;
        try {
            //create a temporary file
            File logFile = new File(out);
            logFile = new File(logFile.getParent(), "log_"+logFile.getName());
            
            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("sep=;\n");
    		writer.write("Variations;Start node;End node;Path;Weight;Path length;Search duration\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
		
		for(int k=0;k<9;k++) {
			System.out.println("Searching with: "+variations[k]);

			int nSearch = 100;
			int pathLength[] = new int[nSearch];
			long searchDuration[] = new long[nSearch];
			int visitedNodes[] = new int[nSearch];
			int maxDegree[] = new int[nSearch];
			double maxWeight[] =  new double[nSearch];
			
			// Log Info
			LinkedList<LinkedList<Vertex>> paths = new LinkedList<LinkedList<Vertex>>();
			LinkedList<Long> searchesDuration = new LinkedList<Long>();
			
			int count = 0;
			Function<Vertex,Double> getWeight = null;
			boolean edgeWeight = false;
			for(int i=0;i<nodeSF.length && count<100;i++) {
				Vertex start = nodes.get(nodeSF[i][0]);
				Vertex end = nodes.get(nodeSF[i][1]);
				long startTime = System.currentTimeMillis();
				DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
				
				
				SearchResult result = null;
				if(k==0) {
					edgeWeight = false;
					result = dijkstra.executeBaseline(start, end);
					getWeight = Vertex::getBaselineWeight;
				}
				if(k==1) {
					edgeWeight = false;
					result = dijkstra.executeDegree(start, end, edgeWeight);
					getWeight = Vertex::getDegree;
				}
				if(k==2) {
					edgeWeight = true;
					result = dijkstra.executeDegree(start, end, edgeWeight);
					getWeight = Vertex::getDegree;
				}
				if(k==3) {
					edgeWeight = false;
					result = dijkstra.executeDegreeNormalized(start, end, edgeWeight);
					getWeight = Vertex::getDegreeNormalized;
				}
				if(k==4) {
					edgeWeight = true;
					result = dijkstra.executeDegreeNormalized(start, end, edgeWeight);
					getWeight = Vertex::getDegreeNormalized;
				}
				if(k==5) {
					edgeWeight = false;
					result = dijkstra.executePageRank(start, end, edgeWeight);
					getWeight = Vertex::getPageRank;
				}
				if(k==6) {
					edgeWeight = true;
					result = dijkstra.executePageRank(start, end, edgeWeight);
					getWeight = Vertex::getPageRank;
				}
				if(k==7) {
					edgeWeight = false;
					result = dijkstra.executePageRankNormalized(start, end, edgeWeight);
					getWeight = Vertex::getPageRankNormalized;
				}
				if(k==8) {
					edgeWeight = true;
					result = dijkstra.executePageRankNormalized(start, end, edgeWeight);
					getWeight = Vertex::getPageRankNormalized;
				}
				
				long endTime = System.currentTimeMillis();
				long duration = (endTime - startTime);
				
				try {
					LinkedList<Vertex> path = result.getPath();
					
					pathLength[count] = path.size();
					searchDuration[count] = duration;
					visitedNodes[count] = result.getVisitedNodes();
					
					for(Vertex node : path) {
						maxDegree[count] = maxDegree[count] < node.getAdjacentEdges().size() ? node.getAdjacentEdges().size() : maxDegree[count];
						maxWeight[count] = maxWeight[count] < getWeight.apply(node) ? getWeight.apply(node) : maxWeight[count];
					}
					
					paths.add(path);
					searchesDuration.add(duration);
					
					count++;
				} catch(NullPointerException e) {
					notFoundPath++;
				}
			}
			
			int lastVal = pathLength.length-1;
			Arrays.sort(pathLength);
			Arrays.sort(searchDuration);
			Arrays.sort(visitedNodes);
			Arrays.sort(maxDegree);
			Arrays.sort(maxWeight);
			resMax[k] = new double[]{pathLength[lastVal], searchDuration[lastVal], visitedNodes[lastVal], maxDegree[lastVal], maxWeight[lastVal]};
			resMin[k] = new double[]{pathLength[0], searchDuration[0], visitedNodes[0], maxDegree[0], maxWeight[0]};
			resMean[k] = new double[]{mean(pathLength), mean(searchDuration), mean(visitedNodes), mean(maxDegree), mean(maxWeight)};
			res1Q[k] = new double[]{quartile(pathLength, 25), quartile(searchDuration, 25), quartile(visitedNodes, 25), quartile(maxDegree, 25), quartile(maxWeight, 25)};
			res2Q[k] = new double[]{quartile(pathLength, 50), quartile(searchDuration, 50), quartile(visitedNodes, 50), quartile(maxDegree, 50), quartile(maxWeight, 50)};
			res3Q[k] = new double[]{quartile(pathLength, 75), quartile(searchDuration, 75), quartile(visitedNodes, 75), quartile(maxDegree, 75), quartile(maxWeight, 75)};
			
			pathLength = new int[nSearch];
			searchDuration = new long[nSearch];
			visitedNodes = new int[nSearch];
			maxDegree = new int[nSearch];
			maxWeight = new double[nSearch];
			
			// Save log info
	        try {
	            //create a temporary file
	            File logFile = new File(out);
	            logFile = new File(logFile.getParent(), "log_"+logFile.getName());

	            writer = new BufferedWriter(new FileWriter(logFile, true));
	            for(int i=0; i<paths.size(); i++) {
	        		writer.write(variations[k]+";Q"+paths.get(i).get(0).getId()+";Q"+paths.get(i).get(paths.get(i).size()-1).getId()+";"+printPath(paths.get(i), edgeWeight)+";"+printWeightPath(paths.get(i), getWeight, edgeWeight)+";"+paths.get(i).size()+";"+searchesDuration.get(i)+"\n");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                // Close the writer regardless of what happens...
	                writer.close();
	            } catch (Exception e) {
	            }
	        }
		}
		
		System.out.println("Not found path: "+notFoundPath);
		
        try {
            //create a temporary file
            File resultFile = new File(out);

            // This will output the full path where the file will be written to...
            System.out.println(resultFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(resultFile));
            writer.write("sep=;\n");
            writer.write("Max\n");
    		writer.write("Variations;Path length;Search duration;Visited nodes;Max degree;Max weight\n");
            for(int i=0; i<9; i++) {
        		writer.write(variations[i]+";"+resMax[i][0]+";"+resMax[i][1]+";"+resMax[i][2]+";"+resMax[i][3]+";"+resMax[i][4]+"\n");
            }
            writer.write("Min\n");
    		writer.write("Variations;Path length;Search duration;Visited nodes;Max degree;Max weight\n");
            for(int i=0; i<9; i++) {
        		writer.write(variations[i]+";"+resMin[i][0]+";"+resMin[i][1]+";"+resMin[i][2]+";"+resMin[i][3]+";"+resMin[i][4]+"\n");
            }
            writer.write("Mean\n");
    		writer.write("Variations;Path length;Search duration;Visited nodes;Max degree;Max weight\n");
            for(int i=0; i<9; i++) {
        		writer.write(variations[i]+";"+resMean[i][0]+";"+resMean[i][1]+";"+resMean[i][2]+";"+resMean[i][3]+";"+Double.toString(resMean[i][4])+"\n");
            }
            writer.write("25 Percentile\n");
    		writer.write("Variations;Path length;Search duration;Visited nodes;Max degree;Max weight\n");
            for(int i=0; i<9; i++) {
        		writer.write(variations[i]+";"+res1Q[i][0]+";"+res1Q[i][1]+";"+res1Q[i][2]+";"+res1Q[i][3]+";"+Double.toString(res1Q[i][4])+"\n");
            }
            writer.write("Median\n");
    		writer.write("Variations;Path length;Search duration;Visited nodes;Max degree;Max weight\n");
            for(int i=0; i<9; i++) {
        		writer.write(variations[i]+";"+res2Q[i][0]+";"+res2Q[i][1]+";"+res2Q[i][2]+";"+res2Q[i][3]+";"+Double.toString(res2Q[i][4])+"\n");
            }
            writer.write("75 Percentile\n");
    		writer.write("Variations;Path length;Search duration;Visited nodes;Max degree;Max weight\n");
            for(int i=0; i<9; i++) {
        		writer.write(variations[i]+";"+res3Q[i][0]+";"+res3Q[i][1]+";"+res3Q[i][2]+";"+res3Q[i][3]+";"+Double.toString(res3Q[i][4])+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
	}
	
	public static double mean(int[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}
	
	public static double mean(long[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}
	
	public static double mean(double[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}
	
	public static double quartile(double[] values, double lowerPercent) {
        int n = (int) Math.round(values.length * lowerPercent / 100);
        return values[n];
    }
	
	public static long quartile(long[] values, double lowerPercent) {
        int n = (int) Math.round(values.length * lowerPercent / 100);
        return values[n];
    }
	
	public static int quartile(int[] values, double lowerPercent) {
        int n = (int) Math.round(values.length * lowerPercent / 100);
        return values[n];
    }
	
	public static String printPath(LinkedList<Vertex> path, boolean edgeWeight) {
		Vertex before = null;
		Edge edge = null;
		String res = "";
		for (Vertex vertex : path) {
        	if(before != null) {
        		edge = before.getEdge(vertex, edgeWeight);
        		if(edge.isOutgoing(vertex))
        			res = res.concat(",P"+edge.getId()+">,");
        		else
        			res = res.concat(",<P"+edge.getId()+",");
        	}
            res = res.concat("Q"+vertex.getId());
            before = vertex;
        }
		return res;
	}
	
	public static String printWeightPath(LinkedList<Vertex> path, Function<Vertex,Double> getWeight, boolean edgeWeight) {
		Vertex before = null;
		Edge edge = null;
		String res = "";
		for (Vertex vertex : path) {
        	if(before != null) {
        		edge = before.getEdge(vertex, edgeWeight);
        		if(edge.isOutgoing(vertex))
        			res = res.concat(","+edge.getWeight()+",");
        		else
        			res = res.concat(","+edge.getWeight()+",");
        	}
            res = res.concat(Double.toString(getWeight.apply(vertex)));
            before = vertex;
        }
		return res;
	}

}
