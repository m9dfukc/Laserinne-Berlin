package com.laserinne.nearest.lasso;

import processing.core.PApplet;
import processing.core.PGraphics;

import com.laserinne.base.Skier;
import com.laserinne.util.Edge;

import toxi.geom.Vec2D;
import toxi.math.LinearInterpolation;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;

public class LassoPhysics {
	
	float strength;
	float totalLength;
	int numPoints;
	boolean bAlive;
	
	VerletPhysics2D physics;
	VerletParticle2D head, tail;
	VerletSpring2D spring;
	
	public LassoPhysics(VerletPhysics2D p, float l, int n, float s) {
		physics = p;
		totalLength = l;
	    numPoints = n;
	    strength = s;
	    
	    float len = totalLength / numPoints;
	    
	    bAlive = false;
	    
	    for(int i=0; i < numPoints; i++) {
	    	VerletParticle2D particle = new VerletParticle2D( 0, 0 );
	    	physics.addParticle(particle);

	        if(	i > 0 ) {
	        	VerletParticle2D previous = physics.particles.get(i-1);
	        	spring = new VerletSpring2D(particle,previous,len,strength);
	        	physics.addSpring(spring);
	        }
	    }
	    
	    head = physics.particles.get(0);
	    tail = physics.particles.get(numPoints-1);
	}
	
	public void updateSpring(float s, float l) {
		spring.setStrength(s);
		spring.setRestLength(l);
	}
	
	public void update(Edge<Skier> skiers) {
		if( skiers == null)	{
			bAlive = false;
			head.set(0f, -1f);
			tail.set(0f, -1f);
		} else {
			if( !bAlive ) {
				LinearInterpolation formAToB = new LinearInterpolation();
				float x1 = skiers.a.base().x;
				float y1 = skiers.a.base().y;
				float x2 = skiers.b.base().x;
				float y2 = skiers.b.base().y;
				float t  = 1.0f / (float)(numPoints-1);
				for(int i=0; i < numPoints; i++) {
					float xT = formAToB.interpolate(x1, x2, PApplet.map((float)i * t, 0.f, 1.f, 0.25f, 0.75f));
					float yT = formAToB.interpolate(y1, y2, (float)i * t);
					physics.particles.get(i).lock();
					physics.particles.get(i).set(new Vec2D(xT, yT));
					physics.particles.get(i).unlock();
				}
			} else {
				head.set(skiers.a.base().x, skiers.a.base().y);
				tail.set(skiers.b.base().x, skiers.b.base().y);
			}
			head.lock();
			tail.lock();
			bAlive = true;
		}
	}
	
	public void draw(final PGraphics theG) {
		if( bAlive ) {
			theG.noFill();
			theG.stroke(0, 255, 0);
			theG.beginShape();
			theG.vertex(physics.particles.get(0).x, physics.particles.get(0).y);
			for(int i = 1; i < physics.particles.size(); i++) {
				VerletParticle2D particle = physics.particles.get(i);
				theG.curveVertex(particle.x, particle.y);
			}
			theG.endShape();
		}
	}
	
}
