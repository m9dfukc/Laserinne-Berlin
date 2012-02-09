package com.laserinne.wave;

import processing.core.PApplet;

import com.laserinne.base.LaserinneSketch;
import com.laserinne.base.Skier;

public class SketchWave extends LaserinneSketch{

	SpringyString _myString;
	
	@Override
	protected void postSetup() {
		_myString = new SpringyString(100);
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawWithLaser() {
		_myString.draw(g);
		
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
