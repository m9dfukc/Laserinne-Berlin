package com.laserinne.bouncytrack;

import java.awt.event.KeyEvent;

import laserschein.Logger;
import laserschein.Logger.LogLevel;
import processing.core.PApplet;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletPhysics2D;

import com.laserinne.util.LaserinneSketch;
import com.laserinne.util.Skier;

@SuppressWarnings("serial")
public class BouncyTrack extends LaserinneSketch {
     
	private Track track; 
	VerletPhysics2D physics;
	private RailPhysics rail;
	
    public static void main(String args[]) {
        PApplet.main(new String[] { BouncyTrack.class.getCanonicalName() });
    }

	@Override
	protected void postSetup() {
		this.background(255);
        track = new Track(.07f);
        physics = new VerletPhysics2D();
        physics.setWorldBounds(new Rect(-1f,-1f,2f,2f));
        
        rail = new RailPhysics(physics, track.getPath(), track.getOutlineLeft(), track.getOutlineRight(), 0.005f);
        
        Logger.set(LogLevel.PROCESS, false);
        Logger.set(LogLevel.DEBUG, false);
	}
	
	@Override 
	public void keyPressed() {
		if( key == 'g' ) {
			track.generate();
		}	
	}
	
	@Override
	public void mouseClicked() {
	}
	
	@Override
	public void mouseDragged() {
		rail.updateSkier(new Vec2D(mX, mY));
	}
	
	@Override
	public void mousePressed() {
		rail.onNewSkier(new Vec2D(mX, mY));
	}

	@Override
	public void mouseReleased() {
	  rail.onDeadSkier();
	}


	@Override
	protected void update() {
		
	}

	@Override
	protected void drawWithLaser() {
		//track.drawWithLaser(g);
	}

	@Override
	protected void drawOnScreen() {
		physics.update();
		rail.drawOnScreen(g);
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
