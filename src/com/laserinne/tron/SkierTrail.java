package com.laserinne.tron;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import processing.core.PGraphics;
import processing.core.PVector;

import com.laserinne.base.Skier;
import com.laserinne.decoration.Decoratable;
import com.laserinne.util.Geometry;

public class SkierTrail implements Decoratable {
	private Skier _mySkier;
	private LinkedList<PVector> _mySegments;
	private boolean _myHasCollision = false;
	
	public static float CREATE_THRESHOLD = 0.01f;
	public static float MAX_NUMBER = 50;
	
	public SkierTrail(Skier theSkier ){
		_mySkier = theSkier;
		_mySegments = new LinkedList<PVector>();
		
		_mySegments.addFirst(_mySkier.base().get());
	}
	
	
	public List<PVector> segments() {
		return _mySegments;
	}
	
	
	public void update() {
		//PVector myHead = _mySegments.getFirst();
		//float myThresholdSq = CREATE_THRESHOLD * CREATE_THRESHOLD;
		
		if(	nextSegmentProgress() > 1) {
			_mySegments.addFirst(_mySkier.base().get());     // copy, of course
		}

		
		Iterator<PVector> myI = _mySegments.iterator();
		
		int myCount = 0;
		while(myI.hasNext()) {
			myI.next();
			myCount++;
			
			if(myCount > MAX_NUMBER) {
				myI.remove();
			}
		}
		
		
	}
	
	
	public float nextSegmentProgress() {
		PVector myHead = _mySegments.getFirst();
	
		float myDistance = myHead.dist(_mySkier.base());
		return myDistance / CREATE_THRESHOLD;
		
	}
	
	
	public Skier skier() {
		return _mySkier;
	}
	
	
	public void drawDebug(PGraphics theG) {
		theG.beginShape();
		
		for(PVector myPoint:_mySegments) {
			theG.vertex(myPoint.x, myPoint.y);
		}
		
		theG.endShape(PGraphics.OPEN);
	}


	@Override
	public boolean isDead() {
		return _mySkier.isDead();
	}


	public boolean collides() {
		return _myHasCollision;
	}
	
	
	public void collides(boolean theB) {
		_myHasCollision = theB;
	}


	boolean collidesWith(Skier theSkier) {
		ArrayList<PVector> mySegments = new ArrayList<PVector>();
		mySegments.addAll(_mySegments);
		
		int STEP = 3;
		for(int i = 0; i < mySegments.size() - STEP; i += STEP) {
			PVector myPoint = mySegments.get(i + STEP);

			PVector myNext = mySegments.get(i + STEP);
			
			
			float myDistance = Geometry.distanceSegmentPoint(myPoint, myNext, theSkier.base());
			
			if(myDistance <= theSkier.radius() ) {
				return true;
			} 
		}
		
	

		return false; 
	}


	public PVector tail() {
		return _mySegments.getLast();
	}


	
}
