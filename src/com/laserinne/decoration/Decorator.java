package com.laserinne.decoration;

import processing.core.PGraphics;

public abstract class Decorator {

	public enum State {
		GENESIS, LIVE, APOCALYPSE, FINISHED;
	}
	
	private State _myState = State.GENESIS;
	
	public State state() {
		return _myState;
	}
	
	public void state(State theState) {
		_myState = theState;
	}
	
	public boolean isFinished() {
		return _myState == State.FINISHED;
	}
	
	public abstract void draw(PGraphics theG);

	public abstract void update();

}
