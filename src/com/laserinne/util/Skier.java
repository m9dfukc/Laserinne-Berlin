/*
 *                This file is part of Laserinne.
 * 
 *  Laser projections in public space, inspiration and
 *  information, exploring the aesthetic and interactive possibilities of
 *  laser-based displays.
 * 
 *  http://www.laserinne.com/
 * 
 * Laserinne is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Laserinne is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Laserinne. If not, see <http://www.gnu.org/licenses/>.
 */

package com.laserinne.util;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Skier {
	private int _myId;

	private ArrayList<PVector> _myHistoryList;
	private PVector[] _myHistory = new PVector[100];
	private int _myHistoryPointer  = 0;

	private PVector _myPosition;
	private PVector _myPreviousPosition;
	private PVector _myDirection;

	
	private float _myDeltaX;
	private float _myDeltaY;

	private float _myAge;

	private float _myWidth;
	private float _myHeight;
	
	



	private float _myLastTimestamp;
	private long _myLastUpdate;

	public static final float DIRECTION_LEARNING_RATE = 0.001f;


	public Skier( int theId, float theX, float theY, float theWidth, float theHeight, float theDeltaX, float theDeltaY, float theAge, float theTimestamp  ) {

		_myHistoryList = new ArrayList<PVector>();
		_myPosition = new PVector();

		_myPreviousPosition = new PVector(0, 0);
		_myPosition = new PVector(0, 0);
		_myDirection = new PVector(0, 0);
		
		_myId = theId;

		updateValues(theX, theY, theWidth, theHeight, theDeltaX, theDeltaY, theAge, theTimestamp);
	}


	public void updateValues(float theX, float theY, float theWidth, float theHeight, float theDeltaX, float theDeltaY, float theAge, float theTimestamp) {

		_myHistory[_myHistoryPointer] = new PVector(_myPosition.x, _myPosition.y);
		_myHistoryPointer++;

		if (_myHistoryPointer >= _myHistory.length ) {
			_myHistoryPointer = 0;
		}

		_myPosition.x = theX;
		_myPosition.y = theY;

		_myDeltaX = theDeltaX;
		_myDeltaY = theDeltaY;

		_myWidth = theWidth;
		_myHeight = theHeight;

		_myAge = theAge;

		_myLastTimestamp = theTimestamp;
		_myLastUpdate = System.currentTimeMillis();
	}


	public void update() {

		_myHistoryList.clear();

		for (int i = 0; i < _myHistory.length; i++) {
			int myPosition = i + _myHistoryPointer;

			if ( myPosition >=  _myHistory.length) myPosition -= _myHistory.length;

			if ( _myHistory[ myPosition ] != null) {
				_myHistoryList.add( _myHistory[ myPosition ] );
			}
		}


		final PVector myDirection = new PVector(_myPreviousPosition.x, _myPreviousPosition.y );
		myDirection.sub(_myPosition);

		if ( myDirection.mag() > 0) {
			_myDirection.x = ((1-DIRECTION_LEARNING_RATE) * _myDirection.x) + ( DIRECTION_LEARNING_RATE * myDirection.x);
			_myDirection.y = ((1-DIRECTION_LEARNING_RATE) * _myDirection.y) + ( DIRECTION_LEARNING_RATE * myDirection.y);
		}


		_myPreviousPosition.set(_myPosition);
	}


	public boolean isDead() {
		if (System.currentTimeMillis() - _myLastUpdate > 200) {  // TODO: unhardcode
			return true;
		} 

		return false;
	}

	
	/**
	 * @return a unique numeric id
	 */
	public int id() {
		return _myId;
	}
	

	public float lastTimestamp() {
		return _myLastTimestamp;
	}



	public PVector centroid() {
		return _myPosition;
	}
	
	
	public PVector base() {
		final float myX = _myPosition.x;
		final float myY = _myPosition.y - _myHeight * 0.5f;

		return new PVector(myX, myY);
	}
	
	
	
	public void drawDebug(PGraphics theG) {
		
		theG.stroke(255);
		theG.rectMode(PGraphics.CENTER);
		theG.rect(_myPosition.x, _myPosition.y, _myWidth, _myHeight);
		
		
		final PVector myBase = base();
		
		theG.stroke(255, 0, 0);
		theG.line(myBase.x - 0.05f, myBase.y, myBase.x + 0.05f, myBase.y);
		
		
	}


}
