package org.example.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandler;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;


/**
 * NtLabels is the class to filter triples with 
 * labels.
 */
public class NtLabels {
	public static String millisToShortDHMS(long duration) { 
	     String res = ""; 
	     long days  = TimeUnit.MILLISECONDS.toDays(duration); 
	     long hours = TimeUnit.MILLISECONDS.toHours(duration) 
	                    - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration)); 
	     long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) 
	                      - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)); 
	     long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) 
	                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)); 
	     if (days == 0) { 
	       res = String.format("%02d hours %02d minutes %02d seconds", hours, minutes, seconds); 
	     } 
	     else { 
	       res = String.format("%d days %02d hours %02d minutes %02d seconds", days, hours, minutes, seconds); 
	     } 
	     return res; 
	   }
	 
	public static void main(String[] args) throws IOException{
		long startTime = System.currentTimeMillis();
		if(args.length!=3 && args.length!=4){
			System.err.println("usage: input output idFilter");
			System.err.println("assumes input and output utf-8");
			System.err.println("use '-' for stdin/stdout");
			System.exit(0);
		}
		
		InputStream in = null;
		if(args[0].equals("-")){
			in = System.in;
		} else{
			in = new FileInputStream(args[0]);
			if(args[0].endsWith(".gz")){
				in = new GZIPInputStream(in);
			}
		}
		
		OutputStream out = null;
		if(args[1].equals("-")){
			out = System.out;
		} else{
			out = new FileOutputStream(args[1]);
			if(args[1].endsWith(".gz")){
				out = new GZIPOutputStream(out);
			}
		}
		BufferedReader inBr = new BufferedReader(new InputStreamReader(in,"utf-8")); 
				
		RDFParser aParser = Rio.createParser(RDFFormat.NTRIPLES);
		
		RDFHandler sink = Rio.createWriter(RDFFormat.NTRIPLES, out);
		
		if(args.length==3){
			sink = RDFPredicateFilterHandler.createRDFPredicateFilterHandler(new File(args[2]), sink);
		}

		aParser.setRDFHandler(sink);
		
		try{
			aParser.parse(inBr, "");
		} catch(Exception e){
			inBr.close();
			throw new IOException(e);
		}
		inBr.close();
		out.close();
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);  
		System.out.format("Milli = %s, ( S_Start : %s, S_End : %s ) \n", duration, startTime, endTime );
		System.out.println("Human-Readable format : "+millisToShortDHMS( duration ) );
	}
}
