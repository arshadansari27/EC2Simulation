package org.olivelabs.simulation;

public class DispatchEvent extends Event {

	Request request;
	Server server;
	EventManager eventManager;
	RequestWaitQueue queue;
	ServerManager serverManager;
	
	public DispatchEvent(long eventTime, Request request, Server server){
		super();
		this.eventTime = eventTime;
		this.server = server;
		this.request = request;
		eventManager = EventManager.getInstance();
		queue = RequestWaitQueue.getInstance();
		serverManager = ServerManager.getInstance();
	}
	
	@Override
	public void processEvent() {
		SimulationClock.CurrentTime = this.eventTime;
		serverManager.free(this.server, this.request);
	}

}
