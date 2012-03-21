package org.olivelabs.simulation;

import static org.junit.Assert.*;

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
		simulator = new SimulationRunner(1000);
		simulator.getEventManager().fel.clear();
	}

	@After
	public void tearDown() throws Exception {
		simulator.getEventManager().fel.clear();
	}


	@Test
	public void testGenerateNextArrivalEvent() {
		for(int i = 0; i<10;i++)
			simulator.getEventGenerator().generateNextArrivalEvent();
		System.out.println(simulator.getEventManager().fel.size());
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
		for(Request request : requestList)
			simulator.getEventGenerator().generateDispatchEvent(request, new Server(simulator, 10));
		Event event  = null;
		while((event = simulator.getEventManager().getNextEvent())!=null){
			Assert.assertTrue(event instanceof DispatchEvent);
			Assert.assertFalse(event instanceof ArrivalEvent);

		}
		requestList.clear();
	}

}
