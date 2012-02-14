package com.laserinne.nearest.lasso;

import java.util.ArrayList;
import java.util.Collections;

import laserschein.Laser3D;
import laserschein.Logger;

import processing.core.PApplet;
import processing.core.PVector;

import toxi.geom.Rect;
import toxi.physics2d.VerletPhysics2D;

import controlP5.*;

import com.laserinne.base.LaserinneSketch;
import com.laserinne.base.Skier;
import com.laserinne.util.Edge;
import com.laserinne.util.SkierYComparator;



@SuppressWarnings("serial")
public class SketchNearestNeighborLasso extends LaserinneSketch {
	
	private Edge<Skier> _myEdge;
	private float _myConnectArea = 0f;   // upper half is the are where things can connect
	private VerletPhysics2D _physics;
	private LassoPhysics _lasso;
	
	private Controller _springStrength;
	private Controller _springLength;
	
	@Override
	protected void postSetup() {
		_springStrength = _controlP5.addSlider("Strength", 0.001f, 0.1f, 0.06f, 10, 10, 140, 15);
		_springStrength.setDecimalPrecision(4);
		_springStrength.moveTo(_controlWindow);
		_springLength = _controlP5.addSlider("Length", 0.0001f, 1f, 0.001f, 10, 40, 140, 15);
		_springLength.setDecimalPrecision(5);
		_springLength.moveTo(_controlWindow);
		
		_myEdge = null;
		_physics = new VerletPhysics2D();
		_physics.setWorldBounds(new Rect(-1f,-1f,2f,2f));
		_lasso = new LassoPhysics(_physics, 0.06f, 30, 0.001f);
	}

	@Override
	protected void update(final float theDelta) {
		
		/* Check if edge is still valid */
		if( _myEdge != null && (_myEdge.a.isDead() || _myEdge.b.isDead() ) ) {
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
		_physics.update();
		_lasso.updateSpring(_springStrength.value(), _springLength.value());
		_lasso.update(_myEdge);
	}

	@Override
	protected void drawWithLaser(final Laser3D theLaser) {
		_lasso.draw(g);
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
		PApplet.main(new String[]{SketchNearestNeighborLasso.class.getCanonicalName()});
	}
	


}
