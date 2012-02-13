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
	public boolean bMirrored;
	
	int _resolution;
	float _xPos;
	ArrayList<PVector> _points = new ArrayList<PVector>();
	
	
	public Path(float _xPos) {
		this(_xPos, 100);
	}
	
	public Path(float xPos, int resolution) {
		_resolution = resolution;
		_xPos = xPos;
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
		long seed = System.currentTimeMillis() + (int)(Math.random() * 1000f);
		_points.clear();
		bMirrored = (seed%3 == 0);
		generator.noiseSeed(seed);
		float stepY = 1.75f /  (_resolution * 1f);
		float sinFactor = 20f * PApplet.map((float)Math.random(), 0.0f, 1.0f, 0.60f, 1.0f);
		for(int i=0; i<_resolution; i++) {
			float spreadFactor1 = PApplet.sin((float)i / _resolution * sinFactor + PApplet.PI/2f) * 0.65f;
			float spreadFactor2 = PApplet.sin((float)i / _resolution * PApplet.PI/1.5f);
			float mirrorFactor = bMirrored ? -1.0f : 1.0f;
			float tmpX = generator.noise(stepY*i*2) * ((float)i / _resolution) * spreadFactor1 * spreadFactor2 * mirrorFactor + _xPos;
			float tmpY = stepY*i - 0.8f;
			_points.add(new PVector(tmpX, tmpY));
		}
	}
}
