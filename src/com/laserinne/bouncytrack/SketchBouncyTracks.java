package com.laserinne.bouncytrack;

import java.util.ArrayList;

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
public class SketchBouncyTracks extends LaserinneSketch {
     
	private VerletPhysics2D _physics;
	private Skier _skier1;
	private Skier _skier2;
	private BouncyTrack _track1;
	private BouncyTrack _track2;
	
    public static void main(String args[]) {
        PApplet.main(new String[] { SketchBouncyTracks.class.getCanonicalName() });
    }

	@Override
	protected void postSetup() {
		_physics = new VerletPhysics2D();
        _physics.setWorldBounds(new Rect(-1f,-1f,2f,2f));
        
		_skier1 = _skier2 = null;

        _track1 = new BouncyTrack(_physics, .07f);
        _track2 = new BouncyTrack(_physics, .07f);
        
        Logger.set(LogLevel.PROCESS, false);
        Logger.set(LogLevel.DEBUG, false);
	}
	
	@Override 
	public void keyPressed() {
		if( key == 'g' ) {
			_physics.clear();
	        _track1 = new BouncyTrack(_physics, .07f);
	        _track2 = new BouncyTrack(_physics, .07f);
		}	
		
		super.keyPressed();
	}
	
	@Override
	protected void update(final float theDelta) {
		_physics.update();
		
		ArrayList<Skier> mySkiers = tracking().skiersConfident();
		
		_track1.updateSkier(_skier1);
		_track2.updateSkier(_skier2);
	}

	@Override
	protected void drawWithLaser(final Laser3D theLaser) {
		g.pushMatrix();
		g.translate(-0.35f, 0f);
		_track1.draw(g);
		g.popMatrix();
		
		g.pushMatrix();
		g.translate(0.35f, 0f);
		_track2.draw(g);
		g.popMatrix();
	}

	@Override
	protected void drawOnScreen() {
		g.pushMatrix();
		g.translate(-0.35f, 0f);
		_track1.drawDebug(g);
		g.popMatrix();
		
		g.pushMatrix();
		g.translate(0.35f, 0f);
		_track2.drawDebug(g);
		g.popMatrix();
		
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
	
	

}
