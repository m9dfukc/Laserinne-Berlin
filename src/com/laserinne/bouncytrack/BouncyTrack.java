package com.laserinne.bouncytrack;

import java.util.ArrayList;

import com.laserinne.base.Skier;
import com.laserinne.util.Geometry;
import com.laserinne.util.ToxiUtil;


import processing.core.PApplet;
import processing.core.PGraphics;
import toxi.geom.Vec2D;
import toxi.physics2d.ParticleString2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletConstrainedSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior;

class BouncyTrack extends Track {

	float strength;
	int railLength;
	
	ParticleString2D stringCenter;
	ParticleString2D stringLeft;
	ParticleString2D stringRight;
	
	ArrayList<VerletParticle2D> particlesCenter = new ArrayList<VerletParticle2D>();
	ArrayList<VerletParticle2D> particlesLeft = new ArrayList<VerletParticle2D>();
	ArrayList<VerletParticle2D> particlesRight = new ArrayList<VerletParticle2D>();
	ArrayList<VerletParticle2D> particlesBoundingLeft = new ArrayList<VerletParticle2D>();
	ArrayList<VerletParticle2D> particlesBoundingRight = new ArrayList<VerletParticle2D>();
	
	ArrayList<Vec2D> pointsCenter;
	ArrayList<Vec2D> pointsLeft;
	ArrayList<Vec2D> pointsRight;
	ArrayList<Vec2D> pointsBoundingLeft = new ArrayList<Vec2D>();
	ArrayList<Vec2D> pointsBoundingRight = new ArrayList<Vec2D>();
	
	private VerletPhysics2D _physics;
	private AttractionBehavior _skierAttractor;
	
	BouncyTrack(VerletPhysics2D physics, float xPos, float trackWidth) {
		this(physics, xPos, trackWidth, 0.004f);
	}
	
	BouncyTrack(VerletPhysics2D physics, float xPos, float trackWidth, float spring) {
		this(physics, xPos, trackWidth, spring, 200);
	}
			
	BouncyTrack(VerletPhysics2D physics, float xPos, float trackWidth, float spring, int pathResolution) {
		super(trackWidth, pathResolution);
		_physics = physics;
		
		pointsCenter = super.points;
		pointsLeft = super.pointsLeft;
		pointsRight = super.pointsRight;

		strength = spring;
		railLength = 20;
		
		generateBoundingOutlines(0.13f);
		
		for (int i = 0; i < pointsCenter.size(); i++) {
			
			VerletParticle2D particleCenter = new VerletParticle2D(pointsCenter.get(i));
			VerletParticle2D particleLeft = new VerletParticle2D(pointsLeft.get(i));
			VerletParticle2D particleRight = new VerletParticle2D(pointsRight.get(i));
			VerletParticle2D particleBoundingLeft = new VerletParticle2D(pointsBoundingLeft.get(i));
			VerletParticle2D particleBoundingRight = new VerletParticle2D(pointsBoundingRight.get(i));
		
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
		
		_skierAttractor = new AttractionBehavior(stringCenter.getHead(), 0.03f, -.011f);
		physics.addBehavior(_skierAttractor);
	}

	void updateSkier(Skier skier) {
		if( skier != null ) {
			Vec2D skierPosition = ToxiUtil.toVec2D(skier.base());
			_skierAttractor.setAttractor(skierPosition);
		} else {
			_skierAttractor.setAttractor(stringCenter.getHead());
		}
		_physics.update();
	}

	void drawDebug(final PGraphics theG) {
		
		theG.noFill();
		theG.stroke(255);
		
		theG.beginShape();
		theG.vertex(particlesLeft.get(0).x, particlesLeft.get(0).y);
		for(int i = 0; i < particlesCenter.size(); i++) {
			VerletParticle2D particle = particlesLeft.get(i);
			theG.curveVertex(particle.x, particle.y);
		}
		theG.endShape();
		
		theG.beginShape();
		theG.vertex(particlesRight.get(0).x, particlesRight.get(0).y);
		for(int i = 0; i < particlesCenter.size(); i++) {
			VerletParticle2D particle = particlesRight.get(i);
			theG.curveVertex(particle.x, particle.y);
		}
		theG.endShape();
		
		theG.stroke(90);
		for(int i = 0; i < particlesCenter.size(); i++) {
			VerletParticle2D particleLeft = particlesLeft.get(i);
			VerletParticle2D particleRight = particlesRight.get(i);
			VerletParticle2D particleBoundingLeft = particlesBoundingLeft.get(i);
			VerletParticle2D particleBoundingRight = particlesBoundingRight.get(i);
			theG.line(particleLeft.x, particleLeft.y, particleBoundingLeft.x, particleBoundingLeft.y);
			theG.line(particleRight.x, particleRight.y, particleBoundingRight.x, particleBoundingRight.y);
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
	
	void draw(final PGraphics theG) {
		if( _skierAttractor == null ) return;
		Vec2D pos = _skierAttractor.getAttractor();
		
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
			Vec2D pointLeft  = ToxiUtil.toVec2D(Geometry.coordinates(pointsLeft.get(i).x, pointsLeft.get(i).y, distance, angleLeft + 90f));
			Vec2D pointRight = ToxiUtil.toVec2D(Geometry.coordinates(pointsRight.get(i).x, pointsRight.get(i).y, distance, angleRight - 90f));
			pointsBoundingLeft.add(pointLeft);
			pointsBoundingRight.add(pointRight);
		}
	}
}