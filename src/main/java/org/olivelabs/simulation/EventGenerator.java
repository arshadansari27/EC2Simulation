package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

public class EventGenerator implements Runnable{

	private Parameters params;
	private long requestCount = 0;
    Random rand = new Random();
    //AtomicBoolean stopGenerating = new AtomicBoolean();
    String requestUrl = "/testURL/SameForAll";
    static Logger log = Logger.getLogger(EventGenerator.class.getName());
    private EventManager eventManager;
    public EventGenerator(EventManager eventManager, Parameters params){
        //stopGenerating.set(false);
        this.params = params;
        this.eventManager = eventManager;
    }

    public synchronized void generateArrivalEventsInBatch(long currentTime){
    	if(currentTime >= params.MAX_CLOCK){
        	generateTerminalEvent();
            return;
        }
    	List<Event> events = new ArrayList<Event>();
    	long requestSize = 0L;
    	switch(rand.nextInt(3)){
    	case 0:
    		requestSize = this.params.averageRequestPerSecond;
    		break;
    	case 1:
    		requestSize = this.params.maxRequestPerSecond;
    		break;
    	case 2:
    		requestSize = this.params.minRequestPerSecond;
    		break;
    	}
    	for(long i = 0; i < requestSize; i++)
    		events.add(new Event(currentTime, getNextRequest(currentTime)));

		log.debug( ":Generating Arrival Request["+requestSize+"] at ["+currentTime+"]");
		eventManager.addEvents(events);
    }

    private void generateTerminalEvent(){
    	eventManager.addSingleEvent(new TerminalEvent(params.MAX_CLOCK,null));
    	//stopGenerating.set(true);
    	log.debug("Generating Terminal Request");
    }
    private Request getNextRequest(Long currentTime){
    	Request request = new Request();
        request.id = requestCount++;
        request.url = requestUrl;//+ (rand.nextInt(300));
        request.arrivalTime = currentTime;
        request.serviceTime = getNextServiceTime();
        return request;
    }


    private Long getNextServiceTime(){
    	long requestServiceTime = 0L;
    	switch(rand.nextInt(3)){
    	case 0:
    		requestServiceTime = this.params.averageRequestServiceTime;
    		break;
    	case 1:
    		requestServiceTime = this.params.maxRequestServiceTime;
    		break;
    	case 2:
    		requestServiceTime = this.params.minRequestServiceTime;
    		break;
    	}
    	return requestServiceTime;
    }

	@Override
	public void run() {

		long currentTime = 0;
		while(currentTime <= params.MAX_CLOCK){

			if(eventManager.getSize() > (2*params.maxRequestPerSecond)){
				synchronized(this){
					try {
						wait(100);
					} catch (InterruptedException e) {}
				}
				continue;
			}
			generateArrivalEventsInBatch(currentTime);

			currentTime++;
		}
		generateTerminalEvent();

	}
}
