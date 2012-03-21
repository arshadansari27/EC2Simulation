package org.olivelabs.simulation;

import java.math.BigInteger;

public final class Request {
	public String url;
	public long id;
	public BigInteger arrivalTime;
	public BigInteger serviceTime;
	public BigInteger serviceBeginTime;
	public BigInteger dispatchTime;
	public BigInteger waitTime(){
		return serviceBeginTime.subtract(arrivalTime);
	}
}
