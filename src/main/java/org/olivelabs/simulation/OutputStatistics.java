package org.olivelabs.simulation;

public class OutputStatistics {
	public static long REQUEST_DISPATCHED= 0;
	public static long REQUEST_REJECTED = 0;
	public static long TOTAL_SERVICE_TIME = 0L;
	public static long TOTAL_WAIT_TIME = 0L;
	public static double AVERAGE_WAIT_TIME = 0D;
	public static double AVERAGE_SERVICE_TIME = 0D;
	
	
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
	
}
