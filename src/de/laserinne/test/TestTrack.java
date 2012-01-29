package de.laserinne.test;
import processing.core.PApplet;

import com.laserinne.util.LaserinneSketch;
import com.laserinne.util.Track;

@SuppressWarnings("serial")
public class TestTrack extends LaserinneSketch {
     
	private Track track; 
	
    public static void main(String args[]) {
        PApplet.main(new String[] { TestTrack.class.getCanonicalName() });
    }

	@Override
	protected void postSetup() {
		this.background(255);
        track = new Track(30f);
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawWithLaser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawOnScreen() {
		track.draw(g);
	}
}
