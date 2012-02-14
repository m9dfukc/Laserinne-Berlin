package com.laserinne.collecting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import laserschein.Laser3D;
import laserschein.Logger;
import processing.core.PApplet;
import processing.core.PVector;

import com.laserinne.base.LaserinneSketch;
import com.laserinne.base.Skier;
import com.laserinne.decoration.DecoratorManager;
import com.laserinne.wave.WavePersonDecorator;

import controlP5.ControlP5;
import controlP5.ControlWindow;
import controlP5.Slider;

@SuppressWarnings("serial")
public class SketchCollect extends LaserinneSketch{

	private DecoratorManager _myDecoratorManager;
	//private HashMap<Skier, WavePersonDecorator> _mySkierDecorators;
	private LinkedList<CollectableItem> _myItems;
	private ControlP5 _myControlP5;
	private ControlWindow _myControlWindow;
	private Slider _myItemLifetime;
	private Slider _myItemTargetCount;

	
	@Override
	protected void postSetup() {
		_myDecoratorManager = new DecoratorManager();
		//_mySkierDecorators = new HashMap<Skier, WavePersonDecorator>();
		_myItems = new LinkedList<CollectableItem>();

		initGui();
		
		createItems();
		shuffleItemAges();
		
		Logger.setAll(false);
	}


	

	private void initGui() {
		_myControlP5 = new ControlP5(this);
		_myControlP5.setAutoDraw(false);
		_myControlWindow = _myControlP5.addControlWindow("controlP5window", 100, 100, 240, 80);
		_myItemLifetime = _myControlP5.addSlider("Lifetime", 1f, 60f, 19f, 10, 10, 140, 15);
		_myItemLifetime.setDecimalPrecision(4);
		_myItemLifetime.moveTo(_myControlWindow);

		_myItemTargetCount = _myControlP5.addSlider("Amount", 1, 30, 6, 10, 40, 140, 15);
		_myItemTargetCount.setDecimalPrecision(5);
		_myItemTargetCount.moveTo(_myControlWindow);


	}

	@Override
	protected void update(float theDelta) {
		_myDecoratorManager.update();

		ArrayList<Skier> mySkiers = tracking().skiersConfident();

		checkCollisions(mySkiers);
		destroyItems();
		createItems();

	}

	
	private void destroyItems() {
		Iterator<CollectableItem> myIterator = _myItems.iterator();

		while(myIterator.hasNext()){
			CollectableItem myItem = myIterator.next();
			
			if(myItem.age() > _myItemLifetime.value() * 0.9) {
				myItem.blink();
			}

			
			if(myItem.age() > _myItemLifetime.value()){
				myIterator.remove();
				myItem.die();
			} else if(myItem.hasBeenCollected()) {
				myIterator.remove();
			}
			
			
		}

	}
	
	
	private void createItems() {
		
		int myTarget = Math.round(_myItemTargetCount.value());
		int myCount = Math.max(0, myTarget - _myItems.size());
		
		for(int i = 0; i < myCount; i++) {
			float myX = random(-0.9f, 0.9f);
			float myY = random(-0.9f, 0.9f);
			
			float myRadius = 0.03f; 	//TODO: radius
			
			CollectableItem myItem = new CollectableItem(new PVector(myX, myY), myRadius);
			
			_myDecoratorManager.add(myItem);
			
			_myItems.add(myItem);
		}
	}
	
	
	private void shuffleItemAges() {
		for(final CollectableItem myItem:_myItems) {
			float _myMaxAge = _myItemLifetime.value();
			myItem.age(random(0, _myMaxAge));
		}
		
	}

	

	private void checkCollisions(ArrayList<Skier> mySkiers) {
		for(final Skier mySkier:mySkiers) {
		
			for(final CollectableItem myItem:_myItems) {
				if(!myItem.hasBeenCollected() && myItem.collidesWith(mySkier)) {
					mySkier.increaseScore();
					myItem.collect();
					Logger.printDebug("Collision");
				} 
			}
		}
	}

	@Override
	protected void drawWithLaser(final Laser3D theLaser) {
		theLaser.smooth();
		theLaser.noSmooth();

		_myDecoratorManager.draw(g, theLaser);
	}

	@Override
	protected void drawOnScreen() {
		for(final Skier mySkier:tracking().skiers()) {
			mySkier.drawDebug(g);
		}
	}

	@Override
	protected void onNewSkier(Skier theSkier) {
		SkierVDecorator myDecorator = new SkierVDecorator(theSkier);
		_myDecoratorManager.add(myDecorator);
	}

	@Override
	protected void onDeadSkier(Skier theSkier) {
	}


	public static void main(String[] args) {
		PApplet.main(new String[]{SketchCollect.class.getCanonicalName()});
	}

}
