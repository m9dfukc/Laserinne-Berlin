package com.laserinne.wave;

import laserschein.Laser3D;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import com.laserinne.base.Skier;
import com.laserinne.decoration.SimpleDecoratorBase;
import com.laserinne.util.Shapes;

import de.looksgood.ani.Ani;

public class WavePersonDecorator extends SimpleDecoratorBase{

	private boolean _myCollides = false;
	private float _myRadius;
	private Skier _mySkier;
	
	private float _myBoomProgress = 0;
	private float _myPulse = 0.9f;

	
	private Ani _myBoomAnimation;
	
	public WavePersonDecorator(Skier theSkier) {
		super(theSkier);
		
		_mySkier = theSkier;
		_myRadius = _mySkier.radius();
		
		_myBoomAnimation = new Ani(this, 2f, "_myBoomProgress", 1);
		_myBoomAnimation.setPlayMode(Ani.YOYO);
		_myBoomAnimation.repeat(2);
		_myBoomAnimation.pause();

	}

	@Override
	public void draw(PGraphics theG, Laser3D theLaser) {
		
		
		_myRadius = (_myRadius * 0.9f) + (_mySkier.radius() * 0.1f);
		
		final PVector myBase = _mySkier.base();
		
		float myRadius = this.progress() * _myRadius * _myPulse + (_myBoomProgress * _myRadius) ;
		
		float myAngle = (1 - _myBoomProgress * 0.8f) * (float)Math.PI * 2;
		
		
		theG.pushMatrix();
		theG.translate(myBase.x, myBase.y);
		theG.rotate(-(float) ((Math.PI * 2) - myAngle) * 0.5f);

		Shapes.arc(theG, 0, 0, myRadius, myAngle, 10);
		theG.popMatrix();
	}
	
	
	@Override
	public void update() {
		
		double myPulse = Math.sin(System.currentTimeMillis() * 0.001 + _mySkier.id());
		_myPulse = PApplet.map((float)myPulse, -1, 1, 0.8f, 1.4f);
		
		if(_myCollides && !_myBoomAnimation.isPlaying()) {
			_myBoomAnimation.setBegin(0);
			_myBoomAnimation.setEnd(1);
			_myBoomAnimation.start();
			_myBoomAnimation.repeat(2);
		}
		
		super.update();

	}

	public void collides(boolean theB) {
		_myCollides = theB;
		
	}
	
	
	public boolean collides() {
		return _myCollides;
	}

}
