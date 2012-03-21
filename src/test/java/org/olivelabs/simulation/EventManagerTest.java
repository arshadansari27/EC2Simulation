package org.olivelabs.simulation;

import static org.junit.Assert.*;

import java.math.BigInteger;

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
		for(int i = 0; i < 1000; i++){
			mockEvent = new MockEvent();
			mockEvent.eventTime = new BigInteger((long)Math.random() * 1000 + "");
			eventManager.addEvent(mockEvent);
		}
		Event event = null;
		BigInteger currentTime = BigInteger.ZERO;
		while( ( event = eventManager.getNextEvent()) != null){
			Assert.assertTrue(event.eventTime.compareTo(currentTime) >=0);
			currentTime = event.eventTime;
		}

	}

	@Test
	public void testAddEvent() {
		eventManager.fel.clear();
		MockEvent mockEvent=null;
		for(int i = 1; i <= 1000; i++){
			mockEvent = new MockEvent();
			mockEvent.eventTime = new BigInteger((long)Math.random() * 1000+"");
			eventManager.addEvent(mockEvent);
			Assert.assertTrue(eventManager.fel.size()==i);
		}
	}

}

class MockEvent extends Event{
	public void processEvent(){
		System.out.println("Test Event Occuring at time : "+this.eventTime+"!!!");
	}
}