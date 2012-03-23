package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class ServerManager{

	Stack<Server> serversInUse;
	Stack<Server> serversNotInUse;
    private SimulationRunner simulator;
    private BlockingQueue<Request> toServe;
    private BlockingQueue<ArrayList<Object>> toFree;
    private volatile AtomicReference<Long> CurrentTime = new AtomicReference<Long>(-1L);


	public ServerManager(SimulationRunner simulator) {
		this.simulator = simulator;
		serversInUse = new Stack<Server>();
		serversNotInUse = new Stack<Server>();
		toServe =  new LinkedBlockingQueue<Request>();
		toFree = new LinkedBlockingQueue<ArrayList<Object>>();
	}

	public ServerManagerProcess getRunnableProcess(){
		return new ServerManagerProcess();
	}
	class ServerManagerProcess implements Runnable{
		public void run(){

			while(simulator.RUNNING.get() || toFree.size()>0 || toServe.size()>0){

				synchronized(this){
					Iterator<ArrayList<Object>> list_free = toFree.iterator();
					for(;list_free.hasNext();){
						Logger.log(this.getClass(), ": Iterating to free requests", 10000);
						ArrayList<Object> dataToFree = list_free.next();
						Server 	server 	= (Server) 	dataToFree.get(0);
						Request request = (Request) dataToFree.get(1);
						server.free(request);
						CurrentTime.set(request.dispatchTime);
						simulator.getRequestStats().collectStatisticsForDispatched(request);
						Server bestServer = null;
						while(simulator.getWaitQueue().size()>0 && ( bestServer = simulator.getServerManager().getBestServer())!=null){
							request = simulator.getWaitQueue().get();
							if(request != null && server != null){
								bestServer.serve(request);
							}
							else if(request != null){
								simulator.getRequestStats().collectStatisticsForRejected(request);
							}

						}
						removeServer();
					}
					toFree.clear();
				}
				synchronized (this) {
					Iterator<Request> list_serve = toServe.iterator();
					for(;list_serve.hasNext();){

						Request request = list_serve.next();
						CurrentTime.set(request.arrivalTime);
						Logger.log(this.getClass(), ": Iterating to serve requests", 10000);

						if(serversInUse.size()<=0 ||
								(simulator.getWaitQueue().size()>=simulator.params.waitQueueMaxSize &&
										serversInUse.size()  < simulator.params.maxServer)){
							Server server = addServer();
							if(server != null)
								server.serve(request);
							else
								simulator.getRequestStats().collectStatisticsForRejected(request);
							continue;
						}



						Server server = getBestServer();
						if((server == null || simulator.getWaitQueue().size()>=1) && simulator.getWaitQueue().size() < simulator.params.waitQueueMaxSize){
							simulator.getWaitQueue().add(request);
							continue;
						}

						if(server != null){
							server.serve(request);
							while(simulator.getWaitQueue().size()>0 && (server = getBestServer())!=null){
								request = simulator.getWaitQueue().get();
								if(request != null)
									server.serve(request);
							}
						}
						else{

							String message = "Reject request with WQSize: "+simulator.getWaitQueue().size()+", Servers ["
																+serversInUse.size()+"/"+(serversInUse.size()+serversNotInUse.size() ) ;
							Logger.log(this.getClass(), ": "+message, 10000);

							simulator.getRequestStats().collectStatisticsForRejected(request);
						}

					}
					toServe.clear();
				}
				try {
					synchronized(this){
						wait(50);
					}

				} catch (InterruptedException e) {}

			}
			removeServer();
			for(Entry<Long, Integer> history : simulator.getRequestStats().serverStats.entrySet()){
				System.out.println(history.getKey()+" => "+history.getValue());
			}

		}
	}
	public synchronized void free(Server server, Request request){
		Logger.log(this.getClass(), ": Freeing old Request", 10000);
		ArrayList<Object> dataToFree = new ArrayList<Object>();
		dataToFree.add(0,server);
		dataToFree.add(1,request);
		toFree.add(dataToFree);
		notifyAll();
	}

	public synchronized void serve(Request request){
		Logger.log(this.getClass(), ": Adding new Request", 10000);
		toServe.add(request);
		notifyAll();
	}

	private synchronized Server getBestServer() {
		Server bestServer = null;
		Iterator<Server> serverIterator = serversInUse.iterator();
		while(serverIterator.hasNext()) {
			Server server = serverIterator.next();
			if(server.getServerCapacity() <= 0)
				continue;
			bestServer = server;
			break;
		}
		 return bestServer;
	}

	public synchronized Server addServer() {

		Logger.log(this.getClass(), ":Adding server", 100);

		if(serversInUse.size() >=simulator.params.maxServer)
			return null;
		Server server = null;
		if (serversNotInUse.empty())
			server = new Server(simulator, simulator.params.concurrentRequestLimit);
		else
			server = serversNotInUse.pop();

		this.serversInUse.add(server);
		this.simulator.getRequestStats().collectStatisticsForServerHistory(this.CurrentTime.get(), serversInUse.size());
		return server;
	}

	public synchronized Server removeServer() {

		// TODO:Improve this code
		Server serverToRemove = null;
		while (serversInUse.size() > 1 || (!simulator.RUNNING.get() && serversInUse.size()==1)) {
			serverToRemove = serversInUse.peek();
			if (serverToRemove.getServerCapacity() >= simulator.params.concurrentRequestLimit || !simulator.RUNNING.get() ) {
				serverToRemove = serversInUse.pop();
				serversNotInUse.push(serverToRemove);
				this.simulator.getRequestStats().collectStatisticsForServerHistory(this.CurrentTime.get(), serversInUse.size());
				Logger.log(this.getClass(), ":Removing server", 100);
			} else
				break;
		}
		return serverToRemove;
	}


	public synchronized int busySize(){
		return this.serversInUse.size();
	}

	public synchronized int freeSize(){
		return this.serversNotInUse.size();
	}

	public synchronized List<Server> getAllServers(){
		List<Server> allServers = new ArrayList<Server>();
		if(busySize()>0)
			allServers.addAll(serversInUse);
		if(freeSize()>0)
			allServers.addAll(serversNotInUse);
		return allServers;
	}


}
