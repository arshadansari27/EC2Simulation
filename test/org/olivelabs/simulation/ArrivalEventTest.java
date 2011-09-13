package org.olivelabs.simulation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArrivalEventTest {

	private EventManager eventMgr;
	private static List<Request> requestList = new ArrayList<Request>();

	@Before
	public void setUp() throws Exception {
		eventMgr = EventManager.getInstance();
		eventMgr.fel.clear();
	}

	@After
	public void tearDown() throws Exception {
		eventMgr.fel.clear();
	}

	@Test
	public void testProcessArrivalEvent() {
		//EventGenerator.TOTAL_REQUEST = 10;
		EventGenerator.generateNextArrivalEvent();
		System.out.println("Initial FEL SIZE :" + eventMgr.fel.size());
		Event event = null;
		while ((event = eventMgr.getNextEvent()) != null) {
			System.out.print("Current FEL SIZE :" + eventMgr.fel.size());
			System.out.print("\tWait Queue Size : "+RequestWaitQueue.getInstance().size());
			System.out.print("\tServers In Use: "+ServerManager.getInstance().serversInUse.size());
			System.out.println("\tServers Not In Use: "+ServerManager.getInstance().serversNotInUse.size());
			if (event instanceof ArrivalEvent) {
				ArrivalEvent arrEvent = (ArrivalEvent) event;
				requestList.add(arrEvent.request);
				arrEvent.processEvent();
			} else
				System.out.println("Dispatching!");
		}

	}
}
