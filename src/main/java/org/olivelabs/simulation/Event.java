package org.olivelabs.simulation;


public abstract class Event implements Comparable<Event>{
	public Long eventTime;
	public abstract void processEvent();


	@Override
	public int compareTo(Event event) {
		return this.eventTime.compareTo(event.eventTime);
	}
}
