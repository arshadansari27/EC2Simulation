package org.olivelabs.simulation;


import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {

	Server server;
	ArrayList<Request> requests = new ArrayList<Request>();
	@Before
	public void setUp() throws Exception {

		server = new Server(new SimulationRunner(100),200);
		for(int i = 0; i < 100; i++){
			Request request = new Request();
			request.id = i;
			requests.add(request);
		}
	}

	@Test
	public void testServe(){
		for(Request request : requests)
			server.serve(request);

		Assert.assertTrue(server.getRequestListSize() == 100);
		Assert.assertTrue(server.getServerCapacity() == 100);
	}

	@Test
	public void testFree(){
		for(Request request : requests)
			server.serve(request);
		for(Request request : requests)
			server.free(request);
		Assert.assertTrue(server.getRequestListSize() == 0);
		Assert.assertTrue(server.getServerCapacity() == 200);
	}

	@Test
	public void testServerCapacity(){
		int count = 200;
		for(Request request : requests){
			server.serve(request);
			Assert.assertTrue(server.getServerCapacity() == --count);
		}
		for(Request request : requests){
			server.free(request);
			Assert.assertTrue(server.getServerCapacity() == ++count);
		}


	}


	@Test
	public void testServerTimeHistory(){
		for(int i = 0; i < 10; i++){
			server.setServerStartTime((long) (Math.random() * 1000000));
			server.setServerEndTime((long) (Math.random() * 1000000));
		}
		for(ServerHistory serverhistory : server.serverHistory)
			System.out.print("\n "+serverhistory.startTime+" --> "+serverhistory.endTime+", ");
		System.out.println();
	}

	@After
	public void tearDown() throws Exception {
	}

}
