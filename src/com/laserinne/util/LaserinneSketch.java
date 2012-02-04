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

package com.laserinne.util;

import laserschein.Laserschein;
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
	private float _lastMillis;

	/**
	 * Sketch width and height.
	 */
	protected static final int WIDTH = 600;
	protected static final int HEIGHT = 600;

	/**
	 * Laser color: ARGB(FF, 00, FF, 00) means full intensity green.
	 */
	public static final int LASER_COLOR = 0xFFFF0000;
	/**
	 * Screen color: ARGB(FF, 00, 00, FF) means full intensity blue.
	 */
	public static final int SCREEN_COLOR = 0xFF0000FF;

	/**
	 * Laser scan speed used when displaying text.
	 */
	public static final int TEXT_SCANSPEED = 60000;
	
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

	
	@Override
	public void setup() {
		size(WIDTH, HEIGHT, OPENGL);
		frameRate(-1); // Use maximum frame rate.
		_lastMillis = millis() / 1000f;
		
		_myLaser = new Laserschein(this, Laserschein.EASYLASEUSB2);               

		smooth();
		colorMode(RGB);
		stroke(LaserinneSketch.SCREEN_COLOR);
		noFill();

		_myTracking = new Tracking("239.0.0.1", 9999);

		postSetup(); // Hook!

	}


	
	@Override
	public void draw() {
		_myTracking.update();
		
		mX = map(mouseX, 0, width, -1.f, 1.f);
		mY = map(mouseY, 0, height, -1.f, 1.f);
		delta = millis()/1000f - _lastMillis;
		_lastMillis = millis()/1000f;
		
		fireEvents();
		
		update(); // Hook!

		background(0);
		
		fill(255);
		stroke(255);
		textSize(12);
		text(frameRate, 100, 100);
		noFill();
		
		
		/* Init [-1, 1] coordinate system */
		translate(width * 0.5f, height * 0.5f);
		scale(width * 0.5f);
		strokeWeight(1/(float)width*2);
		stroke(50);
		line(-1, 0, 1, 0);
		line(0, 0.5f, 0, -1);
		

		pushMatrix();
		drawOnScreen();	// Hook!
	
		
		popMatrix();

		beginRaw(_myLaser.renderer());
		drawWithLaser(); // Hook!
		endRaw();       
	}


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
	protected abstract void update();


	/**
	 * Use this method to draw stuff with laser.
	 */
	protected abstract void drawWithLaser();


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
	

	public Tracking tracking() {
		return _myTracking;
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
	}
}
