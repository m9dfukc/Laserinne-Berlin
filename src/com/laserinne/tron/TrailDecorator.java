package com.laserinne.tron;

import java.util.List;

import laserschein.Laser3D;

import processing.core.PGraphics;
import processing.core.PVector;

import com.laserinne.decoration.Decorator;

import de.looksgood.ani.Ani;

public class TrailDecorator extends Decorator{

	final SkierTrail _myTrail;

	private Ani _myConstructAnimation;
	private Ani _myBoomAnimation;
	
	
	private float _myBoomProgress = 0;

	
	private float _myProgress = 0;

	public TrailDecorator(SkierTrail theTrail) {
		this.state(State.GENESIS);
		_myTrail = theTrail;
		_myConstructAnimation = new Ani(this, 1.5f, "_myProgress", 1);
		_myBoomAnimation = new Ani(this, 1.5f, "_myBoomProgress", (float)Math.PI);
		_myBoomAnimation.pause();

	}

	@Override
	public void draw(PGraphics theG, Laser3D theLaser) {

		theLaser.smooth();
		theG.beginShape();
		
		
		float myDistort = (float) Math.sin(_myBoomProgress);
		float myDistortAmount = 0.3f;
		
		List<PVector> mySegments = _myTrail.segments();
		
		int myMax = Math.round(mySegments.size() * _myProgress);
		
		for(int i = 0; i < myMax; i++) {
			PVector mySegment = mySegments.get(i);
			
			float myRandom = (float) (((Math.random() - 0.5) * myDistort) * myDistortAmount);
			
			theG.vertex(mySegment.x + myRandom, mySegment.y);
		}
	

		theG.endShape(PGraphics.OPEN);

		theLaser.noSmooth();

	}



	@Override
	public void update() {
		if(_myTrail.isDead() && (!state().equals(State.FINISHED) )){

			if(!state().equals(State.APOCALYPSE)) {
				_myConstructAnimation.setBegin(_myProgress);
				_myConstructAnimation.setEnd(0);
				_myConstructAnimation.start();
				this.state(State.APOCALYPSE);

			}


		}


		if(state().equals(State.APOCALYPSE) && _myConstructAnimation.isEnded()) {
			state(State.FINISHED);
		}


		if(_myTrail.collides() && !_myBoomAnimation.isPlaying()) {
			_myBoomAnimation.setBegin(0);
			_myBoomAnimation.setEnd((float)Math.PI);
			_myBoomAnimation.start();
		}
	}

}
