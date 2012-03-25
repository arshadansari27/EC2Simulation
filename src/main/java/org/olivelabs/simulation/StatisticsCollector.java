package org.olivelabs.simulation;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

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

    public TreeMap<Long, Long> serverHistoryInSecs = new TreeMap<Long, Long>();
    public TreeMap<Long, Long> serverHistoryInMins = new TreeMap<Long, Long>();
    public TreeMap<Long, Long> serverHistoryInHours = new TreeMap<Long, Long>();
    boolean haveServerHistoryByMins = false;
    boolean haveServerHistoryByHours = false;

    public static enum SERVER_HISTORY_TIME_LINE_BY {SECONDS, MINUTES, HOURS};

    static Logger log = Logger.getLogger(StatisticsCollector.class.getName());



    public StatisticsCollector(ServerManager serverManager, List<Server> servers){
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
		this.serverHistoryInSecs = serverManager.getServerHistory().serverGraph;
		createServerHistoryBy(60);
		createServerHistoryBy(3600);
    }

    //duration = 60 for converting to mins
    //duration = 3600 for converting to hours
    private void createServerHistoryBy(int duration){
    	TreeMap<Long, Long> serverHistoryOut = new TreeMap<Long, Long>();
    	switch(duration){
    		case 60:
    			haveServerHistoryByMins=true;
    			serverHistoryOut = this.serverHistoryInMins;
    			break;
    		case 3600:
    			haveServerHistoryByHours = true;
    			serverHistoryOut = this.serverHistoryInHours;
    			break;
    		default:
    			log.error("Cannot use duration other than 60 or 3600.");
    	}

    	long counter=0;
    	for(Entry<Long, Long> serverHistory : this.serverHistoryInSecs.entrySet()){
    		long eventTime = serverHistory.getKey();
    		counter =  (eventTime/duration);
    		if(!serverHistoryOut.containsKey(counter)){
    			serverHistoryOut.put(counter, serverHistory.getValue());
    		}
    		else{
    			//Store the max server used in that duration...
    			serverHistoryOut.put(counter, (serverHistoryOut.get(counter) > serverHistory.getValue()) ? serverHistoryOut.get(counter) : serverHistory.getValue());
    		}
		}
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
    	builder.append("\nServer History By Secs: ");
    	for(Entry<Long, Long> serverHistory : this.serverHistoryInSecs.entrySet()){
			builder.append(serverHistory.getKey());
			builder.append("->");
			builder.append(serverHistory.getValue());
			builder.append(", ");
		}
    	if(haveServerHistoryByMins){
	    	builder.append("\nServer History By Mins: ");
	    	for(Entry<Long, Long> serverHistory : this.serverHistoryInMins.entrySet()){
				builder.append(serverHistory.getKey());
				builder.append("->");
				builder.append(serverHistory.getValue());
				builder.append(", ");
			}
    	}
    	if(haveServerHistoryByHours){
	    	builder.append("\nServer History By Hours:");
	    	for(Entry<Long, Long> serverHistory : this.serverHistoryInHours.entrySet()){
				builder.append(serverHistory.getKey());
				builder.append("->");
				builder.append(serverHistory.getValue());
				builder.append(", ");
			}
	    	builder.append("\n");
    	}
    	return builder.toString();
    }
}
