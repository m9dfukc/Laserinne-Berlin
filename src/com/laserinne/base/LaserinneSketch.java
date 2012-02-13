/*
 *                This file is part of Laserinne.
 * 
 *  Laser projections in public space, inspiration and
 *  information, exploring the aesthetic and interactive possibilities of
 *  laser-based displays.
 * 
 *  http://www.laserinne.com/
 * 
 * Laserinne is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Laserinne is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Laserinne. If not, see <http://www.gnu.org/licenses/>.
 */

package com.laserinne.base;

import controlP5.ControlP5;
import controlP5.ControlWindow;
import de.looksgood.ani.Ani;
import laserschein.Laser3D;
import laserschein.Laserschein;
import laserschein.Logger;
import processing.core.PApplet;


/**
 * Abstract superclass for Laserinne sketches.
 * 
 * You are supposed to extend this to get some of the ready made functionality
 * for sketches made to Levi.
 * 
 * Note that this has a coordinate system which is not pixel based, but X and Y are
 * from -1 to 1
 * 
 * @author Jyrki Lilja, Benjamin Maus
 */
@SuppressWarnings("serial")
public abstract class LaserinneSketch extends PApplet {
	
	/**
	 * Holds an instance of ControlP5.
	 */
	public ControlP5 _controlP5;
	
	/**
	 * Holds an instance of seperate ControlP5 window.
	 */
	public ControlWindow _controlWindow;
	
	/**
	 * Holds an instance of Laserschein.
	 */
	private Laserschein _myLaser;

	/**
	 * Tracking gives us all skiers in the slope.
	 */
	private Tracking _myTracking;

	/**
	 * millis in last Frame. 
	 */
	private long _myLastMillis;

	/**
	 * Sketch width and height.
	 */
	protected static final int WIDTH = 700;
	protected static final int HEIGHT = 700;

	/**
	 * Scaled mouseX.
	 */
	public float mX;
	
	/**
	 * Scaled mouseY.
	 */
	public float mY;
	
	/**
	 * Time in millis/1000 since last Frame.
	 */
	public float delta;

	/**
	 * If additional stuff should be drawn on screen. Or is it just the stuff for the laser.
	 */
	public boolean drawOnScreen = true;
	
	
	public boolean doFakeTracking = false;
	FakeTracking _myFake;
	

	@Override
	public void setup() {
		size(WIDTH, HEIGHT, OPENGL);
		frameRate(-1); // Use maximum frame rate.
		
		_myLaser = new Laserschein(this, Laserschein.EASYLASEUSB2);               

		smooth();
		hint(ENABLE_OPENGL_4X_SMOOTH);
		colorMode(RGB);
		stroke(255,0,0);
		noFill();

		_myTracking = new Tracking("239.0.0.1", 9999);
		_myFake = new FakeTracking(_myTracking);
		
		_controlP5 = new ControlP5(this);
		_controlWindow = _controlP5.addControlWindow("controlP5window", 10, 10, 300, 160);
		
		Ani.init(this);
		
		/* HOOK */
		postSetup(); 
		
		_myLastMillis = millis(); // Goes last, so there is no jump
	}


	
	/* (non-Javadoc)
	 * @see processing.core.PApplet#draw()
	 * 
	 * This is the main loop. Do not override this. If you absolutely need to, call this!
	 * 
	 */
	@Override
	public void draw() {
		mX = map(mouseX, 0, width, -1.f, 1.f);
		mY = map(mouseY, 0, height, -1.f, 1.f);
		
		
		long myCurrentMillis = millis();
		delta = (myCurrentMillis - _myLastMillis) / 1000.0f;
		_myLastMillis = myCurrentMillis;
		
		
		if(doFakeTracking) {
			_myFake.update();
			_myFake.mouseUpdate(mX, mY);
		}
		
		_myTracking.update();
		
		fireEvents();
		
		
		/* HOOK */
		update(delta);
		

		background(60, 57, 55);
		
		fill(255);
		stroke(255);
		textSize(12);
		text(frameRate, 20, 30);
		text("Skiers: " + tracking().skiers().size(), 100, 30);
		
		if(!drawOnScreen) {
			text("Not drawing on screen.", 330, 30);
		}
		
		if(doFakeTracking) {
			text("Fake tracking enabled.", 330, 60);
		}
		
		noFill();
		
		
		/* Init [-1, 1] coordinate system */
		translate(width * 0.5f, height * 0.5f);
		scale(width * 0.5f);
		strokeWeight(1/(float)width*2);
		stroke(50);
		line(-1, 0, 1, 0);
		line(0, 0.5f, 0, -1);
		

		/* HOOK */
		if(drawOnScreen) {
			pushMatrix();
			drawOnScreen();	
			popMatrix();	
		}
		
		
		/* HOOK */
		final Laser3D myLaserRenderer = _myLaser.renderer();
		beginRaw(_myLaser.renderer());
		stroke(255);
		noFill();
		curveDetail(3);
		drawWithLaser(myLaserRenderer); // Hook!
		endRaw();       
	}


	/**
	 * Fires events on new and dead skiers to subclasses.
	 */
	private void fireEvents() {
		for(final Skier mySkier:_myTracking.newSkiers()) {
			onNewSkier(mySkier);
		}
		
		for(final Skier mySkier:_myTracking.deadSkiers()) {
			onDeadSkier(mySkier);
		}		
	}



	/**
	 * Use this method to initialize your own stuff.
	 */
	protected abstract void postSetup();   


	/**
	 * Use this method to update logic
	 */
	protected abstract void update(final float theDelta);


	/**
	 * Use this method to draw stuff with laser.
	 */
	protected abstract void drawWithLaser(final Laser3D theLaser);


	/**
	 * Use this method to draw stuff on screen
	 */
	protected abstract void drawOnScreen();

	
	/**
	 * This fires when a new skier is detected.
	 * 
	 * @param theSkier
	 */
	protected abstract void onNewSkier(final Skier theSkier);
	
	
	
	/**
	 * This fires when a skier dies
	 *  
	 * @param theSkier
	 */
	protected abstract void onDeadSkier(final Skier theSkier);
	

	/**
	 * Get a reference to the tracker. Get your freshly tracked skiers there.
	 * ( tracking().skiers() )
	 * @return
	 */
	public Tracking tracking() {
		return _myTracking;
	}
	
	
	public void mousePressed() {
		if(doFakeTracking) _myFake.mousePressed();
	}
	
	public void mouseReleased() {
		if(doFakeTracking) _myFake.mouseReleased();
	}
		
	public void keyPressed() {
		if (key == 's') {
			// Toggle control window
			if (_myLaser.isControlWindowVisible()) {
				_myLaser.hideControlWindow();
			} else {
				_myLaser.showControlWindow();
			}
		}
		
		if (key == 'c') {
			if (_controlP5.window("controlP5window").isVisible()) {
				_controlP5.window("controlP5window").hide();
			} else {
				_controlP5.window("controlP5window").show();
			}
		}
		
		if(key == 'f') {
			doFakeTracking = !doFakeTracking;
		}
	
		
		if(key == 'd') {
			drawOnScreen  = !drawOnScreen;
		}
	}
}
