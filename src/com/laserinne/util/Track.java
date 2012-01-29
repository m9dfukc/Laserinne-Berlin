package com.laserinne.util;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;


public class Track extends Path {
	
	float trackWidth;
	ArrayList<PVector> pointsLeft = new ArrayList<PVector>();
	ArrayList<PVector> pointsRight = new ArrayList<PVector>();
	
	public Track(PApplet p, int w, int h) {
		super(p, w, h);
		trackWidth = 30f;
		generateOutline();
	}
	
	public void generateOutline() {
		for(int i=0; i<points.size(); i++) {
			float angle;
			if( i < points.size() - 1) {
				angle = Geometry.angle(points.get(i), points.get(i+1));
				
			} else {
				angle = Geometry.angle(points.get(i), points.get(i-1)) - 180f;
			}
			float distance = Geometry.transform((float)i, trackWidth, angle);
			PApplet.println(angle);
			PVector pointLeft  = Geometry.coordinates(points.get(i).x, points.get(i).y, distance, angle + 90f);
			PVector pointRight = Geometry.coordinates(points.get(i).x, points.get(i).y, distance, angle - 90f);
			pointsLeft.add(pointLeft);
			pointsRight.add(pointRight);
		}
	}
	
	public void setTrackWidth(float w) {
		if( w > 0 && w < width / 2 ) trackWidth = w;
	}
	
	public ArrayList<PVector> getOutlineLeft() {
		return pointsLeft;
	}
	
	public ArrayList<PVector> getOutlineRight() {
		return pointsRight;
	}
	
	public void draw() {
		super.draw();
		parent.stroke(0, 255, 0);
		parent.noFill();
		
		parent.beginShape();
		parent.vertex(pointsLeft.get(0).x, pointsLeft.get(0).y);
		for(int i = 1; i < pointsLeft.size(); i++) {
			parent.curveVertex(pointsLeft.get(i).x, pointsLeft.get(i).y);
		}
		parent.endShape();
		
		parent.beginShape();
		parent.vertex(pointsRight.get(0).x, pointsRight.get(0).y);
		for(int i = 1; i < pointsRight.size(); i++) {
			parent.curveVertex(pointsRight.get(i).x, pointsRight.get(i).y);
		}
		/*
		parent.vertex(pointsRight.get(pointsRight.size()-1).x, pointsRight.get(pointsRight.size()-1).y);
		for(int i = pointsRight.size()-1; i==0; i--) {
			parent.curveVertex(pointsRight.get(i).x, pointsRight.get(i).y);
		}
		*/
		parent.endShape();
	}
}
