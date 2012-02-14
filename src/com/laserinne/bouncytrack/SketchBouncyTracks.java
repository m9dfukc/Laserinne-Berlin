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

import controlP5.Controller;

@SuppressWarnings("serial")
public class SketchBouncyTracks extends LaserinneSketch {
     
	private Edge<Skier> _myEdge;
	
	private Skier _skier1;
	private Skier _skier2;
	
	private BouncyTrack _track1;
	private BouncyTrack _track2;
	
	private PVector _track1Translate;
	private PVector _track2Translate;
	
	private Controller _trackWithCtrl;
	private Controller _labelCtrl;
	private Controller _spring1Ctrl;
	private Controller _spring2Ctrl;
	
	private float _trackWidth;
	private float _startLineCorrection = -0.015f;
	
    public static void main(String args[]) {
        PApplet.main(new String[] { SketchBouncyTracks.class.getCanonicalName() });
    }

	@Override
	protected void postSetup() {
		_labelCtrl = _controlP5.addTextlabel("restart", "use r to resart", 10, 10);
        _labelCtrl.moveTo(_controlWindow);
        _trackWithCtrl = _controlP5.addSlider("trackWith", 0.001f, 0.1f, 0.07f, 10, 30, 140, 15);
        _trackWithCtrl.setDecimalPrecision(4);
        _trackWithCtrl.moveTo(_controlWindow);
        _spring1Ctrl = _controlP5.addSlider("spring track1", 0.005f, 0.025f, 0.019f, 10, 50, 140, 15);
        _spring1Ctrl.setDecimalPrecision(4);
        _spring1Ctrl.moveTo(_controlWindow);
        _spring2Ctrl = _controlP5.addSlider("spring track2", 0.005f, 0.025f, 0.019f, 10, 70, 140, 15);
        _spring2Ctrl.setDecimalPrecision(4);
        _spring2Ctrl.moveTo(_controlWindow);
        
		_trackWidth = _trackWithCtrl.value();
		
		_track1 = _track2 = null;
		
        generateTrack();	
	}
	
	@Override 
	public void keyPressed() {
		if( key == 'r' ) {
			generateTrack();
		}	
		
		super.keyPressed();
	}
	
	public void generateTrack() {
		_myEdge = null;
		_skier1 = _skier2 = null;
		
        if(_track1 != null) _track1.clear();
		if(_track2 != null) _track2.clear();

        _track1 = new BouncyTrack(_trackWidth);
        _track2 = new BouncyTrack(_trackWidth);
        
        _track1Translate = new PVector(-0.35f, 0.1f);
        _track2Translate = new PVector( 0.35f, 0.1f);
        
        Logger.set(LogLevel.PROCESS, false);
        Logger.set(LogLevel.DEBUG, false);
	}
	
	@Override
	protected void update(final float theDelta) {
		/* I know I know, this is some dirty shit - but works for now! */
		if( _trackWithCtrl.value() != _trackWidth ) {
			_trackWidth = _trackWithCtrl.value();
			generateTrack();
		}
		_track1.updateSpring(_spring1Ctrl.value());
		_track2.updateSpring(_spring2Ctrl.value());
		
		ArrayList<Skier> mySkiers = tracking().skiersConfident();
		Collections.sort(mySkiers, new SkierYComparator()); 	// Sort by their y positions
		
		/* Check if skiers are still valid */
		if( _skier1 != null && _skier1.isDead() ) {
			Logger.printInfo("Skier 1 with id " + _skier1.id() + " released from track!");
			_skier1 = null; 
		}
		if( _skier2 != null && _skier2.isDead() ) {
			Logger.printInfo("Skier 2 with id " + _skier2.id() + " released from track!");
			_skier2 = null; 
		}
		
		if( _skier1 == null && mySkiers.size() > 0) {    // We need two to party
			for(int i=0; i < mySkiers.size(); i++) {
				if( _track1Translate.x - _trackWidth * 2f < mySkiers.get(i).base().x && 
					_track1Translate.x + _trackWidth * 2f > mySkiers.get(i).base().x &&
					_track1Translate.y - 1f + _startLineCorrection < mySkiers.get(i).base().y &&
					_track1Translate.y - 1f + _trackWidth * 2f > mySkiers.get(i).base().y
				) {
					Logger.printInfo("Skier 1 with id " + mySkiers.get(i).id() + " at Left Start Position" );
					_skier1 = mySkiers.get(i);
					break;
				}
			}
		}
		if( _skier2 == null && mySkiers.size() > 0) {
			for(int i=0; i < mySkiers.size(); i++) {
				if( _track2Translate.x - _trackWidth * 2f < mySkiers.get(i).base().x && 
					_track2Translate.x + _trackWidth * 2f > mySkiers.get(i).base().x &&
					_track2Translate.y - 1f + _startLineCorrection < mySkiers.get(i).base().y &&
					_track2Translate.y - 1f + _trackWidth * 2f > mySkiers.get(i).base().y
				) {
					Logger.printInfo("Skier 2 with id " + mySkiers.get(i).id() + " at Right Start Position" );
					_skier2 = mySkiers.get(i);
					break;
				}
			}
		}
		
		if( _skier1 != null ) {
			PVector skier1Pos = PVector.sub(_skier1.base(), _track1Translate);
			_track1.updateSkier(skier1Pos);
		} else {
			_track1.updateSkier(null);
		}
		
		if( _skier2 != null ) {
			PVector skier2Pos = PVector.sub(_skier2.base(), _track2Translate);
			_track2.updateSkier(skier2Pos);
		} else {
			_track2.updateSkier(null);
		}
			
		_track1.update();
		_track2.update();
	}

	@Override
	protected void drawWithLaser(final Laser3D theLaser) {
		float animation = PApplet.map((float)Math.sin(frameCount/25d), 0f, PApplet.PI*2f, 0.22f, 0.98f);
		
		g.stroke(255);
		
		g.pushMatrix();
		g.translate(_track1Translate.x, _track1Translate.y);
		if( _skier1 == null) {
			float t1left = (-1f*_trackWidth*2f) * animation;
			float t1right = (_trackWidth*2f) * animation;
			g.line(t1left, -1f, t1right, -1f);
		}
		_track1.draw(g);
		g.popMatrix();
		
		g.pushMatrix();
		g.translate(_track2Translate.x, _track2Translate.y);
		if( _skier2 == null) {
			float t2left = (-1f*_trackWidth*2f) * animation;
			float t2right = (_trackWidth*2f) * animation;
			g.line(t2left, -1f, t2right, -1f);
		}
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
