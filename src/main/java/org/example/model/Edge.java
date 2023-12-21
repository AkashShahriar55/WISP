package org.example.model;

/**
 * Edge is the class that implements an edge of a vertex and
 * its destination.
 */
public class Edge  {
    private final Integer id;
    private final Vertex origin;
    private final Vertex destination;
    private double weight;

    public Edge(Integer id, Vertex origin, Vertex destination) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.weight = 0;
    }
    
    public Edge(Integer id, Vertex origin, Vertex destination, double weight) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
    }

    public Integer getId() {
        return id;
    }
    
    public double getWeight() {
    	return weight;
    }
    
    public void setWeight(double weight) {
    	this.weight = weight;
    }
    
    public Vertex getDestination() {
        return destination;
    }
    
    public Vertex getOppositeVertex(Vertex node) {
        return origin != node ? origin : destination;
    }

    public Boolean isOutgoing(Vertex node) {
        return node.equals(origin);
    }

    @Override
    public String toString() {
        return " " + id + " " + destination;
    }

}
