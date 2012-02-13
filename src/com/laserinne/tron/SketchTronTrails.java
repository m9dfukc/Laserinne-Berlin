package com.laserinne.tron;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import processing.core.PApplet;

import laserschein.Laser3D;
import laserschein.Logger;

import com.laserinne.base.LaserinneSketch;
import com.laserinne.base.Skier;
import com.laserinne.decoration.DecoratorManager;
import com.laserinne.decoration.SkierCircleDecorator;


@SuppressWarnings("serial")
public class SketchTronTrails extends LaserinneSketch {
	
	private DecoratorManager _myDecoratorManager;
	private HashMap<Skier, SkierTrail> _myTrails;
	
	public static void main(String[] args) {
		PApplet.main(new String[]{SketchTronTrails.class.getCanonicalName()});
	}
	
	@Override
	protected void postSetup() {
		_myDecoratorManager = new DecoratorManager();
		_myTrails = new HashMap<Skier, SkierTrail>();
		
		//Logger.setAll(true);
	}

	@Override
	protected void update(final float theDelta) {
		_myDecoratorManager.update();
		
		ArrayList<Skier> mySkiers = tracking().skiers();
		
		Collection<SkierTrail> myTrails = _myTrails.values();
		
		for(SkierTrail myTrail:myTrails) {
			
			myTrail.update();
			
			myTrail.collides(false);
			
			for(Skier mySkier:mySkiers) {
				if(myTrail.skier() != mySkier){
					if(myTrail.collidesWith(mySkier)) {
						myTrail.collides(true);
					}
				}
				
			}
			
			// TODO: check for collisions
			// TODO: notify decorators on collision
		}

		
	}

	@Override
	protected void drawWithLaser(final Laser3D theLaser) {
		
		_myDecoratorManager.draw(g);
	}

	@Override
	protected void drawOnScreen() {
		Collection<SkierTrail> myTrails = _myTrails.values();
		
		stroke(255);
		for(SkierTrail myTrail:myTrails) {
			myTrail.drawDebug(g);
			myTrail.skier().drawDebug(g);
		}
		
		
		
	}

	@Override
	protected void onNewSkier(Skier theSkier) {
		SkierTrail myTrail = new SkierTrail(theSkier);
		_myTrails.put(theSkier, myTrail);
		
		TrailDecorator myTrailDecorator = new TrailDecorator(myTrail);
		_myDecoratorManager.add(myTrailDecorator);
		
		//SkierCircleDecorator mySkierDecorator = new SkierCircleDecorator(theSkier, 0.01f);
		//_myDecoratorManager.add(mySkierDecorator);
	}

	
	@Override
	protected void onDeadSkier(Skier theSkier) {

		_myTrails.remove(theSkier);
	}

}
