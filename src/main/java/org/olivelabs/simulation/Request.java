package org.olivelabs.simulation;


public final class Request {
	public String url;
	public long id;
	public Long arrivalTime;
	public Long serviceTime;
	public Long serviceBeginTime;
	public Long dispatchTime;
	public Long waitTime(){
		return serviceBeginTime - arrivalTime;
	}
}
