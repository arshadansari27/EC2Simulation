package org.olivelabs.simulation;

public abstract class Event implements Comparable<Event>{
	public long eventTime;
	public abstract void processEvent();

	@Override
	public int compareTo(Event event) {
		if(this.eventTime < event.eventTime)
			return -1;
		else if(this.eventTime > event.eventTime)
			return 1;
		else return 0;
	}
}
