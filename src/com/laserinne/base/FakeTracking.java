package com.laserinne.base;

import java.util.ArrayList;

public class FakeTracking {
	private Tracking _myTracking;
	private ArrayList<FakeSkier> _mySkiers;
	private long _myStartTime;
	private int _myIdPointer = 0;
	
	public FakeTracking(final Tracking theTracking) {
		_mySkiers = new ArrayList<FakeSkier>();
		
		_myTracking = theTracking;
		
		_myStartTime = System.currentTimeMillis();
	}
	
	
	public void update() {
		
		
		ArrayList<FakeSkier> myNewSkiers = new ArrayList<FakeSkier>();

		float myTimestamp = (System.currentTimeMillis() - _myStartTime) / 1000f;

		
		if(Math.random() > 0.995) {
			FakeSkier myNewSkier = new FakeSkier(_myIdPointer++);
			myNewSkiers.add(myNewSkier);
		}
		
		
		
		
		for(FakeSkier mySkier:_mySkiers) {
			mySkier.update();
			
			
			if(!mySkier.isDead()){
				_myTracking.trackingMessage(mySkier.id(), mySkier.position().x, mySkier.position().y, 0.1f, 0.1f, 0, 0, mySkier.age(), myTimestamp);
				myNewSkiers.add(mySkier);
			}
		}
		
		_mySkiers.clear();
		_mySkiers.addAll(myNewSkiers);
	}
}
