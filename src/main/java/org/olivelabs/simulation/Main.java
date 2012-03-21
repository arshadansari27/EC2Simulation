package org.olivelabs.simulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		SimulationRunner simulator = new SimulationRunner(10000000L);
		executor.execute(new DisplayOutput(simulator));

		executor.execute(simulator);
		executor.shutdown();
	}

}
