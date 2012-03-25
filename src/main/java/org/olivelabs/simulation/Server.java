package org.olivelabs.simulation;

import java.util.PriorityQueue;

import org.apache.log4j.Logger;

public class Server {
    private long id;
    private volatile long currentRequestCount;
    private long dispatchedRequestCount;
    private long rejectedRequestCount;
    private PriorityQueue<Long> dispatchQueue;
    private long numberOfConcurrentRequestsLimit;
    private double averageServerUtilization;
    private long startTime, busyStartTime, totalTimeInAction, totalTimeInSystem;
    private long eventsArrivals;
    private long currentTime;
    private int serverMangerId;
    private boolean running;

    //Stats
    private OutputStatistics stats;

    //FOR DEBUGGING
    long totalRequestsActuallyDispatch;
    //FOR DEBUGGING

    static Logger log = Logger.getLogger(Server.class.getName());

    public Server(int id, long numberOfConcurrentRequestsLimit, int serverManagerId) {
        this.id = id;
        this.serverMangerId = serverManagerId;
        this.numberOfConcurrentRequestsLimit = numberOfConcurrentRequestsLimit;
        dispatchQueue = new PriorityQueue<Long>();
        stats = new OutputStatistics();
        //log.debug( this.id+"Server Created");
    }

    public void setBusy(long currentTime){
        if(startTime == 0)	startTime = currentTime;
        if(running == false) running = true;
        busyStartTime =  currentTime;
        this.currentTime = currentTime;
        dispatch(currentTime);
    }
    public void setIdle(long currentTime){
        totalTimeInAction += (currentTime - busyStartTime);
        this.currentTime = currentTime;
        dispatch(currentTime);
    }

    public void shutDown(long currentTime){
        totalTimeInSystem = currentTime - startTime;
        this.currentTime = currentTime;
        dispatch(currentTime);
        log.debug("DispatchQueue size = "+dispatchQueue.size()+", when shutting down.");
        this.dispatchAll();
        stats.collectStatisticsForServer(this);
        running = false;
    }

    public double getAverageUtilization(){
        return averageServerUtilization / eventsArrivals;
    }
    public long getBusyTime(){
        return totalTimeInAction;
    }
    public long getTotalTimeInSystem(){
        return totalTimeInSystem;
    }

    public void serve(Request request, long currentTime) {
        eventsArrivals++;   // This data is used for calculating the averageServerUtilization factor;
        if(numberOfConcurrentRequestsLimit <= currentRequestCount){
            //log.debug(this.id+":Rejecting request at time["+currentTime+"]");
            reject(request);
            return;
        }
        this.currentTime = currentTime;
        //log.debug(this.id+":Serving request at time["+currentTime+"]");
        currentRequestCount++;
        dispatchedRequestCount++;
        averageServerUtilization += (double)currentRequestCount / numberOfConcurrentRequestsLimit;
        request.serviceBeginTime = currentTime;
        request.dispatchTime = request.serviceBeginTime + request.serviceTime;
        dispatchQueue.offer(request.dispatchTime);
        dispatch(currentTime);
        stats.collectStatisticsForDispatched(request);
    }

    public void dispatch(Long time){

    	this.currentTime = time;
        long requestsToRemove = 0;
        Long nextDispatch = dispatchQueue.peek();
        if(nextDispatch == null || nextDispatch  > time) return;
        while(dispatchQueue.peek() != null  && dispatchQueue.peek()  <= time){
            dispatchQueue.poll();
            requestsToRemove++;
        }

        currentRequestCount -= requestsToRemove;
        //log.debug(this.getId()+":["+requestsToRemove+"] requests dispatched at time : "+time);
        totalRequestsActuallyDispatch += requestsToRemove;
        //log.debug(this.getId()+":["+totalRequestsActuallyDispatch+"] requests dispatched till now ["+time+"]");
    }

    public void dispatchAll(){
    	while(dispatchQueue.size()>0){
    		//log.debug("Still many remains to be dispatched");
    		this.currentTime = dispatchQueue.poll();
    	}
    }
    public void reject(Request request){
        //log.debug(this.getId()+":Rejecting  concurrentRequests [ "+currentRequestCount+" / "+numberOfConcurrentRequestsLimit+" ]  at time : "+this.currentTime);
        rejectedRequestCount++;
        stats.collectStatisticsForRejected(request);
    }

    public boolean canHandle(){
        //if(!(numberOfConcurrentRequestsLimit > currentRequestCount))
        //    log.debug("Server :"+getId()+", cannot handle any more request; as of now!");
        return numberOfConcurrentRequestsLimit > currentRequestCount;
    }

    public long getServerCapacity() {
        return numberOfConcurrentRequestsLimit - currentRequestCount;
    }

    public long getRequestServed(){
        return dispatchedRequestCount;
    }

    public int getServerManagerId(){
    	return serverMangerId;
    }

    public String getId() {
        return getServerManagerId()+" => "+this.id;
    }

    public Long getTotalRejected() {
        return rejectedRequestCount;
    }

    public OutputStatistics getStats(){
    	if(running)
    		log.error("Cannot Call for stats while the server is still running");
    	return this.stats;
    }
}

