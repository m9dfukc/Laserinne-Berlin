package com.laserinne.wave;

import java.util.ArrayList;

import com.laserinne.base.Skier;
import com.laserinne.util.Geometry;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;


import traer.physics.Particle;
import traer.physics.ParticleSystem;
import traer.physics.Spring;

class SpringyString {
	private ArrayList<Particle> _myParticles;
	private ArrayList<Spring> _mySprings;
	private ParticleSystem _myPhysics;

	public SpringyString(int theCount) {
		_myParticles = new ArrayList<Particle>();
		_mySprings = new ArrayList<Spring>();

		_myPhysics =  new ParticleSystem( 0.0f, 0.05f );

		Particle myPrev = null;

		float myDistance = 2.0f / (float)theCount;

		for (int i = 0; i < theCount; i++) {

			float myX =  (float)i / (theCount - 1) * 2;
			final Particle myParticle = _myPhysics.makeParticle( 50.0f, myX, 2 / 2f, 0 );
			_myParticles.add(myParticle);

			if (myPrev != null) {
				final Spring mySpring = _myPhysics.makeSpring( myParticle, myPrev, 0.1f, 1f, myDistance );
				_mySprings.add(mySpring);
			}

			myPrev = myParticle;

			if (i == 0 || i == theCount - 1) {
				myParticle.makeFixed();
			}
		}
	}


	void update() {
		Particle mover = _myParticles.get(_myParticles.size() /3);
		mover.makeFixed();

		double myNoiseTime = System.currentTimeMillis()  * 0.0002;
		float myVal = (float) Math.sin(myNoiseTime);
		float myY = PApplet.map(myVal, -1, 1, -0.5f, 0.5f); 

		mover.position().setY( myY);
	}


	void draw(final PGraphics theG) {
		theG.noFill();
		drawLine(theG);
	}

	private void drawLine(PGraphics theG) {
		theG.curveDetail(5);

		theG.beginShape();
		for (int i = 0; i < _myParticles.size(); i++) {
			final Particle myParticle = _myParticles.get(i);
			if (i == 0 || i == _myParticles.size() -1) {
				theG.curveVertex( myParticle.position().x(), myParticle.position().y() );
			}
			theG.curveVertex( myParticle.position().x(), myParticle.position().y() );
		}
		theG.endShape();
	}


	boolean collidesWith(Skier theSkier) {

		for(int i = 1; i < _myParticles.size(); i++){
			Particle myLeft = _myParticles.get(i-1);
			Particle myRight =  _myParticles.get(i);

			PVector myLeftPos = new PVector(myLeft.position().x(), myRight.position().y());
			PVector myRightPos =  new PVector(myRight.position().x(),myRight.position().y());


			float myDistance = Geometry.distanceSegmentPoint(myLeftPos, myRightPos, theSkier.centroid());
			if(myDistance <= theSkier.radius ) {
				return true;
			}  

		}



		return false; 
	}
}

