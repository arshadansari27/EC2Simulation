package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class ServerManager {

	Stack<Server> serversInUse;
	Stack<Server> serversNotInUse;
    private SimulationRunner simulator;

	public ServerManager(SimulationRunner simulator) {
		this.simulator = simulator;
		serversInUse = new Stack<Server>();
		serversNotInUse = new Stack<Server>();
	}

	public void serve(Request request){

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

	public Server addServer() {

		Server server = null;
		if((serversInUse.size() + serversNotInUse.size()) >=simulator.params.maxServer)
			return server;
		if (serversNotInUse.empty())
			server = new Server(simulator, simulator.params.concurrentRequestLimit);
		else
			server = serversNotInUse.pop();
		server.setServerStartTime(simulator.getClock().CurrentTime);
		this.serversInUse.add(server);
		return server;
	}

	public Server removeServer() {
		// TODO:Improve this code
		Server serverToRemove = null;
		while (serversInUse.size() > 1) {
			serverToRemove = serversInUse.peek();
			if (serverToRemove.getServerCapacity() >= simulator.params.concurrentRequestLimit) {
				serverToRemove = serversInUse.pop();
				serverToRemove.setServerEndTime(simulator.getClock().CurrentTime);
				serversNotInUse.push(serverToRemove);
			} else
				break;
		}
		return serverToRemove;
	}

	public void free(Server server, Request request){
		server.free(request);
		simulator.getRequestStats().collectStatisticsForDispatched(request);
		Server bestServer = null;
		while(simulator.getWaitQueue().size()>0 && ( bestServer = simulator.getServerManager().getBestServer())!=null)
			bestServer.serve(simulator.getWaitQueue().get());
		removeServer();
	}

	public int busySize(){
		return this.serversInUse.size();
	}

	public int freeSize(){
		return this.serversNotInUse.size();
	}

	public List<Server> getAllServers(){
		List<Server> allServers = new ArrayList<Server>();
		if(busySize()>0)
			allServers.addAll(serversInUse);
		if(freeSize()>0)
			allServers.addAll(serversNotInUse);
		return allServers;
	}

	public List<String> getServerHistories(){
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
