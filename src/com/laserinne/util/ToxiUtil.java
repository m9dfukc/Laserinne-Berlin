package com.laserinne.util;

import processing.core.PVector;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

public class ToxiUtil {

	public static PVector toPVector(final Vec2D theVec) {
		return  new PVector(theVec.x, theVec.y);
	}
	
	
	public static PVector toPVector(final Vec3D theVec) {
		return  new PVector(theVec.x, theVec.y, theVec.z);
	}
	
	
	public static Vec2D toVec2D(final PVector thePVec) {
		return new Vec2D(thePVec.x, thePVec.y);
	}
	
	
	public static Vec3D toVec3D(final PVector thePVec) {
		return new Vec3D(thePVec.x, thePVec.y, thePVec.z);
	}
	
	/**
	 * Checks if a PVector and a toxi Vec2D are almost equal (with tolerance)
	 * 
	 * @param theP
	 * @param theV
	 * @return
	 */
	public static boolean almost(final PVector theP, final Vec2D theV, final float theT) {
		return Math.abs(theP.x - theV.x) * Math.abs(theP.y - theV.y) < theT * theT;
	}
	
}
