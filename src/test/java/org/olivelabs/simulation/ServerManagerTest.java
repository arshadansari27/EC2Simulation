package org.olivelabs.simulation;

import static org.junit.Assert.*;

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
		ArrayList<Request> requests = new ArrayList<Request>();
		for(int i = 0; i < 1000; i++){
			Request request = new Request();
			request.id = i;
			requests.add(request);
		}
		for(Request request : requests){
			ServerManager.getInstance().serve(request);
			System.out.println("Request# "+ request.id
						+", RequestDispatched: "+OutputStatistics.REQUEST_DISPATCHED
						+", RequestRejected: "+OutputStatistics.REQUEST_REJECTED
						+", Servers In Use: "+ServerManager.getInstance().serversInUse.size()
						+", Servers NotIn Use: "+ServerManager.getInstance().serversNotInUse.size()
						+", WaitQueue size: "+RequestWaitQueue.getInstance().queue.size()
					);
		}
		System.out.println(ServerManager.getInstance().serversInUse.size());
		Assert.assertTrue(ServerManager.getInstance().serversNotInUse.size()==0);
		Iterator<Server>  serversInUse = ServerManager.getInstance().serversInUse.iterator();
		while(serversInUse.hasNext()){
			Assert.assertTrue(serversInUse.next().getRequestListSize()==200);
		}
	}
	
	@Test
	public void testAddRemoveServer(){
		ServerManager.getInstance().serversInUse.clear();
		ServerManager.getInstance().serversNotInUse.clear();
		ServerManager.getInstance().addServer();
		Assert.assertTrue(ServerManager.getInstance().serversInUse.size()==1);
		Assert.assertTrue(ServerManager.getInstance().serversNotInUse.size()==0);
		ServerManager.getInstance().addServer();
		Assert.assertTrue(ServerManager.getInstance().serversInUse.size()==2);
		Assert.assertTrue(ServerManager.getInstance().serversNotInUse.size()==0);
		ServerManager.getInstance().addServer();
		Assert.assertTrue(ServerManager.getInstance().serversInUse.size()==3);
		Assert.assertTrue(ServerManager.getInstance().serversNotInUse.size()==0);
		ServerManager.getInstance().addServer();
		Assert.assertTrue(ServerManager.getInstance().serversInUse.size()==4);
		Assert.assertTrue(ServerManager.getInstance().serversNotInUse.size()==0);
		ServerManager.getInstance().removeServer();
		Assert.assertTrue(ServerManager.getInstance().serversInUse.size()==1);
		Assert.assertTrue(ServerManager.getInstance().serversNotInUse.size()==3);
	}

}
