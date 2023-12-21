package org.example.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import org.example.model.Graph;
import org.example.model.Vertex;

public class PageRankTest {
 	private Graph graph;

    @Before
    public final void setUp() throws IOException {
    	String filename = "./data/pageRanktest.nt";		
		graph = new Graph(filename);
    }
    
    @Test
    public final void testPageRank1Iter() {
		graph.pageRankWeight(1);
    	HashMap<Integer, Vertex> nodes = graph.getNodes();
        assertEquals(nodes.get(1).getPageRank(), 0.21388888888888888888888888888889, 1e-10);
        assertEquals(nodes.get(2).getPageRank(), 0.21388888888888888888888888888889, 1e-10);
        assertEquals(nodes.get(3).getPageRank(), 0.07222222222, 1e-10);
        assertEquals(nodes.get(4).getPageRank(), 0.21388888888888888888888888888889, 1e-10);
        assertEquals(nodes.get(5).getPageRank(), 0.09583333333, 1e-10);
        assertEquals(nodes.get(6).getPageRank(), 0.19027777777, 1e-10);
    }
    
    @Test
    public final void testPageRank2Iter() {
		graph.pageRankWeight(2);
    	HashMap<Integer, Vertex> nodes = graph.getNodes();
        assertEquals(nodes.get(1).getPageRank(), 0.24733796295, 1e-10);
        assertEquals(nodes.get(2).getPageRank(), 0.11354166666, 1e-10);
        assertEquals(nodes.get(3).getPageRank(), 0.08560185185, 1e-10);
        assertEquals(nodes.get(4).getPageRank(), 0.23395833333, 1e-10);
        assertEquals(nodes.get(5).getPageRank(), 0.11590277777, 1e-10);
        assertEquals(nodes.get(6).getPageRank(), 0.2036574074, 1e-10);
    }
}
