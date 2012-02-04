package com.laserinne.connecting;

import processing.core.PGraphics;
import processing.core.PVector;

import com.laserinne.util.Geometry;
import com.laserinne.util.Skier;

import de.looksgood.ani.Ani;

public class AnimatedSkierEdge {

	private final Skier _mySkierA;
	private final Skier _mySkierB;
	
	private State _myState;
	
	private float _myProgress = 0;
	
	private static float DURATION = 0.7f;
	private static float SPARE_BORDER = 0.05f;
	
	
	private Ani _myAnimation;
	
	public enum State {
		CONNECTING, CONNECTED, DISCONNECTING, DISCONNECTED;
	}
	
	
	public AnimatedSkierEdge(Skier theSkierA, Skier theSkierB) {
		_mySkierA = theSkierA;
		_mySkierB = theSkierB;
		_myState = State.CONNECTING;
		
		_myAnimation = new Ani(this, DURATION, "_myProgress", 1, Ani.QUAD_IN_OUT);
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
		if(_myState != State.DISCONNECTED && _myState != State.DISCONNECTING) {
			_myState = State.DISCONNECTING;
			_myAnimation.setBegin(_myProgress);
			_myAnimation.setEnd(0);
			_myAnimation.start();
		}
	}
	
	
	public void activate() {
		if(_myState != State.CONNECTED && _myState != State.CONNECTING) {
			_myState = State.CONNECTING;
			_myAnimation.setBegin(_myProgress);
			_myAnimation.setEnd(1);
			_myAnimation.start();

			

		}
		
	}


	public boolean isDead() {
		return _myState == State.DISCONNECTED;
	}
	
	
	public void draw(PGraphics theG) {
		final PVector myA = _mySkierA.base();
		final PVector myB = _mySkierB.base();
		
		
		final PVector myInbetween = Geometry.lerp(myB, myA, _myProgress);
		
		theG.line(myB.x, myB.y, myInbetween.x, myInbetween.y);
		
	}
	
	
	public void update() {
		if(_myAnimation.isEnded()) {
			if(_myState == State.CONNECTING) {
				_myState = State.CONNECTED;
			} else if(_myState == State.DISCONNECTING) {
				_myState = State.DISCONNECTED;
			}
		}

	}
}
