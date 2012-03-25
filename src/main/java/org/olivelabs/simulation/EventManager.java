package org.olivelabs.simulation;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.log4j.Logger;

public class EventManager {
	Queue<Event> felArrival;
	long generationClock;

	static Logger log = Logger.getLogger(EventManager.class.getName());

	public EventManager(){
		felArrival = new PriorityQueue<Event>();
	}

	public synchronized Event getNextEvent(){
		Event e =  felArrival.poll();
//		if(e!=null)
//			log.debug("Returning event with eventTime:"+e.eventTime);
		return e;
	}

	public synchronized boolean hasNextEvent(){
		return felArrival.size()>0;
	}

	public synchronized void addSingleEvent(Event event){
		addEvent(event);
		notifyAll();
	}
	private void addEvent(Event event){
		felArrival.offer(event);
		log.debug("Added event with eventTime:"+event.eventTime);
	}
	public synchronized void addEvents(List<Event> events){
		felArrival.addAll( events);
		notifyAll();
		log.debug("Added events in list with size:"+events.size());
	}

	public synchronized long getSize(){
		return felArrival.size() ;
	}



	public synchronized void clearFEL(){
		felArrival.clear();
	}


}
