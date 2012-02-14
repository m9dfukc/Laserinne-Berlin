package com.laserinne.util;

import processing.core.PGraphics;

public class Shapes {

	/**
	 * Draws a circle
	 * 
	 * @param theG The PGraphics context
	 * @param theX 
	 * @param theY
	 * @param theRadius
	 * @param theSegments how many segments
	 */
	public static void circle(PGraphics theG, float theX, float theY, float theRadius, int theSegments) {
		theG.beginShape();
		
		for(int i = 0; i < theSegments; i++) {
			double myRad = (i / (double) theSegments) * Math.PI * 2.0;
			float myX = (float) (Math.sin(myRad) * theRadius + theX);
			float myY = (float) (Math.cos(myRad) * theRadius + theY);
			theG.vertex(myX, myY);
		}
		
		theG.endShape(PGraphics.CLOSE);
	}
	
	
	
	
	/**
	 * Draws a circle
	 * 
	 * @param theG The PGraphics context
	 * @param theX 
	 * @param theY
	 * @param theRadius
	 * @param theSegments how many segments
	 */
	public static void ellipse(PGraphics theG, float theX, float theY, float theRadiusX, float theRadiusY, int theSegments) {
		theG.beginShape();
		
		for(int i = 0; i < theSegments; i++) {
			double myRad = (i / (double) theSegments) * Math.PI * 2.0;
			float myX = (float) (Math.sin(myRad) * theRadiusX + theX);
			float myY = (float) (Math.cos(myRad) * theRadiusY + theY);
			theG.vertex(myX, myY);
		}
		
		theG.endShape(PGraphics.CLOSE);
	}
	
	
	
	/**
	 * Draws a circle
	 * 
	 * @param theG The PGraphics context
	 * @param theX 
	 * @param theY
	 * @param theRadius
	 * @param theAngle Angle in radians
	 * @param theSegments how many segments
	 */
	public static void arc(PGraphics theG, float theX, float theY, float theRadius, float theAngle, int theSegments) {
		theG.beginShape();
		
		boolean myBeforeWasGood = true;
		for(int i = 0; i < theSegments; i++) {
			double myRad = (i / (double)(theSegments -1)) * Math.PI * 2.0;
			
			if(myBeforeWasGood && myRad >= theAngle) {
				myRad = theAngle;
			}
				
			if(myRad < theAngle || myBeforeWasGood) {
			
				float myX = (float) (Math.sin(myRad) * theRadius + theX);
				float myY = (float) (Math.cos(myRad) * theRadius + theY);
				theG.vertex(myX, myY);
			} else {
				myBeforeWasGood = false;
			}
			
		}
		
		theG.endShape();
	}




	public static void circleFuzzed(PGraphics theG, float theX, float theY, float theRadius, float myDistort, int theSegments) {
		theG.beginShape();
		
		for(int i = 0; i < theSegments; i++) {
			double myRad = (i / (double) theSegments) * Math.PI * 2.0;
			
			float myRadius = theRadius + (float) (theRadius * (myDistort * (Math.random() - 0.5)));
			
			float myX = (float) (Math.sin(myRad) * myRadius + theX);
			float myY = (float) (Math.cos(myRad) * myRadius + theY);
			theG.vertex(myX, myY);
		}
		
		theG.endShape(PGraphics.CLOSE);		
	}
}
