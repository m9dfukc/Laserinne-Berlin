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

package com.laserinne.util;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;

public class Path {
	PApplet parent;
	ArrayList<PVector> points = new ArrayList<PVector>();
	int width;
	int height;
	int resolution;
	
	public Path(PApplet p, int w, int h) {
		parent = p;
		width = w;
		height = h;
		resolution = 10;
		generatePoints();
	}
	
	public void setPathResolution(int r) {
		if( r > 2 && r < 50 && r != resolution) {
			resolution = r;
			generatePoints();
		}
	}
	
	public ArrayList<PVector> getPath() {
		return points;
	}
	
	public void draw() {
		parent.stroke(255, 0, 0);
		parent.beginShape();
		parent.noFill();
		parent.vertex(points.get(0).x, points.get(0).y);
		for(int i=1; i<points.size(); i++) {
			parent.stroke(0, 0, 255);
			parent.ellipse(points.get(i).x, points.get(i).y, 6f, 6f);
			parent.stroke(255, 0, 0);
			parent.curveVertex(points.get(i).x, points.get(i).y);
		}
		parent.endShape();
	}
	
	private void generatePoints() {
		Random generator = new Random();
		float stepWidth = (width * 1.f) / (resolution * 1.f); 
		float stepHeight = (height * 1.f) / (resolution * 1.f);
		for(int i=0; i<resolution; i++) {
			float tmpX = (float) (i * stepWidth / 5.f * generator.nextDouble()) + (float) (width / 2.f);  
			float tmpY = (float) (i * stepHeight); //  (float)(generator.nextDouble() * stepHeight / 10.f);
			points.add(new PVector(tmpX, tmpY));
		}
	}
    
}
