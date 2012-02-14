package com.laserinne.collecting;

import laserschein.Laser3D;
import processing.core.PGraphics;
import processing.core.PVector;

import com.laserinne.base.Skier;
import com.laserinne.decoration.Decoratable;
import com.laserinne.decoration.Decorator;

import de.looksgood.ani.Ani;

public class SkierVDecorator extends Decorator{
	
	private float _myProgress = 0;
	private Ani _myAni;
	
	private final Decoratable _myDecoratable;
	private Skier _mySkier;
	
	private float SIZE = 0.04f;
	private float OFFSET = 0.035f;

	
	
	public SkierVDecorator(Skier theSkier) {
		this.state(State.GENESIS);
		_myDecoratable = theSkier;
		_mySkier = theSkier;
		_myAni = new Ani(this, 1, "_myProgress", 1);
		_myAni.start();
	}
	
	
	public float progress() {
		return _myProgress;
	}
		
	
	@Override
	public void update() {
		if(_myDecoratable.isDead() && this.state() != State.APOCALYPSE) {
			this.state(State.APOCALYPSE);
			_myAni.setBegin(_myProgress);
			_myAni.setEnd(0);
			_myAni.start();
		}
				
		if(_myAni.isEnded() && this.state() == State.APOCALYPSE) {
			this.state(State.FINISHED);
		}
	}


	@Override
	public void draw(PGraphics theG, Laser3D theLaser) {
		
		theG.stroke(255);
		PVector myBase = _mySkier.base();
		
		float myX = _myProgress * SIZE;
		
		float myY = _myProgress * -SIZE;
		
		theLaser.noSmooth();
		theG.pushMatrix();
		theG.translate(myBase.x, myBase.y + OFFSET);
		theG.beginShape();
		theG.vertex(myX, myY);
		theG.vertex(0, 0);
		theG.vertex(-myX, myY);

		theG.endShape(PGraphics.OPEN);
		theG.popMatrix();

	}

}
