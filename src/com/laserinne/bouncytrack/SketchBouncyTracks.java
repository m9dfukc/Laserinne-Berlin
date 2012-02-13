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
	private BouncyTrack _rail1;
	private BouncyTrack _rail2;
	
    public static void main(String args[]) {
        PApplet.main(new String[] { SketchBouncyTracks.class.getCanonicalName() });
    }

	@Override
	protected void postSetup() {
		_physics = new VerletPhysics2D();
        _physics.setWorldBounds(new Rect(-1f,-1f,2f,2f));
        
		_skier1 = _skier2 = null;

        _rail1 = new BouncyTrack(_physics, 0.005f, -0.35f, .07f, 200);
        _rail2 = new BouncyTrack(_physics, 0.005f,  0.35f, .07f, 200);
        
        Logger.set(LogLevel.PROCESS, false);
        Logger.set(LogLevel.DEBUG, false);
	}
	
	@Override 
	public void keyPressed() {
		if( key == 'g' ) {
			_physics.clear();
			_rail1 = new BouncyTrack(_physics, 0.005f, -0.35f, .07f, 200);
	        _rail2 = new BouncyTrack(_physics, 0.005f,  0.35f, .07f, 200);
		}	
		
		super.keyPressed();
	}
	
	@Override
	public void mouseDragged() {
		
	}
	
	@Override
	protected void update(final float theDelta) {
		_physics.update();
		
		ArrayList<Skier> mySkiers = tracking().skiersConfident();
		
		_rail1.updateSkier(_skier1);
		_rail2.updateSkier(_skier2);
	}

	@Override
	protected void drawWithLaser(final Laser3D theLaser) {
		_rail1.draw(g);
		_rail2.draw(g);
	}

	@Override
	protected void drawOnScreen() {
		_rail1.drawDebug(g);
		_rail2.drawDebug(g);
		
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
