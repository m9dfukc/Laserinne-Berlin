package com.laserinne.decoration;

import de.looksgood.ani.Ani;

public abstract class SimpleDecoratorBase extends Decorator{
	
	private float _myProgress = 0;
	private Ani _myAni;
	
	private final Decoratable _myDecoratable;
	
	public SimpleDecoratorBase(Decoratable theDecoratable) {
		this.state(State.GENESIS);
		_myDecoratable = theDecoratable;
		_myAni = new Ani(this, 1, "_myProgress", 1);
		_myAni.start();
	}
	
	
	public float progress() {
		return _myProgress;
	}
		
	
	@Override
	public void update() {
		if(_myDecoratable.isDead() && this.state() != State.APOCALYPSE) {
			this.state(State.APOCALYPSE);
			_myAni.setBegin(_myProgress);
			_myAni.setEnd(0);
			_myAni.start();
		}
				
		if(_myAni.isEnded() && this.state() == State.APOCALYPSE) {
			this.state(State.FINISHED);
		}
	}

}
