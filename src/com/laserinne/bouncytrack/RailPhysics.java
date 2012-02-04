package com.laserinne.bouncytrack;

import java.util.ArrayList;

import laserschein.Logger;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletConstrainedSpring2D;

class RailPhysics {

	float totalLength;
	int numPoints;
	float strength;
	float radius;
	VerletPhysics2D physics;
	ArrayList<PVector> points;

	VerletParticle2D tail;

	PVector offset = new PVector();
	boolean dragged = false;

	@SuppressWarnings("serial")
	public RailPhysics(VerletPhysics2D physics, ArrayList<PVector> points, float s) {

		this.physics = physics;
		this.points = points;

		strength = s;
		radius = 0.09f;

		// Here is the real work, go through and add particles to the chain
		// itself
		for (int i = 0; i < points.size(); i++) {
			// Make a new particle with an initial starting location
			VerletParticle2D particle = new VerletParticle2D(0f, points.get(i).y);
			physics.addParticle(particle);

			// Connect the particles with a Spring (except for the head)
			if (i > 0) {
				VerletParticle2D previous = physics.particles.get(i - 1);
				
				VerletConstrainedSpring2D spring = new VerletConstrainedSpring2D(particle, previous, 0.001f, strength);
				physics.addSpring(spring);
				if (i == points.size()-1) physics.particles.get(i).lock();
			}
		}

		// Keep the top fixed
		VerletParticle2D head = physics.particles.get(0);
		head.lock();

		// Store reference to the tail
		tail = physics.particles.get(points.size() - 1);
		tail.lock();
	}

	// Check if a point is within the ball at the end of the chain
	// If so, set dragged = true;
	void contains(float x, float y) {
		boolean found = false;
		for (int i = 1; i < points.size()-1; i++) {
			VerletParticle2D body = physics.particles.get(i);
			body.unlock();
			if(!found) body.lock();
			float d = PApplet.dist(x, y, points.get(i).x, points.get(i).y);
			if (d < radius) {
				offset.x = body.x - x ;
				offset.y = body.y - y ;
				dragged = true;
				tail = body;
				found = true;
				//break;
			}
		}
	}

	// Release the ball
	void release() {
		tail.unlock();
		dragged = false;
	}

	// Update tail location if being dragged
	void updateTail(float x, float y, final PGraphics theG) {
		for (int i = 0; i < points.size(); i++) {
			//physics.particles.get(i).set(points.get(i).x, points.get(i).y);
		}
		if (dragged) {
			tail.set(x + offset.x, y + offset.y);
		}
		theG.fill(255);
		theG.ellipse(x, y, radius, radius);
	}

	// Draw the chain
	void drawOnScreen(final PGraphics theG) {
		// Draw line connecting all points
		/*
		for (int i = 0; i < physics.particles.size() - 1; i++) {
			VerletParticle2D p1 = physics.particles.get(i);
			VerletParticle2D p2 = physics.particles.get(i + 1);
			theG.stroke(255);
			//Logger.printInfo("x "+p1.x+" y "+p1.y+" x2 "+p2.x+" y2 "+p2.y);
			theG.line(p1.x, p1.y, p2.x, p2.y);
		}
		 */
		theG.noFill();
		theG.beginShape();
		VerletParticle2D particle = physics.particles.get(0);
		theG.vertex(particle.x+ points.get(0).x, particle.y+ points.get(0).y);
		for(int i = 1; i < physics.particles.size(); i++) {
			particle = physics.particles.get(i);
			theG.curveVertex(particle.x + points.get(i).x, particle.y + points.get(i).y);
		}
		theG.endShape();
		
		// Draw a ball at the tail
		theG.stroke(0);
		theG.fill(255,0,0);
		theG.ellipse(tail.x, tail.y, radius * 0.5f, radius * 0.5f);
	}
}