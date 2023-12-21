package org.example.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.rio.RDFHandler;
import org.eclipse.rdf4j.rio.RDFHandlerException;

/**
 * RDFPredicateFilterHandler is the class with the handler to
 * filter triples with labels.
 */
public class RDFPredicateFilterHandler implements RDFHandler{
	private RDFHandler sink;
	TreeSet<String> allow = null;
	TreeSet<String> startsWithAllow = null;
	
	public RDFPredicateFilterHandler(RDFHandler sink, TreeSet<String> allow, TreeSet<String> startsWithAllow){
		this.sink = sink;
		this.allow = allow;
		this.startsWithAllow = startsWithAllow;
	}
	
	public static RDFPredicateFilterHandler createRDFPredicateFilterHandler(File f, RDFHandler sink) throws IOException{
		TreeSet<String> allow = new TreeSet<String>();
		TreeSet<String> startsWithAllow = new TreeSet<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		while((line=br.readLine())!=null){
			line = line.trim();
			if(!line.isEmpty()){
				if(line.startsWith("^")){
					startsWithAllow.add(line.substring(1));
				} else{
					allow.add(line);
				}
			}
		}
		br.close();
		
		return new RDFPredicateFilterHandler(sink, allow, startsWithAllow);
	}
	
	

	@Override
	public void endRDF() throws RDFHandlerException {
		sink.endRDF();
	}

	@Override
	public void handleComment(String arg0) throws RDFHandlerException {
		//sink.handleComment(arg0);		
	}

	@Override
	public void handleNamespace(String arg0, String arg1) throws RDFHandlerException {
		sink.handleNamespace(arg0,arg1);		
	}

	@Override
	public void handleStatement(Statement arg0) throws RDFHandlerException {
		Value object = arg0.getObject();
		String strObject = object.toString();
		IRI predicate = arg0.getPredicate();
		String strPredicate = predicate.toString();
		String str = "http://www.w3.org/2000/01/rdf-schema#label";
		if(strPredicate.startsWith(str)){
			if(enLanguage(strObject)){
				sink.handleStatement(arg0);
			}
		}
	}

	@Override
	public void startRDF() throws RDFHandlerException {
		sink.startRDF();		
	}
	
	public boolean enLanguage(String expression) {
	    String str = expression.substring(expression.length()-3, expression.length());
	    if(str.equals("@en"))
	    	return true;
	    else
	    	return false;
	}

	public int getLastDigit(String expression) {
	    StringBuffer result = new StringBuffer();

	    for (int i = expression.length() - 1; i >= 0; i--) {
	        char c = expression.charAt(i);

	        if (!Character.isDigit(c)) {
	            break;
	        }
	        else {
	            result.append(c); 
	        }
	    }
	    
	    String res = result.reverse().toString().trim();
	    if(!res.equals("")){
		    return Integer.parseInt(res);
	    } else {
	    	System.out.println(expression);
	    	return -1;
	    }
	}
	
}
