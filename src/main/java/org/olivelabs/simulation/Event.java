package org.olivelabs.simulation;

import java.math.BigInteger;

public abstract class Event implements Comparable<Event>{
	public BigInteger eventTime;
	public abstract void processEvent();


	@Override
	public int compareTo(Event event) {
		return this.eventTime.compareTo(event.eventTime);
	}
}
