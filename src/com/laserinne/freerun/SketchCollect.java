package com.laserinne.freerun;

import java.util.ArrayList;

import laserschein.Logger;
import laserschein.Logger.LogLevel;
import processing.core.PApplet;
import processing.core.PVector;

import com.laserinne.util.LaserinneSketch;
import com.laserinne.util.Skier;

@SuppressWarnings("serial")
public class SketchCollect extends LaserinneSketch {

    public static void main(String args[]) {
        PApplet.main(new String[] { com.laserinne.freerun.SketchCollect.class.getName() });
   }
	

	@Override
	protected void postSetup() {
		//Logger.setAll(true);
    	Logger.set(LogLevel.DEBUG, false);
	}
		

	@Override
	protected void update() {
	
	}

	
	@Override
	protected void drawOnScreen() {
		rect(0, 0, width * 0.5f, height * 0.5f);
	}
	
	
	@Override
	protected void drawWithLaser() {
		
		for(final Skier mySkier:tracking().skiers()) {
			
			stroke(255);
			ellipse(mySkier.position().x, mySkier.position().y, 0.1f, 0.1f);
		}
	}

}
