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

import toxi.geom.Vec2D;
import toxi.math.noise.PerlinNoise;

import java.util.ArrayList;

public class Path {
	
	int resolution;
	
	ArrayList<Vec2D> points = new ArrayList<Vec2D>();
	
	Path() {
		this(100);
	}
	
	Path(int r) {
		resolution = r;
		generatePoints();
	}
	
	ArrayList<Vec2D> getPath() {
		return points;
	}
	
	void drawDebug(final PGraphics theG) {
		theG.stroke(255, 0, 0);
		theG.beginShape();
		theG.noFill();
		theG.vertex(points.get(0).x, points.get(0).y);
		for(int i=1; i<points.size(); i++) {
			theG.curveVertex(points.get(i).x, points.get(i).y);
		}
		theG.endShape();
		
		theG.stroke(0, 0, 255);
		for(int i=1; i<points.size(); i++) {
			theG.ellipse(points.get(i).x, points.get(i).y, 0.01f, 0.01f);
		}
	}
	
	private void generatePoints() {
		PerlinNoise generator = new PerlinNoise();
		long seed = System.currentTimeMillis() + (int)(Math.random() * 1000f);
		boolean bMirrored = (seed%3 == 0);
		generator.noiseSeed(seed);
		float stepY = 1.75f /  (resolution * 1f);
		float sinFactor = 20f * PApplet.map((float)Math.random(), 0.0f, 1.0f, 0.60f, 1.0f);
		for(int i=0; i<resolution; i++) {
			float spreadFactor1 = PApplet.sin((float)i / resolution * sinFactor + PApplet.PI/2f) * 0.65f;
			float spreadFactor2 = PApplet.sin((float)i / resolution * PApplet.PI/1.5f);
			float mirrorFactor = bMirrored ? -1.0f : 1.0f;
			float tmpX = generator.noise(stepY*i*2) * ((float)i / resolution) * spreadFactor1 * spreadFactor2 * mirrorFactor;
			float tmpY = stepY*i - 0.8f;
			points.add(new Vec2D(tmpX, tmpY));
		}
	}
}
