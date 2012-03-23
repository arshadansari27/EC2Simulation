package org.olivelabs.simulation;


public class Parameters {
	public long waitQueueMaxSize = 20000;
	public int concurrentRequestLimit = 10000;
	public int maxServer = 10000;
	public long totalRequest = 1000L;
	public Long MAX_CLOCK = 3600L;
	public int eventProcessorSize = 1;
	public long averageRequestPerSecond =  2000;
	public long maxRequestPerSecond = 5000;
	public long minRequestPerSecond = 8000;
	public int averageRequestServiceTime = 15;
	public int maxRequestServiceTime = 30;
	public int minRequestServiceTime = 3;
}
