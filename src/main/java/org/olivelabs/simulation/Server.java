package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.List;

public class Server {
	List<Request> requestsBeingServed;
	int numberOfCurrentRequestsLimit;
	long serverStartTime;
	long serverEndTime;
	List<ServerHistory> serverHistory;
	long requestCount = 0;
	public Server(int numberOfCurrentRequestsLimit) {
		requestsBeingServed = new ArrayList<Request>();
		this.numberOfCurrentRequestsLimit = numberOfCurrentRequestsLimit;
		serverHistory = new ArrayList<ServerHistory>();
	}

	public long getServerStartTime() {
		return serverStartTime;
	}

	public void setServerStartTime(long serverStartTime) {
		this.serverStartTime = serverStartTime;
	}

	public long getServerEndTime() {
		return serverEndTime;
	}

	public void setServerEndTime(long serverEndTime) {
		this.serverEndTime = serverEndTime;
		serverHistory.add(new ServerHistory(this.serverStartTime, this.serverEndTime));
	}

	public void serve(Request request) {
		if (requestsBeingServed.size() >= numberOfCurrentRequestsLimit)
			throw new RuntimeException(
					"Server cannot handle more requests than the limit set, concurrently!!!");
		request.serviceBeginTime = SimulationClock.CurrentTime;
		this.requestsBeingServed.add(request);
		requestCount++;
		EventGenerator.generateDispatchEvent(request, this);
	}

	public void free(Request request) {
		if (requestsBeingServed.size() <= 0
				|| !requestsBeingServed.contains(request))
			throw new RuntimeException("Server does not have the request!!!");
		request.dispatchTime = SimulationClock.CurrentTime;
		this.requestsBeingServed.remove(request);
		//TODO: Check later if the following line is introducing any bug.. Test scenario is to be defined for same.
		ServerManager.getInstance().removeServer();
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
	public long startTime, endTime;
	public ServerHistory(long startTime, long endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}
}