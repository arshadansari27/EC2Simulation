package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventManagerTest {

	EventManager eventManager;
	static Logger log = Logger.getLogger(EventManagerTest.class.getName());
	@Before
	public void setUp() throws Exception {
		eventManager = new EventManager();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddEvent() throws InterruptedException {
		eventManager.clearFEL();
		Event mockEvent=null;
		for(int i = 1; i <= 1000; i++){
			mockEvent = new Event((long)(new Random().nextInt(i)), null);
			eventManager.addSingleEvent(mockEvent);
		}
		Assert.assertTrue(eventManager.getSize()==1000);
		Long currentTime = 0L;
		Event event = null;
		while( eventManager.hasNextEvent()){
			event = eventManager.getNextEvent();
			Assert.assertTrue(event.eventTime >= currentTime);
			currentTime = event.eventTime;
		}
	}

	@Test
	public void testEventManagerWithEventGenerator() throws InterruptedException{
		eventManager.clearFEL();
		Parameters params = new Parameters();
		params.MAX_CLOCK = 10l;
		EventGenerator generator = new EventGenerator(eventManager, params);
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(generator);

		executor.equals(new Runnable() {

			@Override
			public void run() {
				log.debug("Logging just like that");
				while(true){
	            	log.debug("Getting next event");
	            	Event event = eventManager.getNextEvent();
	            	 if(event == null){
	            		 log.debug("Event didn't arrive yet!, so getting bored!");
	                     try {
	                         synchronized(this){
	                             wait(100);
	                         }
	                     } catch (InterruptedException e) {}
	                     continue;
	                 }
	            	log.debug("Event Arrived with type:"+event.getClass()+" at ["+event.eventTime+"]");
	            }

			}
		});

		executor.awaitTermination(10, TimeUnit.SECONDS);

	}
}

