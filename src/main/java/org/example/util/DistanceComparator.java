package org.example.util;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Distance> {

	@Override
	public int compare(Distance d1, Distance d2) {
		if(d1.getDistance() < d2.getDistance())
			return -1;
		if(d1.getDistance() > d2.getDistance())
			return 1;
		return 0;
	}

}
