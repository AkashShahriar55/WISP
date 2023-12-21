package org.example.util;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.rio.RDFHandler;
import org.eclipse.rdf4j.rio.RDFHandlerException;

import org.example.model.Edge;
import org.example.model.Vertex;

public class RDFGraphHandler implements RDFHandler{
	HashMap<Integer, Vertex> nodes = null;
	HashMap<Integer, Integer> edgesCount = null;
	Integer maxEdgeCount = 0;
	
	public RDFGraphHandler(HashMap<Integer, Vertex> nodes, HashMap<Integer, Integer> edgesCount){
		this.nodes = nodes;
		this.edgesCount = edgesCount;
    	for(Integer value : edgesCount.values()) {
            if(maxEdgeCount < value)
            	maxEdgeCount = value;
        }
	}
	
	public static RDFGraphHandler createRDFGraphHandler(HashMap<Integer, Vertex> nodes, HashMap<Integer, Integer> edgesCount) throws IOException{
		return new RDFGraphHandler(nodes, edgesCount);
	}

	@Override
	public void endRDF() throws RDFHandlerException {
		//sink.endRDF();
	}

	@Override
	public void handleComment(String arg0) throws RDFHandlerException {
		//sink.handleComment(arg0);		
	}

	@Override
	public void handleNamespace(String arg0, String arg1) throws RDFHandlerException {
		//sink.handleNamespace(arg0,arg1);		
	}

	@Override
	public void handleStatement(Statement arg0) throws RDFHandlerException {
		IRI subject = (IRI)arg0.getSubject();
		IRI predicate = arg0.getPredicate();
		Value object = arg0.getObject();
		String strSubject = subject.toString();
		String strPredicate = predicate.toString();
		String strObject = object.toString();
		int subjectKey = getObjectId(strSubject);
		
		Vertex subjectNode;
		Vertex objectNode;
		if(nodes.containsKey(subjectKey)){
			subjectNode = nodes.get(subjectKey);
		} else {
			subjectNode = new Vertex(subjectKey);
		}
		int ObjectKey = getObjectId(strObject);
		if(nodes.containsKey(ObjectKey)){
			objectNode = nodes.get(ObjectKey);
		} else {
			objectNode = new Vertex(ObjectKey);
		}
		double weight = 1.0 + (double) edgesCount.get(getPredicateId(strPredicate))/maxEdgeCount;
		Edge edge = new Edge(getPredicateId(strPredicate), subjectNode, objectNode, weight);
		subjectNode.addEdge(edge);
		objectNode.addEdge(edge);
		nodes.put(subjectKey, subjectNode);
		nodes.put(ObjectKey, objectNode);
	}
	
	private Integer getPredicateId(String pred) {
		if(pred.equals("http://www.w3.org/2002/07/owl#sameAs"))
			return -1;
		else return Integer.parseInt(pred.substring(37, pred.length()));	
	}
	
	private Integer getObjectId(String obj) {
		return Integer.parseInt(obj.substring(32, obj.length()));	
	}

	@Override
	public void startRDF() throws RDFHandlerException {
		//sink.startRDF();		
	}	
}

