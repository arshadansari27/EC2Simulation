package org.olivelabs.simulation;

import java.util.Random;


public class Parameters {
	//public int waitQueueMaxSize = 20000;
	public String taskId =  (new Random().nextInt())+"";
	public int concurrentRequestLimit = 1000;
	public int maxServer = 200;
	public Long MAX_CLOCK = 3600L;
	public int eventProcessorSize = 1;
	public int serverManager  = 0;
	public long averageRequestPerSecond =  200;
	public long maxRequestPerSecond = 500;
	public long minRequestPerSecond = 100;
	public int averageRequestServiceTime = 15;
	public int maxRequestServiceTime = 30;
	public int minRequestServiceTime = 3;
	
}
