package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OutputStatisticsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCollectStatistics() {
		long simulationTime = 0L;
		List<Request> requestList = new ArrayList<Request>();
		long numberOfRequests = 100;
		long totalWaitTime = 0L;
		long totalServiceTime = 0L;
		double averageWaitTime = 0.0;
		double averageServiceTime = 0.0;
		OutputStatistics stats = new OutputStatistics();
		for(int i = 0; i < numberOfRequests; i++){
			Request request = new Request();
			simulationTime += (long)(Math.random() * 1000);
			request.arrivalTime = simulationTime;
			request.serviceTime = (long)(Math.random() * 1000);
			request.serviceBeginTime = simulationTime + (long)(Math.random() * 100);
			request.dispatchTime = (request.serviceBeginTime)+(request.serviceTime);
			request.id = i;
			requestList.add(request);
			totalWaitTime = totalWaitTime + (request.waitTime());
			totalServiceTime = totalServiceTime + (request.serviceTime);

			stats.collectStatisticsForDispatched(request);
		}

		System.out.println(stats.requestDispatched);
		averageWaitTime = totalWaitTime*1.0/numberOfRequests;
		averageServiceTime = totalServiceTime*1.0/numberOfRequests;
		Assert.assertEquals(averageWaitTime, stats.averageWaitTime);
		Assert.assertEquals(averageServiceTime, stats.averageServiceTime);
		Assert.assertEquals(totalServiceTime, stats.totalServiceTime);
		Assert.assertEquals(totalWaitTime, stats.totalWaitTime);
		Assert.assertEquals(numberOfRequests, stats.requestDispatched);

	}

}
