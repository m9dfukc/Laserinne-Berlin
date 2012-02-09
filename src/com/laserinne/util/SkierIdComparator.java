package com.laserinne.util;

import java.util.Comparator;

import com.laserinne.base.Skier;

public class SkierIdComparator implements Comparator<Skier> {

	@Override
	public int compare(Skier theA, Skier theB) {
		if(theA.id() > theB.id()) {
			return 1;
		}

		if(theA.id() < theB.id()) {
			return -1;
		}

		return 0;
	}

}
