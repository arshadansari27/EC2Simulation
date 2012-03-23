package org.olivelabs.simulation;


public class DispatchEvent extends Event {

	Request request;
	Server server;
	SimulationRunner simulator;

	public DispatchEvent(Long eventTime, Request request, Server server, SimulationRunner simulator){
		super();
		this.eventTime = eventTime;
		this.server = server;
		this.request = request;
		this.simulator = simulator;
	}

	@Override
	public void processEvent() {
		//simulator.getClock().CurrentTime.set(this.eventTime);
		simulator.getServerManager().free(this.server, this.request);
		Logger.log(this.getClass(), ":Processing Dispatch Request", 10000);
	}

}
