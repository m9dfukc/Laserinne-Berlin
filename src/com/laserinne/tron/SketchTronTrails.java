package com.laserinne.tron;

import java.util.Collection;
import java.util.HashMap;

import laserschein.Logger;

import com.laserinne.base.LaserinneSketch;
import com.laserinne.base.Skier;
import com.laserinne.decoration.DecoratorManager;
import com.laserinne.decoration.SkierCircleDecorator;

import de.looksgood.ani.Ani;

@SuppressWarnings("serial")
public class SketchTronTrails extends LaserinneSketch {
	
	private DecoratorManager _myDecoratorManager;
	private HashMap<Skier, SkierTrail> _myTrails;
	
	@Override
	protected void postSetup() {
		Ani.init(this);
		_myDecoratorManager = new DecoratorManager();
		_myTrails = new HashMap<Skier, SkierTrail>();
		
		Logger.setAll(true);
	}

	@Override
	protected void update(final float theDelta) {
		_myDecoratorManager.update();
		
		Collection<SkierTrail> myTrails = _myTrails.values();
		
		for(SkierTrail myTrail:myTrails) {
			myTrail.update();
			
			// TODO: check for collisions
			// TODO: notify decorators on collision
		}

		
	}

	@Override
	protected void drawWithLaser() {
		
		
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
		
		SkierCircleDecorator mySkierDecorator = new SkierCircleDecorator(theSkier, 0.01f);
		_myDecoratorManager.add(mySkierDecorator);
	}

	
	@Override
	protected void onDeadSkier(Skier theSkier) {
		_myTrails.remove(theSkier);
	}

}
