package org.example.util;

import java.util.Comparator;

import org.example.model.Vertex;

public class DegreeComparator implements Comparator<Vertex> {
	
	@Override
	public int compare(Vertex v1, Vertex v2) {
		int v1Degree = v1.getAdjacentEdges().size();
		int v2Degree = v2.getAdjacentEdges().size();
		if(v1Degree < v2Degree)
			return -1;
		if(v1Degree > v2Degree)
			return 1;
		return 0;
	}
}

