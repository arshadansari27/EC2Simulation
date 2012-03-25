package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerManagerTest {

	ExecutorService executor;
	static Logger log = Logger.getLogger(ServerManagerTest.class.getName());

	@Before
	public void setUp() throws Exception {
		executor = Executors.newCachedThreadPool();
	}

	@After
	public void tearDown() throws Exception {
		executor.shutdownNow();
	}

	@Test
	public void testAddRemoveServer() {
		ServerManager svrMgr = new ServerManager(executor, new Parameters(),1);

		svrMgr.addServer();
		Assert.assertTrue(svrMgr.getAllServers().size() == 1);
		Assert.assertTrue(svrMgr.freeSize() == 0);
		svrMgr.addServer();
		Assert.assertTrue(svrMgr.busySize() == 2);
		Assert.assertTrue(svrMgr.freeSize() == 0);
		svrMgr.addServer();
		Assert.assertTrue(svrMgr.busySize() == 3);
		Assert.assertTrue(svrMgr.freeSize() == 0);
		svrMgr.addServer();
		Assert.assertTrue(svrMgr.busySize() == 4);
		Assert.assertTrue(svrMgr.freeSize() == 0);
		svrMgr.removeServer();
		Assert.assertTrue(svrMgr.busySize() == 1);
		Assert.assertTrue(svrMgr.freeSize() == 3);
	}

	@Test
	public void testServeFreeThreaded() throws Exception {

		ArrayList<Request> requests = new ArrayList<Request>();
		for (int i = 0; i < 50000; i++) {
			Request request = new Request();
			request.id =i ;
			request.arrivalTime = (long)i/2000;
			request.serviceTime = 10L;
			requests.add(request);
		}
		int countServed = 0;
		Parameters params = new Parameters();
		params.concurrentRequestLimit = 1000;

		ServerManager serverManager = new ServerManager(executor, params,0);
		executor.execute(serverManager);
		log.debug("Serving "+requests.size()+" requests");
		for (Request request : requests) {
			serverManager.serve(request);
			countServed++;
		}
		log.debug("Sent All Requests");
		serverManager.stop(Integer.MAX_VALUE);
		log.debug("Stoppinh server manager");
		while(serverManager.isRunning()){
			log.debug("Waiting..");

			executor.awaitTermination(1, TimeUnit.SECONDS);
		}

		log.debug("Servers stopped running");
		log.debug("Total Requests served : "+countServed);

		for(Server server : serverManager.getAllServers()){
			log.debug("SERVER {"+server.getId()+"} : Served Requests: "+server.getRequestServed()+"\tRejected requests: "+server.getTotalRejected()+"\t Currently can server:"+server.getServerCapacity());
		}

	}

	@Test
	public void testServeFreeThreadedAndChained() throws Exception {

		ArrayList<Request> requests = new ArrayList<Request>();
		for (int i = 0; i < 50000; i++) {
			Request request = new Request();
			request.id = i ;
			request.arrivalTime = (long)i/1000;
			request.serviceTime = 10L;
			requests.add(request);
		}
		int countServed = 0;
		Parameters params = new Parameters();
		params.concurrentRequestLimit = 1000;
		params.maxServer =5;
		ServerManager serverManager = new ServerManager(executor, params,5);
		executor.execute(serverManager);
		log.debug("Serving "+requests.size()+" requests");
		for (Request request : requests) {
			serverManager.serve(request);
			countServed++;
		}

		log.debug("Sent All Requests");
		serverManager.stop(Integer.MAX_VALUE);

		while(serverManager.isRunning()){
			executor.awaitTermination(1, TimeUnit.SECONDS);
			log.debug("Simulation time: "+serverManager.getSimulationTime());
		}


		log.debug("Servers stopped running");
		log.debug("Total Requests served : "+countServed);

		log.debug("Servers stopped running");
		log.debug("Total Requests served : "+countServed);

		for(Server server : serverManager.getAllServers()){
			log.debug("SERVER {"+server.getId()+"} : Served Requests: "+server.getRequestServed()+"\tRejected requests: "+server.getTotalRejected()+"\t Currently can server:"+server.getServerCapacity());
		}



	}

}
