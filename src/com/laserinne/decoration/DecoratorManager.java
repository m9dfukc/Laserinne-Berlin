package com.laserinne.decoration;

import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PGraphics;

public class DecoratorManager {
	public final LinkedList<Decorator> _myDecorators;
	
	public DecoratorManager() {
		_myDecorators = new LinkedList<Decorator>();
	}
	
	
	public void add(final Decorator theDecorator) {
		_myDecorators.add(theDecorator);
	}
	
	
	public void draw(PGraphics theG) {
		for(final Decorator myDecorator:_myDecorators) {
			myDecorator.draw(theG);
		}
	}
	
	
	public void update() {
		
		final Iterator<Decorator> myIterator = _myDecorators.iterator();
		
		while(myIterator.hasNext()) {
			final Decorator myDecorator = myIterator.next();
		
			myDecorator.update();
			
			if(myDecorator.isFinished()) {
				myIterator.remove();
			}
		}
		
	}
}
