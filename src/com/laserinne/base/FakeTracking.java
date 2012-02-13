package com.laserinne.base;

import java.util.ArrayList;

public class FakeTracking {
	private Tracking _myTracking;
	private ArrayList<FakeSkier> _mySkiers;
	private MouseSkier _mouseSkier;
	private long _myStartTime;
	private int _myIdPointer = 0;
	private boolean bMouse = false;
	
	public FakeTracking(final Tracking theTracking) {
		_mySkiers = new ArrayList<FakeSkier>();
		
		_myTracking = theTracking;
		
		_myStartTime = System.currentTimeMillis();
	}
	
	
	public void update() {	
		ArrayList<FakeSkier> myNewSkiers = new ArrayList<FakeSkier>();

		float myTimestamp = (System.currentTimeMillis() - _myStartTime) / 1000.0f;
		
		if(Math.random() > 0.998f) {
			_myIdPointer++;
			FakeSkier myNewSkier = new FakeSkier(_myIdPointer);
			myNewSkiers.add(myNewSkier);
		}		

		
		for(FakeSkier mySkier:_mySkiers) {
			mySkier.update();
			
			if(!mySkier.isDead()){
				_myTracking.trackingMessage(mySkier.id(), mySkier.position().x, mySkier.position().y, 0.03f, 0.04f, 0, 0, mySkier.age(), myTimestamp);
				myNewSkiers.add(mySkier);
			}
		}
		
		_mySkiers = myNewSkiers;
	}
	
	public void mousePressed() {
		bMouse = true;
		_myIdPointer++;
		_mouseSkier = new MouseSkier(_myIdPointer);
	}
	
	public void mouseUpdate(float x, float y) {
		if( bMouse ) {
			float myTimestamp = (System.currentTimeMillis() - _myStartTime) / 1000.0f;
			_mouseSkier.update(x, y);
			_myTracking.trackingMessage(_mouseSkier.id(), _mouseSkier.position().x, _mouseSkier.position().y, 0.03f, 0.04f, 0, 0, _mouseSkier.age(), myTimestamp);
		}
	}
	
	public void mouseReleased() {
		bMouse = false;
	}
}
