package com.laserinne.base;

import processing.core.PApplet;
import processing.core.PVector;

public class MouseSkier {

	private int _myId;
	private int _myAge;
	private PVector _myPosition;

	public MouseSkier(int theId) {
		_myId = theId;
		_myAge = 0;
		_myPosition = new PVector(0f, -0.5f);
	}
	
	public void update(float x, float y) {
		_myAge++;
		_myPosition.x = PApplet.map(x, -1f, 1f, -0.5f, 0.5f);
		_myPosition.y = PApplet.map(y, -1f, 1f, -0.5f, 0.5f);
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
}
