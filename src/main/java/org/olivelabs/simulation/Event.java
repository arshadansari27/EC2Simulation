package org.olivelabs.simulation;

import org.apache.log4j.Logger;


public class Event implements Comparable<Event>{
	public Long eventTime;
	public Request request;

	static Logger log = Logger.getLogger(Event.class.getName());


	public Event(Long eventTime, Request request){
		super();
		this.eventTime = eventTime;
		this.request = request;
	}

	@Override
	public int compareTo(Event event) {
		return this.eventTime.compareTo(event.eventTime);
	}
}
