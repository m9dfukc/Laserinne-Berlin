package com.laserinne.decoration;

import laserschein.Laser3D;
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
		return _myState.equals(State.FINISHED);
	}
	
	public abstract void draw(PGraphics theG, Laser3D theLaser);

	public abstract void update();

}
