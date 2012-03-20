package org.olivelabs.simulation;

public final class Request {
	public String url;
	public long id;
	public long arrivalTime;
	public long serviceTime;
	public long serviceBeginTime;
	public long dispatchTime;
	public long waitTime(){
		return serviceBeginTime - arrivalTime;
	}
}
