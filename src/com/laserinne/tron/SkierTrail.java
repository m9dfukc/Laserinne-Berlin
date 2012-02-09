package com.laserinne.tron;

import java.util.LinkedList;

import laserschein.Logger;

import processing.core.PGraphics;
import processing.core.PVector;

import com.laserinne.base.Skier;
import com.laserinne.decoration.Decoratable;
import com.laserinne.util.Geometry;

public class SkierTrail implements Decoratable {
	private Skier _mySkier;
	private LinkedList<PVector> _mySegments;
	
	public static float CREATE_THRESHOLD = 0.09f;
	
	public SkierTrail(Skier theSkier ){
		_mySkier = theSkier;
		_mySegments = new LinkedList<PVector>();
		
		_mySegments.addFirst(_mySkier.centroid().get());
	}
	
	
	public void update() {
		PVector myHead = _mySegments.getFirst();
		float myThresholdSq = CREATE_THRESHOLD * CREATE_THRESHOLD;
		
		if(Geometry.distanceSquared(myHead, _mySkier.centroid()) > myThresholdSq) {
			_mySegments.addFirst(_mySkier.centroid().get());     // copy, of course
			Logger.printDebug("Adding segment for Skier " + _mySkier.id());
			
		}
		
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
}
