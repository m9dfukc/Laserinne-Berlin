package com.laserinne.base;

import processing.core.PVector;

public class FakeSkier {

	private int _myId;
	private int _myAge;
	private long _myBornTime;
	
	private PVector _myPosition;
	private float _mySpeed;

	public FakeSkier(int theId) {
		_myId = theId;
		_myAge = 0;
		_myPosition = new PVector((float)(Math.random() * 0.5f - 0.25f), -0.5f);
		_mySpeed = (float) (Math.random() * 0.005 + 0.001) * 10f;
		_myBornTime = System.currentTimeMillis();
	}
	
	public void update() {
		_myAge++;
		_myPosition.x =	(float) (Math.sin(this.id() + _myAge * 0.01f) * 0.5f)  * 0.4f;	
		
		float myAge = (System.currentTimeMillis() - _myBornTime) / 1000.0f; 
		_myPosition.y = _mySpeed * myAge - 1;
	}
	
	
	public int age() {
		return _myAge;
	}
	
	public int id() {
		return _myId;
	}

	public PVector position() {
		return _myPosition;
	}
	
	public boolean isDead() {
		return _myPosition.y > 0.5;
	}
}
