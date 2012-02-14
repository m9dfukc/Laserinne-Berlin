package com.laserinne.connecting;

import laserschein.Laser3D;
import processing.core.PGraphics;
import processing.core.PVector;

import com.laserinne.base.Skier;
import com.laserinne.decoration.Decorator;
import com.laserinne.util.Geometry;

import de.looksgood.ani.Ani;

public class AnimatedSkierEdge extends Decorator {

	private final Skier _mySkierA;
	private final Skier _mySkierB;
	
	private float _myProgress = 0;
	
	private static float DURATION_IN = 0.8f;
	private static float DURATION_OUT = 0.3f;

	
	private static float SPARE_BORDER = 0.05f;
		
	private Ani _myAnimation;
	
	
	public AnimatedSkierEdge(Skier theSkierA, Skier theSkierB) {
		_mySkierA = theSkierA;
		_mySkierB = theSkierB;
		this.state(State.GENESIS);
		
		_myAnimation = new Ani(this, DURATION_IN, "_myProgress", 1, Ani.EXPO_IN_OUT);
		_myAnimation.start();
	}
	
	
	public Skier skierA() {
		return _mySkierA;
	}
	
	
	public Skier skierB() {
		return _mySkierB;
	}
	
	
	@Override
	public boolean equals(Object theOther) {
		if(theOther instanceof AnimatedSkierEdge) {
			final AnimatedSkierEdge myOther = (AnimatedSkierEdge)theOther;
			
			return 	(myOther.skierA() == skierA() && myOther.skierB() == skierB()) ||
					(myOther.skierB() == skierA() && myOther.skierA() == skierB());
		}
		
		return false;
	}


	public void deactivate() {
		if(this.state() != State.FINISHED && this.state() != State.APOCALYPSE) {
			this.state(State.APOCALYPSE);
			_myAnimation.setBegin(_myProgress);
			_myAnimation.setEnd(0);
			_myAnimation.setDuration(DURATION_OUT);
			_myAnimation.start();
		}
	}
	
	
	public void activate() {
		if(this.state() != State.LIVE && this.state() != State.GENESIS) {
			this.state(State.GENESIS);
			_myAnimation.setBegin(_myProgress);
			_myAnimation.setEnd(1);
			_myAnimation.setDuration(DURATION_IN);
			_myAnimation.start();
		}		
	}

	
	public void draw(PGraphics theG, Laser3D theLaser) {
		
		final PVector myA = _mySkierA.base();
		final PVector myB = _mySkierB.base();
		
		final PVector mySpare = PVector.sub(myB, myA);
		mySpare.scaleTo(SPARE_BORDER);
		
		myA.add(mySpare);
		myB.sub(mySpare);
		
		final PVector myInbetween = Geometry.lerp(myA, myB, _myProgress);
		
		theG.line(myA.x, myA.y, myInbetween.x, myInbetween.y);
	}
	
	
	public void update() {
		if(_myAnimation.isEnded()) {
			if(this.state() == State.GENESIS) {
				this.state(State.LIVE);
			} else if(this.state() ==  State.APOCALYPSE) {
				this.state(State.FINISHED);
			}
		}

	}


}
