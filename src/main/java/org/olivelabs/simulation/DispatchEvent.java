package org.olivelabs.simulation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class DispatchEvent extends Event {

	Request request;
	Server server;
	SimulationRunner simulator;

	public DispatchEvent(BigInteger eventTime, Request request, Server server, SimulationRunner simulator){
		super();
		this.eventTime = eventTime;
		this.server = server;
		this.request = request;
		this.simulator = simulator;
	}

	@Override
	public void processEvent() {
		simulator.getClock().CurrentTime.set(this.eventTime);
		simulator.getServerManager().free(this.server, this.request);
	}

}
