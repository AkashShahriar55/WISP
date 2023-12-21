package org.example.util;

import java.io.IOException;
import java.util.Set;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.rio.RDFHandler;
import org.eclipse.rdf4j.rio.RDFHandlerException;

public class RDFNodesCounterHandler implements RDFHandler {
	Set<Integer> nodes = null;
	
	public RDFNodesCounterHandler(Set<Integer> nodes){
		this.nodes = nodes;
	}
	
	public static RDFNodesCounterHandler createRDFNodesCounterHandler(Set<Integer> nodes) throws IOException{
		return new RDFNodesCounterHandler(nodes);
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
		Value object = arg0.getObject();
		String strSubject = subject.toString();
		String strObject = object.toString();
		int subjectKey = getObjectId(strSubject);
		int ObjectKey = getObjectId(strObject);
		nodes.add(subjectKey);
		nodes.add(ObjectKey);
	}
	
	private Integer getObjectId(String obj) {
		return Integer.parseInt(obj.substring(32, obj.length()));	
	}

	@Override
	public void startRDF() throws RDFHandlerException {
		//sink.startRDF();		
	}	
}
