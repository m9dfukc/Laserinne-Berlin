package com.laserinne.wave;

import java.util.ArrayList;
import java.util.HashMap;

import laserschein.Laser3D;
import laserschein.Logger;
import processing.core.PApplet;

import com.laserinne.base.LaserinneSketch;
import com.laserinne.base.Skier;
import com.laserinne.decoration.DecoratorManager;

@SuppressWarnings("serial")
public class SketchWave extends LaserinneSketch{

	SpringyString _myString;
	private DecoratorManager _myDecoratorManager;
	private HashMap<Skier, WavePersonDecorator> _myPeopleDecorators;
	
	@Override
	protected void postSetup() {
		_myString = new SpringyString(30);
		_myDecoratorManager = new DecoratorManager();
		_myPeopleDecorators = new HashMap<Skier, WavePersonDecorator>();
		//Logger.setAll(true);
	}

	@Override
	protected void update(float theDelta) {
		_myString.update(theDelta);
		_myDecoratorManager.update();
		
		ArrayList<Skier> mySkiers = tracking().skiersConfident();
		
		for(final Skier mySkier:mySkiers) {
			WavePersonDecorator myDecorator = _myPeopleDecorators.get(mySkier);
			
			if(_myString.collidesWith(mySkier)) {
				myDecorator.collides(true);
				Logger.printInfo("Collision");
			} else {
				myDecorator.collides(false);
			}
		}
		
	}

	@Override
	protected void drawWithLaser(final Laser3D theLaser) {
		theLaser.smooth();
		_myString.draw(g);
		theLaser.noSmooth();	
		
		_myDecoratorManager.draw(g);
	}

	@Override
	protected void drawOnScreen() {
		for(final Skier mySkier:tracking().skiers()) {
			mySkier.drawDebug(g);
		}
		
	}

	@Override
	protected void onNewSkier(Skier theSkier) {
		WavePersonDecorator myDecorator = new WavePersonDecorator(theSkier);
		_myPeopleDecorators.put(theSkier, myDecorator);
		_myDecoratorManager.add(myDecorator);
		
	}

	@Override
	protected void onDeadSkier(Skier theSkier) {
		_myPeopleDecorators.remove(theSkier);
	}
	
	
	public static void main(String[] args) {
		PApplet.main(new String[]{SketchWave.class.getCanonicalName()});
	}

}
