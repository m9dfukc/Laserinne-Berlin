package com.laserinne.connecting;

import java.util.ArrayList;

import processing.core.PApplet;

import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;
import toxi.geom.mesh2d.Voronoi;

import com.laserinne.util.LaserinneSketch;
import com.laserinne.util.Skier;
import com.laserinne.util.ToxiUtil;

@SuppressWarnings("serial")
public class SketchConnect extends LaserinneSketch {

	public static void main(String[] args) {
		PApplet.main(new String[]{com.laserinne.connecting.SketchConnect.class.getCanonicalName()});
	}
	
	
	Voronoi _myVoronoi;
	
	@Override
	protected void postSetup() {
		
	}

	@Override
	protected void update() {
		
		_myVoronoi =  new Voronoi(3);
		
		ArrayList<Skier> mySkiers = tracking().skiers();
		
		for(final Skier mySkier:mySkiers) {
			final Vec2D myPosition = ToxiUtil.toVec2D(mySkier.base());
			
			_myVoronoi.addPoint(myPosition);
		}
	}

	@Override
	protected void drawWithLaser() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void drawOnScreen() {
		
		stroke(255);
		
		for(Triangle2D myTri:_myVoronoi.getTriangles()) {
			beginShape();
			vertex(myTri.a.x, myTri.a.y);
			vertex(myTri.b.x, myTri.b.y);
			vertex(myTri.c.x, myTri.c.y);
			endShape();
		}
		
	}

	@Override
	protected void onNewSkier(Skier theSkier) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDeadSkier(Skier theSkier) {
		// TODO Auto-generated method stub

	}

}
