package com.laserinne.connecting;

public class Edge<T> {

	public T a, b;

	public Edge() {
		a = null;
		b = null;
	}

	public Edge(T theA, T theB) {
		this.a = theA;
		this.b = theB;
	}


	/**
	 * Reverses the Edge order in place
	 */
	public void reverse() {
		T myTmpA = a;
		T myTmpB = b;

		a = myTmpB;
		b = myTmpA;
	}

}
