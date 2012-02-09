package com.laserinne.tron;

import processing.core.PGraphics;

import com.laserinne.decoration.Decorator;

public class TrailDecorator extends Decorator{

	final SkierTrail _myTrail;

	
	
	public TrailDecorator(SkierTrail theTrail) {
		this.state(State.GENESIS);
		_myTrail = theTrail;
	}

	@Override
	public void draw(PGraphics theG) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		if(_myTrail.isDead()){
			this.state(State.FINISHED);
		}
	}
	
}
