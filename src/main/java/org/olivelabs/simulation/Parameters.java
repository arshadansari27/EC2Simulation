package org.olivelabs.simulation;

import java.math.BigInteger;

public class Parameters {
	public long waitQueueMaxSize = 200;
	public int concurrentRequestLimit = 1000;
	public int maxServer = 5000;
	public long totalRequest = 1000L;
	public BigInteger MAX_CLOCK = new BigInteger("100000");
	public int eventProcessorSize = 3;
}
