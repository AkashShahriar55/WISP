package org.example.util;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import org.example.model.Graph;
import org.example.model.Vertex;

public class GetMaxDegree {

	public static void main(String[] args) throws IOException {
		String filename = "./subsets/subset51200000.nt";

		Graph graph = new Graph(filename);
		HashMap<Integer, Vertex> nodes = graph.getNodes();
		Comparator<Vertex> comparator = new DegreeComparator();
		PriorityQueue<Vertex> degree = new PriorityQueue<>(comparator);
		for(Vertex value : nodes.values()) {
			degree.add(value);
			if (degree.size() > 5)
				degree.poll();
        }
		for(int i=0;i<5;i++) {
			Vertex aux = degree.poll();
			System.out.println("nodo ID: "+aux.getId()+" - degree: "+aux.getAdjacentEdges().size());
		}

	}

}
