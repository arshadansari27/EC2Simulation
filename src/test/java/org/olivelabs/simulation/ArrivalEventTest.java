package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArrivalEventTest {

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
	public void testProcessArrivalEvent() {
		simulator.params.MAX_CLOCK = 100L;
		simulator.getEventGenerator().generateArrivalEventsInBatch(0);
		Event event = null;
		while ((event = simulator.getEventManager().getNextEvent()) != null) {
			if (event instanceof ArrivalEvent) {
				ArrivalEvent arrEvent = (ArrivalEvent) event;
				requestList.add(arrEvent.request);
				arrEvent.processEvent();

			}
			else{
				break;
			}
		}
		//Assert.assertTrue(1000<=requestList.size());

	}
}
