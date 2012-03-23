package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventGeneratorTest {

	private SimulationRunner simulator;
	private static List<Request> requestList = new ArrayList<Request>();

	@Before
	public void setUp() throws Exception {
		simulator = new SimulationRunner(new Parameters());
		simulator.getEventManager().clearFEL();
	}

	@After
	public void tearDown() throws Exception {
		simulator.getEventManager().clearFEL();
	}


	@Test
	public void testGenerateNextArrivalEvent() {
		simulator.params.MAX_CLOCK = 100L;
		simulator.params.averageRequestPerSecond = 10L;
		simulator.params.minRequestPerSecond= 10L;
		simulator.params.maxRequestPerSecond = 10L;
		simulator.getEventGenerator().generateArrivalEventsInBatch(0);
		Event event  = null;
		while((event = simulator.getEventManager().getNextEvent())!=null){
			Assert.assertTrue(event instanceof ArrivalEvent);
			Assert.assertFalse(event instanceof DispatchEvent);
			ArrivalEvent arrEvent = (ArrivalEvent) event;
			requestList.add(arrEvent.request);
		}
		Assert.assertTrue(requestList.size()==10);
	}

	@Test
	public void testGenerateDispatchEvent() {
		requestList = new ArrayList<Request>();
		for(int i = 0; i < 10; i++){
			Request request = new Request();
			request.id = i;
			request.arrivalTime = 1L;
			request.serviceTime = 1L;
			request.serviceBeginTime = 1L;
			requestList.add(request);
		}

		for(Request request : requestList)
			simulator.getEventGenerator().generateDispatchEvent(request, new Server(simulator, 10));
		Event event = null;
		while((event = simulator.getEventManager().getNextEvent())!=null){
			Assert.assertTrue(event instanceof DispatchEvent);
			Assert.assertFalse(event instanceof ArrivalEvent);

		}
		requestList.clear();
	}

}
