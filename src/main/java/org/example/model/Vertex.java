package org.example.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Vertex is the class that implements a vertex with edges
 * of a graph.
 */
public class Vertex {

    private Integer id;

    private double distance = Integer.MAX_VALUE;

    private List<Edge> adjacentEdges = new LinkedList<>();
    
    private double degree;
    
    private double degreeNormalized;
    
    private double pageRank;
    
    private double pageRankNormalized;

    public Vertex(Integer id) {
        this.id = id;
        this.degree = 1;
        this.pageRank = 1;
        this.degreeNormalized = 1;
        this.pageRankNormalized = 1;
    }

    public void addEdge(Edge edge) {
        adjacentEdges.add(edge);
    }

    public Integer getId() {
        return id;
    }

    public double getBaselineWeight() {
        return 1;
    }

    public double getDegree() {
        return degree;
    }

    public double getPageRank() {
        return pageRank;
    }

    public double getDegreeNormalized() {
        return degreeNormalized;
    }

    public double getPageRankNormalized() {
        return pageRankNormalized;
    }

    public void setDegree(double degree) {
        this.degree = degree;
    }

    public void setPageRank(double pageRank) {
        this.pageRank = pageRank;
    }

    public void setDegreeNormalized(double degreeNormalized) {
        this.degreeNormalized = degreeNormalized;
    }

    public void setPageRankNormalized(double pageRankNormalized) {
        this.pageRankNormalized = pageRankNormalized;
    }

    public List<Edge> getAdjacentEdges() {
        return adjacentEdges;
    }

    public void setAdjacentEdges(List<Edge> adjacentEdges) {
        this.adjacentEdges = adjacentEdges;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double d) {
        this.distance = d;
    }
    
    public Edge getEdge(Vertex destination, boolean weightedEdges) {
    	Edge edge = null;
    	if(!weightedEdges){
	    	for (Edge e : adjacentEdges) {
	            if(e.getOppositeVertex(this).equals(destination))
	            	return e;
	    	}
    	} else {
	    	double edgeW = Double.MAX_VALUE;
	    	for (Edge e : adjacentEdges) {
	            if(e.getOppositeVertex(this).equals(destination) && e.getWeight() <= edgeW) {
	            	edge = e;
	            	edgeW = e.getWeight();
	            }
	    	}
    	}
    	return edge;
    }
    
    public List<Edge> getOutgoingEdges() {
    	List<Edge> edges = new ArrayList<Edge>();
    	for (Edge e : adjacentEdges) {
            if(e.isOutgoing(this))
            	edges.add(e);
    	}
    	return edges;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return id.toString();
    }

}
