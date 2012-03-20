package org.olivelabs.simulation;

import java.util.Iterator;
import java.util.Stack;

public class ServerManager {

	Stack<Server> serversInUse;
	Stack<Server> serversNotInUse;
	static ServerManager serverManager = null;
    RequestWaitQueue waitQueue;
	private ServerManager() {
		serversInUse = new Stack<Server>();
		serversNotInUse = new Stack<Server>();
		waitQueue = RequestWaitQueue.getInstance();
	}

	public static ServerManager getInstance() {
		if (serverManager == null)
			serverManager = new ServerManager();
		return serverManager;
	}

	public void serve(Request request){
		
		if(serversInUse.size()<=0 ||
				(waitQueue.size()>=Constants.WAIT_QUEUE_SIZE && 
						(serversInUse.size() + serversNotInUse.size()) < Constants.MAX_SERVER)) 
			addServer().serve(request);
		
		Server server = getBestServer();
		if((server == null || waitQueue.size()>=1) && waitQueue.size() < Constants.WAIT_QUEUE_SIZE){
			waitQueue.add(request);
			return;
		}
		
		if(server != null){
			server.serve(request);
			while(waitQueue.size()>0 && (server = getBestServer())!=null){
				server.serve(waitQueue.get());
				
			}
		}
		else{
			OutputStatistics.collectStatisticsForRejected(request);
			
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
		if((serversInUse.size() + serversNotInUse.size()) >=Constants.MAX_SERVER)
			return server;
		if (serversNotInUse.empty())
			server = new Server(Constants.SERVER_CONCURRENT_REQUEST_LIMIT);
		else
			server = serversNotInUse.pop();
		server.setServerStartTime(SimulationClock.CurrentTime);
		this.serversInUse.add(server);
		return server;
	}

	public Server removeServer() {
		// TODO:Improve this code
		Server serverToRemove = null;
		while (serversInUse.size() > 1) {
			serverToRemove = serversInUse.peek();
			if (serverToRemove.getServerCapacity() >= Constants.SERVER_CONCURRENT_REQUEST_LIMIT) {
				serverToRemove = serversInUse.pop();
				serverToRemove.setServerEndTime(SimulationClock.CurrentTime);
				serversNotInUse.push(serverToRemove);
			} else
				break;
		}
		return serverToRemove;
	}
	
	public void free(Server server, Request request){
		server.free(request);
		OutputStatistics.collectStatisticsForDispatched(request);
		Server bestServer = null;
		while(waitQueue.size()>0 && ( bestServer = serverManager.getBestServer())!=null)
			bestServer.serve(waitQueue.get());
	}
	
}