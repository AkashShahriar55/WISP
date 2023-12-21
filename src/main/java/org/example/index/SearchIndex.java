package org.example.index;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import org.example.index.BuildWikiIndex.FieldNames;
import org.example.model.Edge;
import org.example.model.Vertex;

public class SearchIndex {
	public static final HashMap<String,Float> BOOSTS = new HashMap<String,Float>();
	static {
		BOOSTS.put(FieldNames.TITLE.name(), 5f);
		BOOSTS.put(FieldNames.URL.name(), 5f); 
	}

	public static final int DOCS_PER_PAGE  = 10;
	
	private static IndexSearcher searcher;
	
	public SearchIndex(String in) throws IOException {
		// open a reader for the directory
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(in)));
		// open a searcher over the reader
		searcher = new IndexSearcher(reader);
	}
	
	public SearchIndex(IndexSearcher s) throws IOException {
		searcher = s;
	}
	
	public String searchUrl(String url) throws IOException {
		String title = "url:"+url+" not found";
		if(url!=null){
			url = url.trim();
			if(!url.isEmpty()){
				try{
					// parse query
					Term term = new Term("URL", url);
					Query query = new TermQuery(term);
	
					// get hits 
					TopDocs results = searcher.search(query, DOCS_PER_PAGE);
					ScoreDoc[] hits = results.scoreDocs;
	
					Document doc = searcher.doc(hits[0].doc);
					title = doc.get(FieldNames.TITLE.name());
					
				} catch(Exception e){
					System.err.println("Error with query '"+url+"'");
					e.printStackTrace();
				}
			}
		}
		return title;
	}
	
	public String pathDisplay(LinkedList<Vertex> path, boolean edgeWeight) throws IOException{
		Vertex before = null;
		Edge edge = null;
		String res = "";
		for (Vertex vertex : path) {
        	if(before != null) {
        		edge = before.getEdge(vertex, edgeWeight);
        		String edgeLabel = getLabel(this.searchUrl("<http://www.wikidata.org/entity/P"+before.getEdge(vertex, edgeWeight).getId()+">"));
        		if(edge.isOutgoing(before))
        			res = res.concat(" --"+edgeLabel+"--> ");
        		else
        			res = res.concat(" <--"+edgeLabel+"-- ");
        	}
            res = res.concat(getLabel(this.searchUrl("<http://www.wikidata.org/entity/Q"+vertex.getId()+">")));
            before = vertex;
        }
		return res;
	}
	
	private String getLabel(String label) {
		return label.substring(1,label.length()-4);
	}
}
