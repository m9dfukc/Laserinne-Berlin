package com.laserinne.freerun;

import com.laserinne.base.Skier;
import com.laserinne.util.Geometry;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class CollectableItem {
	private PVector _myPosition;
	private float _myRadius;
	
	public enum CollectableItemState {
		ACTIVE, INACTIVE, COLLECTED, DYING
	}
	
	
	private CollectableItemState _myState; 
	
	public CollectableItem(final PVector thePosition, float theRadius) {
		_myPosition.set(thePosition);
		_myRadius = theRadius;
		
		_myState = CollectableItemState.ACTIVE;
	}
	
	
	public void draw(final PGraphics theG) {
		theG.ellipseMode(PConstants.CENTER);
		theG.ellipse(_myPosition.x, _myPosition.y, _myRadius * 2, _myRadius * 2);
	}
	
	
	public boolean canBeCollected() {
		if(_myState == CollectableItemState.ACTIVE) {
			return true;
		}
		
		return false;
	}
	
	
	public boolean collidesWith(Skier theSkier) {
		final float myDistanceSquared = Geometry.distanceSquared(theSkier.centroid(), _myPosition);
		
		return myDistanceSquared < _myRadius * _myRadius;
	}
	
	

	
}
