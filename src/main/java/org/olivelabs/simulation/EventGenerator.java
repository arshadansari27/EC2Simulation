package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventGenerator implements Runnable{

    public long totalRequest = 10000L;
    public long requestCount = 0;
    private SimulationRunner simulator;
    Random rand = new Random();
    AtomicBoolean stopGenerating = new AtomicBoolean();
    String requestUrl = "/testURL/SameForAll";
    public EventGenerator(SimulationRunner simulator){
        this.simulator = simulator;
    	stopGenerating.set(false);

    }

    public synchronized void generateArrivalEventsInBatch(long currentTime){
    	if(currentTime >= simulator.params.MAX_CLOCK){
        	generateTerminalEvent();
            return;
        }
    	List<Event> events = new ArrayList<Event>();
    	long requestSize = 0L;
    	switch(rand.nextInt(3)){
    	case 0:
    		requestSize = this.simulator.params.averageRequestPerSecond;
    		break;
    	case 1:
    		requestSize = this.simulator.params.maxRequestPerSecond;
    		break;
    	case 2:
    		requestSize = this.simulator.params.minRequestPerSecond;
    		break;
    	}
    	for(long i = 0; i < requestSize; i++)
    		events.add(new ArrivalEvent(currentTime, getNextRequest(currentTime), this.simulator));

		Logger.log(this.getClass(), ":Generating Arrival Request["+requestSize+"] at ["+currentTime+"]", 10);
        simulator.getEventManager().addEvents(events);
    }
    public synchronized void generateDispatchEvent(Request request, Server server){

    	request.dispatchTime = request.serviceBeginTime + request.serviceTime;
    	simulator.getEventManager().addSingleEvent(new DispatchEvent(request.dispatchTime, request, server, simulator));
    	Logger.log(this.getClass(), ":Generating dispatch Request", 10000);
    }

    private void generateTerminalEvent(){
    	simulator.getEventManager().addSingleEvent(new TerminalEvent(simulator.params.MAX_CLOCK));
    	stopGenerating.set(true);
    	Logger.log(this.getClass(), ":Generating Terminal Request", 10000);
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
    		requestServiceTime = this.simulator.params.averageRequestServiceTime;
    		break;
    	case 1:
    		requestServiceTime = this.simulator.params.maxRequestServiceTime;
    		break;
    	case 2:
    		requestServiceTime = this.simulator.params.minRequestServiceTime;
    		break;
    	}
    	return requestServiceTime;
    }

	@Override
	public void run() {

		long currentTime = 0;
		while(currentTime <= simulator.params.MAX_CLOCK){

			if(simulator.getEventManager().getCurrentArrivalCount() > (2*simulator.params.maxRequestPerSecond)){
				synchronized(this){
					try {
						wait(100);
					} catch (InterruptedException e) {}
				}
				continue;
			}
			generateArrivalEventsInBatch(currentTime);
			simulator.getEventManager().setGenerationClock(currentTime);

			currentTime++;
		}

	}
}
