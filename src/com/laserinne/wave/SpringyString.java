package com.laserinne.wave;

import java.util.ArrayList;

import processing.core.PGraphics;


import traer.physics.Particle;
import traer.physics.ParticleSystem;
import traer.physics.Spring;

class SpringyString {
	  public ArrayList<Particle> particles;
	  public ArrayList<Spring> springs;
	  ParticleSystem physics;

	  public SpringyString(int theCount )
	  {
	    particles = new ArrayList<Particle>();
	    springs = new ArrayList<Spring>();
	    

	    physics =  new ParticleSystem( 0.0f, 0.05f );
;

	    Particle myPrev = null;

	    float myDistance = width / (float)theCount;

	    for (int i = 0; i < theCount; i++) {

	      float myX =  (float)i / (theCount - 1) * width;
	      final Particle myParticle = physics.makeParticle( 50.0f, myX, height / 2f, 0 );
	      particles.add(myParticle);

	      if (myPrev != null) {
	        final Spring mySpring = physics.makeSpring( myParticle, myPrev, 0.1f, 1f, myDistance );
	        springs.add(mySpring);
	      }

	      myPrev = myParticle;

	      if (i == 0 || i == theCount - 1) {
	        myParticle.makeFixed();
	      }
	    }
	  }

	  
	  void update() {
	    Particle mover = particles.get(particles.size() /3);
	    mover.makeFixed();
	    noiseDetail(2);

	    float myY = map(noise(millis() * 0.1 * 0.005), 0, 1, height/2 - height/2, height/2 + height/2); 


	    float myVal = sin(millis()  * 0.0002);
	    myY = map(myVal, -1, 1, height/2 - height/4, height/2 + height/4); 


	    // myY = mouseY;
	    mover.position().setY( myY);
	  }


	  void draw(final PGraphics theG) {
	    stroke(40, 255, 40, 128);
	    strokeWeight(1);
	    drawLine();

	    stroke(30, 255, 30, 20);
	    strokeWeight(2);
	    drawLine();

	    stroke(10, 255, 10, 20);
	    strokeWeight(5);
	    drawLine();
	    noFill();

	    strokeWeight(1);
	  }

	  private void drawLine() {

	    curveDetail(5);

	    beginShape();
	    for (int i = 0; i < particles.size(); i++) {
	      final Particle myParticle = particles.get(i);
	      if (i == 0 || i == particles.size() -1) {
	        curveVertex( myParticle.position().x(), myParticle.position().y() );
	      }
	      curveVertex( myParticle.position().x(), myParticle.position().y() );
	    }
	    endShape();
	  }
	  
	  
	  boolean collidesWith(Actor theActor) {
	    
	    for(int i = 1; i < particles.size(); i++){
	      Particle myLeft = particles.get(i-1);
	       Particle myRight =  particles.get(i);
	      
	      float myDistance = distanceSegmentPoint(new PVector(myLeft.position().x(), myRight.position().y()),  new PVector(myRight.position().x(),myRight.position().y()), theActor.position);
	      if(myDistance <= theActor.radius ) {
	        return true;
	      }  

	  }
	    
	      
	    
	     return false; 
	  }
	}

