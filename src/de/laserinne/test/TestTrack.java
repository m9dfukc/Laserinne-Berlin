package de.laserinne.test;
import laserschein.Logger;
import processing.core.PApplet;

import com.laserinne.util.LaserinneSketch;
import com.laserinne.util.Skier;
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
        track = new Track(.07f);
        //Logger.setAll(false);
	}
	
	@Override
	public void mouseClicked() {
		//if (e.getButton() == e.BUTTON1) 
		track.generate();
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawWithLaser() {
		track.drawWithLaser(g);
	}

	@Override
	protected void drawOnScreen() {
		track.drawOnScreen(g);
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
