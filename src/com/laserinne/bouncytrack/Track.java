package com.laserinne.bouncytrack;

import java.util.ArrayList;

import com.laserinne.util.Geometry;
import com.laserinne.util.ToxiUtil;

import processing.core.PApplet;
import processing.core.PGraphics;
import toxi.geom.Vec2D;


public class Track extends Path {
	
	float width;
	
	public ArrayList<Vec2D> pointsLeft = new ArrayList<Vec2D>();
	public ArrayList<Vec2D> pointsRight = new ArrayList<Vec2D>();
	
	public Track(float trackWidth, int pathResolution) {
		super(pathResolution);
		
		width = PApplet.constrain(trackWidth, 0.001f, 0.30f);
		generateOutlines();
	}
	
	void generateOutlines() {
		for(int i=0; i<pointsCenter.size(); i++) {
			float angle;
			if( i < pointsCenter.size() - 1) {
				angle = Geometry.angle(ToxiUtil.toPVector(pointsCenter.get(i)), ToxiUtil.toPVector(pointsCenter.get(i+1)));
			} else {
				angle = Geometry.angle(ToxiUtil.toPVector(pointsCenter.get(i)), ToxiUtil.toPVector(pointsCenter.get(i-1))) -180f;
			}
			Vec2D pointLeft  = ToxiUtil.toVec2D(Geometry.coordinates(pointsCenter.get(i).x, pointsCenter.get(i).y, width, angle + 90f));
			Vec2D pointRight = ToxiUtil.toVec2D(Geometry.coordinates(pointsCenter.get(i).x, pointsCenter.get(i).y, width, angle - 90f));
			pointsLeft.add(pointLeft);
			pointsRight.add(pointRight);
		}
	}
	
	ArrayList<Vec2D> getOutlineLeft() {
		return pointsLeft;
	}
	
	ArrayList<Vec2D> getOutlineRight() {
		return pointsRight;
	}
	
	void draw(final PGraphics theG) {
		theG.stroke(0, 255, 0);
		_draw(theG);
	}
	
	void drawDebug(final PGraphics theG) {
		theG.stroke(255, 0, 0);
		_draw(theG);
	}
	
	private void _draw(final PGraphics theG) {
		theG.noFill();
		
		theG.beginShape();
		theG.vertex(pointsLeft.get(0).x, pointsLeft.get(0).y);
		for(int i = 1; i < pointsLeft.size(); i++) {
			theG.curveVertex(pointsLeft.get(i).x, pointsLeft.get(i).y);
		}
		theG.endShape();
		
		theG.beginShape();
		theG.vertex(pointsRight.get(0).x, pointsRight.get(0).y);
		for(int i = 1; i < pointsRight.size(); i++) {
			theG.curveVertex(pointsRight.get(i).x, pointsRight.get(i).y);
		}
		theG.endShape();
	}
}
