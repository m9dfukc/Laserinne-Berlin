package com.laserinne.util;

public abstract class SkierDecorator {
	
	private final Skier _mySkier;
	
	public SkierDecorator(final Skier theSkier) {
		_mySkier = theSkier;
	}
	
	
	public boolean isEnded() {
		return true; // TODO: implement
	}
}
