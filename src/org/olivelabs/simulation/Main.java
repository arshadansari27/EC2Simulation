package org.olivelabs.simulation;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventGenerator.TOTAL_REQUEST = 10000000L;
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		BlockingQueue<StatisticsCollector> statistics = new LinkedBlockingQueue<StatisticsCollector>(); 
		executor.execute(new SimulationRunner(statistics));
		//executor.execute(new DisplayOutput(statistics));
		executor.shutdown();
		
	}

}
