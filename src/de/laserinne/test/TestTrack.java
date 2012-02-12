package de.laserinne.test;
import laserschein.Laser3D;
import processing.core.PApplet;

import com.laserinne.base.LaserinneSketch;
import com.laserinne.base.Skier;
import com.laserinne.bouncytrack.Track;

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
	protected void update(final float theDelta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawWithLaser(final Laser3D theLaser) {
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
