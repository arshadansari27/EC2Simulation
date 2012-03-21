package org.olivelabs.simulation;

public class ArrivalEvent extends Event {

	Request request;
	SimulationRunner simulator;


	public ArrivalEvent(long eventTime, Request request, SimulationRunner simulator){
		super();
		this.eventTime = eventTime;
		this.request = request;
		this.simulator = simulator;
	}

	@Override
	public void processEvent() {
		simulator.getClock().CurrentTime = this.eventTime;
		simulator.getServerManager().serve(request);
		simulator.getEventGenerator().generateNextArrivalEvent();
	}


}
