package org.olivelabs.simulation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerManagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testServeFree() {
		SimulationRunner simulator = new SimulationRunner(new Parameters());
		ArrayList<Request> requests = new ArrayList<Request>();
		for(int i = 0; i < simulator.params.concurrentRequestLimit+100; i++){
			Request request = new Request();
			request.id = i;
			request.arrivalTime = new BigInteger(""+(i*2));
			request.serviceTime = new BigInteger(""+(i*2+1));
			request.serviceBeginTime = new BigInteger(""+(long)(Math.random()*100000));
			requests.add(request);
		}
		//OutputStatistics stats = new OutputStatistics(simulator);
		for(Request request : requests){
			simulator.getServerManager().serve(request);
		}
		Assert.assertTrue(simulator.getServerManager().serversNotInUse.size()==0);
		Iterator<Server>  serversInUse = simulator.getServerManager().serversInUse.iterator();
		while(serversInUse.hasNext()){
			Server server = serversInUse.next();
			Assert.assertTrue(server.getRequestListSize()>=simulator.params.concurrentRequestLimit);
		}
	}

	@Test
	public void testAddRemoveServer(){
		SimulationRunner simulator = new SimulationRunner(new Parameters());
		ServerManager svrMgr = simulator.getServerManager();
		svrMgr.serversInUse.clear();
		svrMgr.serversNotInUse.clear();
		svrMgr.addServer();
		Assert.assertTrue(svrMgr.serversInUse.size()==1);
		Assert.assertTrue(svrMgr.serversNotInUse.size()==0);
		svrMgr.addServer();
		Assert.assertTrue(svrMgr.serversInUse.size()==2);
		Assert.assertTrue(svrMgr.serversNotInUse.size()==0);
		svrMgr.addServer();
		Assert.assertTrue(svrMgr.serversInUse.size()==3);
		Assert.assertTrue(svrMgr.serversNotInUse.size()==0);
		svrMgr.addServer();
		Assert.assertTrue(svrMgr.serversInUse.size()==4);
		Assert.assertTrue(svrMgr.serversNotInUse.size()==0);
		svrMgr.removeServer();
		Assert.assertTrue(svrMgr.serversInUse.size()==1);
		Assert.assertTrue(svrMgr.serversNotInUse.size()==3);
	}

}
