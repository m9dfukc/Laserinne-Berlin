/*
 *                This file is part of Laserinne.
 * 
 *  Laser projections in public space, inspiration and
 *  information, exploring the aesthetic and interactive possibilities of
 *  laser-based displays.
 * 
 *  http://www.laserinne.com/
 * 
 * Laserinne is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Laserinne is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Laserinne. If not, see <http://www.gnu.org/licenses/>.
 */

package com.laserinne.bouncytrack;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import toxi.math.noise.PerlinNoise;

import java.util.ArrayList;

public class Path {
	int _resolution;
	ArrayList<PVector> _points = new ArrayList<PVector>();
	
	public Path() {
		this(10);
	}
	
	public Path(int resolution) {
		_resolution = resolution;
		generatePoints();
	}
	
	public void setPathResolution(int r) {
		if( r > 2 && r < 50 && r != _resolution) {
			_resolution = r;
			generatePoints();
		}
	}
	
	public ArrayList<PVector> getPath() {
		return _points;
	}
	
	public void drawOnScreen(final PGraphics theG) {
		theG.stroke(255, 0, 0);
		theG.beginShape();
		theG.noFill();
		theG.vertex(_points.get(0).x, _points.get(0).y);
		for(int i=1; i<_points.size(); i++) {
			theG.curveVertex(_points.get(i).x, _points.get(i).y);
		}
		theG.endShape();
		
		theG.stroke(0, 0, 255);
		for(int i=1; i<_points.size(); i++) {
			theG.ellipse(_points.get(i).x, _points.get(i).y, 0.01f, 0.01f);
		}
	}
	
	public void generatePoints() {
		PerlinNoise generator = new PerlinNoise();
		long seed = System.currentTimeMillis();
		_points.clear();
		generator.noiseSeed(seed);
		float stepY = 2.0f /  (_resolution * 1f);
		for(int i=0; i<_resolution; i++) {
			float tmpX = generator.noise(stepY*i*2) * ((float)i / _resolution) + PApplet.sin((float)i / _resolution * 15) * 0.25f;
			float tmpY = stepY*i - 1;
			_points.add(new PVector(tmpX, tmpY));
		}
		
		
		/*
		float stepWidth = .5f / (_resolution * 1f); 
		float stepHeight = 2.5f / (_resolution * 1f);
		for(int i=0; i<_resolution; i++) {
			float tmpX = (float) PApplet.map((float) generator.nextDouble(), 0f, 1f, -1f * stepWidth, stepWidth) * (i + 2) / 2 ;  
			float tmpY = (float) (i * stepHeight) - 1f + PApplet.map((float)generator.nextDouble(), 0f, 1f, -1f*stepHeight/4, stepHeight/4); //  (float)(generator.nextDouble() * stepHeight / 10.f);
			_points.add(new PVector(tmpX, tmpY));
		}
		*/
	}
}
