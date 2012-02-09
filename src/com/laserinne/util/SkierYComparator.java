package com.laserinne.util;

import java.util.Comparator;

import com.laserinne.base.Skier;

public class SkierYComparator implements Comparator<Skier>{
	@Override
	public int compare(Skier theA, Skier theB) {
		if(theA.centroid().y > theB.centroid().y) {
			return 1;
		}

		if(theA.centroid().y < theB.centroid().y) {
			return -1;
		}

		return 0;
	}
}
