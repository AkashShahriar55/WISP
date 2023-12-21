package org.example.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;

import org.example.util.RDFEdgeCounterHandler;
import org.example.util.RDFGraphHandler;

/**
 * Vertex is the class that implements a graph with a set of
 * vertex.
 */
public class Graph {

    private HashMap<Integer, Vertex> nodes = new HashMap<Integer, Vertex>();

    public Graph(HashMap<Integer, Vertex> nodes) {
    	this.nodes = new HashMap<Integer, Vertex>(nodes);
    }

    public Graph(String filename) throws IOException {
    	InputStream in = null;
		in = new FileInputStream(filename);
		
		BufferedReader inBr = new BufferedReader(new InputStreamReader(in,"utf-8")); 
		
		RDFParser aParser = Rio.createParser(RDFFormat.NTRIPLES);
		HashMap<Integer, Integer> edges = new HashMap<Integer, Integer>();
		aParser.setRDFHandler(RDFEdgeCounterHandler.createRDFEdgeCounterHandler(edges));
		
		try{
			aParser.parse(inBr, "");
		} catch(Exception e){
			inBr.close();
			throw new IOException(e);
		}
		inBr.close();
		
		in = new FileInputStream(filename);
		
		inBr = new BufferedReader(new InputStreamReader(in,"utf-8")); 
		
		aParser = Rio.createParser(RDFFormat.NTRIPLES);
		this.nodes = new HashMap<Integer, Vertex>();
		aParser.setRDFHandler(RDFGraphHandler.createRDFGraphHandler(nodes, edges));
		
		try{
			aParser.parse(inBr, "");
		} catch(Exception e){
			inBr.close();
			throw new IOException(e);
		}
		inBr.close();
    }
    
    public void addNode(int key, Vertex node) {
        nodes.put(key, node);
    }

    public HashMap<Integer, Vertex> getNodes() {
        return nodes;
    }

    public void setNodes(HashMap<Integer, Vertex> nodes) {
        this.nodes = nodes;
    }
    
    public void degreeWeight() {
    	for(Vertex value : nodes.values()) {
            value.setDegree(value.getAdjacentEdges().size());
        }
    }
    
    public void degreeWeight2() {
    	Double maxWeight = 0.0;
    	for(Vertex value : nodes.values()) {
            if(maxWeight < value.getDegree())
            	maxWeight = value.getDegree();
        }

    	for(Vertex value : nodes.values()) {
            value.setDegreeNormalized(1+value.getDegree()/maxWeight);
        }
    }
    
    public void pageRankWeight(int iters) {
    	double D = 0.85d;
    	
		int graphSize = nodes.size();
		
		Map<Integer, Double> oldranks = new HashMap<Integer, Double>();
		
		double initial = 1d / graphSize;
		
		for (Integer key : nodes.keySet()) {
			oldranks.put(key, initial);
		}
		
		Map<Integer, Double> ranks = null;
		for(int i=0; i<iters; i++){
			double noLinkRank = 0d;
			ranks = new HashMap<Integer, Double>();
			
			for (Vertex node : nodes.values()) {
				List<Edge> out = node.getOutgoingEdges();
				if(!out.isEmpty()){
					double share = oldranks.get(node.getId())*D/out.size();
					for(Edge o:out){
						ranks.compute(o.getDestination().getId(), (k, v) -> v == null? share: v + share);
					}
				} else {
					noLinkRank += oldranks.get(node.getId());
				}
			}
			
			double shareNoLink = (noLinkRank*D) / graphSize; 
			
			double shareMinusD = (1d - D) / graphSize; 
			
			double weakRank = shareNoLink + shareMinusD;
			
//			double suma = 0d;
//			double e = 0d;
			
			for(Vertex node : nodes.values()){
				ranks.compute(node.getId(), (k, v) -> v == null? weakRank: v + weakRank);
//				suma += ranks.get(node.getId());
//				e += Math.abs(oldranks.get(node.getId()) - ranks.get(node.getId()));
			}
			
//			System.err.println("Iteration "+i+" finished! Sum "+suma+" Epsilon "+e);
			
			oldranks = new HashMap<Integer, Double>(ranks);
		}
		
		for (Map.Entry<Integer, Double> entry : ranks.entrySet()) {
		    nodes.get(entry.getKey()).setPageRank(entry.getValue());
		}
    }
    
    public void pageRankWeight2() {
    	Double maxWeight = 0.0;
    	for(Vertex value : nodes.values()) {
            if(maxWeight < value.getPageRank())
            	maxWeight = value.getPageRank();
        }
    	
    	for(Vertex value : nodes.values()) {
            value.setPageRankNormalized(1+value.getPageRank()/maxWeight);
        }
    }
}