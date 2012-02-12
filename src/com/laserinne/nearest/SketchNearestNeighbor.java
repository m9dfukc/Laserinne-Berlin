package com.laserinne.nearest;

import java.util.ArrayList;
import java.util.Collections;

import laserschein.Laser3D;
import laserschein.Logger;

import processing.core.PApplet;
import processing.core.PVector;

import com.laserinne.base.LaserinneSketch;
import com.laserinne.base.Skier;
import com.laserinne.util.Edge;
import com.laserinne.util.SkierYComparator;



@SuppressWarnings("serial")
public class SketchNearestNeighbor extends LaserinneSketch {
	
	private Edge<Skier> _myEdge;
	private float _myConnectArea = -0.3f;   // upper third is the are where things can connect
	
	@Override
	protected void postSetup() {
		_myEdge = null;
	}

	@Override
	protected void update(final float theDelta) {
		
		/* Check if edge is still valid */
		if(_myEdge != null && (_myEdge.a.isDead() || _myEdge.b.isDead() ) ) {
			Logger.printInfo("Destroyed edge between Skier " + _myEdge.a.id() + " and " + _myEdge.a.id());

			_myEdge = null;
		}
				
		ArrayList<Skier> mySkiers = tracking().skiersConfident();

		Collections.sort(mySkiers, new SkierYComparator()); 	// Sort by their y positions
		
		if(_myEdge == null && mySkiers.size() > 1) {    // We need two to party
			Skier myA = mySkiers.get(0);
			Skier myB = mySkiers.get(1);
			
			if(myA.centroid().y < _myConnectArea && myB.centroid().y < _myConnectArea) {
				_myEdge = new Edge<Skier>(myA, myB);
				Logger.printInfo("Create edge between Skier " + myA.id() + " and " + myB.id());
			}
		}
					
	}

	@Override
	protected void drawWithLaser(final Laser3D theLaser) {
		
	}

	@Override
	protected void drawOnScreen() {
		stroke(15, 193, 238);
		line(-1, _myConnectArea, 1, _myConnectArea);
		
		
		for(Skier mySkier:tracking().skiers()) {
			mySkier.drawDebug(g);
		}
		
		
		if(_myEdge != null) {
			PVector myPointA = _myEdge.a.base();
			PVector myPointB = _myEdge.b.base();
			
			line(myPointA.x, myPointA.y, myPointB.x, myPointB.y);
		}
	}

	@Override
	protected void onNewSkier(Skier theSkier) {
		
		
	}

	@Override
	protected void onDeadSkier(Skier theSkier) {
		
	}
	
	
	public static void main(String[] args) {
		PApplet.main(new String[]{SketchNearestNeighbor.class.getCanonicalName()});
	}
	


}
