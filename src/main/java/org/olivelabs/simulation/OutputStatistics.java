package org.olivelabs.simulation;

import java.util.Map;
import java.util.TreeMap;


public class OutputStatistics {
	public volatile long requestDispatched = 0L;
	public volatile long requestRejected = 0L;
	public volatile long totalServiceTime = 0L;
	public volatile long totalWaitTime = 0L;
	public volatile double averageWaitTime = 0D;
	public volatile double AverageServiceTime = 0D;
	public volatile long simulationClock = 0L;
	SimulationRunner simulator;
	public volatile Map<Long, Integer> serverStats = new TreeMap<Long, Integer>();

	public OutputStatistics(SimulationRunner simulator){
		this.simulator = simulator;
	}

	public void collectStatisticsForServerHistory(long clock, int serverCount){
		this.simulationClock = clock;
		serverStats.put(clock, serverCount);
	}
	public void collectStatisticsForDispatched(Request request){
		requestDispatched += 1;
		totalServiceTime += request.serviceTime;
		totalWaitTime += request.waitTime();

		averageWaitTime    =  totalWaitTime*1.0  / requestDispatched;
		AverageServiceTime =  totalServiceTime*1.0 / requestDispatched;

		simulationClock = request.dispatchTime;
	}
	public void collectStatisticsForRejected(Request request){
		simulationClock = request.arrivalTime;
		requestRejected++;
	}

	public  StatisticsCollector getStats(){
		StatisticsCollector data = new StatisticsCollector();
		data.simulationClock = simulationClock;
		data.requestDipatchedCount= requestDispatched;
		data.requestRejectedCount = requestRejected;
		data.requestInWaitQueue = simulator.getWaitQueue().size();
		data.serversInUse = simulator.getServerManager().serversInUse.size();
		data.averageServiceTime = AverageServiceTime;
		data.averageWaitTime = averageWaitTime;
		return data;
	}
}
