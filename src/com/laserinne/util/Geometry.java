package com.laserinne.util;

import processing.core.PVector;

public class Geometry {
	
	/**
	 * Calculates the distance of a point to a given line segment in 2D
	 * 
	 * @param theA the first point of the line segment
	 * @param theB the second point of the line segment
	 * @param thePoint the point to check against.
	 * @return
	 */
	float distanceSegmentPoint(PVector theA, PVector theB, PVector thePoint) {

		final float myDeltaX = theB.x - theA.x;
		final float myDeltaY = theB.y - theA.y;

		if ((myDeltaX == 0) && (myDeltaY == 0)) {  // Line is just a point
			return theA.dist(thePoint);
		}

		final float myU = ((thePoint.x - theA.x) * myDeltaX + (thePoint.y - theA.y) * myDeltaY) / (myDeltaX * myDeltaX + myDeltaY * myDeltaY);

		final PVector myClosestPoint;
		if (myU < 0) {
			myClosestPoint = theA;
		} 
		else if (myU > 1) {
			myClosestPoint = theB;
		} 
		else {
			myClosestPoint = new PVector(theA.x + myU * myDeltaX, theA.y + myU * myDeltaY);
		}

		return myClosestPoint.dist(thePoint);
	}

}
