package org.olivelabs.simulation;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

public class SimulationRunner{

	public volatile boolean RUNNING;
	private EventGenerator eventGenerator;
	private ServerManager serverManager;
	private EventManager eventManager;
	private SimulationClock clock;
	private RequestWaitQueue waitQueue;
	private OutputStatistics requestStats;
	public Parameters params;

	public SimulationRunner(Parameters params){
		this.clock = new SimulationClock();
		this.waitQueue = new RequestWaitQueue();
		this.eventManager = new EventManager();
		this.serverManager = new ServerManager(this);
		this.eventGenerator = new EventGenerator(this);
		this.params = params;
		this.eventGenerator.totalRequest = params.totalRequest;
		this.requestStats = new OutputStatistics(this);
		RUNNING = true;
	}

	public OutputStatistics getRequestStats(){
		return this.requestStats;
	}

	public RequestWaitQueue getWaitQueue(){
		return this.waitQueue;
	}

	public SimulationClock getClock(){
		return this.clock;
	}

	public EventManager getEventManager(){
		return this.eventManager;
	}

	public EventGenerator getEventGenerator(){
		return this.eventGenerator;
	}

	public ServerManager getServerManager(){
		return this.serverManager;
	}

	public void start(){
		eventGenerator.generateNextArrivalEvent();
		System.out.println("Simulation Begin Time : " + new Date());
		System.out.print("[");

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);
		SimulationProcess process1 = new SimulationProcess();
		//SimulationProcess process2 = new SimulationProcess();
		//SimulationProcess process3 = new SimulationProcess();

		executor.execute(this.serverManager);
		executor.execute(new DisplayOutput(this));
		executor.execute(process1);
		//executor.execute(process2);
		//executor.execute(process3);

		executor.shutdown();
		try {
			executor.awaitTermination(10L, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("]");
		System.out.println("Simulation End Time : " + new Date());
		RUNNING = false;
		serverManager.removeServer();
		for(String history : serverDetails()){
			System.out.println(history);
		}


	}

	public double percentComplete(){
		return (eventGenerator.requestCount*1.0/eventGenerator.totalRequest) * 100;
	}

	public List<String> serverDetails(){
		return serverManager.getServerHistories();
	}

	class SimulationProcess implements Runnable{
		@Override
		public void run() {
			Event event = null;
			while (!((event = eventManager.getNextEvent()) instanceof TerminalEvent)) {
				if(event == null){
					try {
						synchronized(eventManager){
							wait();
						}

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
				event.processEvent();
			}

		}
	}


}
