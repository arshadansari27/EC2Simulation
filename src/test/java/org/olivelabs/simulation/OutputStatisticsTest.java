package org.olivelabs.simulation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class OutputStatisticsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testCollectStatistics() {
		long simulationTime = 0L;
		List<Request> requestList = new ArrayList<Request>();
		long numberOfRequests = 100;
		long totalWaitTime = 0;
		long totalServiceTime = 0l;
		double averageWaitTime = 0.0;
		double averageServiceTime = 0.0;
		OutputStatistics stats = new OutputStatistics(null);
		for(int i = 0; i < numberOfRequests; i++){
			Request request = new Request();
			simulationTime += (long)(Math.random() * 1000);
			request.arrivalTime = simulationTime;
			request.serviceTime = (long)(Math.random() * 1000);
			request.serviceBeginTime = simulationTime + (long)(Math.random() * 100);
			request.dispatchTime = request.serviceBeginTime + request.serviceTime;
			request.id = i;
			requestList.add(request);
			totalWaitTime += request.waitTime();
			totalServiceTime += request.serviceTime;

			stats.collectStatisticsForDispatched(request);
		}
		averageWaitTime = (1.0*totalWaitTime)/numberOfRequests;
		averageServiceTime = (1.0*totalServiceTime)/numberOfRequests;

		Assert.assertEquals(averageWaitTime, stats.AVERAGE_WAIT_TIME);
		Assert.assertEquals(averageServiceTime, stats.AVERAGE_SERVICE_TIME);
		Assert.assertEquals(totalServiceTime, stats.TOTAL_SERVICE_TIME);
		Assert.assertEquals(totalWaitTime, stats.TOTAL_WAIT_TIME);
		Assert.assertEquals(numberOfRequests, stats.REQUEST_DISPATCHED);

	}

}
