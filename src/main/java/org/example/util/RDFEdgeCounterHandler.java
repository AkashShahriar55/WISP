package org.example.util;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.rio.RDFHandler;
import org.eclipse.rdf4j.rio.RDFHandlerException;


public class RDFEdgeCounterHandler implements RDFHandler {
	HashMap<Integer, Integer> edges = null;
	
	public RDFEdgeCounterHandler(HashMap<Integer, Integer> edges){
		this.edges = edges;
	}
	
	public static RDFEdgeCounterHandler createRDFEdgeCounterHandler(HashMap<Integer, Integer> edges) throws IOException{
		return new RDFEdgeCounterHandler(edges);
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
		Value object = arg0.getObject();
		String strObject = object.toString();
		Value subject = arg0.getSubject();
		String strSubject = subject.toString();
		IRI predicate = arg0.getPredicate();
		String strPredicate = predicate.toString();
		String str = "http://www.wikidata.org/entity/Q";
		if(strSubject.startsWith(str) && strObject.startsWith(str)){
			Integer predicateId = getPredicateId(strPredicate);
			edges.put(predicateId, edges.get(predicateId) != null ? edges.get(predicateId) + 1 : 1);
		}
	}
	
	private Integer getPredicateId(String pred) {
		if(pred.equals("http://www.w3.org/2002/07/owl#sameAs"))
			return -1;
		else return Integer.parseInt(pred.substring(37, pred.length()));	
	}

	@Override
	public void startRDF() throws RDFHandlerException {
		//sink.startRDF();		
	}	
}
