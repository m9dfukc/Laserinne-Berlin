package com.laserinne.tron;

import java.util.ArrayList;
import java.util.List;

import processing.core.PGraphics;
import processing.core.PVector;

import com.laserinne.decoration.Decorator;
import com.laserinne.util.Geometry;

import de.looksgood.ani.Ani;

public class TrailDecorator extends Decorator{

	final SkierTrail _myTrail;

	private Ani _myConstructAnimation;
	private float _myProgress = 0;

	public TrailDecorator(SkierTrail theTrail) {
		this.state(State.GENESIS);
		_myTrail = theTrail;
		_myConstructAnimation = new Ani(this, 1.5f, "_myProgress", 1);
	}

	@Override
	public void draw(PGraphics theG) {

		theG.beginShape();
		List<PVector> mySegments = _myTrail.segments();
		theG.curveVertex(_myTrail.skier().centroid().x , _myTrail.skier().centroid().y );
		theG.curveVertex(_myTrail.skier().centroid().x , _myTrail.skier().centroid().y );

		int myMax = (int) Math.min(mySegments.size() - 1, SkierTrail.MAX_NUMBER - 1);
		
		for(int i = 0; i < myMax; i++) {
			PVector mySegment = mySegments.get(i);
			theG.curveVertex(mySegment.x, mySegment.y);	
		}
		
		if(mySegments.size() > myMax && mySegments.size() > 1) {
			PVector myPrevSegment = mySegments.get(myMax - 1);

			PVector mySegment = mySegments.get(myMax);
			
			PVector myTail = Geometry.lerp(myPrevSegment, mySegment, 1-_myTrail.nextSegmentProgress());
			theG.curveVertex(myTail.x, myTail.y);

			theG.curveVertex(myTail.x, myTail.y);

		}
	

		theG.endShape();

	}



	@Override
	public void update() {
		if(_myTrail.isDead() && (!state().equals(State.FINISHED) )){
			this.state(State.APOCALYPSE);

			if(!state().equals(State.APOCALYPSE)) {
				_myConstructAnimation.setBegin(_myProgress);
				_myConstructAnimation.setEnd(0);
				_myConstructAnimation.start();
			}


		}


		if(state().equals(State.APOCALYPSE) && _myConstructAnimation.isEnded()) {
			state(State.FINISHED);
		}


		if(_myTrail.collides()) {

		}
	}

}
