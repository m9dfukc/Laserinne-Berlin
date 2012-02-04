package com.laserinne.connecting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;

import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;
import toxi.geom.mesh2d.Voronoi;

import com.laserinne.util.LaserinneSketch;
import com.laserinne.util.Skier;
import com.laserinne.util.ToxiUtil;

import de.looksgood.ani.Ani;

@SuppressWarnings("serial")
public class SketchConnect extends LaserinneSketch {

	private static final int DELAUNAY_ROOT_SIZE = 4000;
	private static final float ALMOST = 1/4000.0f;

	private ArrayList<AnimatedSkierEdge> _mySkierEdges;

	public static void main(String[] args) {
		PApplet.main(new String[]{com.laserinne.connecting.SketchConnect.class.getCanonicalName()});
	}



	@Override
	protected void postSetup() {
		_mySkierEdges = new ArrayList<AnimatedSkierEdge>();
		Ani.init(this);

	}

	@Override
	protected void update() {

		ArrayList<Edge<Vec2D>> myEdges = new ArrayList<Edge<Vec2D>>();
		
		final Voronoi myVoronoi = new Voronoi(DELAUNAY_ROOT_SIZE);

		ArrayList<Skier> mySkiers = tracking().skiersConfident();

		for(final Skier mySkier:mySkiers) {
			final Vec2D myPosition = ToxiUtil.toVec2D(mySkier.base());
			myPosition.x = map(myPosition.x, -1, 1, 0, 1000);
			myPosition.y = map(myPosition.y, -1, 1, 0, 1000);

			myVoronoi.addPoint(myPosition);
		}

		myEdges = findUniqueEdges(myVoronoi.getTriangles());

		/* Two skiers make no triangles */
		if(mySkiers.size()  == 2) {  
			final Vec2D myPosA = ToxiUtil.toVec2D(mySkiers.get(0).centroid()); 
			final Vec2D myPosB = ToxiUtil.toVec2D(mySkiers.get(1).centroid()); 
			myEdges.add(new Edge<Vec2D>(myPosA, myPosB));
		}
		
		ArrayList<AnimatedSkierEdge> myNewEdges = assignSkiersToEdges(mySkiers, myEdges);
		
		matchWithExistingEdges(myNewEdges);
		purgeDeadAndUpdateEdges();
	}
	
	
	private void purgeDeadAndUpdateEdges() {
		/* Purge */
		Iterator<AnimatedSkierEdge> myIterator = _mySkierEdges.iterator();

		while(myIterator.hasNext()) {
			final AnimatedSkierEdge myEdge = myIterator.next();
			
			if(myEdge.isDead()) {
				myIterator.remove();
			} else {
				myEdge.update();
			}
		}
	}

	
	private void matchWithExistingEdges(ArrayList<AnimatedSkierEdge> theEdges) {

		for(final AnimatedSkierEdge myExistingEdge:_mySkierEdges) {
			
			AnimatedSkierEdge myMatch = null;
			
			Iterator<AnimatedSkierEdge> myIterator = theEdges.iterator();
		
			while(myIterator.hasNext()) {
				AnimatedSkierEdge myNewEdge = myIterator.next();
				
				if(myExistingEdge.equals(myNewEdge)) {
					myMatch = myNewEdge;
					myIterator.remove();
					break;
				}
			}
			
			
			if(myMatch != null) {
				myExistingEdge.activate();
			} else {
				myExistingEdge.deactivate();
			}
		}
		
		
		/* Handle new edges that are left over */
		for(final AnimatedSkierEdge myNewEdge:theEdges) {
			myNewEdge.activate();
			_mySkierEdges.add(myNewEdge);
			
		}
		
		
		
	}



	private ArrayList<AnimatedSkierEdge> assignSkiersToEdges(ArrayList<Skier> theSkiers, ArrayList<Edge<Vec2D>> theEdges) {
		final ArrayList<AnimatedSkierEdge> mySkierEdges = new ArrayList<AnimatedSkierEdge>();
		
		for(final Edge<Vec2D> myEdge:theEdges) {
			final Vec2D myA = myEdge.a;
			final Vec2D myB = myEdge.b;
			
			Skier mySkierA = null;
			Skier mySkierB = null;
			
			for(final Skier mySkier:theSkiers) {
				if(ToxiUtil.almost(mySkier.base(), myA, ALMOST )) {
					mySkierA = mySkier;
				}
				
				if(ToxiUtil.almost(mySkier.base(), myB, ALMOST )) {
					mySkierB = mySkier;
				}
				
				if(mySkierA != null && mySkierB != null) {
					break;
				}
			}
			
			
			if(mySkierA != null && mySkierB != null) {
				AnimatedSkierEdge myAnimation;
				
				if(mySkierA.id() < mySkierB.id()) {
					myAnimation = new AnimatedSkierEdge(mySkierA, mySkierB);
				} else {
					myAnimation = new AnimatedSkierEdge(mySkierB, mySkierA);
				}
				
				mySkierEdges.add(myAnimation);	
			}
		}
		
		return mySkierEdges;
	}



	@Override
	protected void drawWithLaser() {
		for(final AnimatedSkierEdge myEdge:_mySkierEdges) {
			myEdge.draw(g);
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
			if(abs(myTri.a.x) == DELAUNAY_ROOT_SIZE || abs(myTri.a.y) == DELAUNAY_ROOT_SIZE) {	// kill the root
				continue;
			}

			//if(myTri.isClockwise()) myTri = myTri.flipVertexOrder();
			final ArrayList<Edge<Vec2D>> myTriangleEdges = new ArrayList<Edge<Vec2D>>();

			myTriangleEdges.add(new Edge<Vec2D>(myTri.a.copy(), myTri.b.copy()));
			myTriangleEdges.add(new Edge<Vec2D>(myTri.b.copy(), myTri.c.copy()));
			myTriangleEdges.add(new Edge<Vec2D>(myTri.c.copy(), myTri.a.copy()));

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
