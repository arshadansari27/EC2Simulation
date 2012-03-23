package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventManagerTest {

	EventManager eventManager;
	@Before
	public void setUp() throws Exception {
		eventManager = new SimulationRunner(new Parameters()).getEventManager();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetNextEvent() {
		MockEvent mockEvent=null;
		List<Event> events = new ArrayList<Event>();
		for(int i = 0; i < 1000; i++){
			mockEvent = new MockEvent();
			mockEvent.eventTime = (long)i;
			events.add(mockEvent);
		}
		eventManager.addEvents(events);
		Event event = null;
		Long currentTime = 0L;
		while( ( event = eventManager.getNextEvent()) != null){
			event.processEvent();
			Assert.assertTrue(event.eventTime > currentTime);
			currentTime = event.eventTime;
		}

	}

	@Test
	public void testAddEvent() {
		eventManager.clearFEL();
		MockEvent mockEvent=null;
		List<Event> events = new ArrayList<Event>();
		for(int i = 1; i <= 1000; i++){
			mockEvent = new MockEvent();
			mockEvent.eventTime = (long)(new Random()).nextInt(1000);
			events.add(mockEvent);
		}
		eventManager.addEvents(events);
		Assert.assertTrue(eventManager.getSize()==1000);
	}

}

class MockEvent extends Event{
	static Long clock = 1L;
	public void processEvent(){
		clock = clock+this.eventTime;
		eventTime = clock;
	}
}