package org.olivelabs.simulation;

import java.util.Date;
import java.util.List;

public class SimulationRunner implements Runnable {

	public volatile boolean RUNNING;
	private EventGenerator eventGenerator;
	private ServerManager serverManager;
	private EventManager eventManager;
	private SimulationClock clock;
	private RequestWaitQueue waitQueue;
	private OutputStatistics requestStats;
	public Parameters params;

	public SimulationRunner(Parameters params){
		this.clock = new SimulationClock();
		this.waitQueue = new RequestWaitQueue();
		this.eventManager = new EventManager();
		this.serverManager = new ServerManager(this);
		this.eventGenerator = new EventGenerator(this);
		this.params = params;
		this.eventGenerator.totalRequest = params.totalRequest;
		this.requestStats = new OutputStatistics(this);
		RUNNING = true;
	}

	public OutputStatistics getRequestStats(){
		return this.requestStats;
	}

	public RequestWaitQueue getWaitQueue(){
		return this.waitQueue;
	}

	public SimulationClock getClock(){
		return this.clock;
	}

	public EventManager getEventManager(){
		return this.eventManager;
	}

	public EventGenerator getEventGenerator(){
		return this.eventGenerator;
	}

	public ServerManager getServerManager(){
		return this.serverManager;
	}

	@Override
	public void run() {
		Event event = null;
		eventGenerator.generateNextArrivalEvent();
		System.out.println("Simulation Begin Time : " + new Date());
		System.out.print("[");
		while (!((event = eventManager.getNextEvent()) instanceof TerminalEvent)) {
			event.processEvent();
		}
		System.out.println("]");
		System.out.println("Simulation End Time : " + new Date());
		RUNNING = false;
		this.serverManager.removeServer();
		for(String history : serverDetails()){
			System.out.println(history);
		}
	}

	public double percentComplete(){
		return (eventGenerator.requestCount*1.0/eventGenerator.totalRequest) * 100;
	}

	public List<String> serverDetails(){
		return serverManager.getServerHistories();
	}

}
