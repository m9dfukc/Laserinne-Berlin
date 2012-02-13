package com.laserinne.bouncytrack;

import java.util.ArrayList;

import netP5.Logger;

import com.laserinne.util.Geometry;


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
	int railLength;
	
	VerletPhysics2D physics;
	AttractionBehavior skierAttractor;
	Vec2D skier;
	
	ParticleString2D stringCenter;
	ParticleString2D stringLeft;
	ParticleString2D stringRight;
	
	ArrayList<VerletParticle2D> particlesCenter = new ArrayList<VerletParticle2D>();
	ArrayList<VerletParticle2D> particlesLeft = new ArrayList<VerletParticle2D>();
	ArrayList<VerletParticle2D> particlesRight = new ArrayList<VerletParticle2D>();
	ArrayList<VerletParticle2D> particlesBoundingLeft = new ArrayList<VerletParticle2D>();
	ArrayList<VerletParticle2D> particlesBoundingRight = new ArrayList<VerletParticle2D>();
	
	ArrayList<PVector> pointsCenter = new ArrayList<PVector>();
	ArrayList<PVector> pointsLeft = new ArrayList<PVector>();
	ArrayList<PVector> pointsRight = new ArrayList<PVector>();
	ArrayList<PVector> pointsBoundingLeft = new ArrayList<PVector>();
	ArrayList<PVector> pointsBoundingRight = new ArrayList<PVector>();

	private boolean mouseDown;
	
	public RailPhysics(VerletPhysics2D physics, ArrayList<PVector> center, ArrayList<PVector> left, ArrayList<PVector> right, float s) {

		this.physics = physics;
		pointsCenter = center;
		pointsLeft = left;
		pointsRight = right;

		strength = s;
		
		mouseDown = false;
		
		railLength = 10;
		
		generateBoundingOutlines(0.2f);
		
		for (int i = 0; i < center.size(); i++) {
			
			VerletParticle2D particleCenter = new VerletParticle2D(pointsCenter.get(i).x, pointsCenter.get(i).y);
			VerletParticle2D particleLeft = new VerletParticle2D(pointsLeft.get(i).x, pointsLeft.get(i).y);
			VerletParticle2D particleRight = new VerletParticle2D(pointsRight.get(i).x, pointsRight.get(i).y);
			VerletParticle2D particleBoundingLeft = new VerletParticle2D(pointsBoundingLeft.get(i).x, pointsBoundingLeft.get(i).y);
			VerletParticle2D particleBoundingRight = new VerletParticle2D(pointsBoundingRight.get(i).x, pointsBoundingRight.get(i).y);
		
			particleCenter.lock();
			particleBoundingLeft.lock();
			particleBoundingRight.lock();
			
			if( i < railLength ) {
				particleLeft.lock();
				particleRight.lock();
			}
			
			VerletConstrainedSpring2D springCenterLeft = 
				new VerletConstrainedSpring2D (
					particleCenter, 
					particleLeft, 
					PApplet.dist(particleCenter.x, particleCenter.y, particleLeft.x, particleLeft.y), 
					0.0004f
				);
			VerletConstrainedSpring2D springCenterRight = 
				new VerletConstrainedSpring2D (
						particleCenter, 
						particleRight, 
						PApplet.dist(particleCenter.x, particleCenter.y, particleRight.x, particleRight.y), 
						0.0004f 
					);
			VerletConstrainedSpring2D springBoundingLeft = 
					new VerletConstrainedSpring2D (
						particleLeft, 
						particleBoundingLeft, 
						PApplet.dist(particleBoundingLeft.x, particleBoundingLeft.y, particleLeft.x, particleLeft.y), 
						0.0004f
					);
			VerletConstrainedSpring2D springBoundingRight = 
				new VerletConstrainedSpring2D (
						particleRight, 
						particleBoundingRight,
						PApplet.dist(particleBoundingRight.x, particleBoundingRight.y, particleRight.x, particleRight.y), 
						0.0004f
					);
			springCenterLeft.lockA(true);
			springCenterRight.lockA(true);
			
			particlesCenter.add(particleCenter);
			particlesLeft.add(particleLeft);
			particlesRight.add(particleRight);
			particlesBoundingLeft.add(particleBoundingLeft);
			particlesBoundingRight.add(particleBoundingRight);
			
			physics.addSpring(springCenterLeft);
			physics.addSpring(springCenterRight);
			physics.addSpring(springBoundingLeft);
			physics.addSpring(springBoundingRight);

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
			skierAttractor = new AttractionBehavior(skier, 0.03f, -.011f);
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
		for(int i = 0; i < particlesLeft.size(); i++) {
			VerletParticle2D particle = particlesLeft.get(i);
			theG.curveVertex(particle.x, particle.y);
		}
		theG.endShape();
		
		theG.beginShape();
		theG.vertex(particlesRight.get(0).x, particlesRight.get(0).y);
		for(int i = 0; i < particlesRight.size(); i++) {
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
		
		theG.stroke(255,0,255);
		for(int i = 0; i < particlesCenter.size(); i++) {
			VerletParticle2D particleCenter = particlesCenter.get(i);
			VerletParticle2D particleRight = particlesRight.get(i);
			VerletParticle2D particleLeft = particlesLeft.get(i);
			theG.line(particleCenter.x, particleCenter.y, particleRight.x, particleRight.y);
			theG.line(particleCenter.x, particleCenter.y, particleLeft.x, particleLeft.y);
		}
		
	}
	
	void drawWithLaser(final PGraphics theG) {
		if( skierAttractor == null ) return;
		Vec2D pos = skierAttractor.getAttractor();
		
		theG.stroke(0,255,0);
		
		theG.beginShape();
		for(int i = 0; i < particlesCenter.size(); i++) {
			if( particlesLeft.get(i).x <= pos.x && 
				particlesRight.get(i).x >= pos.x &&
				particlesCenter.get(i).y + 0.1 >= pos.y &&
				particlesCenter.get(i).y - 0.1 <= pos.y 
			) {
				VerletParticle2D particleLeft = particlesLeft.get(i);
				theG.vertex(particleLeft.x, particleLeft.y);
				for (int j = i+1; j < i+railLength && j < particlesCenter.size(); j++) {
					theG.curveVertex(particlesLeft.get(j).x, particlesLeft.get(j).y);
				}
				break;
			}
		}
		theG.endShape();
		
		theG.beginShape();
		for(int i = 0; i < particlesCenter.size(); i++) {
			if( particlesLeft.get(i).x <= pos.x && 
				particlesRight.get(i).x >= pos.x &&
				particlesCenter.get(i).y + 0.1 >= pos.y &&
				particlesCenter.get(i).y - 0.1 <= pos.y 
				
			) {
				VerletParticle2D particleRight = particlesRight.get(i);
				theG.vertex(particleRight.x, particleRight.y);
				for (int j = i+1; j < i+railLength && j < particlesCenter.size(); j++) {
					theG.curveVertex(particlesRight.get(j).x, particlesRight.get(j).y);
				}
				break;
			}
		}
		theG.endShape();
		
	}
	
	private void generateBoundingOutlines(float distance) {
		pointsBoundingLeft.clear();
		pointsBoundingRight.clear();
		for(int i=0; i<pointsCenter.size(); i++) {
			float angleLeft = 0f, angleRight = 0f;
			if( i < pointsCenter.size() - 1) {
				angleLeft = Geometry.angle(pointsLeft.get(i), pointsLeft.get(i+1));
				angleRight = Geometry.angle(pointsRight.get(i), pointsRight.get(i+1));
			} else {
				angleLeft = Geometry.angle(pointsLeft.get(i), pointsLeft.get(i-1)) - 180f;
				angleRight = Geometry.angle(pointsRight.get(i), pointsRight.get(i-1)) - 180f;
			}
			PVector pointLeft  = Geometry.coordinates(pointsLeft.get(i).x, pointsLeft.get(i).y, distance, angleLeft + 90f);
			PVector pointRight = Geometry.coordinates(pointsRight.get(i).x, pointsRight.get(i).y, distance, angleRight - 90f);
			pointsBoundingLeft.add(pointLeft);
			pointsBoundingRight.add(pointRight);
		}
	}
}