package com.laserinne.connecting;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;
import toxi.geom.mesh2d.Voronoi;

import com.laserinne.util.LaserinneSketch;
import com.laserinne.util.Skier;
import com.laserinne.util.ToxiUtil;

@SuppressWarnings("serial")
public class SketchConnect extends LaserinneSketch {

	private static final int DELAUNAY_ROOT_SIZE = 10000;

	public static void main(String[] args) {
		PApplet.main(new String[]{com.laserinne.connecting.SketchConnect.class.getCanonicalName()});
	}


	ArrayList<Edge<Vec2D>> _myEdges;

	@Override
	protected void postSetup() {
		

	}

	@Override
	protected void update() {

		_myEdges = new ArrayList<Edge<Vec2D>>();
		
		final Voronoi myVoronoi = new Voronoi(DELAUNAY_ROOT_SIZE);

		ArrayList<Skier> mySkiers = tracking().skiers();

		for(final Skier mySkier:mySkiers) {
			final Vec2D myPosition = ToxiUtil.toVec2D(mySkier.centroid());
			myPosition.x = PApplet.map(myPosition.x, -1, 1, 0, 1000);
			myPosition.y = PApplet.map(myPosition.y, -1, 1, 0, 1000);

			myVoronoi.addPoint(myPosition);
		}

		_myEdges = findUniqueEdges(myVoronoi.getTriangles());


		/* Two skiers make no triangles */
		if(mySkiers.size()  == 2) {  
			final Vec2D myPosA = ToxiUtil.toVec2D(mySkiers.get(0).centroid()); 
			final Vec2D myPosB = ToxiUtil.toVec2D(mySkiers.get(1).centroid()); 
			_myEdges.add(new Edge<Vec2D>(myPosA, myPosB));
		}
		
	}

	@Override
	protected void drawWithLaser() {
		for (Edge<Vec2D> myEdge : _myEdges) {
			stroke(255, 128);
			line(myEdge.a.x, myEdge.a.y, myEdge.b.x, myEdge.b.y);
		}
	}

	

	@Override
	protected void drawOnScreen() {


		for(Skier mySkier:tracking().skiers()) {
			mySkier.drawDebug(g);
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
	
	
	
	private ArrayList<Edge<Vec2D>> findUniqueEdges(final List<Triangle2D> theTriangles) {
		final ArrayList<Edge<Vec2D>> myEdges = new ArrayList<Edge<Vec2D>>();

		/* Find duplicate Edges */
		for(Triangle2D myTri:theTriangles) {
			if(abs(myTri.a.x) == DELAUNAY_ROOT_SIZE || abs(myTri.a.y) == DELAUNAY_ROOT_SIZE) {	
				continue;
			}

			//if(myTri.isClockwise()) myTri = myTri.flipVertexOrder();
			final ArrayList<Edge<Vec2D>> myTriangleEdges = new ArrayList<Edge<Vec2D>>();

			myTriangleEdges.add(new Edge<Vec2D>(myTri.a, myTri.b));
			myTriangleEdges.add(new Edge<Vec2D>(myTri.b, myTri.c));
			myTriangleEdges.add(new Edge<Vec2D>(myTri.c, myTri.a));

			for(Edge<Vec2D> myNewEdge:myTriangleEdges) {
				boolean myDidExist = false;

				for(Edge<Vec2D> myExistingEdge:myEdges) {
					if(	myNewEdge.a.equalsWithTolerance(myExistingEdge.b, 1/500.0f) 
							&&	myNewEdge.b.equalsWithTolerance(myExistingEdge.a, 1/500.0f)) {
						myDidExist = true;
					} else if(myNewEdge.b.equalsWithTolerance(myExistingEdge.b, 1/500.0f) 
							&& myNewEdge.a.equalsWithTolerance(myExistingEdge.a, 1/500.0f)) {
						myDidExist = true;
					} 
				}

				if(!myDidExist) {
					myEdges.add(myNewEdge);
				}
			}
		}
				
		
		/* Transform them all back */
		for(final Edge<Vec2D> myEdge: myEdges) {
			myEdge.a.x = map(myEdge.a.x, 0, 1000, -1, 1);
			myEdge.a.y = map(myEdge.a.y, 0, 1000, -1, 1);
			myEdge.b.x = map(myEdge.b.x, 0, 1000, -1, 1);
			myEdge.b.y = map(myEdge.b.y, 0, 1000, -1, 1);
		}
		
		
		return myEdges;
	}

}
