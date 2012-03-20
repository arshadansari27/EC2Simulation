package org.olivelabs.simulation;

public class EventManager {
	FEL<Event> fel ;
	static EventManager eventManager = null;
	private EventManager(){
		fel = new FEL<Event>();
	}

	public static EventManager getInstance(){
		if(eventManager == null)
			eventManager = new EventManager();
		return eventManager;
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
