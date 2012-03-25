package org.olivelabs.simulation;


public class Request implements Comparable<Request>{
	public String url;
	public long id;
	public Long arrivalTime;
	public Long serviceTime;
	public Long serviceBeginTime;
	public Long dispatchTime;
	public Long waitTime(){
		return serviceBeginTime - arrivalTime;
	}

	@Override
	public int compareTo(Request request) {
		if(this.arrivalTime < request.arrivalTime) return -1;
		else if(this.arrivalTime > request.arrivalTime) return 1;
		else return 0;
	}
}
