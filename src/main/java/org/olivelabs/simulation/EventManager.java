package org.olivelabs.simulation;

public class EventManager {
	FEL<Event> fel ;
	public EventManager(){
		fel = new FEL<Event>();
	}



	public synchronized Event getNextEvent(){
		return fel.poll();
	}

	public synchronized void addEvent(Event event){
		fel.offer(event);
		notifyAll();
	}

	public synchronized int getSize(){
		return fel.size();
	}

}
