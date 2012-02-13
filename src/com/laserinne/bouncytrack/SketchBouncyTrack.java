package com.laserinne.bouncytrack;

import laserschein.Laser3D;
import laserschein.Logger;
import laserschein.Logger.LogLevel;
import processing.core.PApplet;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletPhysics2D;

import com.laserinne.base.LaserinneSketch;
import com.laserinne.base.Skier;

@SuppressWarnings("serial")
public class SketchBouncyTrack extends LaserinneSketch {
     
	private VerletPhysics2D physics;
	private Track track1; 
	private Track track2;
	private RailPhysics rail1;
	private RailPhysics rail2;
	
    public static void main(String args[]) {
        PApplet.main(new String[] { SketchBouncyTrack.class.getCanonicalName() });
    }

	@Override
	protected void postSetup() {
		this.background(255);
		track1 = new Track(-0.25f, .07f, 200);
		track2 = new Track(0.25f, .07f, 200);
		
        physics = new VerletPhysics2D();
        physics.setWorldBounds(new Rect(-1f,-1f,2f,2f));
        
        rail1 = new RailPhysics(physics, track1.getPath(), track1.getOutlineLeft(), track1.getOutlineRight(), 0.005f);
        rail2 = new RailPhysics(physics, track2.getPath(), track2.getOutlineLeft(), track2.getOutlineRight(), 0.005f);
        
        Logger.set(LogLevel.PROCESS, false);
        Logger.set(LogLevel.DEBUG, false);
	}
	
	@Override 
	public void keyPressed() {
		if( key == 'g' ) {
			physics.clear();
			track1.generate();
			rail1 = new RailPhysics(physics, track1.getPath(), track1.getOutlineLeft(), track1.getOutlineRight(), 0.005f);
			track2.generate();
			rail2 = new RailPhysics(physics, track2.getPath(), track2.getOutlineLeft(), track2.getOutlineRight(), 0.005f);
		}	
		
		super.keyPressed();
	}
	
	@Override
	public void mouseClicked() {
	}
	
	@Override
	public void mouseDragged() {
		rail1.updateSkier(new Vec2D(mX, mY));
		rail2.updateSkier(new Vec2D(mX, mY));
	}
	
	@Override
	public void mousePressed() {
		rail1.onNewSkier(new Vec2D(mX, mY));
		rail2.onNewSkier(new Vec2D(mX, mY));
	}

	@Override
	public void mouseReleased() {
		rail1.onDeadSkier();
		rail2.onDeadSkier();
	}


	@Override
	protected void update(final float theDelta) {
		physics.update();
	}

	@Override
	protected void drawWithLaser(final Laser3D theLaser) {
		rail1.drawWithLaser(g);
		rail2.drawWithLaser(g);
	}

	@Override
	protected void drawOnScreen() {
		rail1.drawOnScreen(g);
		rail2.drawOnScreen(g);
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
