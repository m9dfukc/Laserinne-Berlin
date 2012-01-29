package com.laserinne.util;

import processing.core.PApplet;
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
	public static float distanceSegmentPoint(PVector theA, PVector theB, PVector thePoint) {

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
	
	
	/**
	 * Calculates the squared distance of two points in 2D
	 * @param theA
	 * @param theB
	 * @return
	 */
	public static float distanceSquared(final PVector theA, final PVector theB) {
		final float theDeltaX = (theA.x - theB.x);
		final float theDeltaY = (theA.y - theB.y);

		return theDeltaX * theDeltaX + theDeltaY * theDeltaY;
	}
	
	/**
	 * Calculates something ;-)
	 * @param time
	 * @param distance
	 * @param angle
	 * @return 
	 */
	public static float transform(float time, float distance, float angle) {
		float newDistance = distance * Geometry.smoothstep(0f, 1f, time);
		return newDistance;
	}
	
	/**
	 * Returns a smooth transition between 0.0 and 1.0 
     * using hermite interpolation (cubic spline),
     * where x is a number between a and b. 
     * The return value will ease (slow down) as x nears a or b.
     * For x smaller than a, returns 0.0. For x bigger than b, returns 1.0.
	 * @param a
	 * @param b
	 * @param x
	 * @return 
	 */
	public static float smoothstep(float a, float b, float x) {
		if(x <  a) return 0f;
		if(x >= b) return 1f;
		float _x = (x-a) / (b-a);
		return _x*_x * (3f-2f*_x);
	}
	
	/**
	 * Calculates the a outline point on a given vector
	 * @param x0
	 * @param y0
	 * @param distance
	 * @param angle
	 * @return 
	 */
	public static PVector coordinates(float x0, float y0, float distance, float angle) {
	    float x1 = x0 + PApplet.cos(PApplet.radians(angle)) * distance;
	    float y1 = y0 + PApplet.sin(PApplet.radians(angle)) * distance;
	    return new PVector(x1, y1); 
	}
	
	/**
	 * Calculates the angle between two vectors
	 * @param x0
	 * @param y0
	 * @param distance
	 * @param angle
	 * @return 
	 */
	public static float angle(PVector p1, PVector p2) {
		float a = PApplet.degrees( PApplet.atan2(p2.y-p1.y, p2.x-p1.x) );
		return a;
	}
	


}
