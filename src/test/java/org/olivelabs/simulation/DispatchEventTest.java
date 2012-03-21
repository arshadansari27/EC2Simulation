package org.olivelabs.simulation;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DispatchEventTest {

	private SimulationRunner simulator;
	private static List<Request> requestList = new ArrayList<Request>();

	@Before
	public void setUp() throws Exception {
		simulator = new SimulationRunner(new Parameters());
		simulator.getEventManager().fel.clear();
	}

	@After
	public void tearDown() throws Exception {
		simulator.getEventManager().fel.clear();
	}

	@Test
	public void testProcessDispatchEvent() {
		//EventGenerator.TOTAL_REQUEST = 10;

		simulator.getEventGenerator().generateNextArrivalEvent();
		Event event = null;
		while ((event = simulator.getEventManager().getNextEvent()) != null) {
			event.processEvent();
			if (event instanceof DispatchEvent) {
				DispatchEvent dEvent = (DispatchEvent) event;
				requestList.add(dEvent.request);
			}
			if(1000<=requestList.size()){
				break;
			}
		}



	}

}
