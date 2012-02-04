package com.laserinne.connecting;

import processing.core.PGraphics;
import processing.core.PVector;

import com.laserinne.util.Skier;

public class AnimatedSkierEdge {

	private final Skier _mySkierA;
	private final Skier _mySkierB;
	
	private State _myState;
	
	private float _myProgress = 0;
	
	public enum State {
		CONNECTING, CONNECTED, DISCONNECTING, DISCONNECTED;
	}
	
	
	public AnimatedSkierEdge(Skier theSkierA, Skier theSkierB) {
		_mySkierA = theSkierA;
		_mySkierB = theSkierB;
		_myState = State.CONNECTING;
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
		if(_myState != State.DISCONNECTED) {
			_myState = State.DISCONNECTING;
		}
	}
	
	
	public void activate() {
		if(_myState != State.CONNECTED) {
			_myState = State.CONNECTING;
		}
		
	}


	public boolean isDead() {
		return _myState == State.DISCONNECTED;
	}
	
	
	public void draw(PGraphics theG) {
		final PVector myA = _mySkierA.base();
		final PVector myB = _mySkierB.base();
		
		theG.line(myA.x, myA.y, myB.x, myB.y);
		
	}
	
	
	public void update() {
		
	}
}
