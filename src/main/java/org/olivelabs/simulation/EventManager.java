package org.olivelabs.simulation;

public class EventManager {
	FEL<Event> fel ;
	public EventManager(){
		fel = new FEL<Event>();
	}



	public Event getNextEvent(){
		return fel.poll();
	}

	public void addEvent(Event event){
		fel.offer(event);
	}

	public int getSize(){
		return fel.size();
	}

}
