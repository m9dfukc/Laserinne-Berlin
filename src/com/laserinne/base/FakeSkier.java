package com.laserinne.base;

import processing.core.PVector;

public class FakeSkier {

	private int _myId;
	private int _myAge;
	private long _myBornTime;
	
	private PVector _myPosition;
	private float _mySpeed;
	private float _myXSpeed;

	public FakeSkier(int theId) {
		_myId = theId;
		_myAge = 0;
		_myPosition = new PVector((float)(Math.random() * 0.5f - 0.25f), -0.5f);
		_mySpeed = (float) (Math.random() * 0.005 + 0.001) * 10f;
		_myXSpeed = (float) (Math.random() * 0.005 + 0.001) + 1;

		_myBornTime = System.currentTimeMillis();
	}
	
	public void update() {
		_myAge++;
		_myPosition.x =	(float) (Math.sin(this.id() + _myAge * 0.005f * _myXSpeed) * 0.5f)  * 0.7f;	
		
		float myAge = (System.currentTimeMillis() - _myBornTime) / 1000.0f; 
		_myPosition.y = _mySpeed * myAge - 0.5f;
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
