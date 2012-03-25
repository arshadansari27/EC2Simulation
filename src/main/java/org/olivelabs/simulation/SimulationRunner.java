package org.olivelabs.simulation;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class SimulationRunner{

    private EventGenerator eventGenerator;
    private ServerManager serverManager;
    private EventManager eventManager;
    private OutputStatistics requestStats;
    public Parameters params;
    private ExecutorService executor;

    static Logger log = Logger.getLogger(SimulationRunner.class.getName());


    public SimulationRunner(Parameters params){
        //executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);
        executor = Executors.newCachedThreadPool();
        this.eventManager = new EventManager();
        this.params = params;
        this.serverManager = new ServerManager(executor, params,params.serverManager);
        this.eventGenerator = new EventGenerator(this.eventManager, params);
    }

    public OutputStatistics getRequestStats(){
        return this.requestStats;
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
        log.info("Simulation Begin Time : " + new Date());

        executor.execute(this.eventGenerator);
        executor.execute(this.serverManager);
        for(int i = 0; i < params.eventProcessorSize; i++){
        	executor.execute(new SimulationProcess(i));
        }


        while(serverManager.isRunning()){
        	log.info(String.format("Completed : %3.2f percent done ", (double)this.serverManager.getSimulationTime()/this.params.MAX_CLOCK * 100));
            executor.awaitTermination(7, TimeUnit.SECONDS);
        }
        executor.shutdownNow();
        log.info("Simulation End Time : " + new Date());


    }

    public StatisticsCollector getStatistics(){
        List<Server> servers = serverManager.getAllServers();
        StatisticsCollector statsData = new StatisticsCollector(serverManager, servers);
        log.info(statsData);
        log.debug(statsData);
        return statsData;

    }
    class SimulationProcess implements Runnable{
        int id;
        SimulationProcess(int id){
            this.id = id;
        }
        @Override
        public void run() {
            Event event = null;
            log.debug("Starting the listening of event that are put on event manager");
            while(true){
            	//log.debug("Getting next event");
            	event = eventManager.getNextEvent();
            	 if(event == null){
            		 log.debug("Event didn't arrive yet!, so getting bored!");
                     try {
                         synchronized(this){
                             wait(10);
                         }
                     } catch (InterruptedException e) {}
                     continue;
                 }
            	//log.debug("Event Arrived with type:"+event.getClass()+" at ["+event.eventTime+"]");
            	if(event instanceof TerminalEvent){
            		serverManager.stop(event.eventTime);
            		break;
            	}

                process(event.request);
            }
        }

        void process(Request request){
            serverManager.serve(request);
        }
    }




}
