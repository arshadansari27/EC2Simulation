package org.olivelabs.simulation;

public class EventGenerator {

	public long totalRequest = 10000L;
	public long requestCount = 0;
	private SimulationRunner simulator;

	public EventGenerator(SimulationRunner simulator){
		this.simulator = simulator;
	}

	public void generateNextArrivalEvent(){
		if(requestCount == totalRequest){
			return;
		}
		Request request = new Request();
		request.id = requestCount++;
		request.url = "/testUrl/"+ (requestCount%300);
		request.arrivalTime = getNextArrivalTime();
		request.serviceTime = getNextServiceTime();
		simulator.getEventManager().addEvent(new ArrivalEvent(request.arrivalTime, request, simulator));
	}

	public void generateDispatchEvent(Request request, Server server){

		simulator.getEventManager().addEvent(new DispatchEvent(request.serviceBeginTime+request.serviceTime, request, server, simulator));
	}

	private long getNextArrivalTime(){
		long interArrivalTime = (long)(Math.random()*10);

		return simulator.getClock().CurrentTime + interArrivalTime;
	}

	private long getNextServiceTime(){
		long serviceTime = (long)(Math.random()*10000);

		return serviceTime;
	}
}
