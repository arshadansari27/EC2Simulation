package org.olivelabs.simulation;


public class ArrivalEvent extends Event {

	Request request;
	SimulationRunner simulator;


	public ArrivalEvent(Long eventTime, Request request, SimulationRunner simulator){
		super();
		this.eventTime = eventTime;
		this.request = request;
		this.simulator = simulator;
	}

	@Override
	public void processEvent() {
		//simulator.getClock().CurrentTime.set(this.eventTime);
		simulator.getServerManager().serve(request);
		Logger.log(this.getClass(),":Processing Arrival Request with arrival time : "+this.eventTime, 10000);
	}


}
