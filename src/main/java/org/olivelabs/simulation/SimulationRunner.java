package org.olivelabs.simulation;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationRunner{

	public volatile AtomicBoolean RUNNING = new AtomicBoolean();
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
		RUNNING.set(true);
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

	public void start() throws InterruptedException{
		System.out.println("Simulation Begin Time : " + new Date());
		System.out.print("[");

		//ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);
		//ExecutorService executor = Executors.newFixedThreadPool(this.params.eventProcessorSize+3);
		ExecutorService executor = Executors.newCachedThreadPool();

		executor.execute(new DisplayOutput(this));
		executor.execute(this.eventGenerator);
		executor.execute(this.serverManager.getRunnableProcess());
		for(int i = 0;i < this.params.eventProcessorSize; i++){

			executor.execute(new SimulationProcess(i));
		}
		executor.shutdown();

		while(RUNNING.get()){
			executor.awaitTermination(10, TimeUnit.SECONDS);
		}
		System.out.println("]");
		System.out.println("Simulation End Time : " + new Date());


	}

	public double percentComplete(){
		return (eventGenerator.requestCount*1.0/eventGenerator.totalRequest) * 100;
	}



	class SimulationProcess implements Runnable{
		int id;
		SimulationProcess(int id){
			this.id = id;
		}
		@Override
		public void run() {
			Event event = null;
			long clock = 0;
			while (!((event = eventManager.getNextEvent()) instanceof TerminalEvent)
					&& RUNNING.get()) {
				if(event == null){
					try {
						synchronized(this){
							wait(50);
						}

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
				clock = event.eventTime;
				event.processEvent();
			}
			RUNNING.set(false);
		}
	}




}
