package org.olivelabs.simulation;

import java.math.BigInteger;

public class ArrivalEvent extends Event {

	Request request;
	SimulationRunner simulator;


	public ArrivalEvent(BigInteger eventTime, Request request, SimulationRunner simulator){
		super();
		this.eventTime = eventTime;
		this.request = request;
		this.simulator = simulator;
	}

	@Override
	public void processEvent() {
		System.out.println(simulator.getClock().CurrentTime.compareTo(this.eventTime));
		simulator.getClock().CurrentTime = this.eventTime;
		simulator.getServerManager().serve(request);
		simulator.getEventGenerator().generateNextArrivalEvent();
	}


}
