package org.olivelabs.simulation;

import java.math.BigInteger;

public class Parameters {
	public long waitQueueMaxSize = 200;
	public int concurrentRequestLimit = 200;
	public int maxServer = 500;
	public long totalRequest = 1000L;
	public BigInteger MAX_CLOCK = new BigInteger("36000000000");
}
