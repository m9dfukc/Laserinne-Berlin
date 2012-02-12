package com.laserinne.wave;

import laserschein.Laser3D;
import laserschein.Logger;
import processing.core.PApplet;

import com.laserinne.base.LaserinneSketch;
import com.laserinne.base.Skier;

@SuppressWarnings("serial")
public class SketchWave extends LaserinneSketch{

	SpringyString _myString;
	
	@Override
	protected void postSetup() {
		_myString = new SpringyString(20);
		Logger.setAll(true);
	}

	@Override
	protected void update(float theDelta) {
		_myString.update(theDelta);		
	}

	@Override
	protected void drawWithLaser(final Laser3D theLaser) {
		theLaser.smooth();
		_myString.draw(g);
		theLaser.noSmooth();
		
		
		rect(0,0, 2, 1);
	}

	@Override
	protected void drawOnScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onNewSkier(Skier theSkier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDeadSkier(Skier theSkier) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void main(String[] args) {
		PApplet.main(new String[]{SketchWave.class.getCanonicalName()});
	}

}
