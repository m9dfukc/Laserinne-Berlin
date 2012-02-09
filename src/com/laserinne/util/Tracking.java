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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import laserschein.Logger;

import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

public class Tracking {
	private OscP5 _myOscP5;

	private HashMap<Integer, Skier> _mySkierTable;
	protected ArrayList<Skier> _mySkiers;
	protected ArrayList<Skier> _myNewlyTrackedSkiers;
	protected ArrayList<Skier> _myNewSkiers;
	protected ArrayList<Skier> _myDeadSkiers;
	
	public static final float TRACKING_RANGE_LOWER = -0.5f;
	public static final float TRACKING_RANGE_UPPER = 0.5f;
	
	public static final int SKIER_DEATH_TIMEOUT = 100;

	private static final int AGE_OF_CONFIDENCE = 10;

	
	
	/**
	 * Defaults to 239.0.0.1:9999
	 */
	public Tracking() {   
		this( "239.0.0.1", 9999 );
	}


	public Tracking(String theAddress, int thePort) {
		_myOscP5 = new OscP5(this, theAddress, thePort);

		_myOscP5.plug(this, "trackingMessage", "/tracking/");

		_mySkierTable = new HashMap<Integer, Skier>();
		_mySkiers = new ArrayList<Skier>();
		_myNewlyTrackedSkiers = new ArrayList<Skier>();
		_myNewSkiers = new ArrayList<Skier>();
		_myDeadSkiers = new ArrayList<Skier>();
	}


	
	/**
	 * Update this every frame. To be able to access the tracked skiers
	 */
	public void update() {

		_mySkiers.clear();
		_myNewSkiers.clear();
		_myDeadSkiers.clear();
		
		synchronized (_myNewlyTrackedSkiers) {
			_myNewSkiers.addAll(_myNewlyTrackedSkiers);
			_myNewlyTrackedSkiers.clear();
		}
		

		synchronized(_mySkierTable) {
			Collection<Skier> mySkiers = _mySkierTable.values();
			Iterator<Skier> myIterator = mySkiers.iterator();

			while (myIterator.hasNext()) {
				final Skier mySkier = myIterator.next();

				mySkier.update();

				if ( mySkier.isDead() ) {
					Logger.printInfo("Removing skier " + mySkier.id() + "(Age: " + mySkier.age() + ")");
					
					Logger.printDebug("Skier " + mySkier.id() + "  had an update rate of " + mySkier.calculateTrackingRate() + " Hz");
					
					_myDeadSkiers.add(mySkier);
					myIterator.remove();
				} else {
					_mySkiers.add( mySkier );
				}
			}
		}		
	}


	
	
	
	/**
	 * Returns a copy of the list of all skiers that have died for this update cycle.
	 * This list is not going to change. So get a new one each frame
	 * 
	 * @return 
	 */
	public ArrayList<Skier> deadSkiers() {
		synchronized (_myDeadSkiers) {
			final ArrayList<Skier> myList = new ArrayList<Skier>(_myDeadSkiers);
			return myList;
		}
	}
	
	
	
	/**
	 * Returns a copy of the list of all skiers that have appeared for this update cycle.
	 * This list is not going to change. So get a new one each frame
	 * 
	 * @return 
	 */
	public ArrayList<Skier> newSkiers() {
		synchronized (_myNewSkiers) {
			final ArrayList<Skier> myList = new ArrayList<Skier>(_myNewSkiers);
			return myList;
		}
	}
	
	
	
	/**
	 * Returns a copy of the list of all active skiers.
	 * This list is not going to change. So get a new one each frame
	 * 
	 * @return 
	 */
	public ArrayList<Skier> skiers() {
		synchronized (_mySkiers) {
			final ArrayList<Skier> myList = new ArrayList<Skier>(_mySkiers);
			
			return myList;
		}
	}
	
	
	
	
	/**
	 * Returns a copy of the list of all active skiers with a minumum age
	 * This list is not going to change. So get a new one each frame
	 * 
	 * @return 
	 */
	public ArrayList<Skier> skiersConfident() {
		synchronized (_mySkiers) {
			final ArrayList<Skier> myList = new ArrayList<Skier>();
			
			for(final Skier mySkier:_mySkiers) {
				if(mySkier.age() > AGE_OF_CONFIDENCE) {
					myList.add(mySkier);
				}
			}
			
			return myList;
		}
	}

	
	
	/**
	 * Plug method for OscP5 to catch tracking messages 
	 * 
	 * @param theId
	 * @param theX
	 * @param theY
	 * @param theWidth
	 * @param theHeight
	 * @param theDeltaX
	 * @param theDeltaY
	 * @param theAge (in frames)
	 * @param theTimestamp (in seconds (float))
	 */
	public void trackingMessage(float theId, float theX, float theY, float theWidth, float theHeight, float theDeltaX, float theDeltaY, float theAge, float theTimestamp  ) {

		Logger.printDebug("Received a tracking message.");

		synchronized( _mySkierTable ) {
			int myId = Math.round(theId);

			/* We know this guy already */
			if(_mySkierTable.containsKey(myId)) {
				final Skier myKnownSkier =  _mySkierTable.get(myId);

				/* Check if the message we got is newer than our last state */
				if ( myKnownSkier.lastTimestamp() < theTimestamp ) {
					myKnownSkier.updateValues(mapValue(theX), mapValue(theY), mapValue(theWidth), mapValue(theHeight), mapValue(theDeltaX), mapValue(theDeltaY), theAge, theTimestamp);
				} else {
					Logger.printWarning("Timestamp mixup. Skier has: " + myKnownSkier.lastTimestamp() + " | Received:" + theTimestamp);
				}
			} else {
				final Skier myNewSkier = new Skier(myId, mapValue(theX), mapValue(theY), mapValue(theWidth), mapValue(theHeight), mapValue(theDeltaX), mapValue(theDeltaY), theAge, theTimestamp); 
				_mySkierTable.put(myId, myNewSkier);
			
				Logger.printInfo("Adding skier " + myNewSkier.id() );
								
				synchronized (_myNewlyTrackedSkiers) {
					_myNewlyTrackedSkiers.add(myNewSkier);
				}
			}
		}
	}
	
	
	
	private float mapValue(float theValue) {
		return PApplet.map(theValue, TRACKING_RANGE_LOWER, TRACKING_RANGE_UPPER, -1, 1);
	}



	/**
	 * Plug method for OscP5 to catch unknown messages 
	 * 
	 * @param theOscMessage
	 */
	@SuppressWarnings("unused")
	private void oscEvent(OscMessage theOscMessage) {
		if (!theOscMessage.isPlugged() ) {
			/* print the address pattern and the typetag of the received OscMessage */
			Logger.printDebug("Received an OSC message that I don't know: addrpattern: "+theOscMessage.addrPattern() + " typetag: "+theOscMessage.typetag() );
		}
	}
}

