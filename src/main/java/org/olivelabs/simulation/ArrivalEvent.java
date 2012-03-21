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
		simulator.getEventGenerator().generateNextArrivalEvent();

		if(simulator.getClock().CurrentTime.get().compareTo(this.eventTime) <= 0){
			simulator.getClock().CurrentTime.set(this.eventTime);
			simulator.getServerManager().serve(request);
		}
	}


}
