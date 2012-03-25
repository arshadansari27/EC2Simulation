package org.olivelabs.simulation;


public class Parameters {
	//public int waitQueueMaxSize = 20000;
	public int concurrentRequestLimit = 1000;
	public int maxServer = 200;
	public Long MAX_CLOCK = 3600L;
	public int eventProcessorSize = 1;
	public int serverManager  = 0;
	public long averageRequestPerSecond =  2000;
	public long maxRequestPerSecond = 5000;
	public long minRequestPerSecond = 8000;
	public int averageRequestServiceTime = 15;
	public int maxRequestServiceTime = 30;
	public int minRequestServiceTime = 3;
}
