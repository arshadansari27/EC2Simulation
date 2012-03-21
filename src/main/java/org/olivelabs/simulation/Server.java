package org.olivelabs.simulation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Server {
	List<Request> requestsBeingServed;
	int numberOfCurrentRequestsLimit;
	BigInteger serverStartTime;
	BigInteger serverEndTime;
	List<ServerHistory> serverHistory;
	long requestCount = 0;
	SimulationRunner simulator;
	public Server(SimulationRunner simulator, int numberOfCurrentRequestsLimit) {
		this.simulator = simulator;
		requestsBeingServed = new ArrayList<Request>();
		this.numberOfCurrentRequestsLimit = numberOfCurrentRequestsLimit;
		serverHistory = new ArrayList<ServerHistory>();
	}

	public BigInteger getServerStartTime() {
		return serverStartTime;
	}

	public void setServerStartTime(BigInteger currentTime) {
		this.serverStartTime = currentTime;
	}

	public BigInteger getServerEndTime() {
		return serverEndTime;
	}

	public void setServerEndTime(BigInteger serverEndTime) {
		this.serverEndTime = serverEndTime;
		serverHistory.add(new ServerHistory(this.serverStartTime, this.serverEndTime));
	}

	public void serve(Request request) {
		if (requestsBeingServed.size() >= numberOfCurrentRequestsLimit)
			throw new RuntimeException(
					"Server cannot handle more requests than the limit set, concurrently!!!");
		request.serviceBeginTime = simulator.getClock().CurrentTime;
		this.requestsBeingServed.add(request);
		requestCount++;
		simulator.getEventGenerator().generateDispatchEvent(request, this);
	}

	public void free(Request request) {
		if (requestsBeingServed.size() <= 0
				|| !requestsBeingServed.contains(request))
			throw new RuntimeException("Server does not have the request!!!");
		request.dispatchTime = simulator.getClock().CurrentTime;
		this.requestsBeingServed.remove(request);

	}

	public int getRequestListSize() {
		return requestsBeingServed.size();
	}

	public int getServerCapacity() {
		return numberOfCurrentRequestsLimit - requestsBeingServed.size();
	}

	public long getRequestServed(){
		return requestCount;
	}

}
class ServerHistory{
	public BigInteger startTime, endTime;
	public ServerHistory(BigInteger startTime, BigInteger endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}
}