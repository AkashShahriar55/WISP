package org.example.util;

import java.util.Comparator;

import org.example.model.Vertex;

public class VertexDistanceComparator implements Comparator<Vertex> {

	@Override
	public int compare(Vertex v1, Vertex v2) {
		if(v1.getDistance() < v2.getDistance())
			return -1;
		if(v1.getDistance() > v2.getDistance())
			return 1;
		return 0;
	}

}
