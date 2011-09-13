package org.olivelabs.simulation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventGeneratorTest {

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
	public void testGenerateNextArrivalEvent() {
		for(int i = 0; i<10;i++)
			EventGenerator.generateNextArrivalEvent();
		System.out.println(eventMgr.fel.size());
		Event event  = null;
		while((event = eventMgr.getNextEvent())!=null){
			Assert.assertTrue(event instanceof ArrivalEvent);
			Assert.assertFalse(event instanceof DispatchEvent);
			ArrivalEvent arrEvent = (ArrivalEvent) event;
			requestList.add(arrEvent.request);
		}
		System.out.println(requestList.size());
		Assert.assertTrue(requestList.size()==10);
	}

	@Test
	public void testGenerateDispatchEvent() {
		for(Request request : requestList)
			EventGenerator.generateDispatchEvent(request, new Server(10));
		System.out.println(eventMgr.fel.size());
		Event event  = null;
		while((event = eventMgr.getNextEvent())!=null){
			Assert.assertTrue(event instanceof DispatchEvent);
			Assert.assertFalse(event instanceof ArrivalEvent);
			
		}
		requestList.clear();
	}

}
