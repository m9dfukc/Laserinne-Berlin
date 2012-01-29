package com.laserinne.util;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;


public class Track extends Path {
	
	float _trackWidth;
	ArrayList<PVector> _pointsLeft = new ArrayList<PVector>();
	ArrayList<PVector> _pointsRight = new ArrayList<PVector>();
	
	public Track(float trackWidth) {
		super(10);
		_trackWidth = trackWidth;
		generateOutline();
	}
	
	public void generateOutline() {
		for(int i=0; i<_points.size(); i++) {
			float angle;
			if( i < _points.size() - 1) {
				angle = Geometry.angle(_points.get(i), _points.get(i+1));
				
			} else {
				angle = Geometry.angle(_points.get(i), _points.get(i-1)) - 180f;
			}
			float distance = Geometry.transform((float)i, _trackWidth, angle);
			PApplet.println(angle);
			PVector pointLeft  = Geometry.coordinates(_points.get(i).x, _points.get(i).y, distance, angle + 90f);
			PVector pointRight = Geometry.coordinates(_points.get(i).x, _points.get(i).y, distance, angle - 90f);
			_pointsLeft.add(pointLeft);
			_pointsRight.add(pointRight);
		}
	}
	
	public void setTrackWidth(float w) {
		if( w > 0 && w < 1 ) _trackWidth = w;
	}
	
	public ArrayList<PVector> getOutlineLeft() {
		return _pointsLeft;
	}
	
	public ArrayList<PVector> getOutlineRight() {
		return _pointsRight;
	}
	
	public void draw(final PGraphics theG) {
		super.draw(theG);
		theG.stroke(0, 255, 0);
		theG.noFill();
		
		theG.beginShape();
		theG.vertex(_pointsLeft.get(0).x, _pointsLeft.get(0).y);
		for(int i = 1; i < _pointsLeft.size(); i++) {
			theG.curveVertex(_pointsLeft.get(i).x, _pointsLeft.get(i).y);
		}
		theG.endShape();
		
		theG.beginShape();
		theG.vertex(_pointsRight.get(0).x, _pointsRight.get(0).y);
		for(int i = 1; i < _pointsRight.size(); i++) {
			theG.curveVertex(_pointsRight.get(i).x, _pointsRight.get(i).y);
		}
		theG.endShape();
	}
}
