package com.laserinne.bouncytrack;

import java.util.ArrayList;

import com.laserinne.util.Geometry;
import com.laserinne.util.ToxiUtil;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import toxi.geom.Rect;
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
	
	ArrayList<Vec2D> pointsBoundingLeft = new ArrayList<Vec2D>();
	ArrayList<Vec2D> pointsBoundingRight = new ArrayList<Vec2D>();
	
	private VerletPhysics2D _physics;
	private AttractionBehavior _skierAttractor;
	
	BouncyTrack(float trackWidth) {
		this(trackWidth, 0.0004f);
	}
	
	BouncyTrack(float trackWidth, float spring) {
		this(trackWidth, spring, 200);
	}
			
	BouncyTrack(float trackWidth, float spring, int pathResolution) {
		super(trackWidth, pathResolution);
		
		_physics = new VerletPhysics2D();
        _physics.setWorldBounds(new Rect(-1f,-1f,2f,2f));
		
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
					strength
				);
			VerletConstrainedSpring2D springCenterRight = 
				new VerletConstrainedSpring2D (
					particleCenter, 
					particleRight, 
					PApplet.dist(particleCenter.x, particleCenter.y, particleRight.x, particleRight.y), 
					strength 
				);
			VerletConstrainedSpring2D springBoundingLeft = 
				new VerletConstrainedSpring2D (
					particleLeft, 
					particleBoundingLeft, 
					PApplet.dist(particleBoundingLeft.x, particleBoundingLeft.y, particleLeft.x, particleLeft.y), 
					strength
				);
			VerletConstrainedSpring2D springBoundingRight = 
				new VerletConstrainedSpring2D (
					particleRight, 
					particleBoundingRight,
					PApplet.dist(particleBoundingRight.x, particleBoundingRight.y, particleRight.x, particleRight.y), 
					strength
				);
			springCenterLeft.lockA(true);
			springCenterRight.lockA(true);
			
			particlesCenter.add(particleCenter);
			particlesLeft.add(particleLeft);
			particlesRight.add(particleRight);
			particlesBoundingLeft.add(particleBoundingLeft);
			particlesBoundingRight.add(particleBoundingRight);
			
			_physics.addSpring(springCenterLeft);
			_physics.addSpring(springCenterRight);
			_physics.addSpring(springBoundingLeft);
			_physics.addSpring(springBoundingRight);
		}
		stringCenter = new ParticleString2D(_physics, particlesCenter, strength);
		stringLeft = new ParticleString2D(_physics, particlesLeft, strength);
		stringRight = new ParticleString2D(_physics, particlesRight, strength);
		
		stringLeft.getHead().lock();
		stringLeft.getTail().lock();
		stringRight.getHead().lock();
		stringRight.getTail().lock();
		
		_skierAttractor = new AttractionBehavior(stringCenter.getHead(), 0.04f, -.011f);
		_physics.addBehavior(_skierAttractor);
	}
	
	void updateSkier(PVector skierPosition) {
		if( skierPosition != null ) {
			_skierAttractor.setAttractor(ToxiUtil.toVec2D(skierPosition));
		} else {
			_skierAttractor.setAttractor(stringCenter.getHead());
		}
	}
	
	void updateSpring(float value) {
		_skierAttractor.setStrength(value*-1.0f);
	}
	
	void update() {
		_physics.update();
	}
	
	void clear() {
		_physics.clear();
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
		/*
		theG.stroke(90);
		for(int i = 0; i < particlesCenter.size(); i++) {
			VerletParticle2D particleLeft = particlesLeft.get(i);
			VerletParticle2D particleRight = particlesRight.get(i);
			VerletParticle2D particleBoundingLeft = particlesBoundingLeft.get(i);
			VerletParticle2D particleBoundingRight = particlesBoundingRight.get(i);
			theG.line(particleLeft.x, particleLeft.y, particleBoundingLeft.x, particleBoundingLeft.y);
			theG.line(particleRight.x, particleRight.y, particleBoundingRight.x, particleBoundingRight.y);
		}
		*/
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
				angleLeft = Geometry.angle(ToxiUtil.toPVector(pointsLeft.get(i)), ToxiUtil.toPVector(pointsLeft.get(i+1)));
				angleRight = Geometry.angle(ToxiUtil.toPVector(pointsRight.get(i)), ToxiUtil.toPVector(pointsRight.get(i+1)));
			} else {
				angleLeft = Geometry.angle(ToxiUtil.toPVector(pointsLeft.get(i)), ToxiUtil.toPVector(pointsLeft.get(i-1))) - 180f;
				angleRight = Geometry.angle(ToxiUtil.toPVector(pointsRight.get(i)), ToxiUtil.toPVector(pointsRight.get(i-1))) - 180f;
			}
			Vec2D pointLeft  = ToxiUtil.toVec2D(Geometry.coordinates(pointsLeft.get(i).x, pointsLeft.get(i).y, distance, angleLeft + 90f));
			Vec2D pointRight = ToxiUtil.toVec2D(Geometry.coordinates(pointsRight.get(i).x, pointsRight.get(i).y, distance, angleRight - 90f));
			pointsBoundingLeft.add(pointLeft);
			pointsBoundingRight.add(pointRight);
		}
	}
}