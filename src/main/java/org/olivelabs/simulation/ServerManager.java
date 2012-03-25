package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class ServerManager implements Runnable {

	private int id;
	private Stack<Server> serversInUse;
	private Stack<Server> serversNotInUse;
	private BlockingQueue<Request> waitQueue;
	private Long currentTime;
	private boolean running;
	private Parameters params;
	private Server currentBestServer;
	private ExecutorService executor;
	private ServerManager nextServerManager;
	private int depthNextServerManager;
	private long stopTime;
	static Logger log = Logger.getLogger(ServerManager.class.getName());
	Random random = new Random();

	public ServerManager(ExecutorService executor, Parameters params,
			int depthNextServerManager) {
		this.id = depthNextServerManager;
		currentTime = 0L;
		serversInUse = new Stack<Server>();
		serversNotInUse = new Stack<Server>();
		waitQueue = new LinkedBlockingQueue<Request>();
		running = true;
		this.params = params;
		this.executor = executor;
		this.depthNextServerManager = depthNextServerManager;
	}

	private void getBestServer(long currentTime) {
		Server bestServer = null;
		Iterator<Server> serverIterator = serversInUse.iterator();
		Long maxCapacity = 0L;
		synchronized (serversInUse) {
			while (serverIterator.hasNext()) {
				Server server = serverIterator.next();
				server.dispatch(currentTime);
				if (maxCapacity < server.getServerCapacity()) {
					maxCapacity = server.getServerCapacity();
					bestServer = server;
				}
			}
		}

		if (bestServer == null
				|| (!canHandle(bestServer) && serversInUse.size() < params.maxServer)) {
			bestServer = addServer();
		}
		currentBestServer = bestServer;
		// log.debug(this.id+"Got best Server:" + ((currentBestServer==null)?
		// "NULL": currentBestServer.getId()) + "["+currentTime+"]");
	}

	private Server getRandomServer() {
		return serversInUse.get(random.nextInt(serversInUse.size()));
	}

	private boolean canHandle(Server server) {
		return server.canHandle();
	}

	public synchronized boolean isRunning() {
		boolean nextRunning = false;
		if(nextServerManager!=null) nextRunning = nextServerManager.isRunning();
		return running || waitQueue.size() > 0 || nextRunning;
	}

	private synchronized void passRequestToNextServerManager(Request request) {
		if (nextServerManager == null) {
			log.debug(this.id + "Creating the next ServerManager..["
					+ currentTime + "]");
			nextServerManager = new ServerManager(this.executor, this.params,
					depthNextServerManager - 1);
			executor.execute(nextServerManager);
		}
		nextServerManager.serve(request);
	}

	public void run() {
		while (running || waitQueue.size() > 0) {
			log.debug("Running server Manager");

			if (waitQueue.size() == 0) {
				log.debug("Nothing in wait queue yet! yawning...");
				synchronized (waitQueue) {
					try {
						TimeUnit.MICROSECONDS.sleep(200);
					} catch (InterruptedException e) {
					}
				}
			}
			for (Server server : serversInUse) {
				server.dispatch(currentTime);
			}
			removeServer();
			ArrayList<Request> requests = new ArrayList<Request>();
			synchronized (waitQueue) {
				waitQueue.drainTo(requests);
				waitQueue.clear();
			}
			Collections.sort(requests);
			for (Request request : requests) {
				currentTime = request.arrivalTime;
				getBestServer(currentTime);
				if (currentBestServer == null || !canHandle(currentBestServer)) {
					if (depthNextServerManager > 0) {
						// log.debug(
						// "Passing request on to the next ServerManager..");
						passRequestToNextServerManager(request);
						continue;
					} else {
						log.debug("Getting random server to have the request rejected.. or so i think :)");
						currentBestServer = getRandomServer();
					}
				}
				currentBestServer.serve(request, currentTime);
			}
			try {
				synchronized (this) {
					wait(100);
				}

			} catch (InterruptedException e) {
			}

			removeServer();
		}
		if (nextServerManager != null)
			nextServerManager.stop(this.currentTime);
		for (Server server : getAllServers()) {
			server.shutDown(stopTime);
		}
	}

	public Long getSimulationTime() {
		return this.currentTime;
	}

	public synchronized void serve(Request request) {
		waitQueue.add(request);
		notifyAll();
	}

	protected Server addServer() {
		if (serversInUse.size() >= params.maxServer)
			return null;
		Server server = null;
		if (serversNotInUse.empty())
			server = new Server(getAllServers().size() + 1,
					params.concurrentRequestLimit, this.id);
		else
			server = serversNotInUse.pop();
		serversInUse.add(server);
		server.setBusy(this.currentTime);
		log.debug(this.id + ":Adding server" + server.getId());
		return server;
	}

	protected Server removeServer() {

		// TODO:Improve this code
		Server serverToRemove = null;
		while (serversInUse.size() > 1
				|| (!running && serversInUse.size() == 1)) {
			serverToRemove = serversInUse.peek();
			if (serverToRemove.getServerCapacity() >= params.concurrentRequestLimit
					|| !running) {
				serverToRemove = serversInUse.pop();
				serversNotInUse.push(serverToRemove);
				serverToRemove.setIdle(this.currentTime);
				log.debug(this.id + ":Removing server" + serverToRemove.getId());
			} else
				break;
		}
		return serverToRemove;
	}

	public synchronized int busySize() {
		if (nextServerManager != null)
			return nextServerManager.busySize() + serversInUse.size();
		return this.serversInUse.size();
	}

	public synchronized int freeSize() {
		if (nextServerManager != null)
			return nextServerManager.freeSize() + serversNotInUse.size();
		return this.serversNotInUse.size();
	}

	public synchronized List<Server> getAllServers() {
		List<Server> allServers = new ArrayList<Server>();
		if (busySize() > 0)
			allServers.addAll(serversInUse);
		if (freeSize() > 0)
			allServers.addAll(serversNotInUse);
		if (nextServerManager != null)
			allServers.addAll(nextServerManager.getAllServers());
		return allServers;
	}

	public synchronized void stop(long currentTime) {
		log.debug(this.id + ":Stopping server manager");
		this.stopTime = currentTime;
		running = false;

	}

}
