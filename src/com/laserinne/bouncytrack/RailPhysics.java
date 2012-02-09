package com.laserinne.bouncytrack;

import java.util.ArrayList;


import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import toxi.geom.Vec2D;
import toxi.physics2d.ParticleString2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletConstrainedSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior;

class RailPhysics {

	float strength;
	float radius;
	
	VerletPhysics2D physics;
	AttractionBehavior skierAttractor;
	Vec2D skier;
	
	ParticleString2D stringCenter;
	ParticleString2D stringLeft;
	ParticleString2D stringRight;
	
	ArrayList<VerletParticle2D> particlesCenter = new ArrayList<VerletParticle2D>();
	ArrayList<VerletParticle2D> particlesLeft = new ArrayList<VerletParticle2D>();
	ArrayList<VerletParticle2D> particlesRight = new ArrayList<VerletParticle2D>();
	
	ArrayList<PVector> pointsCenter;
	ArrayList<PVector> pointsLeft;
	ArrayList<PVector> pointsRight;

	private boolean mouseDown;
	
	public RailPhysics(VerletPhysics2D physics, ArrayList<PVector> center, ArrayList<PVector> left, ArrayList<PVector> right, float s) {

		this.physics = physics;
		pointsCenter = center;
		pointsLeft = left;
		pointsRight = right;

		strength = s;
		radius = 0.09f;
		
		mouseDown = false;
		
		for (int i = 0; i < center.size(); i++) {
			VerletParticle2D particleCenter = new VerletParticle2D(pointsCenter.get(i).x, pointsCenter.get(i).y);
			VerletParticle2D particleLeft = new VerletParticle2D(pointsLeft.get(i).x, pointsLeft.get(i).y);
			VerletParticle2D particleRight = new VerletParticle2D(pointsRight.get(i).x, pointsRight.get(i).y);
			
			particleCenter.lock();
			
			VerletConstrainedSpring2D  springLeft = 
				new VerletConstrainedSpring2D (
					particleCenter, 
					particleLeft, 
					PApplet.dist(particleCenter.x, particleCenter.y, particleLeft.x, particleLeft.y), 
					0.001f, 
					PApplet.dist(particleCenter.x, particleCenter.y, particleLeft.x, particleLeft.y) * 3.25f
				);
			VerletConstrainedSpring2D  springRight = 
				new VerletConstrainedSpring2D (
						particleCenter, 
						particleRight, 
						PApplet.dist(particleCenter.x, particleCenter.y, particleRight.x, particleRight.y), 
						0.001f,
						PApplet.dist(particleCenter.x, particleCenter.y, particleRight.x, particleRight.y) * 3.25f
					);
			springLeft.lockA(true);
			springRight.lockA(true);
			
			particlesCenter.add(particleCenter);
			particlesLeft.add(particleLeft);
			particlesRight.add(particleRight);
			
			physics.addSpring(springLeft);
			physics.addSpring(springRight);
		}
		stringCenter = new ParticleString2D(physics, particlesCenter, strength);
		stringLeft = new ParticleString2D(physics, particlesLeft, strength);
		stringRight = new ParticleString2D(physics, particlesRight, strength);
		
		stringLeft.getHead().lock();
		stringLeft.getTail().lock();
		stringRight.getHead().lock();
		stringRight.getTail().lock();

	}

	void updateSkier(Vec2D skier) {
		this.skier = skier;	
		if( mouseDown ) {
			skierAttractor = new AttractionBehavior(skier, 0.01f, -.09f);
			physics.addBehavior(skierAttractor);
			physics.update();
			physics.removeBehavior(skierAttractor);
		}
	}

	void onNewSkier(Vec2D skier) {
		mouseDown = true;
	}
	
	void onDeadSkier() {
		mouseDown = false;
	}

	// Draw the chain
	void drawOnScreen(final PGraphics theG) {
		
		theG.noFill();
		theG.stroke(255);
		
		theG.beginShape();
		theG.vertex(particlesLeft.get(0).x, particlesLeft.get(0).y);
		for(int i = 1; i < particlesLeft.size(); i++) {
			VerletParticle2D particle = particlesLeft.get(i);
			theG.curveVertex(particle.x, particle.y);
		}
		theG.endShape();
		
		theG.beginShape();
		theG.vertex(particlesRight.get(0).x, particlesRight.get(0).y);
		for(int i = 1; i < particlesRight.size(); i++) {
			VerletParticle2D particle = particlesRight.get(i);
			theG.curveVertex(particle.x, particle.y);
		}
		theG.endShape();
		
		theG.stroke(255, 0, 0);
		for(int i = 0; i < particlesCenter.size(); i++) {
			VerletParticle2D particle = particlesCenter.get(i);
			theG.ellipse(particle.x, particle.y, 0.002f, 0.002f);
		}
		
		theG.stroke(0,0,255);
		for(int i = 0; i < particlesCenter.size(); i++) {
			VerletParticle2D particleLeft = particlesLeft.get(i);
			VerletParticle2D particleRight = particlesRight.get(i);
			theG.line(particleLeft.x, particleLeft.y, particleRight.x, particleRight.y);
		}
		
	}
}