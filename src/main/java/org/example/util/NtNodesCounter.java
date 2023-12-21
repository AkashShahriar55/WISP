package org.example.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;

public class NtNodesCounter {
	
	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}
	
	public static void main(String[] args) throws IOException{
		long startTime = System.currentTimeMillis();
		String filename = "data/subset51200000.nt";

		InputStream in = null;
		in = new FileInputStream(filename);
		if(filename.endsWith(".gz")){
			in = new GZIPInputStream(in);
		}
		
		BufferedReader inBr = new BufferedReader(new InputStreamReader(in,"utf-8")); 
		
		RDFParser aParser = Rio.createParser(RDFFormat.NTRIPLES);
		Set<Integer> nodes = new HashSet<>();
		aParser.setRDFHandler(RDFNodesCounterHandler.createRDFNodesCounterHandler(nodes));
		
		try{
			aParser.parse(inBr, "");
		} catch(Exception e){
			inBr.close();
			throw new IOException(e);
		}
		inBr.close();
		System.out.println("Number of nodes: "+nodes.size());
		
		long endTime2 = System.currentTimeMillis();
		long duration2 = (endTime2 - startTime);
		System.out.format("Milli = %s, ( S_Start : %s, S_End : %s ) \n", duration2, endTime2, startTime );
		System.out.println("Human-Readable format : "+TimeParser.millisToShortDHMS( duration2 ) );
		
		System.out.println(nodes.size());
	}
}
