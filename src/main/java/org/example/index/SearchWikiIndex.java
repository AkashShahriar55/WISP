package org.example.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.AlreadyBoundException;
import java.util.HashMap;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import org.example.index.BuildWikiIndex.FieldNames;

/**
 * Main method to search articles using Lucene.
 * 
 * @author Aidan
 */
public class SearchWikiIndex {

	public static final HashMap<String,Float> BOOSTS = new HashMap<String,Float>();
	static {
//		BOOSTS.put(FieldNames.ABSTRACT.name(), 1f); //<- default
		BOOSTS.put(FieldNames.TITLE.name(), 5f);
		BOOSTS.put(FieldNames.URL.name(), 5f); 
	}

	public static final int DOCS_PER_PAGE  = 10;

	public static void main(String args[]) throws IOException, ClassNotFoundException, AlreadyBoundException, InstantiationException, IllegalAccessException{
		Option inO = new Option("i", "input index directory");
		inO.setArgs(1);
		inO.setRequired(true);

		Options options = new Options();
		options.addOption(inO);

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

		String in = cmd.getOptionValue(inO.getOpt());
		System.err.println("Opening directory at  "+in);

		startSearchApp(in);
	}

	/**
	 * 
	 * @param inDirectory : the location of the index directory
	 * @throws IOException
	 */
	public static void startSearchApp(String inDirectory) throws IOException {
		// open a reader for the directory
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(inDirectory)));
		// open a searcher over the reader
		IndexSearcher searcher = new IndexSearcher(reader);
		// use the same analyzer as the build
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);

		// this accepts queries/searches and parses them into
		// searches over the index
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
				Version.LUCENE_48,
				new String[] {FieldNames.TITLE.name()},
				analyzer, BOOSTS);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "utf-8"));

		while (true) {
			System.out.println("Enter a keyword search phrase:");

			String line = br.readLine();
			if(line!=null){
				line = line.trim();
				if(!line.isEmpty()){
					try{
						// parse query
//						Term term = new Term("URL", line);
//						Query query = new TermQuery(term);
//						Query query = new QueryParser(Version.LUCENE_48, "URL", analyzer).parse(line);
						Query query = queryParser.parse(line);

						// get hits 
						TopDocs results = searcher.search(query, DOCS_PER_PAGE);
						ScoreDoc[] hits = results.scoreDocs;

						System.out.println("Running query: "+line);
						System.out.println("Parsed query: "+query);
						System.out.println("Matching documents: "+results.totalHits);
						System.out.println("Showing top "+DOCS_PER_PAGE+" results");

						for(int i=0; i<hits.length; i++) {
							Document doc = searcher.doc(hits[i].doc);
							String title = doc.get(FieldNames.TITLE.name());
//							String abst = doc.get(FieldNames.ABSTRACT.name());
							String url = doc.get(FieldNames.URL.name());
//							String rank = doc.get(FieldNames.RANK.name());
							//System.out.println(rank+"\t"+(i+1)+"\t"+url+"\t"+title+"\t"+abst); 
							//System.out.println((i+1)+"\t"+url+"\t"+title+"\t"+abst);
							System.out.println((i+1)+"\t"+url+"\t"+title);
						}
					} catch(Exception e){
						System.err.println("Error with query '"+line+"'");
						e.printStackTrace();
					}
				}
			}

		}
	}
}