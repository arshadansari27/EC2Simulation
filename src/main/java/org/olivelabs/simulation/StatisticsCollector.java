package org.olivelabs.simulation;

public class StatisticsCollector {
	public long simulationClock;
	public long requestDipatchedCount;
	public long requestRejectedCount;
	public int serversInUse;
	public long requestInWaitQueue;
	
	public double averageServiceTime;
	public double averageWaitTime;
}
