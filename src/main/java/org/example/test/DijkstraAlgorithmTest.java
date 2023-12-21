package org.example.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import org.example.dijkstra.DijkstraAlgorithm;
import org.example.model.Edge;
import org.example.model.Graph;
import org.example.model.Vertex;
import org.example.util.SearchResult;

public class DijkstraAlgorithmTest {

    private Graph graph;
    private DijkstraAlgorithm dijkstra;

    @Before
    public final void setUp() {
        HashMap<Integer, Vertex> nodes = new HashMap<Integer, Vertex>();
        for(int i=1;i<=5;i++){
        	Vertex node = new Vertex(i);
			nodes.put(i, node);
        }
        Vertex node1 = nodes.get(1);
        node1.addEdge(new Edge(1, nodes.get(1), nodes.get(3)));
        node1.addEdge(new Edge(2, nodes.get(1), nodes.get(2)));
        nodes.replace(1, node1);
        Vertex node2 = nodes.get(2);
        node2.addEdge(new Edge(4, nodes.get(2), nodes.get(5)));
        node2.addEdge(new Edge(2, nodes.get(2), nodes.get(1)));
        nodes.replace(2, node2);
        Vertex node3 = nodes.get(3);
        node3.addEdge(new Edge(1, nodes.get(3), nodes.get(1)));
        node3.addEdge(new Edge(3, nodes.get(3), nodes.get(4)));
        nodes.replace(3, node3);
        Vertex node4 = nodes.get(4);
        node4.addEdge(new Edge(3, nodes.get(4), nodes.get(3)));
        node4.addEdge(new Edge(5, nodes.get(4), nodes.get(5)));
        nodes.replace(4, node4);
        Vertex node5 = nodes.get(5);
        node5.addEdge(new Edge(4, nodes.get(5), nodes.get(2)));
        node5.addEdge(new Edge(5, nodes.get(5), nodes.get(4)));
        nodes.replace(5, node5);
        graph = new Graph(nodes);
        dijkstra = new DijkstraAlgorithm(graph);
    }

    @Test
    public final void testGetPath() {
    	HashMap<Integer, Vertex> nodes = graph.getNodes();
    	Vertex start = nodes.get(1);
    	Vertex end = nodes.get(2);
    	LinkedList<Vertex> path = new LinkedList<Vertex>();
    	path.add(nodes.get(1));
    	path.add(nodes.get(2));
    	SearchResult result = dijkstra.executeBaseline(start, end);
        assertEquals(path, result.getPath());
    }
    
    @Test
    public final void testGetPath2() {
    	HashMap<Integer, Vertex> nodes = graph.getNodes();
    	Vertex start = nodes.get(2);
    	Vertex end = nodes.get(1);
    	LinkedList<Vertex> path = new LinkedList<Vertex>();
    	path.add(nodes.get(2));
    	path.add(nodes.get(1));
    	SearchResult result = dijkstra.executeBaseline(start, end);
        assertEquals(path, result.getPath());
    }
    
    @Test
    public final void testGetPath3() {
    	HashMap<Integer, Vertex> nodes = graph.getNodes();
    	Vertex start = nodes.get(1);
    	Vertex end = nodes.get(5);
    	LinkedList<Vertex> path = new LinkedList<Vertex>();
    	path.add(nodes.get(1));
    	path.add(nodes.get(2));
    	path.add(nodes.get(5));
    	SearchResult result = dijkstra.executeBaseline(start, end);
        assertEquals(path, result.getPath());
    }
    
    @Test
    public final void testGetPath4() {
    	HashMap<Integer, Vertex> nodes = graph.getNodes();
    	Vertex start = nodes.get(4);
    	Vertex end = nodes.get(1);
    	LinkedList<Vertex> path = new LinkedList<Vertex>();
    	path.add(nodes.get(4));
    	path.add(nodes.get(3));
    	path.add(nodes.get(1));
    	SearchResult result = dijkstra.executeBaseline(start, end);
        assertEquals(path, result.getPath());
    }
    
    @Test
    public final void testMultipleSearch() {
    	DijkstraAlgorithm dijkstra1 = new DijkstraAlgorithm(graph);
    	HashMap<Integer, Vertex> nodes = graph.getNodes();
    	Vertex start = nodes.get(1);
    	Vertex end = nodes.get(5);
    	LinkedList<Vertex> path = new LinkedList<Vertex>();
    	path.add(nodes.get(1));
    	path.add(nodes.get(2));
    	path.add(nodes.get(5));
    	SearchResult result = dijkstra1.executeBaseline(start, end);
        assertEquals(path, result.getPath());
        DijkstraAlgorithm dijkstra2 = new DijkstraAlgorithm(graph);
    	Vertex start2 = nodes.get(4);
    	Vertex end2 = nodes.get(1);
    	LinkedList<Vertex> path2 = new LinkedList<Vertex>();
    	path2.add(nodes.get(4));
    	path2.add(nodes.get(3));
    	path2.add(nodes.get(1));
    	SearchResult result2 = dijkstra2.executeBaseline(start2, end2);
        assertEquals(path2, result2.getPath());
    }
    
    @Test
    public final void testGetPath5() {
    	HashMap<Integer, Vertex> nodes = graph.getNodes();
        Vertex node3 = nodes.get(3);
        node3.setPageRank(10);
        nodes.replace(3, node3);
    	Vertex start = nodes.get(4);
    	Vertex end = nodes.get(1);
    	LinkedList<Vertex> path = new LinkedList<Vertex>();
    	path.add(nodes.get(4));
    	path.add(nodes.get(5));
    	path.add(nodes.get(2));
    	path.add(nodes.get(1));
    	SearchResult result = dijkstra.executePageRank(start, end, false);
        assertEquals(path, result.getPath());
    }
}
