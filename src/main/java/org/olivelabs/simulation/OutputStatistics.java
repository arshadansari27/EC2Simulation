package org.olivelabs.simulation;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;


public class OutputStatistics {

	//Request Stats
	public long requestDispatched = 0L;
	public long requestRejected = 0L;
	public long totalServiceTime = 0L;
	public long totalActualServiceTime = 0L;
	public long totalWaitTime = 0L;
	public double averageWaitTime = 0D;
	public double averageServiceTime = 0D;
	public double averageActualServiceTime = 0D;
	public long simulationClock = 0L;

	//ServerStats
	public String id;
	public double averageServerUtilization;
    public long totalTimeInAction, totalTimeInSystem;
    public int serverMangerId;

	public OutputStatistics(){

	}

	public void collectStatisticsForServer(Server server){
		id = server.getId();
		averageServerUtilization = server.getAverageUtilization();
		totalTimeInAction = server.getBusyTime();
		totalTimeInSystem = server.getTotalTimeInSystem();
	}

	public void collectStatisticsForDispatched(Request request){
		requestDispatched += 1;
		totalServiceTime += request.serviceTime;
		totalWaitTime += request.waitTime();
		totalActualServiceTime = request.serviceTime + request.waitTime();

		averageWaitTime    =  totalWaitTime*1.0  / (requestDispatched+requestRejected);
		averageServiceTime =  totalServiceTime*1.0 / (requestDispatched+requestRejected);
		averageActualServiceTime = totalActualServiceTime * 1.0 / (requestDispatched+requestRejected);

		simulationClock = request.dispatchTime;
	}

	public void collectStatisticsForRejected(Request request){
		simulationClock = request.arrivalTime;
		requestRejected++;
	}

}
