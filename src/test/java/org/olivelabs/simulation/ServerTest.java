package org.olivelabs.simulation;


import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {

	Server server;
	ArrayList<Request> requests = new ArrayList<Request>();
	int requestCount;
	static Logger log = Logger.getLogger(ServerTest.class.getName());
	@Before
	public void setUp() throws Exception {

		requestCount = 4000;

		for(int i = 0; i < requestCount; i++){
			Request request = new Request();
			request.id = i;
			request.arrivalTime = (long)i/200;
	        request.serviceTime = (long)20;
			requests.add(request);
		}
	}

	@Test
	public void testServe() throws InterruptedException{
		server = new Server(1,2000,1);
		for(Request request : requests)
			server.serve(request, request.arrivalTime);

		server.shutDown(requestCount);
		ArrayList<Server> servers = new ArrayList<Server>();
		servers.add(server);
		//StatisticsCollector data = new StatisticsCollector(servers);
		//log.debug(data);
		log.debug("SERVER{"+server.getId()+"} : Served Requests: "+server.getRequestServed()+"\tRejected requests: "+server.getTotalRejected()+"\t Currently can server:"+server.getServerCapacity());
		Assert.assertTrue(server.getServerCapacity() == 2000);
		Assert.assertTrue(server.getRequestServed() == 2000);
		Assert.assertTrue(server.getTotalRejected() == 2000);

	}



	@After
	public void tearDown() throws Exception {
	}

}
