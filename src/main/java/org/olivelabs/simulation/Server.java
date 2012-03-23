package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Server {
	List<Request> requestsBeingServed;
	int numberOfCurrentRequestsLimit;
	Long serverStartTime;
	Long serverEndTime;
	long requestCount = 0;
	SimulationRunner simulator;
    private volatile AtomicReference<Long> CurrentTime = new AtomicReference<Long>(-1L);

	public Server(SimulationRunner simulator, int numberOfCurrentRequestsLimit) {
		this.simulator = simulator;
		requestsBeingServed = new ArrayList<Request>();
		this.numberOfCurrentRequestsLimit = numberOfCurrentRequestsLimit;
	}



	public synchronized void serve(Request request) {

		CurrentTime.set(request.arrivalTime);
		if (requestsBeingServed.size() >= numberOfCurrentRequestsLimit)
			return;
		request.serviceBeginTime = CurrentTime.get();
		this.requestsBeingServed.add(request);
		requestCount++;
		Logger.log(this.getClass(), ":Serving Request", 10000);
		simulator.getEventGenerator().generateDispatchEvent(request, this);
	}

	public synchronized  void free(Request request) {
		if (requestsBeingServed.size() <= 0
				|| !requestsBeingServed.contains(request))
			return;
		CurrentTime.set(request.dispatchTime);
		Logger.log(this.getClass(), ":Freeing Request", 10000);
		this.requestsBeingServed.remove(request);
	}

	public synchronized  int getRequestListSize() {
		return requestsBeingServed.size();
	}

	public synchronized  int getServerCapacity() {
		return numberOfCurrentRequestsLimit - requestsBeingServed.size();
	}

	public  synchronized long getRequestServed(){
		return requestCount;
	}

}
