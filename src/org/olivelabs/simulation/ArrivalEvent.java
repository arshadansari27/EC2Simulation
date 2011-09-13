package org.olivelabs.simulation;

public class ArrivalEvent extends Event {

	Request request;
	EventManager eventManager;
	RequestWaitQueue queue;
	ServerManager serverManager;
	public ArrivalEvent(long eventTime, Request request){
		super();
		this.eventTime = eventTime;
		this.request = request;
		eventManager = EventManager.getInstance();
		queue = RequestWaitQueue.getInstance();
		serverManager = ServerManager.getInstance();
	}
	
	@Override
	public void processEvent() {
		SimulationClock.CurrentTime = this.eventTime;
		serverManager.serve(request);
		EventGenerator.generateNextArrivalEvent();
	}
	
	
}
