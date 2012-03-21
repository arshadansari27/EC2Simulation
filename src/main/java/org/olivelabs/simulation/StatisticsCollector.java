package org.olivelabs.simulation;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

public class StatisticsCollector implements Serializable{
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public BigInteger simulationClock;
    public BigInteger requestDipatchedCount;
    public BigInteger requestRejectedCount;
    public long serversInUse;
    public long requestInWaitQueue;

    public double averageServiceTime;
    public double averageWaitTime;

    public String toString(){
    	StringBuilder builder = new StringBuilder();
    	//TimeUnit.MILLISECONDS.toMinutes(simulationClock), TimeUnit.MILLISECONDS.toSeconds(simulationClock) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(simulationClock))
    	builder.append(String.format("Clock : %10.4f  sec\t",simulationClock.divide(new BigInteger("1000000"))));

    	builder.append(String.format("RequestCount : %10d\t", requestDipatchedCount.add(requestRejectedCount)));
    	builder.append(String.format("Waiting : %10d\t", requestInWaitQueue));
    	builder.append(String.format("Servers : %10d\t", serversInUse));
    	builder.append(String.format("Avg Service : %10.4f\t", averageServiceTime));
    	builder.append(String.format("Avg Wait : %10.4f\t", averageWaitTime));
    	return builder.toString();
    }
}
