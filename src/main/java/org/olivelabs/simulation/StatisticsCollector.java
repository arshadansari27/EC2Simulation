package org.olivelabs.simulation;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatisticsCollector implements Serializable{
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public Long simulationClock = 0L;
    public Long requestDipatchedCount = 0L;
    public Long requestRejectedCount = 0L;

    public double averageServiceTime = 0D;
    public double averageWaitTime = 0D;
	public double averageActualServiceTime = 0D;

	//ServerStats
	public long servers;
	public double averageServerUtilization;        	// Ratio of concurrent request to max allowed
	public double averageServerUsage;				// Ratio of time server was busy serving with total time in system.
    public long totalTimeInAction, totalTimeInSystem;




    public StatisticsCollector(List<Server> servers){
    	this.servers = servers.size();
    	for(Server server : servers){
    		OutputStatistics stats = server.getStats();

    		if(this.simulationClock < stats.simulationClock)
    			this.simulationClock = stats.simulationClock;
    		this.requestDipatchedCount += stats.requestDispatched;
    		this.requestRejectedCount += stats.requestRejected;

    		this.averageServiceTime += stats.averageServiceTime;
    		this.averageWaitTime += stats.averageWaitTime;
    		this.averageServerUtilization += stats.averageServerUtilization;
    		this.averageActualServiceTime += stats.averageActualServiceTime;
    		this.totalTimeInAction += stats.totalTimeInAction;
    		this.totalTimeInSystem += stats.totalTimeInSystem;

    	}
		this.averageServiceTime = this.averageServiceTime/this.servers;
		this.averageWaitTime = this.averageWaitTime/this.servers;
		this.averageActualServiceTime = this.averageActualServiceTime/this.servers;
		this.averageServerUsage = (double)this.totalTimeInAction/this.totalTimeInSystem;
    }


    public String toString(){
    	StringBuilder builder = new StringBuilder();


    	builder.append(String.format("Clock : %d min %d sec [%d]\t",TimeUnit.SECONDS.toMinutes(simulationClock.longValue()),TimeUnit.SECONDS.toSeconds(simulationClock.longValue()) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(simulationClock.longValue())),simulationClock.longValue()));
    	builder.append(String.format("RequestCount : %d\t", requestDipatchedCount+requestRejectedCount));
    	builder.append(String.format("Dispatched : %d\t", requestDipatchedCount));
    	builder.append(String.format("Servers : %d\t", servers));
    	builder.append(String.format("Avg act Service rate : %f\t", averageActualServiceTime));
    	builder.append(String.format("Avg Service : %f\t", averageServiceTime));
    	builder.append(String.format("Avg Wait : %f\t", averageWaitTime));
    	builder.append(String.format("Rejected : %d\t", requestRejectedCount));
    	builder.append(String.format("Avg Server utilization : %f\t", averageServerUtilization));
    	builder.append(String.format("Avg Server time spend in system : %f\t", averageServerUsage));


    	return builder.toString();
    }
}
