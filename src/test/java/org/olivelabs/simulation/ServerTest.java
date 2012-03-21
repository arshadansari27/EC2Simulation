package org.olivelabs.simulation;


import java.math.BigInteger;
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

		server = new Server(new SimulationRunner(new Parameters()),200);
		for(int i = 0; i < 100; i++){
			Request request = new Request();
			request.id = i;
			request.arrivalTime = BigInteger.TEN;
	        request.serviceTime = BigInteger.TEN;
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
			server.setServerStartTime(new BigInteger(""+ i*2));
			server.setServerEndTime(new BigInteger(""+ i*2+1));
		}
		for(ServerHistory serverhistory : server.serverHistory)
			Assert.assertTrue(serverhistory.startTime.compareTo(serverhistory.endTime) < 0);
	}

	@After
	public void tearDown() throws Exception {
	}

}
