package com.laserinne.collecting;

import laserschein.Logger;

import com.laserinne.base.Skier;
import com.laserinne.decoration.Decorator;
import com.laserinne.util.Geometry;
import com.laserinne.util.Shapes;

import de.looksgood.ani.Ani;

import processing.core.PGraphics;
import processing.core.PVector;

public class CollectableItem extends Decorator {
	private PVector _myPosition;
	private float _myRadius;

	private long _myCreatedMillis;
	private boolean _myIsCollected = false;

	private float _myProgress = 0;

	private Ani _myProgressAnimation;

	public CollectableItem(final PVector thePosition, float theRadius) {
		_myPosition = thePosition.get();
		_myRadius = theRadius;

		_myCreatedMillis = System.currentTimeMillis();
		
		_myProgressAnimation = new Ani(this, 0.8f, "_myProgress", 1,  Ani.EXPO_IN_OUT);
		_myProgressAnimation.start();
	}


	public void draw(final PGraphics theG) {

		theG.pushMatrix();

		theG.translate(_myPosition.x, _myPosition.y);

		theG.scale(_myProgress);
		Shapes.circle(theG, 0, 0, _myRadius, 12);


		if(_myIsCollected && state() == State.APOCALYPSE) {
			//theG.rotate( (1 - _myProgress) * 3);

			theG.rectMode(PGraphics.CENTER);
			theG.rect(0, 0, _myRadius * 2, _myRadius * 2);
		}

		

		theG.popMatrix();
	}



	@Override
	public void update() {

		if(state() == State.APOCALYPSE && _myProgressAnimation.isEnded()) {
			state(State.FINISHED);
		}

	

	}


	public void collect() {
				
		_myIsCollected = true;
		
		state(State.APOCALYPSE);
		
		_myProgressAnimation.setBegin(_myProgress);
		_myProgressAnimation.setDuration(3f);
		_myProgressAnimation.setEnd(0);
		_myProgressAnimation.pause();
	}



	public boolean collidesWith(Skier theSkier) {
		final float myDistanceSquared = Geometry.distanceSquared(theSkier.base(), _myPosition);
		final float myDistance = _myRadius + theSkier.radius();

		return myDistanceSquared < myDistance * myDistance;
	}





	public void age(float theAge) {
		_myCreatedMillis = System.currentTimeMillis() - (long)(theAge * 1000.0f);
	}


	public float age() {
		return (System.currentTimeMillis() - _myCreatedMillis) / 1000.0f;
	}


	public void die() {
		state(State.APOCALYPSE);
		_myProgressAnimation.setBegin(_myProgress);
		_myProgressAnimation.setEnd(0);
		_myProgressAnimation.start();

	}


	public boolean hasBeenCollected() {
		return _myIsCollected;
	}

}
