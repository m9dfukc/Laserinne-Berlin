package com.laserinne.bouncytrack;

import java.util.ArrayList;
import java.util.Collections;

import laserschein.Laser3D;
import laserschein.Logger;
import laserschein.Logger.LogLevel;
import processing.core.PApplet;
import processing.core.PVector;

import com.laserinne.base.LaserinneSketch;
import com.laserinne.base.Skier;
import com.laserinne.util.Edge;
import com.laserinne.util.SkierYComparator;

@SuppressWarnings("serial")
public class SketchBouncyTracks extends LaserinneSketch {
     
	private Edge<Skier> _myEdge;
	
	private Skier _skier1;
	private Skier _skier2;
	
	private BouncyTrack _track1;
	private BouncyTrack _track2;
	
	private PVector _track1Translate;
	private PVector _track2Translate;
	
	private float _trackWidth;
	
    public static void main(String args[]) {
        PApplet.main(new String[] { SketchBouncyTracks.class.getCanonicalName() });
    }

	@Override
	protected void postSetup() {
		_trackWidth = 0.07f;
		
		_myEdge = null;
		_skier1 = _skier2 = null;

        _track1 = new BouncyTrack(_trackWidth);
        _track2 = new BouncyTrack(_trackWidth);
        
        _track1Translate = new PVector(-0.35f, 0.1f);
        _track2Translate = new PVector( 0.35f, 0.1f);
        
        Logger.set(LogLevel.PROCESS, false);
        Logger.set(LogLevel.DEBUG, false);
	}
	
	@Override 
	public void keyPressed() {
		if( key == 'g' ) {
			_track1.clear();
			_track2.clear();
	        _track1 = new BouncyTrack(_trackWidth);
	        _track2 = new BouncyTrack(_trackWidth);
		}	
		
		super.keyPressed();
	}
	
	@Override
	protected void update(final float theDelta) {
		ArrayList<Skier> mySkiers = tracking().skiersConfident();
		Collections.sort(mySkiers, new SkierYComparator()); 	// Sort by their y positions
		
		/* Check if edge is still valid */
		if( _myEdge != null && (_myEdge.a.isDead() || _myEdge.b.isDead() ) ) {
			Logger.printInfo("Destroyed edge between Skier " + _myEdge.a.id() + " and " + _myEdge.a.id());
			_myEdge = null; 
			_skier1 = null; 
			_skier2 = null;
		}
		
		if( _myEdge == null && mySkiers.size() > 1) {    // We need two to party
			for(int i=0; i < mySkiers.size(); i++) {
				if( _track1Translate.x - _trackWidth / 2f < mySkiers.get(i).base().x && 
					_track1Translate.x + _trackWidth / 2f > mySkiers.get(i).base().x &&
					_track1Translate.y - 1f - _trackWidth / 2f < mySkiers.get(i).base().y &&
					_track1Translate.y - 1f + _trackWidth / 2f > mySkiers.get(i).base().y
				) {
					Logger.printInfo("Skier " + mySkiers.get(i) + " at Left Start Position" );
					_skier1 = mySkiers.get(i);
					break;
				}
			}
			for(int i=0; i < mySkiers.size(); i++) {
				if( _track2Translate.x - _trackWidth / 2f < mySkiers.get(i).base().x && 
					_track2Translate.x + _trackWidth / 2f > mySkiers.get(i).base().x &&
					_track2Translate.y - 1f - _trackWidth / 2f < mySkiers.get(i).base().y &&
					_track2Translate.y - 1f + _trackWidth / 2f > mySkiers.get(i).base().y
				) {
					Logger.printInfo("Skier " + mySkiers.get(i) + " at Right Start Position" );
					_skier2 = mySkiers.get(i);
					break;
				}
			}
			if( _skier1 != null && _skier2 != null ) {
				_myEdge = new Edge<Skier>(_skier1, _skier2);
				Logger.printInfo("Create edge between Skier " + _skier1.id() + " and " + _skier2.id());
			}
		}
		
		if( _myEdge != null ) {
			PVector skier1Pos = PVector.sub(_myEdge.a.base(), _track1Translate);
			PVector skier2Pos = PVector.sub(_myEdge.b.base(), _track2Translate);
			_track1.updateSkier(skier1Pos);
			_track2.updateSkier(skier2Pos);
		} else {
			_track1.updateSkier(null);
			_track2.updateSkier(null);
		}
			
		_track1.update();
		_track2.update();
	}

	@Override
	protected void drawWithLaser(final Laser3D theLaser) {
		g.pushMatrix();
		g.translate(_track1Translate.x, _track1Translate.y);
		_track1.draw(g);
		g.popMatrix();
		
		g.pushMatrix();
		g.translate(_track2Translate.x, _track2Translate.y);
		_track2.draw(g);
		g.popMatrix();
	}

	@Override
	protected void drawOnScreen() {
		g.pushMatrix();
		g.translate(_track1Translate.x, _track1Translate.y);
		_track1.drawDebug(g);
		g.popMatrix();
		
		g.pushMatrix();
		g.translate(_track2Translate.x, _track2Translate.y);
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
