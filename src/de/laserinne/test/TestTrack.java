package de.laserinne.test;
import processing.core.PApplet;
import com.laserinne.util.Track;

@SuppressWarnings("serial")
public class TestTrack extends PApplet {
     
	private Track track; 
	
    public static void main(String args[]) {
        PApplet.main(new String[] { TestTrack.class.getCanonicalName() });
    }
    
    public void setup() {
        this.size(600, 600);
        this.background(255);
        track = new Track(this, 600, 600);
    }
    
    public void draw() {
    	this.background(255);
    	track.draw();    
    }
}
