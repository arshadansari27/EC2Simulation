package org.olivelabs.simulation;

public class OutputStatistics {
	public volatile long REQUEST_DISPATCHED= 0;
	public volatile long REQUEST_REJECTED = 0;
	public volatile long TOTAL_SERVICE_TIME = 0L;
	public volatile long TOTAL_WAIT_TIME = 0L;
	public volatile double AVERAGE_WAIT_TIME = 0D;
	public volatile double AVERAGE_SERVICE_TIME = 0D;
	SimulationRunner simulator;

	public OutputStatistics(SimulationRunner simulator){
		this.simulator = simulator;
	}
	public void collectStatisticsForDispatched(Request request){
		REQUEST_DISPATCHED++;
		TOTAL_SERVICE_TIME += request.serviceTime;
		TOTAL_WAIT_TIME += request.waitTime();
		AVERAGE_WAIT_TIME = TOTAL_WAIT_TIME * 1.0 / REQUEST_DISPATCHED;
		AVERAGE_SERVICE_TIME = TOTAL_SERVICE_TIME * 1.0 / REQUEST_DISPATCHED;
	}
	public void collectStatisticsForRejected(Request request){
			REQUEST_REJECTED++;
	}

	public  StatisticsCollector getStats(){
		StatisticsCollector data = new StatisticsCollector();
		data.simulationClock = simulator.getClock().CurrentTime;
		data.requestDipatchedCount= REQUEST_DISPATCHED;
		data.requestRejectedCount = REQUEST_REJECTED;
		data.requestInWaitQueue = simulator.getWaitQueue().size();
		data.serversInUse = simulator.getServerManager().serversInUse.size();
		data.averageServiceTime = AVERAGE_SERVICE_TIME;
		data.averageWaitTime = AVERAGE_WAIT_TIME;
		return data;
	}
}
