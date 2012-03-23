package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class EventManager {
	List<Event> felArrival;
	FEL<Event> felDispatch ;
	volatile AtomicReference<Long> generationClock = new AtomicReference<Long>(0L);

	public EventManager(){

		felArrival = new ArrayList<Event>();
		felDispatch = new FEL<Event>();
	}



	public synchronized Event getNextEvent(){
		Event toReturn = null;
		Event arrival = null;
		if(!felArrival.isEmpty()) arrival = felArrival.get(0);
		Event dispatch = felDispatch.peek();


		if(arrival != null && dispatch != null ){
			if(arrival.compareTo(dispatch) <= 0){
				if(arrival.eventTime > generationClock.get()){

					toReturn = null;
				}
				else{
					toReturn = felArrival.remove(0);
				}
			}
			else{
				if(dispatch.eventTime > generationClock.get()){
					toReturn = null;
				}
				else{
					toReturn = felDispatch.poll();
				}

			}
		}
		else if(arrival != null){
			if(arrival.eventTime > generationClock.get()){
				toReturn = null;
			}
			else{
				toReturn = felArrival.remove(0);
			}
		}
		else{
			toReturn = null;
		}
		return toReturn;

	}

	public synchronized void addSingleEvent(Event event){
		addEvent(event);
		notifyAll();
	}
	private void addEvent(Event event){
		if(event instanceof DispatchEvent){
			felDispatch.offer(event);
		}
		else{
			int index = (felArrival.isEmpty())? 0 : felArrival.size()-1;
			felArrival.add(index,event);
		}
	}
	public synchronized void addEvents(List<Event> events){
		if(events.get(0) instanceof ArrivalEvent){
			if(felArrival.isEmpty()){
				felArrival.addAll( events);
			}
			else{
				felArrival.addAll(felArrival.size()-1, events);
			}
		}
		else{
			for(Event event : events){
				addEvent(event);
			}
		}
		notifyAll();
	}

	public synchronized long getSize(){
		return felArrival.size() + felDispatch.size();
	}

	public synchronized long getCurrentArrivalCount(){
		return felArrival.size();
	}

	public synchronized void clearFEL(){
		felArrival.clear();
		felDispatch.clear();
	}



	public synchronized void setGenerationClock(long currentTime) {
		generationClock.set(currentTime);

	}

}
