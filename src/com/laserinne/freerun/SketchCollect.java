package com.laserinne.freerun;


import laserschein.Logger;
import laserschein.Logger.LogLevel;
import processing.core.PApplet;

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
    	Logger.set(LogLevel.INFO, false);

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
			mySkier.drawDebug(g);
		}
	}


	@Override
	protected void onNewSkier(Skier theSkier) {
		println("I has new skier " + theSkier.id()); // TODO: remove debug code
	}


	@Override
	protected void onDeadSkier(Skier theSkier) {
		println("I has dead skier " + theSkier.id()); // TODO: remove debug code 
		
	}

}
