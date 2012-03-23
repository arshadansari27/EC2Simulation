package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

	@Test
	public void testServeFreeThreaded() throws Exception{
		ExecutorService exec = Executors.newCachedThreadPool();
		final SimulationRunner simulator = new SimulationRunner(new Parameters());

		class RequestServer implements Runnable{
			public void run(){
				ArrayList<Request> requests = new ArrayList<Request>();
				for(int i = 0; i < simulator.params.concurrentRequestLimit+1; i++){
					Request request = new Request();
					request.id = i;
					request.arrivalTime = i*2L;
					request.serviceTime = i*2L+1;
					request.serviceBeginTime = (long)(Math.random()*100000);
					requests.add(request);
				}
				//OutputStatistics stats = new OutputStatistics(simulator);
				int countServed = 0;
				for(Request request : requests){
					System.out.println("Serving request "+request.id+" from server thread....");
					simulator.getServerManager().serve(request);
					countServed++;
				}
				Iterator<Server>  serversInUse = simulator.getServerManager().serversInUse.iterator();
				while(serversInUse.hasNext()){
					Server server = serversInUse.next();
				}
				int countFreed = 0;


				while(true){
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Event e = simulator.getEventManager().getNextEvent();
					if(e instanceof DispatchEvent){
						simulator.getServerManager().free(((DispatchEvent) e).server, ((DispatchEvent) e).request);
						countFreed++;
						System.out.println("Freeing request "+((DispatchEvent) e).request.id+" from server thread....");
					}
					if(countFreed == countServed){
						break;
					}
				}
				System.out.println(countServed);
				System.out.println(countFreed);

				Assert.assertTrue(countServed == simulator.params.concurrentRequestLimit+1);
				Assert.assertTrue(countFreed == simulator.params.concurrentRequestLimit+1);
				Iterator<Server> servers = simulator.getServerManager().getAllServers().iterator();
				while(servers.hasNext()){
					Assert.assertTrue(servers.next().getRequestListSize()==0);

				}

				simulator.RUNNING.set(false);

			}
		}
		exec.execute(simulator.getServerManager().getRunnableProcess());
		exec.execute(new RequestServer());
		exec.awaitTermination(5, TimeUnit.SECONDS);

	}

}
