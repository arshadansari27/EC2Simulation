package org.olivelabs.simulation;

public class OutputStatistics {
	public static volatile long REQUEST_DISPATCHED= 0;
	public static volatile long REQUEST_REJECTED = 0;
	public static volatile long TOTAL_SERVICE_TIME = 0L;
	public static volatile long TOTAL_WAIT_TIME = 0L;
	public static volatile double AVERAGE_WAIT_TIME = 0D;
	public static volatile double AVERAGE_SERVICE_TIME = 0D;


	public static void collectStatisticsForDispatched(Request request){
		REQUEST_DISPATCHED++;
		TOTAL_SERVICE_TIME += request.serviceTime;
		TOTAL_WAIT_TIME += request.waitTime();
		AVERAGE_WAIT_TIME = TOTAL_WAIT_TIME * 1.0 / REQUEST_DISPATCHED;
		AVERAGE_SERVICE_TIME = TOTAL_SERVICE_TIME * 1.0 / REQUEST_DISPATCHED;
	}
	public static void collectStatisticsForRejected(Request request){
			REQUEST_REJECTED++;
	}

	public static StatisticsCollector getStats(){
		StatisticsCollector data = new StatisticsCollector();
		data.simulationClock = SimulationClock.CurrentTime;
		data.requestDipatchedCount= OutputStatistics.REQUEST_DISPATCHED;
		data.requestRejectedCount = OutputStatistics.REQUEST_REJECTED;
		data.requestInWaitQueue = RequestWaitQueue.getInstance().size();
		data.serversInUse = ServerManager.getInstance().serversInUse.size();
		data.averageServiceTime = OutputStatistics.AVERAGE_SERVICE_TIME;
		data.averageWaitTime = OutputStatistics.AVERAGE_WAIT_TIME;
		return data;
	}
}
