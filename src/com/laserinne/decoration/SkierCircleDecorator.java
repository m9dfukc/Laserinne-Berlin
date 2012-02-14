package com.laserinne.decoration;

import laserschein.Laser3D;

import com.laserinne.base.Skier;
import com.laserinne.util.Shapes;

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
	public void draw(PGraphics theG, Laser3D theLaser) {
		final PVector myBase = _mySkier.base();
		
		float myRadius = this.progress() * _myRadius;
		
		Shapes.circle(theG, myBase.x, myBase.y, myRadius, 8);
	}

	
	@Override
	public void update() {
		super.update();		
	}

}
