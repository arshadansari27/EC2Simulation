package org.olivelabs.simulation;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
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

	@Test
	public void testCollectStatistics() {
		BigInteger simulationTime = BigInteger.ZERO;
		List<Request> requestList = new ArrayList<Request>();
		long numberOfRequests = 100;
		BigInteger totalWaitTime = BigInteger.ZERO;
		BigInteger totalServiceTime = BigInteger.ZERO;
		double averageWaitTime = 0.0;
		double averageServiceTime = 0.0;
		OutputStatistics stats = new OutputStatistics(null);
		for(int i = 0; i < numberOfRequests; i++){
			Request request = new Request();
			simulationTime = simulationTime.add(new BigInteger((long)Math.random() * 1000+""));
			request.arrivalTime = simulationTime;
			request.serviceTime = new BigInteger((long)Math.random() * 1000+"");
			request.serviceBeginTime = simulationTime.add( new BigInteger(((long)Math.random() * 100+"")));
			request.dispatchTime = BigInteger.ZERO.add(request.serviceBeginTime).add(request.serviceTime);
			request.id = i;
			requestList.add(request);
			totalWaitTime.add(request.waitTime());
			totalServiceTime.add(request.serviceTime);

			stats.collectStatisticsForDispatched(request);
		}
		averageWaitTime = new BigDecimal(totalWaitTime).divide(new BigDecimal(numberOfRequests)).doubleValue();
		averageServiceTime = new BigDecimal(totalServiceTime).divide(new BigDecimal(numberOfRequests)).doubleValue();

		Assert.assertEquals(averageWaitTime, stats.AVERAGE_WAIT_TIME);
		Assert.assertEquals(averageServiceTime, stats.AVERAGE_SERVICE_TIME);
		Assert.assertEquals(totalServiceTime, stats.TOTAL_SERVICE_TIME);
		Assert.assertEquals(totalWaitTime, stats.TOTAL_WAIT_TIME);
		Assert.assertEquals(numberOfRequests, stats.REQUEST_DISPATCHED.longValue());

	}

}
