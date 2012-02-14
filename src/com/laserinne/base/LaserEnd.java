package com.laserinne.base;

import processing.core.PApplet;
import laserschein.Laser3D;


public class LaserEnd extends LaserinneSketch {

	@Override
	protected void postSetup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void update(float theDelta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawWithLaser(Laser3D theLaser) {
	//float x = sin(millis() * 0.001f);
	//float y = sin(millis() * 0.0010111123232434234234f);

		//line(x, -1, x, 1);
		//line(-1, y, 1, y);

		scale(1.6f);
		float np = millis() * 0.0002f;
		
		beginShape();
		float myX = noise(np + 234) * 2 - 1;
		float myY = noise(np + 2123) * 2 - 1;
vertex(myX, myY);
		
		 myX = noise(np +75 ) * 2 - 1;
		 myY = noise(np + 85435) * 2 - 1;
		 vertex(myX, myY);

		
		 myX = noise(np + 234) * 2 - 1;
		 myY = noise(np +79687 ) * 2 - 1;
		 vertex(myX, myY);

		
		
		
		endShape(PApplet.CLOSE);
		
	}

	@Override
	protected void drawOnScreen() {

		
	}

	@Override
	protected void onNewSkier(Skier theSkier) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	public static void main(String[] args) {
		PApplet.main(new String[]{ LaserEnd.class.getCanonicalName() });
	}

	@Override
	protected void onDeadSkier(Skier theSkier) {
		// TODO Auto-generated method stub
		
	}

	
}
