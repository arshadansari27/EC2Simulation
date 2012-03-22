package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ServerManager implements Runnable{

	Stack<Server> serversInUse;
	Stack<Server> serversNotInUse;
    private SimulationRunner simulator;
    private BlockingQueue<Request> toServe;
    private BlockingQueue<ArrayList<Object>> toFree;

	public ServerManager(SimulationRunner simulator) {
		this.simulator = simulator;
		serversInUse = new Stack<Server>();
		serversNotInUse = new Stack<Server>();
		toServe =  new LinkedBlockingQueue<Request>();
		toFree = new LinkedBlockingQueue<ArrayList<Object>>();
	}

	public void run(){

		while(this.simulator.RUNNING){



			Iterator<ArrayList<Object>> list_free = toFree.iterator();
			for(;list_free.hasNext();){
				ArrayList<Object> dataToFree = list_free.next();
				Server 	server 	= (Server) 	dataToFree.get(0);
				Request request = (Request) dataToFree.get(1);
				server.free(request);
				simulator.getRequestStats().collectStatisticsForDispatched(request);
				Server bestServer = null;
				while(simulator.getWaitQueue().size()>0 && ( bestServer = simulator.getServerManager().getBestServer())!=null)
					bestServer.serve(simulator.getWaitQueue().get());
				removeServer();
			}
			toFree.clear();

			Iterator<Request> list_serve = toServe.iterator();
			for(;list_serve.hasNext();){
				Request request = list_serve.next();
				if(serversInUse.size()<=0 ||
						(simulator.getWaitQueue().size()>=simulator.params.waitQueueMaxSize &&
								(serversInUse.size() + serversNotInUse.size()) < simulator.params.maxServer))
					addServer().serve(request);

				Server server = getBestServer();
				if((server == null || simulator.getWaitQueue().size()>=1) && simulator.getWaitQueue().size() < simulator.params.waitQueueMaxSize){
					simulator.getWaitQueue().add(request);
					return;
				}

				if(server != null){
					server.serve(request);
					while(simulator.getWaitQueue().size()>0 && (server = getBestServer())!=null){
						server.serve(simulator.getWaitQueue().get());

					}
				}
				else{
					simulator.getRequestStats().collectStatisticsForRejected(request);

				}
			}
			toServe.clear();
			try {
				synchronized(this){
					wait();
				}

			} catch (InterruptedException e) {}
		}


	}

	public synchronized void free(Server server, Request request){
		ArrayList<Object> dataToFree = new ArrayList<Object>();
		dataToFree.add(0,server);
		dataToFree.add(1,request);
		toFree.add(dataToFree);
		notifyAll();
	}

	public synchronized void serve(Request request){
		toServe.add(request);
		notifyAll();
	}

	private Server getBestServer() {
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

		Server server = null;
		if((serversInUse.size() + serversNotInUse.size()) >=simulator.params.maxServer)
			return server;
		if (serversNotInUse.empty())
			server = new Server(simulator, simulator.params.concurrentRequestLimit);
		else
			server = serversNotInUse.pop();
		server.setServerStartTime(simulator.getClock().CurrentTime.get());
		this.serversInUse.add(server);
		return server;
	}

	public synchronized Server removeServer() {
		// TODO:Improve this code
		Server serverToRemove = null;
		while (serversInUse.size() > 1 || (!simulator.RUNNING && serversInUse.size()==1)) {
			serverToRemove = serversInUse.peek();
			if (serverToRemove.getServerCapacity() >= simulator.params.concurrentRequestLimit || !simulator.RUNNING ) {
				serverToRemove = serversInUse.pop();
				serverToRemove.setServerEndTime(simulator.getClock().CurrentTime.get());
				serversNotInUse.push(serverToRemove);
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

	public synchronized List<String> getServerHistories(){

		List<Server> servers = getAllServers();
		List<String> serverHistories = new ArrayList<String>();

		for(Server server : servers){
			StringBuilder builder = new StringBuilder();
			builder.append(String.format("Server [%1$2s]: currReqCount[%2$2s] => ", server.toString(), server.getRequestServed()));
			for(ServerHistory history : server.serverHistory){
				builder.append(String.format("%10d =>%10d, ",history.startTime,history.endTime));

			}
			serverHistories.add(builder.toString());
		}
		return serverHistories;

	}
}
