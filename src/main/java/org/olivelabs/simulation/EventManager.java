package org.olivelabs.simulation;

import java.util.concurrent.atomic.AtomicLong;

public class EventManager {
	FEL<Event> fel ;
	AtomicLong arrivalEventsCount;
	public EventManager(){
		fel = new FEL<Event>();
		arrivalEventsCount = new AtomicLong(0);
	}



	public synchronized Event getNextEvent(){
		Event e = fel.poll();
		if(e instanceof ArrivalEvent) arrivalEventsCount.set(arrivalEventsCount.decrementAndGet());
		return e;
	}

	public synchronized void addEvent(Event event){
		if(event instanceof ArrivalEvent) arrivalEventsCount.set(arrivalEventsCount.incrementAndGet());
		fel.offer(event);
		notifyAll();
	}

	public synchronized long getSize(){
		return fel.size();
	}

	public synchronized long getCurrentArrivalCount(){
		return arrivalEventsCount.get();
	}

}
