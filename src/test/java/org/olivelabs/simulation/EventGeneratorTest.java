package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventGeneratorTest {

	private static List<Request> requestList = new ArrayList<Request>();

	EventManager eventManager;
	EventGenerator eventGenerator;

	@Before
	public void setUp() throws Exception {
		Parameters params = new Parameters();
		params.MAX_CLOCK = 100L;
		params.averageRequestPerSecond = 10L;
		params.minRequestPerSecond= 10L;
		params.maxRequestPerSecond = 10L;
		eventManager = new EventManager();
		eventGenerator = new EventGenerator(eventManager, params);
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testGenerateNextArrivalEvent() {
		eventGenerator.generateArrivalEventsInBatch(0);
		while(eventManager.hasNextEvent()){
			Event arrEvent =  eventManager.getNextEvent();
			requestList.add(arrEvent.request);
		}
		Assert.assertTrue(requestList.size()==10);
	}



}
