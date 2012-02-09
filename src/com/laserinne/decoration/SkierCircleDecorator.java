package com.laserinne.decoration;

import com.laserinne.util.Skier;

import processing.core.PGraphics;
import processing.core.PVector;

public class SkierCircleDecorator extends SimpleDecoratorBase {

	private Skier _mySkier;
	private float _myRadius;

	
	public SkierCircleDecorator(Skier theSkier, float theRadius) {
		super(theSkier);
		
		_myRadius = theRadius;
		_mySkier = theSkier;		
	}


	@Override
	public void draw(PGraphics theG) {
		final PVector myBase = _mySkier.base();
		
		float myRadius = this.progress() * _myRadius;
		
		theG.ellipseMode(PGraphics.CENTER);
		theG.ellipse(myBase.x, myBase.y, myRadius, myRadius);	
	}

	
	@Override
	public void update() {
		super.update();		
	}

}
