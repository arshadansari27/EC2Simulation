package org.olivelabs.simulation;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class StatisticsCollector implements Serializable{
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public Long simulationClock;
    public Long requestDipatchedCount;
    public Long requestRejectedCount;
    public long serversInUse;
    public long requestInWaitQueue;

    public double averageServiceTime;
    public double averageWaitTime;

    public String toString(){
    	StringBuilder builder = new StringBuilder();


    	builder.append(String.format("Clock : %d min %d sec [%d]\t",TimeUnit.SECONDS.toMinutes(simulationClock.longValue()),TimeUnit.SECONDS.toSeconds(simulationClock.longValue()) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(simulationClock.longValue())),simulationClock.longValue()));
    	builder.append(String.format("RequestCount : %d\t", requestDipatchedCount+requestRejectedCount));
    	builder.append(String.format("Waiting : %d\t", requestInWaitQueue));
    	builder.append(String.format("Servers : %d\t", serversInUse));
    	builder.append(String.format("Avg Service : %f\t", averageServiceTime));
    	builder.append(String.format("Avg Wait : %f\t", averageWaitTime));
    	builder.append(String.format("Rejected : %d\t", requestRejectedCount));

    	return builder.toString();
    }
}
