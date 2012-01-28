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

import processing.core.PGraphics;
import processing.core.PVector;

public class Track {
	
	private int id;

	
	private PVector[] trackPoints = new PVector[10];
	private PGraphics pG;
	
	private static int width;
	private static int height;
	
	public Track(PGraphics _pG) {
		this.pG = _pG;
		
		for(int i=0; i < trackPoints.length; i++) {
			
			trackPoints[i] = new PVector();
		}
	}
	
	public void update() {
		
	}
	
	public void draw() {
	
	}
	
	public void width() {
		
	}
	
	private void drawPath() {
		
	}
	
	private void drawSector() {
		
	}
	
    public static final void width(int width) {
        Track.width = width;
    } 
	
    public static final void height(int height) {
        Track.height = height;
    }
    
}
