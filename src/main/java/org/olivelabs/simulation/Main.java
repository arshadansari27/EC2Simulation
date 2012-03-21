package org.olivelabs.simulation;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		Parameters params = new Parameters();
		params.MAX_CLOCK = new BigInteger("600000000");
		SimulationRunner simulator = new SimulationRunner(params);
		executor.execute(new DisplayOutput(simulator));

		executor.execute(simulator);
		executor.shutdown();
	}

}
