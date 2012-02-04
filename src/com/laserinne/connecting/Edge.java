package com.laserinne.connecting;

public class Edge<Type> {

	public Type a, b;

	public Edge(Type theA, Type theB) {
		this.a = theA;
		this.b = theB;
	}


	/**
	 * Reverses the Edge order in place
	 */
	public void reverse() {
		Type myTmpA = a;
		Type myTmpB = b;

		a = myTmpB;
		b = myTmpA;
	}

}
